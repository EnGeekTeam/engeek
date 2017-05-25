package vn.giki.rest.config;

import javax.persistence.EntityManagerFactory;

import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.SessionFactoryBuilder;
import org.hibernate.boot.model.naming.ImplicitNamingStrategyJpaCompliantImpl;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import vn.giki.rest.utils.PackageUtils;

@Configuration
@EnableJpaRepositories(basePackages = "vn.giki.rest.dao")
@EnableTransactionManagement
@ComponentScan("vn.giki.*")
@PropertySource("classpath:hibernate.properties")
public class GikiDAOConfig {

	private Environment env;

	@Autowired
	public void setEnv(Environment env) {
		this.env = env;
	}

	@Bean
	public EntityManagerFactory entityManagerFactory() {
		StandardServiceRegistry standardRegistry = new StandardServiceRegistryBuilder().build();

		MetadataSources metadataSources = new MetadataSources(standardRegistry);
		try {
			for (Class<?> clazz : PackageUtils.getClass(env.getProperty("entity_package", "vn.giki.rest.entity"))) {
				metadataSources.addAnnotatedClass(clazz);
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		Metadata metadata = metadataSources.getMetadataBuilder()
				.applyImplicitNamingStrategy(ImplicitNamingStrategyJpaCompliantImpl.INSTANCE).build();
		SessionFactoryBuilder sessionFactoryBuilder = metadata.getSessionFactoryBuilder();

		SessionFactory sessionFactory = sessionFactoryBuilder.build();
		return sessionFactory;
	}

	@Bean
	public JpaTransactionManager transactionManager(@Autowired EntityManagerFactory entityManagerFactory) {
		return new JpaTransactionManager(entityManagerFactory);
	}
}
