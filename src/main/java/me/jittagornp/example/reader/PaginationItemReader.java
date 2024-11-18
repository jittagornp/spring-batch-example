package me.jittagornp.example.reader;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.data.AbstractPaginatedDataItemReader;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Component
public class PaginationItemReader extends AbstractPaginatedDataItemReader<Integer> {

    private AtomicInteger atomicInteger = new AtomicInteger(0);

    @Override
    protected Iterator<Integer> doPageRead() {
        log.info("Reading data page: {}, size: {}", page, pageSize);

        if (atomicInteger.get() > 90) {
            log.info("Result size is empty");
            return Collections.emptyIterator();
        }

        List<Integer> result = new ArrayList<>();
        for (int i = 0; i < this.pageSize; i++) {
            result.add(atomicInteger.incrementAndGet());
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
