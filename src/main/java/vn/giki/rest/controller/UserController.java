package vn.giki.rest.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import vn.giki.rest.dao.UserRepository;
import vn.giki.rest.entity.User;
import vn.giki.rest.entity.UserProfile;
import vn.giki.rest.utils.Constant;
import vn.giki.rest.utils.GoogleSignIn;
import vn.giki.rest.utils.Utils;
import vn.giki.rest.utils.exception.TokenInValidException;

@Controller
public class UserController {

	@Autowired
	UserRepository userRepository;
	
	@GetMapping("/demo")
	@ResponseBody
	public User demo(){
		System.out.println("1111111");
		return new User();
	}

	BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	@PostMapping("/register")
	public @ResponseBody User register(@RequestParam("userName") String userName, @RequestParam("email") String email,
			@RequestParam("password") String password) {
		if (userRepository.findByUserName(userName) == null) {
			User user = new User(userName, passwordEncoder.encode(password), "", "", "",
					Utils.hashTokenClient(userName), new Date(), null);
			userRepository.save(user);
			return user;
		} else {
			return new User();
		}

	}

	@PostMapping("/login")
	public @ResponseBody User login(@RequestParam(name = "userName", defaultValue = "") String userName,
			@RequestParam(name = "password", defaultValue = "") String password,
			@RequestParam(name = "googleId", defaultValue = "") String googleId,
			@RequestParam(name = "facebookId", defaultValue = "") String facebookId,
			@RequestParam(name = "token", defaultValue = "") String token,
			@RequestParam(name = "tokenCient", defaultValue = "") String tokenClient,
			@RequestParam(name = "platform", defaultValue = "") String platform,
			@RequestParam(name = "avatarUrl", defaultValue = "") String avatarUrl,
			@RequestParam(name = "gender", defaultValue = "") String gender,
			@RequestParam(name = "hint", defaultValue = "0") int hint,
			@RequestParam(name = "name", defaultValue = "") String name) throws TokenInValidException {

		System.out.println("222222222222: " + platform);
		
		User userTmp;
		switch (platform) {
		case Constant.USER.PLATFORM_DEVICE:
			userTmp = userRepository.findByUserName(userName);
			if (passwordEncoder.matches(password, userTmp.getPassword())) {
				return userTmp;
			} else {
				return new User();
			}

		case Constant.USER.PLATFORM_GOOGLE:
			System.out.println("TAG: google");
			userTmp = userRepository.findByGoogleId(googleId);
			if (userTmp != null) {
				// login
				if (userTmp.getTokenClient().equals(tokenClient) && GoogleSignIn.test(token, googleId)) {
					System.out.println("login");
					return userTmp;

				} else {
					throw new TokenInValidException("Token InValid");
				}

			} else {
				System.out.println(googleId);
				if (GoogleSignIn.test(token, googleId)) {
					User userInsert = new User(userName, password, facebookId, googleId, token,
							Utils.hashTokenClient(googleId), new Date(),
							new UserProfile(name, gender, avatarUrl, hint, 0, 0, null, null, 0));
					userRepository.save(userInsert);
					return userInsert;
				} else {
					 throw new TokenInValidException("Token InValid");
				}
			}

		case Constant.USER.PLATFORM_FACEBOOK:
			userTmp = userRepository.findByFacebookId(facebookId);
			if (userTmp != null) {
				if (userTmp.getTokenClient().equals(tokenClient)) {
					return userTmp;
				} else {
					return new User();
				}

			} else {
				User userInsert = new User(userName, password, facebookId, googleId, token,
						Utils.hashTokenClient(facebookId), new Date(),
						new UserProfile(name, gender, avatarUrl, hint, 0, 0, null, null, 0));
				userRepository.save(userInsert);
				return userInsert;
			}

		default:
			break;
		}

		return null;

	}

}
