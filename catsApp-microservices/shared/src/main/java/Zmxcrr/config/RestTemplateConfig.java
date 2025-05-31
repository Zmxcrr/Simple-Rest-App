package Zmxcrr.config;

import Zmxcrr.exceptions.CustomResponseErrorHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {
    @Bean
    public ResponseErrorHandler responseErrorHandler() {
        return new CustomResponseErrorHandler();
    }
    @Bean
    public RestTemplate restTemplate() {
        var template = new RestTemplate();
        template.setErrorHandler(responseErrorHandler());

        return template;
    }
}
