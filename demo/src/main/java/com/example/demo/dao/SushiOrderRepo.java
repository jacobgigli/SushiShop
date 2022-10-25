package com.example.demo.dao;

import com.example.demo.model.SushiOrder;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SushiOrderRepo extends CrudRepository<SushiOrder, Integer> {
    List<SushiOrder> findByStatusId(int StatusId);

    SushiOrder findById(int Id);
}
