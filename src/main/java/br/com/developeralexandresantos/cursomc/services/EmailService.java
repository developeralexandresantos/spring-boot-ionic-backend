package br.com.developeralexandresantos.cursomc.services;

import org.springframework.mail.SimpleMailMessage;

import br.com.developeralexandresantos.cursomc.domain.Pedido;

public interface EmailService {
	
	void sendOrderConfirmationEmail(Pedido obj);
	
	void sendEmail(SimpleMailMessage msg);

}
