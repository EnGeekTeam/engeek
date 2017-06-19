package vn.giki.rest.filter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import vn.giki.rest.dao.UserDAO;
import vn.giki.rest.utils.exception.TokenClientException;

public class AuthenticationFilter implements HandlerInterceptor {
	private static final String AUTHEN_HEADER = "Hash";
	
	@Autowired
	UserDAO  userDAO;
	

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		System.out.println("authen");
		try {
			String token = request.getHeader(AUTHEN_HEADER);
			System.out.println("JWT: "+token);
			
			String url = request.getRequestURI();
			int userId=0;
			if (url.contains("/users/")){
				url = url.substring(7, url.length());
				userId = Integer.parseInt(url.substring(0, url.indexOf("/")));
				System.out.println(userId);
			}
			
			if (token != null) {
				System.out.println("--");
				System.out.println(userDAO==null);
				 if (userDAO.checkClientToken(userId, token)){
					 System.out.println("pass");
					 return true;
				 }
			}
			response.setStatus(400);
			System.out.println("authen fail with url " + request.getRequestURI());
			return false;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
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
