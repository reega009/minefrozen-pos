package com.minefrozen.pos.Configuration;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

@Configuration
public class TransactionConfiguration {

    @Bean(name = "posTransaction")
    public DataSourceTransactionManager posTransaction(@Qualifier("posDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean(name = "serverTransaction")
    public DataSourceTransactionManager serverTransaction(@Qualifier("minefrozenDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

}
