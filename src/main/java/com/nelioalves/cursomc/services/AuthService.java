package com.nelioalves.cursomc.services;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.nelioalves.cursomc.domain.Cliente;
import com.nelioalves.cursomc.repositories.ClienteRepository;
import com.nelioalves.cursomc.services.exception.ObjectsNotFoundException;

@Service
public class AuthService {
	
	@Autowired
	private ClienteRepository clienteRepository;
	
	@Autowired
	private BCryptPasswordEncoder pe;
	
	@Autowired
	private EmailService emailService;	
	
	public void sendNewPassword(String email) {
		Cliente cliente = clienteRepository.findByEmail(email);
		if(cliente == null) {
			throw new ObjectsNotFoundException("Email não encontrado");
		}
		String newPass = newPassword();
		
		cliente.setSenha(pe.encode(newPass));
		
		try {
			clienteRepository.save(cliente);
			emailService.sendNewPasswordEmail(cliente, newPass);
		} catch (Exception e) {
			
		}
		
	}

	private String newPassword() {
		char[] vet = new char[10];
		for(int i=0; i<10 ; i++) {
			vet[i] = randomChar();
		}
		return new String(vet);
	}

	private char randomChar() {
		Random rand = new Random();
		int opt = rand.nextInt(3);
		if(opt == 0) {//gera um digito
			return (char) (rand.nextInt(10) + 48);//gerando numero aleatorio de 0 a 9 pelos codigos da tabela unicode
		}else if(opt == 1) {//gera letra maiuscula
			return (char) (rand.nextInt(26) + 65);//gerando letra aleatoria de (A - Z) pelos codigos da tabela unicode
		}else {//gera letra minuscula
			return (char) (rand.nextInt(26) + 97);//gerando letra aleatoria de (a - z) pelos codigos da tabela unicode
		}		
	}
}
