package com.avaliacorp.api;

import java.util.Properties;

import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import io.github.cdimascio.dotenv.Dotenv;

@Component
public class EnvLoader implements ApplicationListener<ApplicationEnvironmentPreparedEvent> {
    @Override
    public void onApplicationEvent(@NonNull ApplicationEnvironmentPreparedEvent event) {
        System.out.println("Creating...");
        Dotenv dotenv = Dotenv.load();
        ConfigurableEnvironment environment = event.getEnvironment();
        MutablePropertySources propertySources = environment.getPropertySources();

        Properties properties = new Properties();
        dotenv.entries().forEach(entry -> properties.setProperty(entry.getKey(), entry.getValue()));

        propertySources.addFirst(new PropertiesPropertySource("dotenvProperties", properties));
        System.out.println("Environment Variables created succesfully");;
    }
}