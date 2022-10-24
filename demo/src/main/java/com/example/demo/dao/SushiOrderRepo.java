package com.example.demo.dao;

import com.example.demo.model.SushiOrder;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SushiOrderRepo extends CrudRepository<SushiOrder, Integer> {
    List<SushiOrder> findByStatusId(int StatusId);
    List<SushiOrder> findById(int Id);
}
