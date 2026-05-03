package com.RestroPlusWebsite_20.Controllers;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/web")
public class BaseController {
	
	@GetMapping("/base")
	public String home(Model model) {
	    model.addAttribute("content", "home"); // <-- template name
	    return "base";
	}


}
