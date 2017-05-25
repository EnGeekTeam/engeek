package vn.giki.rest.utils;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import vn.giki.rest.utils.exception.ResourceNotFoundException;

public class JSONDecoratorFilter implements Filter {

	@Override
	public void destroy() {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
			throws IOException, ServletException {
		JSONResponseWrapper capturingResponseWrapper = new JSONResponseWrapper((HttpServletResponse) response);
		String error, message, data;
		try {
			filterChain.doFilter(request, capturingResponseWrapper);
			data = capturingResponseWrapper.getCaptureAsString();
			if (data == null || data.isEmpty()) {
				throw new ResourceNotFoundException();
			}
			error = "0";
			message = "Success";
		} catch (Throwable e) {
			e.printStackTrace();
			error = "1";
			message = lookUpMessage(e);
			data = "{}";
		}
		response.setContentType("application/json");
		StringBuilder content = new StringBuilder("{\"error\":\"");
		content.append(error);
		content.append("\",\"message\":\"");
		content.append(message);
		content.append("\",\"data\":");
		content.append(data);
		content.append("}");
		response.getWriter().write(content.toString());

	}

	private String lookUpMessage(Throwable e) {
		if (e instanceof ResourceNotFoundException) {
			return "Resource not found";
		}
		return e.getMessage();
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
	}

}