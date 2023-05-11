package com.example.demo.controller;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation.document;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.demo.controller.ExampleController.ExampleResponse;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureWebTestClient
@AutoConfigureRestDocs
//@Import(RestDocsWebTestClientConfiguration.class)
class ExampleControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    WebTestClient webTestClient;

    @Test
    void shouldReturnNonReactive() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/non-reactive?id=5")
                .contentType("application/json"))
            .andExpect(status().is(HttpStatus.OK.value()))
            .andExpect(jsonPath("text", equalTo("test")))
            .andExpect(jsonPath("number", equalTo(5)));
    }


    @Test
    void shouldGenerateRestDocsForNonReactive() throws Exception {
        mockMvc.perform(RestDocumentationRequestBuilders.get("/non-reactive?id=5")
                .contentType("application/json"))
            .andExpect(status().is(HttpStatus.OK.value()))
            .andExpect(jsonPath("text", equalTo("test")))
            .andExpect(jsonPath("number", equalTo(5)))
            .andDo(com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document("non-reactive-id",
                requestParameters(
                    parameterWithName("id").description("This is number")
                )
            ));
    }


    @Test
    void shouldReturnReactive() {
        webTestClient.get().uri("/reactive/{id}?type=5", Map.of("id", 5))
            .exchange()
            .expectStatus().isEqualTo(HttpStatus.OK)
            .expectBody(new ParameterizedTypeReference<List<ExampleResponse>>() {})
            .isEqualTo(List.of(new ExampleResponse("test", 5)));
    }

    @Test
    void shouldGenerateRestDocsForReactive() {
        webTestClient.get().uri("/reactive/{id}?type=5", Map.of("id", 5))
            .exchange()
            .expectStatus().isEqualTo(HttpStatus.OK)
            .expectBody(new ParameterizedTypeReference<List<ExampleResponse>>() {})
            .isEqualTo(List.of(new ExampleResponse("test", 5)))
            .consumeWith(document(
                "reactive-id",
                pathParameters(
                    parameterWithName("id").description("This is number")
                ),
                requestParameters(
                    parameterWithName("type").description("This is type")
                )
            ));
    }
}