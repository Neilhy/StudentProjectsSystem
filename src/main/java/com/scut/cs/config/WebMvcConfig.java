package com.scut.cs.config;

import com.scut.cs.web.request.RequestUrls;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Created by NeilHY on 2016/8/11.
 */
@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter{
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController(RequestUrls.LoginUrl).setViewName("login");
        registry.addViewController(RequestUrls.HomeUrl).setViewName("home");
        registry.addViewController("/projectManagement").setViewName("projectManagement");
        registry.addViewController("/userManagement").setViewName("userManagement");
        registry.addViewController("/projectAdd").setViewName("projectAdd");
        registry.addViewController("/dictManagement").setViewName("dictManagement");
        registry.addViewController("/file").setViewName("testFileUpload");
    }

//    @Bean
//    public MultipartResolver multipartResolver() {
//        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
//        multipartResolver.setMaxUploadSize(1000000);
//        return multipartResolver;
//    }
}
