package vn.giki.rest.filter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import vn.giki.rest.utils.Utils;
import vn.giki.rest.utils.exception.TokenClientException;

public class AuthenticationFilter implements HandlerInterceptor {
	private static final String AUTHEN_HEADER = "Hash";

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		System.out.println("authen");
		try {
			String jwt = request.getHeader(AUTHEN_HEADER);
			System.out.println("JWT: "+jwt);
			
			if (jwt != null) {
				Integer userId = Utils.decodeJWT(jwt);
				System.out.println("---"+userId);
				// TODO
				// Sample filter, in complex situation, you have to change it.
				if (request.getRequestURI() != null
						&& request.getRequestURI().contains("/users/" + String.valueOf(userId))) {
					System.out.println("authen pass");
					return true;
				}
			}
			response.setStatus(400);
			System.out.println("authen fail with url " + request.getRequestURI());
			return false;
		} catch (Exception e) {
			// TODO: handle exception
			throw new TokenClientException();
		}
		
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {

	}

}
