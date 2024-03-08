package com.minefrozen.pos.Configuration;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
public class JdbcConfiguration {

    @Bean
    @ConfigurationProperties("spring.datasource.pos")
    public DataSourceProperties posDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    @ConfigurationProperties("spring.datasource.minefrozen")
    public DataSourceProperties minefrozenDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    public DataSource posDataSource() {
        return posDataSourceProperties()
                .initializeDataSourceBuilder()
                .build();
    }


    @Bean
    public DataSource minefrozenDataSource() {
        return minefrozenDataSourceProperties()
                .initializeDataSourceBuilder()
                .build();
    }


    @Bean(name = "posJdbc")
    @Primary
    public NamedParameterJdbcTemplate posJdbc(@Qualifier("posDataSource") DataSource dataSource) {
        return new NamedParameterJdbcTemplate(dataSource);
    }



    @Bean(name = "serverJdbc")
    public NamedParameterJdbcTemplate serverJdbc(@Qualifier("minefrozenDataSource") DataSource dataSource) {
        return new NamedParameterJdbcTemplate(dataSource);
    }

}
