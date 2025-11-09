package com.example.project.config;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Configuration
public class WebClientConfig {

    @Value("${kinopoisk.base-url}")
    private String baseUrl;

    @Value("${kinopoisk.api-key}")
    private String apiKey;

    @Value("${kinopoisk.timeout:2000}")
    private int timeoutMillis;

    @Bean
    public WebClient webClientWithTimeout() {
        // Настройка HttpClient с таймаутами
        HttpClient httpClient = HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, timeoutMillis)
                .responseTimeout(Duration.ofMillis(timeoutMillis))
                .doOnConnected(conn ->
                        conn.addHandlerLast(new ReadTimeoutHandler(timeoutMillis, TimeUnit.MILLISECONDS))
                                .addHandlerLast(new WriteTimeoutHandler(timeoutMillis, TimeUnit.MILLISECONDS))
                );

        return WebClient.builder()
                .baseUrl(baseUrl)
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .defaultHeader("X-API-KEY", apiKey)
                .defaultHeader("Content-Type", "application/json")
                // Простой логгер запросов/ответов
                .filter(logRequest())
                .build();
    }

    private ExchangeFilterFunction logRequest() {
        return ExchangeFilterFunction.ofRequestProcessor(clientRequest -> {
            System.out.println("Request: " + clientRequest.method() + " " + clientRequest.url());
            return Mono.just(clientRequest);
        });
    }
}
