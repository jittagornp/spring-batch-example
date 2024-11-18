package me.jittagornp.example.reader;

import me.jittagornp.example.model.Price;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.data.AbstractPaginatedDataItemReader;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Component
public class PriceItemReader extends AbstractPaginatedDataItemReader<Price> {

    private AtomicInteger atomicInteger = new AtomicInteger(0);

    @Override
    protected Iterator<Price> doPageRead() {
        log.info("Reading data page: {}, size: {}", page, pageSize);

        if (atomicInteger.get() > 90) {
            log.info("Result size is empty");
            return Collections.emptyIterator();
        }

        List<Price> result = new ArrayList<>();
        for (int i = 0; i < this.pageSize; i++) {
            result.add(
                    Price.builder()
                            .productId(atomicInteger.incrementAndGet())
                            .price(new BigDecimal((int) (Math.random() * 100) + 1))
                            .build()
            );
        }

        log.info("Result size is {}", result.size());
        return result.iterator();
    }

    @Override
    protected void doOpen() throws Exception {
        super.doOpen();
        setName(getClass().getName());
    }

}
