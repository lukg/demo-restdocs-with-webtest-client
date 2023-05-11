package com.example.demo;

import java.lang.reflect.Constructor;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.restdocs.RestDocsProperties;
import org.springframework.boot.test.autoconfigure.restdocs.RestDocsWebTestClientConfigurationCustomizer;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.reactive.server.WebTestClientBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation;
import org.springframework.restdocs.webtestclient.WebTestClientRestDocumentationConfigurer;

@TestConfiguration
@ConditionalOnClass(WebTestClientRestDocumentation.class)
@EnableConfigurationProperties(RestDocsProperties.class)
public class RestDocsWebTestClientConfiguration {

    @Bean
    @ConditionalOnMissingBean
    WebTestClientRestDocumentationConfigurer restDocsWebTestClientConfigurer(
        ObjectProvider<RestDocsWebTestClientConfigurationCustomizer> configurationCustomizers,
        RestDocumentationContextProvider contextProvider) {
        WebTestClientRestDocumentationConfigurer configurer = WebTestClientRestDocumentation.documentationConfiguration(contextProvider);
        configurationCustomizers.orderedStream()
            .forEach(configurationCustomizer -> configurationCustomizer.customize(configurer));
        return configurer;
    }

    @Bean
    WebTestClientBuilderCustomizer webTestClientBuilderCustomizer(RestDocsProperties properties,
        WebTestClientRestDocumentationConfigurer configurer) throws Exception {
        Class<?> customizerClass = Class.forName("org.springframework.boot.test.autoconfigure.restdocs.RestDocsWebTestClientBuilderCustomizer");
        Constructor<?> constructor = customizerClass.getDeclaredConstructor(RestDocsProperties.class, WebTestClientRestDocumentationConfigurer.class);
        constructor.setAccessible(true);
        return (WebTestClientBuilderCustomizer) constructor.newInstance(properties, configurer);
    }
}