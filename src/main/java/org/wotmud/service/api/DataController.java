package org.wotmud.service.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by lenore on 26/02/17.
 */

@Controller
@RequestMapping("/")
public class DataController {

//	private final SocketService socketService;

//	@Autowired
//	public DataController() {
//		this.socketService = socketService;
//	}

	@RequestMapping("/test")
	public String greeting(@RequestParam(value="name", required=false, defaultValue="World") String name, Model model) {
		model.addAttribute("name", name);
		return "greeting";
	}
}
