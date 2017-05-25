package vn.giki.rest.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
@ComponentScan(basePackages = "vn.giki.rest.controller")
@EnableWebMvc
public class GikiAPIConfig extends WebMvcConfigurerAdapter {
	public GikiAPIConfig() {
		System.out.println("asdfuh");
	}

	@Override
	public void configureViewResolvers(ViewResolverRegistry registry) {
		registry.jsp();
	}
}
