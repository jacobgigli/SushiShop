package com.example.demo.service;

import com.example.demo.dao.SushiOrderRepo;
import com.example.demo.model.Chef;
import com.example.demo.model.SushiOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;

@Service


public class SushiOrderService {

    List<Chef> chefs;
    int numberOfSecondsOpen;
    PriorityQueue<SushiOrder> orderQueue;

    public void sushiShop(int numChefs) {
        this.chefs = new ArrayList<Chef>();
        for (int i = 0; i < numChefs; i++) {
            chefs.add(new Chef());
        }
        this.orderQueue = new PriorityQueue<SushiOrder>(new Comparator<SushiOrder>() {
            @Override
            public int compare(SushiOrder o1, SushiOrder o2) {
                Date d1 = o1.getCreatedAt();
                Date d2 = o2.getCreatedAt();
                return d1.compareTo(d2);
            }


        });
        this.numberOfSecondsOpen = 1800;

    }

    @Autowired
    SushiOrderRepo repo;

    public void addSushiOrder(SushiOrder sushiOrder) {

        repo.save(sushiOrder);
        this.orderQueue.add(repo.findById(sushiOrder.getId()).get(0));

    }

    public List<List<SushiOrder>> displaySushiOrders() {
        List<List<SushiOrder>> r1 = new ArrayList<>();
        r1.add(repo.findByStatusId(2));
        r1.add(repo.findByStatusId(1));
        r1.add(repo.findByStatusId(3));
        r1.add(repo.findByStatusId(5));
        r1.add(repo.findByStatusId(4));
        return r1;

    }

    public int deleteSushiOrders(int orderId) {
        if (repo.existsById(orderId)) {
            SushiOrder cancelledOrder = repo.findById(orderId).get(0);
            if (cancelledOrder.getStatusId() != 4 && cancelledOrder.getStatusId() != 5) {
                cancelledOrder.setStatusId(5);
                repo.save(cancelledOrder);
                return 1;

            }

            if (cancelledOrder.getStatusId() == 4 || cancelledOrder.getStatusId() == 5) {

                return 2;


            }

        }
        return 0;
    }


    public int pauseSushiOrders(int orderId) {
        if (repo.existsById(orderId)) {
            SushiOrder pausedOrder = repo.findById(orderId).get(0);

            if (pausedOrder.getStatusId() != 4 && pausedOrder.getStatusId() != 5) {
                pausedOrder.setStatusId(3);
                repo.save(pausedOrder);
                return 1;

            }

            if (pausedOrder.getStatusId() == 4 || pausedOrder.getStatusId() == 5) {

                return 2;


            }

        }
        return 0;
    }

    public int resumeSushiOrders(int orderId) {
        if (repo.existsById(orderId)) {
            SushiOrder resumedOrder = repo.findById(orderId).get(0);

            if (resumedOrder.getStatusId() == 3) {

                if (orderQueue.peek() == null){
                    resumedOrder.setStatusId(1);
                    orderQueue.add(resumedOrder);
                    repo.save(resumedOrder);
                    return 1;

                }

                else {

                    Date d1 = orderQueue.peek().getCreatedAt();
                    Instant i1 = d1.toInstant();
                    System.out.println(i1);
                    i1 = i1.minusMillis(1);
                    resumedOrder.setCreatedAt(Date.from(i1));
                    resumedOrder.setStatusId(1);
                    orderQueue.add(resumedOrder);
                    repo.save(resumedOrder);
                    return 1;

                }


            }

            else {

                return 2;


            }

        }
        return 0;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void sushiRestaurant() throws InterruptedException {

        sushiShop(3);
        System.out.println("Welcome to the sushi restruant");
        while (numberOfSecondsOpen > 0) {
            for (Chef c : this.chefs) {
                if (c.isBusy()) {
                    continue;
                }
                SushiOrder currentOrder = orderQueue.poll();

                while (currentOrder != null) {
                    if (repo.findById(currentOrder.getId()).get(0).getStatusId() == 1) {// check in database to see if it is a new order
                        break;
                    }
                    currentOrder = orderQueue.poll();

                }


                if (currentOrder != null) {
                    currentOrder.setStatusId(2);
                    repo.save(currentOrder);
                    c.assignOrder(currentOrder.getId(), currentOrder.getTime(), currentOrder.getTimeSpent());
                }
            }
            Thread.sleep(1000);
            numberOfSecondsOpen -= 1;
            for (Chef c : this.chefs) {
                if (!c.isBusy()) {
                    continue; // chef is idel so don't progress
                }
                System.out.println("processing busy chefs");
                c.increment();
                SushiOrder chefOrder = repo.findById(c.getOrderId()).get(0);
                System.out.println(c.getTimeSpent());
                System.out.println(c.getOrderTime());
                if (c.finishedOrder() || chefOrder.getStatusId() != 2) {
                    System.out.println("order status changed from in progress");
                    if (c.finishedOrder()) {
                        System.out.println("Finished Order");
                        chefOrder.setStatusId(4);
                    }
                    chefOrder.setTimeSpent(c.getTimeSpent());
                    repo.save(chefOrder);
                    c.clear();

                } else {
                    chefOrder.setTimeSpent(c.getTimeSpent());
                    repo.save(chefOrder);
                }
            }
        }

    }
}

