package com.nelioalves.cursomc.services;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.nelioalves.cursomc.domain.Cliente;
import com.nelioalves.cursomc.dto.ClienteDTO;
import com.nelioalves.cursomc.repositories.ClienteRepository;
import com.nelioalves.cursomc.services.exception.DataIntegrityException;
import com.nelioalves.cursomc.services.exception.ObjectsNotFoundException;

@Service
public class ClienteService {
	
	@Autowired
	private ClienteRepository repo;
	
	public ClienteService(ClienteRepository repo) {
		this.repo = repo;
	}
	
	public List<Cliente> findAll() {
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
	
	public Cliente insert(Cliente obj) {
		obj.setId(null);
		return repo.save(obj);
	}
	
	public Cliente update(Cliente obj) {
		Cliente newObj = find(obj.getId());
		//metodo que atualiza dos dados do objeto antes de salvar
		updateData(newObj, obj);
		return repo.save(newObj);
	}
	
	public void delete(Integer id) {
		find(id);
		try {
			repo.deleteById(id);
		/*
		 * caso ocorra uma DataIntegrityViolationException do pacote: 
		 * org.springframework.dao.DataIntegrityViolationException;
		 * 
		 * lanço uma do meu pacote de excessao:
		 * com.nelioalves.cursomc.services.exception.DataIntegrityException
		 */
		}catch(DataIntegrityViolationException e) {
			throw new DataIntegrityException("Não é possível excluír uma Cliente que possuí Entidades Associadas!");
		}
		
	}
	
	//lista paginando
	public Page<Cliente> findPage(Integer page, Integer linesPerPage, String orderBy, String direction){
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		return repo.findAll(pageRequest);
	}
	
	public Cliente fromDTO(ClienteDTO objDto) {
		return new Cliente(objDto.getId(), objDto.getNome(), objDto.getEmail(), null, null);
	}
	
	private void updateData(Cliente newObj, Cliente obj) {
		newObj.setNome(obj.getNome());
		newObj.setEmail(obj.getEmail());
	}
}
