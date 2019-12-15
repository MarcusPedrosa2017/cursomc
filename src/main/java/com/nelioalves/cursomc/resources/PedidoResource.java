package com.nelioalves.cursomc.resources;

import java.net.URI;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.nelioalves.cursomc.domain.Pedido;
import com.nelioalves.cursomc.services.PedidoService;

@RestController
@RequestMapping(value="/pedidos")
public class PedidoResource {
	
	@Autowired
	private PedidoService service;
	
	@RequestMapping(value="/listar", method=RequestMethod.GET)
	public ResponseEntity<List<Pedido>> findAll(){
		List<Pedido> obj = service.findAll();		
		return ResponseEntity.ok().body(obj);		
	}
	
	/*
	 * FOI INCLUÍDO O PARÂMETRO id PARA REALIZAR A BUSCA E FOI ACRESCENTADA A ANOTAÇÃO @PathVariable PARA QUE O VALOR DO ID SEJA PASSADO 
	 * PARA O MÉTODO COM PARÂMETRO 
	*/
	@RequestMapping(value="/{id}", method=RequestMethod.GET)
	public ResponseEntity<Pedido> find(@PathVariable Integer id){
		Pedido obj = service.find(id);
		/*
		 * FOI RETORNADO UM ResponseEntity QUE RETORNA UM HTML TRATADO COM O OBJETO DENTRO QUE ESTAMOS INCLUÍNDO NO BODY, QUANDO
		 *  FOR UM RETORNO OK
		*/
		return ResponseEntity.ok().body(obj);
		
	}
	
	@RequestMapping(method=RequestMethod.POST)
	public ResponseEntity<Void> insert(@Valid @RequestBody Pedido obj){
		obj = service.insert(obj);		
		//pega a uri do recurso request atual
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getId()).toUri();
		//retorna a uri acima no ResposeEntity
		return ResponseEntity.created(uri).build();
	}
}
