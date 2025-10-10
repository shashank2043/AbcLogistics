package com.alpha.ABCLogistics.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alpha.ABCLogistics.Service.MailService;

@RestController
public class MailController {
	@Autowired
	MailService mailService;
	@PostMapping("/sendmail")
	public void sendMail() {
		String tomail = "shashankpulluri1@gmail.com";
		String sub = "Offer Letter";
		String content = "You are slected for the job";
		mailService.sendMail(tomail, sub, content);
	}
}
