package com.foo.server;

import de.foo.writer.MyWriter;
import de.foo.writer.api.Person;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProcessorService {

    private static final Logger LOG = LoggerFactory.getLogger(ProcessorService.class);

    public ProcessorService() {
    }

    @Async
    public void processAsync(List<Person> persons) {
        LOG.debug("processAsync -- start");
        final String result = process(persons);
        LOG.info("result: {}", result);
        LOG.debug("processAsync -- end");
    }

    public String process(List<Person> persons) {

        LOG.info("Start processing");
        sleepSeconds(5);

        try {

            final String targetFilePath = new MyWriter().process(persons);
            LOG.info("Generated {}", targetFilePath);

            return targetFilePath;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private static void sleepSeconds(long seconds) {
        try {
            LOG.info("Start sleep");
            Thread.sleep(seconds * 1000L);
            LOG.info("end sleep");
        } catch (InterruptedException e) {
            LOG.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }


}
