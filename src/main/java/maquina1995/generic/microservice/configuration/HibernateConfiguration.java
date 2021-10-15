package maquina1995.generic.microservice.configuration;

import javax.sql.DataSource;

import org.hibernate.cfg.Configuration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.zaxxer.hikari.HikariDataSource;

/**
 * Configuración de hibernate/base de datos
 */
@SpringBootConfiguration
@EnableTransactionManagement
public class HibernateConfiguration {

	/**
	 * Crea el bean encargado de la configuración de base de datos
	 * 
	 * @param user {@link String} <code>${database.user}</code> usuario de base de
	 *             datos
	 * @param pass {@link String} <code>${database.password}</code> password de base
	 *             de datos
	 * @param user {@link String} <code>${database.driver}</code> driver de base de
	 *             datos
	 * @param pass {@link String} <code>${database.url}</code> url de base de datos
	 * 
	 * @return {@link HikariDataSource} configurado
	 */
	@Bean
	public DataSource datasource(@Value(value = "${database.user}") String user,
	        @Value(value = "${database.password}") String pass, @Value(value = "${database.driver}") String driver,
	        @Value(value = "${database.url}") String url) {

		HikariDataSource dataSource = new HikariDataSource();

		dataSource.setUsername(user);
		dataSource.setPassword(pass);
		dataSource.setDriverClassName(driver);
		dataSource.setJdbcUrl(url);

		dataSource.setMaximumPoolSize(5);
		dataSource.setPoolName("MaQuina1995-HikariCP");

		dataSource.addDataSourceProperty("dataSource.cachePrepStmts", "true");
		dataSource.addDataSourceProperty("dataSource.prepStmtCacheSize", "250");
		dataSource.addDataSourceProperty("dataSource.prepStmtCacheSqlLimit", "2048");
		dataSource.addDataSourceProperty("dataSource.useServerPrepStmts", "true");

		return dataSource;
	}

	/**
	 * Crea la {@link Configuration} enlazandole
	 * {@link CustomPhysicalNamingStrategy} pasado por parámetro
	 * 
	 * @param customPhysicalNamingStrategy {@link CustomPhysicalNamingStrategy}
	 * 
	 * @return {@link Configuration}
	 */
	@Bean
	public Configuration hibernateConfig(CustomPhysicalNamingStrategy customPhysicalNamingStrategy) {

		Configuration hibernateConfiguration = new Configuration();
		hibernateConfiguration.setPhysicalNamingStrategy(customPhysicalNamingStrategy);

		return hibernateConfiguration;

	}

}
