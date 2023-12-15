package com.foo.server;

import com.github.javafaker.Faker;
import de.foo.writer.api.Person;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RestController
public class ProcessController {

    private static final Logger LOG = LoggerFactory.getLogger(ProcessController.class);

    private final ProcessorService processorService;

    public ProcessController(final ProcessorService processorService) {
        this.processorService = processorService;
    }

    @GetMapping("/hello")
    public ResponseEntity<Result> hello() {

        final Faker faker = new Faker();
        final List<Person> persons = IntStream.range(0, 100)
                .mapToObj(i -> new Person(faker.name().name()))
                .collect(Collectors.toList());

        LOG.debug("Generated names {}", persons);

        String targetFilePath = processorService.process(persons);
        return ResponseEntity.ok(new Result(targetFilePath, persons));
    }

    public record Result(String excelFilePath, List<Person> persons) {
    }
}
