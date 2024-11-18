package me.jittagornp.example.reader;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemReader;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Component
public class SimpleItemReader implements ItemReader<Integer> {

    private static final int MAX_ITEMS = 100;

    private AtomicInteger atomicInteger = new AtomicInteger(0);

    @Override
    public Integer read() throws Exception {
        int input = atomicInteger.incrementAndGet();

        if (input > MAX_ITEMS) {
            return null;
        }

        log.info("Read item...{}", input);
        Thread.sleep(100);

        return input;
    }

}
