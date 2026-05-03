package com.RestroPlusWebsite_20.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/web")
public class SelectRoleController {
	
	@GetMapping("/select-role")
	public String RoleController() {
		
		return "select-role";
	}

}
