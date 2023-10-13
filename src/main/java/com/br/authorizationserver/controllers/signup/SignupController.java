package com.br.authorizationserver.controllers.signup;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("sign-up")
public class SignupController {

	@GetMapping
	public String greeting() {
		return "signup/sign-up";
	}

}