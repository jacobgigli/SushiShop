package com.example.demo.service;

import com.example.demo.dao.SushiOrderRepo;
import com.example.demo.model.Chef;
import com.example.demo.model.SushiOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;

@Service


public class SushiOrderService {

    private final int CREATED = 1;
    private final int INPROGRESS = 2;
    private final int PAUSED = 3;
    private final int FINISHED = 4;
    private final int CANCELLED = 5;

    private final int RESPONSENOTFOUND = 0;

    private final int RESPONSESUCCESS = 1;

    private final int RESPONSEDENIED = 2;

    private final int NUMOFCHEFS = 3;

    List<Chef> chefs;

    // The time the sushi restaurant is open, the time is in seconds that the simulation is ran for
    int numberOfSecondsOpen = 1800;
    // sushi orders, order in most recent at the head of the queue
    PriorityQueue<SushiOrder> orderQueue;

    public void sushiShop(int numChefs) {
        this.chefs = new ArrayList<Chef>();
        for (int i = 0; i < numChefs; i++) {
            chefs.add(new Chef());
        }
        this.orderQueue = new PriorityQueue<SushiOrder>(new Comparator<SushiOrder>() {
            @Override
            // sorting the priority queue based on timestamp, earliest timestamp is at the head of the queue
            public int compare(SushiOrder o1, SushiOrder o2) {
                Date d1 = o1.getCreatedAt();
                Date d2 = o2.getCreatedAt();
                return d1.compareTo(d2);
            }


        });

    }

    @Autowired
    SushiOrderRepo repo;

    public void addSushiOrder(SushiOrder sushiOrder) {

        repo.save(sushiOrder);
        this.orderQueue.add(repo.findById(sushiOrder.getId()));

    }

    public List<List<SushiOrder>> displaySushiOrders() {
        // print out sushi orders based on status id
        List<List<SushiOrder>> sushiOrderDisplay = new ArrayList<>();
        sushiOrderDisplay.add(repo.findByStatusId(INPROGRESS));
        sushiOrderDisplay.add(repo.findByStatusId(CREATED));
        sushiOrderDisplay.add(repo.findByStatusId(PAUSED));
        sushiOrderDisplay.add(repo.findByStatusId(CANCELLED));
        sushiOrderDisplay.add(repo.findByStatusId(FINISHED));
        return sushiOrderDisplay;

    }

    public int deleteSushiOrders(int orderId) {
        if (repo.existsById(orderId)) {
            SushiOrder cancelledOrder = repo.findById(orderId);
            if (cancelledOrder.getStatusId() != FINISHED && cancelledOrder.getStatusId() != CANCELLED) {
                cancelledOrder.setStatusId(CANCELLED);
                repo.save(cancelledOrder);
                return RESPONSESUCCESS;

            }

            if (cancelledOrder.getStatusId() == FINISHED || cancelledOrder.getStatusId() == CANCELLED) {

                return RESPONSEDENIED;


            }

        }
        return RESPONSENOTFOUND;
    }


    public int pauseSushiOrders(int orderId) {
        if (repo.existsById(orderId)) {
            SushiOrder pausedOrder = repo.findById(orderId);

            if (pausedOrder.getStatusId() != FINISHED && pausedOrder.getStatusId() != CANCELLED && pausedOrder.getStatusId() != PAUSED) {
                pausedOrder.setStatusId(PAUSED);
                repo.save(pausedOrder);
                return RESPONSESUCCESS;

            }

            if (pausedOrder.getStatusId() == FINISHED || pausedOrder.getStatusId() == CANCELLED || pausedOrder.getStatusId() == PAUSED) {

                return RESPONSEDENIED;


            }

        }
        return RESPONSENOTFOUND;
    }

    public int resumeSushiOrders(int orderId) {
        if (repo.existsById(orderId)) {
            SushiOrder resumedOrder = repo.findById(orderId);

            if (resumedOrder.getStatusId() == PAUSED) {
                // edge case if there is only one order and it is paused, don't do this as orderQueue would be empty and ensured it will get to head of the queue
                if (orderQueue.peek() != null) {
                    // timestamp that ensures it processed as soon as possible, by placing it at the head of the priority queue
                    Date d1 = orderQueue.peek().getCreatedAt();
                    Instant i1 = d1.toInstant();
                    i1 = i1.minusMillis(1);
                    resumedOrder.setCreatedAt(Date.from(i1));

                }
                resumedOrder.setStatusId(CREATED);
                orderQueue.add(resumedOrder);
                repo.save(resumedOrder);
                return RESPONSESUCCESS;


            } else {

                return RESPONSEDENIED;


            }

        }
        return RESPONSENOTFOUND;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void sushiRestaurant() throws InterruptedException {

        sushiShop(NUMOFCHEFS);
        // time remaining for simulation
        while (numberOfSecondsOpen > 0) {
            for (Chef chef : this.chefs) {
                if (chef.isBusy()) {
                    continue;
                }
                SushiOrder currentOrder = orderQueue.poll();
                // go through orders until there is a valid order to process
                while (currentOrder != null) {
                    if (repo.findById(currentOrder.getId()).getStatusId() == CREATED) {// check in database to see if it is a new order
                        break;
                    }
                    currentOrder = orderQueue.poll();

                }


                if (currentOrder != null) {
                    currentOrder.setStatusId(INPROGRESS);
                    repo.save(currentOrder);
                    chef.assignOrder(currentOrder.getId(), currentOrder.getTime(), currentOrder.getTimeSpent());
                }
            }
            // simulate 1 second of work
            Thread.sleep(1000);
            numberOfSecondsOpen -= 1;
            for (Chef chef : this.chefs) {
                if (!chef.isBusy()) {
                    continue; // chef is idle so don't process
                }

                // add a second of work to order timespent
                chef.increment();
                SushiOrder chefOrder = repo.findById(chef.getOrderId());
                if (chef.finishedOrder() || chefOrder.getStatusId() != INPROGRESS) {
                    if (chef.finishedOrder()) {
                        chefOrder.setStatusId(FINISHED);
                    }
                    chefOrder.setTimeSpent(chef.getTimeSpent());
                    repo.save(chefOrder);
                    // chef is ready for a new order
                    chef.clear();

                } else {
                    chefOrder.setTimeSpent(chef.getTimeSpent());
                    repo.save(chefOrder);
                }
            }
        }

    }
}

