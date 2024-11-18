package me.jittagornp.example.writer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class SimpleItemWriter implements ItemWriter<String> {

    @Override
    public void write(Chunk<? extends String> chunk) throws Exception {
        List<? extends String> items = chunk.getItems();
        String string = items.stream()
                .collect(Collectors.joining(", "));

        log.info("Write...{}", string);
    }

}
