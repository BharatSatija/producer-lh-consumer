package com.example.service;

import com.example.domain.PriceUpdate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.IntStream;

import static java.util.concurrent.TimeUnit.SECONDS;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

@Component
public class LoadHandler {

    private static final int MAX_PRICE_UPDATES_EACH_SECOND = 100;

    @Autowired
    LinkedBlockingQueue<PriceUpdate> sharedQueue;

    @Autowired
    Consumer consumer;

    public void receive(PriceUpdate pu) throws InterruptedException {
        sharedQueue.offer(pu, 1, SECONDS);
    }

    @Scheduled(fixedDelay = 1 * 1000)
    void sendToConsumer() {

        Map<String, PriceUpdate> poll = poll();
        List priceUpdates = new ArrayList(poll.values());

        int chunkSize = 10;
        List<List<PriceUpdate>> partition = partition(priceUpdates, chunkSize);

        // to leverage method call to consumer in max 10 call with 100 msc duration.
        // i.e: legacy system that cant take too many data in one call.
        partition.forEach(pus -> {
            consumer.send(pus);
            sleep(100);
        });
    }

    Map<String, PriceUpdate> poll() {
        return
                IntStream
                        .range(0, MAX_PRICE_UPDATES_EACH_SECOND)
                        .mapToObj(value -> {
                            try {
                                return sharedQueue.poll(10, SECONDS);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            return null;
                        })
                        .filter(Objects::nonNull)
                        .collect(
                                toMap(
                                        //key
                                        PriceUpdate::getCompanyName,
                                        //value : object itself
                                        Function.identity(),
                                        //take latest object
                                        (indey, pu2) -> pu2
                                ));
    }

    void sleep(int millisecond) {
        try {
            TimeUnit.MILLISECONDS.sleep(millisecond);
        } catch (InterruptedException e) {
            throw new IllegalStateException(e);
        }
    }

    // taken from
    // http://stackoverflow.com/questions/28210775/split-list-into-multiple-lists-with-fixed-number-of-elements-in-java-8
    static List<List<PriceUpdate>> partition(List<PriceUpdate> lists, int chunkSize) {
        int listSize = lists.size();
        return
                IntStream
                        .range(0, (listSize - 1) / chunkSize + 1)
                        .mapToObj(i -> {
                            int from = (i *= chunkSize);
                            int to = listSize - chunkSize >= i ? i + chunkSize : listSize;
                            return lists.subList(from, to);
                        })
                        .collect(toList());
    }

}
