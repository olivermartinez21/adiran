package com.tmm.myre.base.configuration;


import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
  entityManagerFactoryRef = "primaryEntityManagerFactory",
  transactionManagerRef = "primaryTransactionManager",
  basePackages = { "com.tmm.myre.appointments.repository",
"com.tmm.myre.base.repository",
"com.tmm.myre.containers.repository",
"com.tmm.myre.inspections.repository",
"com.tmm.myre.catalog.repository",
"com.tmm.myre.userRegister.repository",
"com.tmm.myre.event.repository",
"com.tmm.myre.assignments.repository",
"com.tmm.myre.deliveryOrders.repository",
"com.tmm.myre.quote.repository",
"com.tmm.myre.photo.repository"}
)
public class PrimaryDBConfig {
	
	@Bean(name="primaryDataSource")
	@Primary
	@ConfigurationProperties(prefix="spring.datasource")
	public DataSource primaryDataSource() {
	    return DataSourceBuilder.create().build();
	}

	@Primary
	@Bean(name = "primaryEntityManagerFactory")
	public LocalContainerEntityManagerFactoryBean primaryEntityManagerFactory(EntityManagerFactoryBuilder builder,
			@Qualifier("primaryDataSource") DataSource primaryDataSource) {
		return builder
				.dataSource(primaryDataSource)
				.packages("com.tmm.myre.appointments.model",
						"com.tmm.myre.base.model",
						"com.tmm.myre.containers.model",
						"com.tmm.myre.inspections.model",
						"com.tmm.myre.catalog.model",
						"com.tmm.myre.userRegister.model",
						"com.tmm.myre.event.model",
						"com.tmm.myre.assignments.model",
						"com.tmm.myre.deliveryOrders.model",
						"com.tmm.myre.quote.model",
						"com.tmm.myre.photo.model")
				.build();
	}

	@Bean(name = "primaryTransactionManager")
	public PlatformTransactionManager primaryTransactionManager(
			@Qualifier("primaryEntityManagerFactory") EntityManagerFactory primaryEntityManagerFactory) {
		return new JpaTransactionManager(primaryEntityManagerFactory);
	}
}