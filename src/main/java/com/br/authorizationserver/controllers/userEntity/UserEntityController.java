package com.br.authorizationserver.controllers.userEntity;

import java.util.List;


import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.br.authorizationserver.entity.userEntity.UserEntity;
import com.br.authorizationserver.service.userEntity.UserEntityService;

import lombok.AllArgsConstructor;

@RestController
@Controller
@RequestMapping("user")
@AllArgsConstructor
public class UserEntityController {

    private UserEntityService service;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<UserEntity> findAll() {
        return service.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserEntity save(@RequestBody final UserEntity user) {
        return service.save(user);
    }

    @GetMapping("sign-up")
	public String signUp() {
		return "userEntity/sign-up";
	}
}