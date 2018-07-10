package com.orb.interview.controller;

import java.nio.charset.StandardCharsets;
import java.util.Locale;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.FixedLocaleResolver;

@Configuration
public class ControllersConfiguration {
  @Value("${interview.locale.language.default}")
  private String language;

  @Bean
  public ReloadableResourceBundleMessageSource messageSource() {
    ReloadableResourceBundleMessageSource ms = new ReloadableResourceBundleMessageSource();
    ms.setBasenames("classpath:messages");
    ms.setCacheSeconds(0);
    ms.setFallbackToSystemLocale(false);
    ms.setDefaultEncoding(StandardCharsets.UTF_8.displayName());
    return ms;
  }

  @Bean
  public LocaleResolver localeResolver() {
    return new FixedLocaleResolver(new Locale(language));
  }

  @Bean
  public LocalValidatorFactoryBean validator() {
    LocalValidatorFactoryBean factoryBean = new LocalValidatorFactoryBean();
    factoryBean.setValidationMessageSource(messageSource());
    return factoryBean;
  }

  @Bean
  public WebMvcConfigurer webMvcConfigurer() {
    return new WebMvcConfigurer() {
      @Override
      public Validator getValidator() {
        return validator();
      }
    };
  }
}
