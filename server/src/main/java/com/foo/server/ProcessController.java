package com.foo.server;

import com.github.javafaker.Faker;
import de.foo.writer.api.Person;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.IntStream;

@RestController
public class ProcessController {

    private static final Logger LOG = LoggerFactory.getLogger(ProcessController.class);

    private final ProcessorService processorService;

    public ProcessController(final ProcessorService processorService) {
        this.processorService = processorService;
    }

    @GetMapping("/hello2")
    public String helloAsyncInService() {
        final List<Person> persons = getPeople();
        try {
            // with this construct I get the NullPointerException: Cannot invoke "org.jxls.expression.ExpressionEvaluatorFactory.createExpressionEvaluator(String)"
            // in the completable future
            return processorService.processSupplyAsync(persons).get();
        } catch (InterruptedException e) {
            LOG.error("Interrupted!", e);
            Thread.currentThread().interrupt();
            return e.getMessage();
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/hello2-other-classloader")
    public String helloAsyncInServiceFixed() {
        final List<Person> persons = getPeople();
        try {
            // Workaround via setting classloader https://github.com/jxlsteam/jxls/discussions/276#discussioncomment-8073204
            return processorService.processSupplyAsyncWithOtherClassloader(persons).get();
        } catch (InterruptedException e) {
            LOG.error("Interrupted!", e);
            Thread.currentThread().interrupt();
            return e.getMessage();
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/hello-async")
    public ResponseEntity<String> helloAsync() {
        final List<Person> persons = getPeople();
        processorService.processAsync(persons);
        return ResponseEntity.ok("will finish in background");
    }

    @GetMapping("/hello")
    public ResponseEntity<Result> hello() {

        final List<Person> persons = getPeople();

        final String targetFilePath = processorService.process(persons);
        return ResponseEntity.ok(new Result(targetFilePath, persons));
    }


    private static List<Person> getPeople() {
        final List<Person> persons = IntStream.range(0, 100)
                .mapToObj(i -> new Person(new Faker().name().name()))
                .toList();

        LOG.debug("Generated names {}", persons);
        return persons;
    }

    public record Result(String excelFilePath, List<Person> persons) {
    }
}
