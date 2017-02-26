package org.wotmud.service.api;

import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;

@EnableAutoConfiguration
public class Example {

	public Example() {

	}

	public static void main(String[] args) throws Exception {
		SpringApplication.run(Example.class, args);
	}

}