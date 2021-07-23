package com.misha.scoreboard.web;

import java.util.concurrent.ExecutionException;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WelcomeCtrl {
	
	private String welcomeMessage = "Hello everyone, "
			+ "I would like to thank you to get me the opportunity to work with you."
			+ "The task was really interesting for me as I learned new approaches during development "
			+ "and I hope I implemented them in the right way as it was the first time for me.  "
			+ "Please let me know your thoughts even they are negative, this will really help me "
			+ "to improve in the future for developing scalable solutions."
			+ "Thank you very much for your time and hope to hear from you soon.";

	/**
	 * Method used to handle all GET requests to the root path /.
	 * @return
	 * @throws ExecutionException 
	 * @throws InterruptedException 
	 */
	@GetMapping("/")
	public String welcome(final Model model) throws InterruptedException, ExecutionException {
		model.addAttribute("welcome",welcomeMessage );

		return "index";
	}
}
