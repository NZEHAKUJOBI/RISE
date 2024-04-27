package com.matchrate.RISE;

import liquibase.integration.spring.SpringLiquibase;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import com.matchrate.RISE.etl.extraction.extractor;

import javax.sql.DataSource;
import java.util.Properties;

@SpringBootApplication
public class RiseApplication {

	@Value("${spring.datasource.url}")
	private String databaseUrl;

	@Value("${spring.datasource.username}")
	private String databaseUsername;

	@Value("${spring.datasource.password}")
	private String databasePassword;

	public static void main(String[] args) {
		SpringApplication.run(RiseApplication.class, args);
		try {
			extractor.performDataInsertion();
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
	}

	@Bean
	public SpringLiquibase liquibase() {
		SpringLiquibase liquibase = new SpringLiquibase();
		liquibase.setChangeLog("classpath:config/liquibase/master.xml");
		liquibase.setDataSource(dataSource());
		return liquibase;
	}

	@Bean
	public DataSource dataSource() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setUrl(databaseUrl);
		dataSource.setUsername(databaseUsername);
		dataSource.setPassword(databasePassword);
		dataSource.setDriverClassName("org.postgresql.Driver");
		return dataSource;
	}
}
