package org.wotmud.service.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * Created by lenore on 26/02/17.
 */

@RestController
public class DataController {

	private final SocketService socketService;
	Logger logger = LoggerFactory.getLogger(DataController.class);
	@Autowired
	public DataController(SocketService socketService) {
		this.socketService = socketService;
	}

	@RequestMapping("/{name}")
	public String index(@PathVariable("name") String name) {
		System.out.println("method index called");
		return socketService.send(name);
	}

}
