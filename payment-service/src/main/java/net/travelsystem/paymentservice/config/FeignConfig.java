package net.travelsystem.paymentservice.config;

import feign.codec.ErrorDecoder;
import net.travelsystem.paymentservice.exceptions.CustomErrorDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignConfig {

    @Bean
    public ErrorDecoder errorDecoder() { return new CustomErrorDecoder(); }
}
