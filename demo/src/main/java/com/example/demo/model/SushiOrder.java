package com.example.demo.model;

import com.example.demo.exception.SushiOrderRequestException;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;


import java.util.Date;
import java.util.Objects;


public class SushiOrder {
    @Override
    public String toString() {
        return "SushiOrder{" +
                "name='" + name + '\'' +
                ", id=" + id +
                ", statusId=" + statusId +
                ", createdAt=" + createdAt +
                ", sushiId=" + sushiId +
                '}';
    }

    public int getTime() {
        return this.time;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SushiOrder that = (SushiOrder) o;
        return id == that.id && time == that.time && timeSpent == that.timeSpent && statusId == that.statusId && sushiId == that.sushiId && Objects.equals(name, that.name) && Objects.equals(createdAt, that.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, id, time, timeSpent, statusId, createdAt, sushiId);
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getTimeSpent() {
        return timeSpent;
    }

    public void setTimeSpent(int timeSpent) {
        this.timeSpent = timeSpent;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public SushiOrder(@JsonProperty("sushi_name") String name) {
        if (name == null) {
            throw new SushiOrderRequestException("Name cannot be null");

        } else if (name.equals("California Roll")) {
            this.sushiId = 1;
            this.statusId = 1;
            this.time = 30;
            this.name = "California Roll";
        } else if (name.equals("Kamikaze Roll")) {
            this.sushiId = 2;
            this.statusId = 1;
            this.time = 40;
            this.name = "Kamikaze Roll";
        } else if (name.equals("Dragon Eye")) {
            this.sushiId = 3;
            this.statusId = 1;
            this.time = 50;
            this.name = "Dragon Eye";
        } else {
            throw new SushiOrderRequestException("Name does not correspond to current sushi options");
        }

        this.createdAt = new Date();
        this.timeSpent = 0;
    }


    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Id

    private int id;
    private int time;
    private int timeSpent;


    private int statusId;

    private Date createdAt;

    public Date getCreatedAt() {
        return createdAt;
    }

    public int getStatusId() {
        return statusId;
    }

    public void setStatusId(int statusId) {
        this.statusId = statusId;
    }

    public int getSushiId() {
        return sushiId;
    }

    public void setSushiId(int sushiId) {
        this.sushiId = sushiId;
    }

    private int sushiId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
