package com.RestroPlusWebsite_20.Controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/web")
public class HelathCheakController {
	
	@GetMapping("/cheak")
	public String healthcheak() {
		return "ok";
	}

}
