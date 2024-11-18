package me.jittagornp.example.processor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SimpleItemProcessor implements ItemProcessor<Integer, String> {

    @Override
    public String process(Integer item) throws Exception {
        log.info("Process {}...", item);
        return String.valueOf(item);
    }

}
