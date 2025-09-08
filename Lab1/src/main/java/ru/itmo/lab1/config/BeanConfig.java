package ru.itmo.lab1.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.itmo.lab1.service.MainService;

@Configuration
public class BeanConfig {

    @Bean
    public FilterRegistrationBean<AuthFilter> authFilter(MainService mainService) {
        var registrationBean = new FilterRegistrationBean<AuthFilter>();
        registrationBean.setFilter(new AuthFilter(mainService));
        registrationBean.addUrlPatterns("/api/*");
        registrationBean.setOrder(1);
        return registrationBean;
    }

}
