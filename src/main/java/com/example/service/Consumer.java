package com.example.service;

import com.example.domain.PriceUpdate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class Consumer {

    public void send(List<PriceUpdate> priceUpdates) {
        priceUpdates.forEach(priceUpdate -> {
            System.out.println("customer receive : " + priceUpdate);
        });
    }
}
