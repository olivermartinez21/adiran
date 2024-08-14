package com.tmm.myre.preRegistration.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.tmm.myre.base.controller.AbstractMyreController;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping(PreRegistrationController.HOME)
public class PreRegistrationController extends AbstractMyreController{

public static final String HOME = PREFIX_PREREGISTRATION + "preRegistration";
	
	@GetMapping(EMPTY)
	public String onLoadHome() {
		return HOME;
	}
}
