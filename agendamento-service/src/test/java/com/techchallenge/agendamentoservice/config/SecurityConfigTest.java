package com.techchallenge.agendamentoservice.config;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.security.web.SecurityFilterChain;

class SecurityConfigTest {

    @Test
    void deveCarregarBeanDeSecurityFilterChain() {
        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(SecurityConfig.class)) {
            SecurityFilterChain bean = context.getBean(SecurityFilterChain.class);
            assertThat(bean).isNotNull();
        }
    }
}
