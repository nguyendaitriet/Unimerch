package com.unimerch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.data.jpa.datatables.repository.DataTablesRepositoryFactoryBean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
@EnableJpaRepositories(repositoryFactoryBeanClass = DataTablesRepositoryFactoryBean.class)
public class UnimerchApplication implements WebMvcConfigurer {

    public static void main(String[] args) {
        SpringApplication.run(UnimerchApplication.class, args);
    }

    @Bean
    public ResourceBundleMessageSource messageSource() {
        ResourceBundleMessageSource source = new ResourceBundleMessageSource();
        source.setBasenames(
                "static/texts/views/common_content_vi",
                "static/texts/messages/messages_vi",
                "static/texts/views/group_content_vi",
                "static/texts/views/amzn_acc_content_vi",
                "static/texts/views/user_content_vi",
                "static/texts/views/user_asgn_content_vi");
        source.setDefaultEncoding("UTF-8");
        source.setUseCodeAsDefaultMessage(true);
        return source;
    }


}

