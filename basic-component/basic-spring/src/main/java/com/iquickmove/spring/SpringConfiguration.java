package com.iquickmove.spring;

import com.iquickmove.log.RequestAndResponseLoggingFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.nio.charset.StandardCharsets;

/**
 * @author elijah
 */
@Configuration
public class SpringConfiguration {

    @Bean
    CharacterEncodingFilter characterEncodingFilter() {
        CharacterEncodingFilter filter = new CharacterEncodingFilter();
        filter.setEncoding(StandardCharsets.UTF_8.name());
        filter.setForceEncoding(true);
        return filter;
    }
    @Bean
    RequestAndResponseLoggingFilter requestAndResponseLoggingFilter(){
        return new RequestAndResponseLoggingFilter();
    }
}
