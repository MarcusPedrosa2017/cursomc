package com.nelioalves.cursomc.services;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nelioalves.cursomc.domain.Cliente;
import com.nelioalves.cursomc.repositories.ClienteRepository;
import com.nelioalves.cursomc.services.exception.ObjectsNotFoundException;

@Service
public class ClienteService {
	
	@Autowired
	private ClienteRepository repo;
	
	public ClienteService(ClienteRepository repo) {
		this.repo = repo;
	}
	
	public List<Cliente> list() {
		List<Cliente> lista = new ArrayList<>();
		lista = repo.findAll();
		return lista;
	}
	
	public Cliente find(Integer id) {
		
		Optional<Cliente> obj = repo.findById(id);
		
		try{
			
			if(Objects.isNull(obj.get())) {}
			
		}catch(NoSuchElementException e) {
			throw new ObjectsNotFoundException("Objeto não econtrado! ID: " + id + ", Tipo: " + Cliente.class.getName());	
		}
		return obj.get();//DEVIDO AO CONTAINER Optional E NECESSARIO PEGAR O OBJ PELO GET
	}
}
