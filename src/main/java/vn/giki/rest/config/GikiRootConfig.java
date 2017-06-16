package vn.giki.rest.config;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.apache.tomcat.jdbc.pool.PoolProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

@Configuration
@PropertySource("classpath:datasource.properties")
public class GikiRootConfig {
	private static final String MYSQL_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
	private Environment env;

	@Autowired
	public void setEnv(Environment env) {
		this.env = env;
	}

	@Bean
	public Connection getConnection() throws SQLException, ClassNotFoundException {
//		Class.forName(env.getRequiredProperty("driver_class"));
//		Connection conn = DriverManager.getConnection(env.getRequiredProperty("url"), env.getRequiredProperty("user"),
//				env.getRequiredProperty("password"));
		
		PoolProperties p = new PoolProperties();
		p.setUrl(env.getRequiredProperty("url"));
		p.setDriverClassName(env.getRequiredProperty("driver_class"));
		p.setUsername(env.getRequiredProperty("user"));
		p.setPassword(env.getRequiredProperty("password"));
		p.setMaxWait(Integer.parseInt(env.getRequiredProperty("max_wait")));
		p.setMaxActive(Integer.parseInt(env.getRequiredProperty("max_active")));
		p.setTestOnBorrow(true);

		DataSource dataSource = new DataSource();
		dataSource.setPoolProperties(p);
		
		Connection conn = dataSource.getConnection();
		
		
		return conn;
	}

	@Bean
	public SimpleDateFormat dateTimeFormat() {
		return new SimpleDateFormat(MYSQL_DATE_TIME_FORMAT);
	}
}
