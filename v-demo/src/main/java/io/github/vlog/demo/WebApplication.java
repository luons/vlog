package io.github.vlog.demo;

import io.github.vlog.demo.filter.LogFilter;
import io.github.vlog.logback.aspect.TraceAspect;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.Filter;

@SpringBootApplication
@ServletComponentScan("io.github.vlog")
public class WebApplication implements WebMvcConfigurer {

    public static void main(String[] args) {
        SpringApplication.run(WebApplication.class, args);
    }

    @Bean
    public Filter getLogFilter() {
        return new LogFilter();
    }

    // 使用@Trace单方法扫描
    @Bean
    public TraceAspect getTraceAspect() {
        return new TraceAspect();
    }
}
