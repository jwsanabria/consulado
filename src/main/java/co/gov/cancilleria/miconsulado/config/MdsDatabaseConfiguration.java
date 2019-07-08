package co.gov.cancilleria.miconsulado.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Properties;


@Configuration
@EnableTransactionManagement
@EntityScan(basePackages = "co.gov.cancilleria.miconsulado.domain.mds")
@EnableJpaRepositories(transactionManagerRef = "mdsTransactionManager", entityManagerFactoryRef = "mdsEntityManagerFactory", basePackages = "co.gov.cancilleria.miconsulado.repository.mds")
public class MdsDatabaseConfiguration {

    private final Logger log = LoggerFactory.getLogger(MdsDatabaseConfiguration.class);

    @Bean("mdsDataSourceProperties")
    @ConfigurationProperties("mds.datasource")
    public DataSourceProperties mdsDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean("mdsDataSource")
    @ConfigurationProperties("mds.datasource")
    public DataSource mdsDataSource() {
        return mdsDataSourceProperties().initializeDataSourceBuilder().build();
    }

    @Bean("mdsJpaProperties")
    @ConfigurationProperties("mds.jpa")
    public Properties mdsJpaProperties() {
        return new Properties();
    }

    @Bean(name = "mdsEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean mdsEntityManagerFactory(
        EntityManagerFactoryBuilder builder) {

        Properties properties = mdsJpaProperties();

        LocalContainerEntityManagerFactoryBean emf = builder
            .dataSource(mdsDataSource())
            .packages("co.gov.cancilleria.miconsulado.domain.mds")
            .persistenceUnit("mds")
            .build();
        emf.setJpaProperties(properties);

        return emf;
    }

    @Bean(name = "mdsTransactionManager")
    public JpaTransactionManager db2TransactionManager(@Qualifier("mdsEntityManagerFactory") final EntityManagerFactory emf) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(emf);
        return transactionManager;
    }
}
