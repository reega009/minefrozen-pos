package com.minefrozen.pos.Configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {

    @Autowired
    private Environment env;

    @Bean(name = "dataSource.pos")
    @Qualifier("dataSources")
    public DataSource dataSourcePos() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(env.getProperty("spring.datasource.pos.driverClassName"));
        dataSource.setUrl(env.getProperty("spring.datasource.pos.url"));
        dataSource.setUsername(env.getProperty("spring.datasource.pos.username"));
        dataSource.setPassword(env.getProperty("spring.datasource.pos.password"));
        return dataSource;
    }

}
