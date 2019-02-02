package com.nelioalves.cursomc.services.validation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerMapping;

import com.nelioalves.cursomc.domain.Cliente;
import com.nelioalves.cursomc.dto.ClienteDTO;
import com.nelioalves.cursomc.repositories.ClienteRepository;
import com.nelioalves.cursomc.resources.exception.FieldMessage;

public class ClienteUpdateValidator implements ConstraintValidator<ClienteUpdate, ClienteDTO> {
	
	//foi posto para poder pegar o id do cliente que vem na urie nao no json
	@Autowired
	HttpServletRequest request;
	
	@Autowired
	ClienteRepository repo;
	
	@Override
	public void initialize(ClienteUpdate ann) {
	}

	@Override
	public boolean isValid(ClienteDTO objDto, ConstraintValidatorContext context) {
		
		/*
		 *como o id vem na uri, e necessario pegar esse valor atraves do getAttribute que tras os valores que vem no Map da uri
		 *por isso foi criado um Map para receber e feito o Casting 
		*/
		@SuppressWarnings("unchecked")
		Map<String, String> map = (Map<String, String>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE); 
		Integer uriId = Integer.parseInt(map.get("id"));		
		
		List<FieldMessage> list = new ArrayList<>();		
		
		Cliente aux = repo.findByEmail(objDto.getEmail());
		
		if(Objects.nonNull(aux) && !aux.getId().equals(uriId) ) {
			list.add(new FieldMessage("email", "Email já existente"));			
		}
		
		// inclua os testes aqui, inserindo erros na lista
		for (FieldMessage e : list) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(e.getMessage()).addPropertyNode(e.getFieldName())
					.addConstraintViolation();
		}
		return list.isEmpty();
	}
}