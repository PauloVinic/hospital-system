package com.techchallenge.agendamentoservice.config;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

class OpenApiConfigTest {

    @Test
    void deveCarregarBeanDeOpenApiCustomiser() {
        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(OpenApiConfig.class)) {
            var bean = context.getBean("customOpenAPI");
            assertThat(bean).isNotNull();
        }
    }
}
