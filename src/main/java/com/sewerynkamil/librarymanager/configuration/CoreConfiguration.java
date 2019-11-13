package com.sewerynkamil.librarymanager.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.*;

/**
 * Author Kamil Seweryn
 */

@Configuration
public class CoreConfiguration implements WebMvcConfigurer {
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Override
    public void addResourceHandlers(final ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/lib/**").addResourceLocations("/lib/").setCachePeriod(0);
        registry.addResourceHandler("/images/**").addResourceLocations("/images/").setCachePeriod(0);
        registry.addResourceHandler("/css/**").addResourceLocations("/css/").setCachePeriod(0);
        registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
    }
}