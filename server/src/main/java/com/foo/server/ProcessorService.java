package com.foo.server;

import de.foo.writer.MyWriter;
import de.foo.writer.api.Person;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProcessorService {

    private static final Logger LOG = LoggerFactory.getLogger(ProcessorService.class);

    public ProcessorService() {
    }


    public String process(List<Person> persons) {
        LOG.info("Start processing");
        try {

            final String targetFilePath = new MyWriter().process(persons);
            LOG.debug("DONE");

            return targetFilePath;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }


}
