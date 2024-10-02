package com.gnx.config;

import com.gnx.metrics.MetricsAspect;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.prometheus.PrometheusMeterRegistry;
import io.micrometer.prometheus.PrometheusConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.ServerResponse;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.RouterFunctions;
import static org.springframework.web.servlet.function.RequestPredicates.GET;

@Configuration
public class ApplicationConfig {

    @Bean
    public PrometheusMeterRegistry prometheusMeterRegistry() {
        return new PrometheusMeterRegistry(PrometheusConfig.DEFAULT);
    }

    @Bean
    public MetricsAspect metricsAspect(MeterRegistry meterRegistry) {
        return new MetricsAspect(meterRegistry);
    }

    @Bean
    public RouterFunction<ServerResponse> metricsEndpoint(PrometheusMeterRegistry prometheusMeterRegistry) {
        return RouterFunctions.route(GET("/metrics"), request ->
                ServerResponse.ok().body(prometheusMeterRegistry.scrape())
        );
    }
}