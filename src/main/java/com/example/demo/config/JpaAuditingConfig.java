package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing(auditorAwareRef = "commonAuditorAware", modifyOnCreate = false)
@Configuration
public class JpaAuditingConfig {
    @Bean
    public AuditorAware<String> commonAuditorAware() {
        return new CommonAuditorAware();
    }
}
