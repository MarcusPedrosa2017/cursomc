package com.nelioalves.cursomc.services;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nelioalves.cursomc.domain.Pedido;
import com.nelioalves.cursomc.repositories.PedidoRepository;
import com.nelioalves.cursomc.services.exception.ObjectsNotFoundException;

@Service
public class PedidoService {

	@Autowired
	private PedidoRepository repo;
	
	public PedidoService(PedidoRepository repo) {
		this.repo = repo;
	}
	
	public List<Pedido> findAll() {
		List<Pedido> lista = new ArrayList<>();
		lista = repo.findAll();
		return lista;
	}
	
	public Pedido find(Integer id) {
		
		Optional<Pedido> obj = repo.findById(id);
		
		try{
			
			if(Objects.isNull(obj.get())) {}
			
		}catch(NoSuchElementException e) {
			throw new ObjectsNotFoundException("Objeto não econtrado! ID: " + id + ", Tipo: " + Pedido.class.getName());	
		}
		return obj.get();//DEVIDO AO CONTAINER Optional E NECESSARIO PEGAR O OBJ PELO GET
	}
}
