package com.gnx.config;

import com.gnx.kafka.metrics.MetricsAspect;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig {

    @Bean
    public MeterRegistry meterRegistry() {
        return new SimpleMeterRegistry();
    }

    @Bean
    public MetricsAspect metricsAspect(MeterRegistry meterRegistry) {
        return new MetricsAspect(meterRegistry);
    }
}
