package com.sploit.socialnetwork.auth.config;

import liquibase.integration.spring.SpringLiquibase;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class LiquibaseConfiguration {

    @Bean
    @ConditionalOnProperty(name = "spring.datasource.url")
    public SpringLiquibase postgresLiquibase(DataSource dataSource) {
        SpringLiquibase liquibase = new SpringLiquibase();
        liquibase.setChangeLog("classpath:db/changelog/postgres.xml");
        liquibase.setDataSource(dataSource);
        return liquibase;
    }

    @Bean
    @ConditionalOnProperty(name = "spring.data.redis.host")
    public SpringLiquibase redisLiquibase(DataSource dataSource) {
        SpringLiquibase liquibase = new SpringLiquibase();
        liquibase.setChangeLog("classpath:db/changelog/redis.xml");
        liquibase.setDataSource(dataSource);
        return liquibase;
    }
}
