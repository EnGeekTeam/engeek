package vn.giki.rest.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import vn.giki.rest.utils.Response;

@RestController
public class ErrorController {
	@GetMapping("/errorException")
	public Map<String, Object> errorException(HttpServletRequest request) {
		Throwable throwable = (Throwable) request.getAttribute("javax.servlet.error.exception");
		System.out.println(throwable.getMessage());
		System.out.println(throwable.getMessage());
		return new Response().setThrowable(throwable).renderResponse();
	}

	@GetMapping("/errorCode")
	public Map<String, Object> errorCode(HttpServletRequest request) {
		Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
		String message = "Unknown";
		switch (statusCode) {
		case 404:
			message = "404 Not Found";
			break;
		case 403:
			message = "403 Forbidden";
			break;
		case 401:
			message = "401 Unauthorized";
			break;
		case 400:
			message = "400 Bad Request";
			break;
		case 500:
			message = "500 Internal Server Error";
			break;
		}
		return new Response().setThrowable(new Exception(message)).renderResponse();
	}
}
