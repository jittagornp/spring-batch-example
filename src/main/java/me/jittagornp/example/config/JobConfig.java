package me.jittagornp.example.config;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.PropertySource;

import java.util.Iterator;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class JobConfig {

    private static final String PREFIX = "job";

    private final Environment environment;

    @PostConstruct
    public void showConfig() {
        Iterator<PropertySource<?>> iterator = ((AbstractEnvironment) environment).getPropertySources()
                .iterator();
        while (iterator.hasNext()) {
            PropertySource<?> propertySource = iterator.next();
            if (propertySource instanceof MapPropertySource) {
                ((MapPropertySource) propertySource).getSource()
                        .entrySet()
                        .stream()
                        .filter(entry -> entry.getKey().startsWith(PREFIX))
                        .forEach(entry -> logProperty(entry.getKey(), entry.getValue()));
            }
        }
    }

    private void logProperty(String name, Object value) {
        if (isPasswordProperty(name)) {
            log.info("{} = {}", name, hidePassword(String.valueOf(value)));
        } else {
            log.info("{} = {}", name, value);
        }
    }

    private boolean isPasswordProperty(String name) {
        String n = name.toLowerCase();
        return n.contains("password")
                || n.contains("secret");
    }

    private String hidePassword(String password) {
        try {
            return password.substring(0, 2) + "************";
        } catch (Exception e) {
            return "****************";
        }
    }

}
