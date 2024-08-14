package com.tmm.myre.pantaco.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.tmm.myre.base.controller.AbstractMyreController;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping(PantacoMainController.HOME)
public class PantacoMainController extends AbstractMyreController {

	public static final String HOME = PREFIX_PANTACO + "pantacoIndex";
	
	@GetMapping(EMPTY)
	public String onLoadHome() {
		return HOME;
	}
}
