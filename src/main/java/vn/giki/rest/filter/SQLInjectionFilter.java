package vn.giki.rest.filter;

import java.util.Arrays;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

public class SQLInjectionFilter implements HandlerInterceptor {

	private static final String SQL_INJECTION_REGEX = "[^A-Za-z0-9,'\"% ]+";

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		try {
			
			System.out.println("si");
			for (Entry<String, String[]> entry : request.getParameterMap().entrySet()) {
				for (String value : entry.getValue()) {
					if (value.matches(SQL_INJECTION_REGEX)) {
						System.out.println(
								"SQL injection rick at " + entry.getKey() + "=" + Arrays.toString(entry.getValue()));
						throw new Exception(
								"SQL injection rick at " + entry.getKey() + "=" + Arrays.toString(entry.getValue()));
					}
				}
			}
			System.out.println("si pass");
			return true;
		} catch (Exception e) {
			throw new Exception("SQL Injection!");
		}
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// Do nothing
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// Do nothing
	}

}
