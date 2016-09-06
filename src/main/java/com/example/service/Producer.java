package com.example.service;

import com.example.domain.PriceUpdate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class Producer {

    @Autowired
    LoadHandler loadHandler;

    public void generateUpdate(){
        try {
            for (int i = 1; i < 100; i++) {
                loadHandler.receive(new PriceUpdate("Google", 160.71));
                loadHandler.receive(new PriceUpdate("Facebook", 91.66));
                loadHandler.receive(new PriceUpdate("Google", 160.73));
                loadHandler.receive(new PriceUpdate("Facebook", 91.71));
                loadHandler.receive(new PriceUpdate("Google", 160.76));
                loadHandler.receive(new PriceUpdate("Apple", 97.85));
                loadHandler.receive(new PriceUpdate("Google", 160.71));
                loadHandler.receive(new PriceUpdate("Facebook", 91.63));
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
