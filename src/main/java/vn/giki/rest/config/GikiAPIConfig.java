package vn.giki.rest.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import vn.giki.rest.filter.AuthenticationFilter;
import vn.giki.rest.filter.SQLInjectionFilter;

@Configuration
@ComponentScan(basePackages = "vn.giki.rest.*")
@EnableWebMvc
@Import(GikiAPIDocumentConfiguration.class)
public class GikiAPIConfig extends WebMvcConfigurerAdapter {

	@Override
	public void configureViewResolvers(ViewResolverRegistry registry) {
		registry.jsp();
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new SQLInjectionFilter()).excludePathPatterns("/error**");
		registry.addInterceptor(new AuthenticationFilter()).addPathPatterns("/users/**")
				.excludePathPatterns("/users/info", "/users/high-scores", "/error**");
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("swagger-ui.html").addResourceLocations("/WEB-INF/resources/");

		registry.addResourceHandler("/webjars/**").addResourceLocations("/WEB-INF/resources/webjars/");
	}
}
