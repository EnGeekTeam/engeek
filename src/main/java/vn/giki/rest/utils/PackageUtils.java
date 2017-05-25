package vn.giki.rest.utils;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;

public class PackageUtils {

	public static Set<Class<?>> getClass(String... packageNames) throws ClassNotFoundException {
		ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false);
		scanner.addIncludeFilter(new AnnotationTypeFilter(Entity.class));
		Set<Class<?>> result = new HashSet<Class<?>>();
		for (String packageName : packageNames) {
			for (BeanDefinition bd : scanner.findCandidateComponents(packageName)) {
				result.add(Class.forName(bd.getBeanClassName()));
			}
		}
		return result;
	}
}
