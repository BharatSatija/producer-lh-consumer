package com.example;

import com.example.domain.PriceUpdate;
import com.example.service.Producer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.util.concurrent.LinkedBlockingQueue;

@EnableScheduling
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        ConfigurableApplicationContext ctx = SpringApplication.run(Application.class, args);
        // it will force to shutdown Executor and Scheduler
        ctx.close();
    }

    @Bean
    CommandLineRunner demo(Producer producer, LinkedBlockingQueue sharedQueue) {
        return (args) -> {

            producer.generateUpdate();

            //sleep 60 sec
            Thread.sleep(5 * 1000);
            System.out.println("Queue size : " + sharedQueue.size());
        };
    }

    @Bean
    ThreadPoolTaskScheduler scheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setAwaitTerminationSeconds(2);
        scheduler.setWaitForTasksToCompleteOnShutdown(true);
        return scheduler;
    }

    @Bean
    LinkedBlockingQueue<PriceUpdate> sharedQueue() {
        return new LinkedBlockingQueue<>();
    }

}
