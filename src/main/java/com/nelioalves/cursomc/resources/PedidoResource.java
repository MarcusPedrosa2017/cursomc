package com.nelioalves.cursomc.resources;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.nelioalves.cursomc.domain.Pedido;
import com.nelioalves.cursomc.services.PedidoService;

@RestController
@RequestMapping(value="/pedidos")
public class PedidoResource {
	
	@Autowired
	private PedidoService service;
	
	@RequestMapping(value="/listar", method=RequestMethod.GET)
	public List<Pedido> listar(){
		return service.listar();			
	}
	
	/*
	 * FOI INCLUÍDO O PARÂMETRO id PARA REALIZAR A BUSCA E FOI ACRESCENTADA A ANOTAÇÃO @PathVariable PARA QUE O VALOR DO ID SEJA PASSADO 
	 * PARA O MÉTODO COM PARÂMETRO 
	*/
	@RequestMapping(value="/{id}", method=RequestMethod.GET)
	public ResponseEntity<?> find(@PathVariable Integer id){
		Pedido obj = service.buscar(id);
		/*
		 * FOI RETORNADO UM ResponseEntity QUE RETORNA UM HTML TRATADO COM O OBJETO DENTRO QUE ESTAMOS INCLUÍNDO NO BODY, QUANDO
		 *  FOR UM RETORNO OK
		*/
		return ResponseEntity.ok().body(obj);
		
	}
}
