package com.alpha.ABCLogistics.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailService {

	@Autowired
	JavaMailSender javaMailSender;
	
	public void sendMail(String tomail, String sub, String content) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom("shashankpulluri1@gmail.com");
		message.setTo(tomail);
		message.setSubject(sub);
		message.setText(content);
		javaMailSender.send(message);
	}
}
