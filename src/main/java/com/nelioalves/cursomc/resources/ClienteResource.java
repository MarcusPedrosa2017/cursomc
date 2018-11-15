package com.nelioalves.cursomc.resources;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.nelioalves.cursomc.domain.Cliente;
import com.nelioalves.cursomc.dto.CategoriaDTO;
import com.nelioalves.cursomc.dto.ClienteDTO;
import com.nelioalves.cursomc.services.ClienteService;

@RestController
@RequestMapping(value="/clientes")
public class ClienteResource {
	
	
	@Autowired
	private ClienteService service;
	
	@RequestMapping(value="/listar", method=RequestMethod.GET)
	public ResponseEntity<List<ClienteDTO>> findAll(){
		List<Cliente> list = service.findAll();
		//conversao de Cliente para ClienteDTO usando o construtor do dto e montando a lista com o recurso de stream
	    List<ClienteDTO> listDto = list.stream().map(obj -> new ClienteDTO(obj)).collect(Collectors.toList());	
		return ResponseEntity.ok().body(listDto);		
	}
	
	/*
	 * FOI INCLUÍDO O PARÂMETRO id PARA REALIZAR A BUSCA E FOI ACRESCENTADA A ANOTAÇÃO @PathVariable PARA QUE O VALOR DO ID SEJA PASSADO 
	 * PARA O MÉTODO COM PARÂMETRO 
	*/
	@RequestMapping(value="/{id}", method=RequestMethod.GET)
	public ResponseEntity<Cliente> find(@PathVariable Integer id){
		Cliente obj = service.find(id);
		/*
		 * FOI RETORNADO UM ResponseEntity QUE RETORNA UM HTML TRATADO COM O OBJETO DENTRO QUE ESTAMOS INCLUÍNDO NO BODY, QUANDO
		 *  FOR UM RETORNO OK
		*/
		return ResponseEntity.ok().body(obj);
		
	}
	
	/*
	 * FOI INCLUIDA A ANOTACAO @Valid PARA PODER VALIDAR OS CAMPOS DE ACORDO COM AS ANOTAÇOES INCLUÍDAS NO DTO
	 */
	@RequestMapping(method=RequestMethod.POST)
	public ResponseEntity<Void> insert(@Valid @RequestBody ClienteDTO objDTO){
		Cliente obj = service.fromDTO(objDTO);
		obj = service.insert(obj);		
		//pega a uri do recurso request atual
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
				.path("/{id}").buildAndExpand(obj.getId()).toUri();
		//retorna a uri acima no ResposeEntity
		return ResponseEntity.created(uri).build();
	}
	
	@RequestMapping(value="/{id}", method=RequestMethod.PUT)
	public ResponseEntity<Void> update(@Valid @RequestBody ClienteDTO objDto, @PathVariable Integer id){
		Cliente obj =  service.fromDTO(objDto);
		obj.setId(id);
		obj = service.update(obj);
		return ResponseEntity.noContent().build();
	}
	
	@RequestMapping(value="/{id}", method=RequestMethod.DELETE)
	public ResponseEntity<Void> delete(@PathVariable Integer id){
		service.delete(id);
		return ResponseEntity.noContent().build();
	}
	
	/*
	 * metodo que retorna a lista PAGINADA de clientes de acordo com os parametros passados no GET
	*/
	@RequestMapping(value="/page", method=RequestMethod.GET)
	public ResponseEntity<Page<ClienteDTO>> findPage(
			@RequestParam(value="page", defaultValue="0") Integer page,
			@RequestParam(value="linesPerPage", defaultValue="24") Integer linesPerPage,
			@RequestParam(value="orderBy", defaultValue="nome") String orderBy,
			@RequestParam(value="direction", defaultValue="ASC") String direction){
		
		Page<Cliente> list = service.findPage(page, linesPerPage, orderBy, direction);		
		Page<ClienteDTO> listDto = list.map(obj -> new ClienteDTO(obj));		
		return ResponseEntity.ok().body(listDto);		
	}
}

