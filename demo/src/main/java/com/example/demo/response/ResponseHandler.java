
package com.example.demo.response;

import com.example.demo.model.SushiOrder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResponseHandler {
    public static ResponseEntity<Object> generatePostResponse(HttpStatus status, Object responseObj) {
        Map<String, Object> map = new HashMap<String, Object>();

        map.put("order", responseObj);
        map.put("msg", "Order Created");
        map.put("code", 0);
        return new ResponseEntity<Object>(map, status);


    }

    public static ResponseEntity<Object> generateGetResponse(HttpStatus status, List<List<SushiOrder>> responseObj) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("In progress", responseObj.get(0));
        map.put("created", responseObj.get(1));
        map.put("paused", responseObj.get(2));
        map.put("cancelled", responseObj.get(3));
        map.put("completed", responseObj.get(4));
        return new ResponseEntity<Object>(map, status);
    }

    public static ResponseEntity<Object> generateDeleteResponse(int deleteResponseCode) {
        Map<String, Object> map = new HashMap<String, Object>();
        if (deleteResponseCode == 1) {
            map.put("code", 0);
            map.put("msg", "order cancelled");
            return new ResponseEntity<Object>(map, HttpStatus.OK);

        } else if (deleteResponseCode == 2) {
            map.put("code", 2);
            map.put("msg", "Can't cancel completed or cancelled order");

            return new ResponseEntity<Object>(map, HttpStatus.BAD_REQUEST);
        } else {
            map.put("code", 1);
            map.put("msg", "order not found");

            return new ResponseEntity<Object>(map, HttpStatus.NOT_FOUND);
        }


    }

    public static ResponseEntity<Object> generatePauseResponse(int pausedResponseCode) {
        Map<String, Object> map = new HashMap<String, Object>();
        if (pausedResponseCode == 1) {
            map.put("code", 0);
            map.put("msg", "order paused");
            return new ResponseEntity<Object>(map, HttpStatus.OK);

        } else if (pausedResponseCode == 2) {
            map.put("code", 2);
            map.put("msg", "Can't pause completed, cancelled or paused order");

            return new ResponseEntity<Object>(map, HttpStatus.BAD_REQUEST);
        } else {
            map.put("code", 1);
            map.put("msg", "order not found");

            return new ResponseEntity<Object>(map, HttpStatus.NOT_FOUND);
        }


    }

    public static ResponseEntity<Object> generateResumeResponse(int resumedResponseCode) {
        Map<String, Object> map = new HashMap<String, Object>();
        if (resumedResponseCode == 1) {
            map.put("code", 0);
            map.put("msg", "order resumed");
            return new ResponseEntity<Object>(map, HttpStatus.OK);

        } else if (resumedResponseCode == 2) {
            map.put("code", 2);
            map.put("msg", "Can't resume completed, cancelled, deleted, or in progress order");

            return new ResponseEntity<Object>(map, HttpStatus.BAD_REQUEST);
        } else {
            map.put("code", 1);
            map.put("msg", "order not found");

            return new ResponseEntity<Object>(map, HttpStatus.NOT_FOUND);
        }


    }
}