package cn.amaging.encology.es.common;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Created by DuQiyu on 2018/6/15.
 */
@Configuration
public class AppConfigurer implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new BaseInterceptor()).excludePathPatterns("/assets/**", "/js/**");
    }
}
