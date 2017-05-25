package vn.giki.rest;

import javax.servlet.Filter;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import vn.giki.rest.config.GikiAPIConfig;
import vn.giki.rest.config.GikiDAOConfig;
import vn.giki.rest.config.GikiRootConfig;
import vn.giki.rest.utils.JSONDecoratorFilter;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "vn.giki.rest.config")
public class GikiAPIInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {
	public static final String SORT_REGEX = "([\\w_]+)(,[\\w_]+)*(;(asc|desc))*";

	@Override
	protected Class<?>[] getRootConfigClasses() {
		System.out.println("root config");
		return new Class[] { GikiRootConfig.class };
	}

	@Override
	protected Class<?>[] getServletConfigClasses() {
		System.out.println("api config");
		return new Class[] { GikiAPIConfig.class, GikiDAOConfig.class };
	}

	@Override
	protected Filter[] getServletFilters() {
		return new Filter[] { new JSONDecoratorFilter() };
	}

	@Override
	protected String[] getServletMappings() {
		return new String[] { "/" };
	}
}
