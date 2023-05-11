package com.example.demo.controller;

import java.util.stream.Stream;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
public class ExampleController {

    record ExampleResponse(String text, int number){}


    @GetMapping("non-reactive")
    ExampleResponse exampleResponseNonReactive(@RequestParam Integer id) {
        return new ExampleResponse("test", id);
    }

    @GetMapping("reactive/{id}")
    Flux<ExampleResponse> exampleResponseReactive(@PathVariable Integer id, @RequestParam Integer type) {
        return Flux.fromStream(Stream.of(new ExampleResponse("test", id)));
    }

}
