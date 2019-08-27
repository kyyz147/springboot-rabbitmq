package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.mq.MessageSender;

@RestController
public class MessageController {
	@Autowired
	private MessageSender messagesender;
	
	@RequestMapping("/boot/send")
	public String send(){
		messagesender.send("exchange1", "routingKey1");
		return "success";
	}
	@RequestMapping("/boot/send2")
	public String send2(){
		messagesender.send2("exchange2", "");
		return "success";
	}
	@RequestMapping("/boot/send3")
	public String send3(){
		messagesender.send3("exchange3", "k4.asd");
		messagesender.send3("exchange3", "zxc.k5");
		return "success";
	}
}
