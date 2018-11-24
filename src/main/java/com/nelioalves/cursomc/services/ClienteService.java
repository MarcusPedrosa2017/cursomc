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

import com.nelioalves.cursomc.domain.Cidade;
import com.nelioalves.cursomc.domain.Cliente;
import com.nelioalves.cursomc.domain.Endereco;
import com.nelioalves.cursomc.domain.enums.TipoCliente;
import com.nelioalves.cursomc.dto.ClienteDTO;
import com.nelioalves.cursomc.dto.ClienteNewDTO;
import com.nelioalves.cursomc.repositories.CidadeRepository;
import com.nelioalves.cursomc.repositories.ClienteRepository;
import com.nelioalves.cursomc.repositories.EnderecoRepository;
import com.nelioalves.cursomc.services.exception.DataIntegrityException;
import com.nelioalves.cursomc.services.exception.ObjectsNotFoundException;

@Service
public class ClienteService {
	
	@Autowired
	private ClienteRepository repo;
	
	@Autowired
	private CidadeRepository cidadeRepository;
	
	@Autowired
	private EnderecoRepository enderecoRepository;
	
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
		obj = repo.save(obj);
		enderecoRepository.saveAll(obj.getEndrecos());
		return obj;
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
	
	public Cliente fromDTO(ClienteNewDTO objDto) {
		Cliente cliente = new Cliente(null, objDto.getNome(), objDto.getEmail(), objDto.getCpfOuCnpj()
				, TipoCliente.toEnum(objDto.getTipo()));
		
		Optional<Cidade> cid = cidadeRepository.findById(objDto.getCidadeId());
		
		Cidade cidade = new Cidade();
		
		if(Objects.nonNull(cid.get())) {
			if(Objects.nonNull(cid.get().getEstado())) {
				cidade.setEstado(cid.get().getEstado());
			}
			if(Objects.nonNull(cid.get().getId())) {
				cidade.setId(cid.get().getId());
			}
			if(Objects.nonNull(cid.get().getNome())) {
				cidade.setNome(cid.get().getNome());
			}
		}
				
		Endereco endereco = new Endereco(null, objDto.getLogradouro(), objDto.getNumero(), objDto.getComplemento(), objDto.getBairro()
				, objDto.getCep(), cliente, cidade);
		
		cliente.getEndrecos().add(endereco);
		
		if(Objects.nonNull(objDto.getTelefone1())) {
			cliente.getTelefones().add(objDto.getTelefone1());
		}
		if(Objects.nonNull(objDto.getTelefone2())) {
			cliente.getTelefones().add(objDto.getTelefone2());
		}
		if(Objects.nonNull(objDto.getTelefone3())) {
			cliente.getTelefones().add(objDto.getTelefone3());
		}
		
		return cliente;
	}
	
	private void updateData(Cliente newObj, Cliente obj) {
		newObj.setNome(obj.getNome());
		newObj.setEmail(obj.getEmail());
	}
}
