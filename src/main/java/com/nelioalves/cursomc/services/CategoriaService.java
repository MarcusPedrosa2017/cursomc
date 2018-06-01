package com.nelioalves.cursomc.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nelioalves.cursomc.domain.Categoria;
import com.nelioalves.cursomc.repositories.CategoriaRepository;

@Service
public class CategoriaService {
	
	/*ANOTAÇÃO DO SPRING QUE INJETA UM OBJETO CategoriaRepository AO INSTÂNCIAR UM OBJETO
	 * CategoriaService 
	*/ 
	@Autowired 
	private CategoriaRepository repo;
	
	public List<Categoria> listar() {
		List<Categoria> lista = new ArrayList<>();		
		lista = repo.findAll();
		return lista;	
	}
	
	public Categoria buscar(Integer id) {
		Optional<Categoria> obj = repo.findById(id); 
		return obj.orElse(null);
	}
}
