package com.nelioalves.cursomc.services;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.nelioalves.cursomc.domain.Categoria;
import com.nelioalves.cursomc.repositories.CategoriaRepository;
import com.nelioalves.cursomc.services.exception.DataIntegrityException;
import com.nelioalves.cursomc.services.exception.ObjectsNotFoundException;

@Service
public class CategoriaService {
	
	@Autowired
	private CategoriaRepository repo;
	
	public CategoriaService(CategoriaRepository repo) {
		this.repo = repo;
	}
	
	
	public List<Categoria> list() {
		List<Categoria> lista = new ArrayList<>();		
		lista = repo.findAll();
		return lista;	
	}
	
	public Categoria find(Integer id) {
		
		Optional<Categoria> obj = repo.findById(id);
		
		try{
			
			if(Objects.isNull(obj.get())) {}
			
		}catch(NoSuchElementException e) {
			throw new ObjectsNotFoundException("Objeto não econtrado! ID: " + id + ", Tipo: " + Categoria.class.getName());	
		}
		return obj.get();//DEVIDO AO CONTAINER Optional E NECESSARIO PEGAR O OBJ PELO GET
	}
	
	public Categoria insert(Categoria obj) {
		obj.setId(null);
		return repo.save(obj);
	}
	
	public Categoria update(Categoria obj) {
		find(obj.getId());
		return repo.save(obj);
	}
	
	public void delete(Integer id) {
		find(id);
		try {
			repo.deleteById(id);
		/*
		 * caso ocorra uma ConstraintViolationException do pacote: 
		 * import org.hibernate.exception.ConstraintViolationException
		 * 
		 * lanço uma do meu pacote de excessao:
		 * com.nelioalves.cursomc.services.exception.DataConstraintViolationException
		 */
		}catch(DataIntegrityViolationException e) {
			throw new DataIntegrityException("Não é possível excluír uma Categoria que possuí Produtos Associados!");
		}
		
	}
}
