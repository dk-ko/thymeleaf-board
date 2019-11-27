package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Optional;
import java.util.Random;

@EnableJpaAuditing (auditorAwareRef = "commonAuditorAware")
@Configuration
public class JpaAuditingConfig {
    @Bean
    public AuditorAware<Long> commonAuditorAware() {
        return () -> Optional.of(new Random().nextLong());
    }
}
