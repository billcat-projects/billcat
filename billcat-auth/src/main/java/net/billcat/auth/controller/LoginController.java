package net.billcat.auth.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Slf4j
public class LoginController {

	// private UserService userService;

	@GetMapping(value = "/login")
	public String loginPage() {
		return "login";
	}

	// @GetMapping(value = "/login")
	// public String loginPage(@RequestParam(value = "error", required = false) String
	// error,
	// @RequestParam(value = "logout", required = false) String logout, HttpSession
	// session, Model model) {
	//
	// AuthenticationException ex = (AuthenticationException) session
	// .getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
	//
	// String errorMessage = (error != null && ex != null) ? ex.getMessage() : null;
	//
	// if (logout != null) {
	// errorMessage = "You have been successfully logged out !!";
	// }
	//
	// model.addAttribute("errorMessage", errorMessage);
	//
	// log.debug("error message is {}", errorMessage);
	//
	// return "login";
	// }

	@GetMapping(value = "/logout")
	public String logoutPage(HttpServletRequest request, HttpServletResponse response) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null) {
			new SecurityContextLogoutHandler().logout(request, response, auth);
		}
		return "redirect:/login?logout=true";
	}

	@GetMapping("/register")
	public String register() {
		return "register";
	}

}