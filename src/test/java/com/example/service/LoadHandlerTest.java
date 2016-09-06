package com.example.service;

import com.example.domain.PriceUpdate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;

import static com.example.service.LoadHandler.partition;
import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertThat;

@RunWith(JUnit4.class)
public class LoadHandlerTest {

    @Test
    public void testQueuePollTakesTheLastestPriceUpdate() throws Exception {
        //given
        LoadHandler lh = new LoadHandler();
        lh.sharedQueue = new LinkedBlockingQueue<>();
        lh.sharedQueue.put(new PriceUpdate("Google", 10.0));
        lh.sharedQueue.put(new PriceUpdate("Apple", 10.0));
        lh.sharedQueue.put(new PriceUpdate("Oracle", 10.0));
        lh.sharedQueue.put(new PriceUpdate("Google", 20.0));
        lh.sharedQueue.put(new PriceUpdate("Google", 21.0));

        //when
        Map<String, PriceUpdate> poll = lh.poll();

        //then
        assertThat(poll.size(), is(3));

        assertThat(poll.keySet(), hasItems("Google", "Apple", "Oracle"));

        assertThat(poll.get("Google").getPrice(), is(21.0));
    }


    @Test
    public void testPartition() {

        //given
        PriceUpdate google = new PriceUpdate("Google", 10.0);
        PriceUpdate apple = new PriceUpdate("Apple", 20.0);
        PriceUpdate oracle = new PriceUpdate("Oracle", 20.0);
        PriceUpdate ibm = new PriceUpdate("IBM", 20.0);
        PriceUpdate staples = new PriceUpdate("Staples", 20.0);

        List<PriceUpdate> lists = asList(google,apple,oracle,ibm,staples
        );

        //when
        List<List<PriceUpdate>> partition = partition(lists, 3);

        //then
        assertThat(partition.size(), is(2));

        List<PriceUpdate> partition1 = partition.get(1);
        assertThat(partition1.size(), is(2));
        assertThat(partition1, containsInAnyOrder(ibm, staples));
    }


}