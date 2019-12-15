package com.nelioalves.cursomc.resources;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nelioalves.cursomc.domain.Produto;
import com.nelioalves.cursomc.dto.ProdutoDTO;
import com.nelioalves.cursomc.resources.utils.URL;
import com.nelioalves.cursomc.services.ProdutoService;

@RestController
@RequestMapping(value="/produtos")
public class ProdutoResource {
	
	@Autowired
	private ProdutoService service;
	
	@RequestMapping(value="/listar", method=RequestMethod.GET)
	public ResponseEntity<List<Produto>> findAll(){
		List<Produto> obj = service.findAll();		
		return ResponseEntity.ok().body(obj);		
	}
	
	/*
	 * FOI INCLUÍDO O PARÂMETRO id PARA REALIZAR A BUSCA E FOI ACRESCENTADA A ANOTAÇÃO @PathVariable PARA QUE O VALOR DO ID SEJA PASSADO 
	 * PARA O MÉTODO COM PARÂMETRO 
	*/
	@RequestMapping(value="/{id}", method=RequestMethod.GET)
	public ResponseEntity<Produto> find(@PathVariable Integer id){
		Produto obj = service.find(id);
		/*
		 * FOI RETORNADO UM ResponseEntity QUE RETORNA UM HTML TRATADO COM O OBJETO DENTRO QUE ESTAMOS INCLUÍNDO NO BODY, QUANDO
		 *  FOR UM RETORNO OK
		*/
		return ResponseEntity.ok().body(obj);
		
	}
	
	/*
	 * metodo que retorna a lista PAGINADA de produtos de acordo com os parametros passados no GET
	*/
	@RequestMapping(method=RequestMethod.GET)
	public ResponseEntity<Page<ProdutoDTO>> findPage(
			@RequestParam(value="nome", defaultValue="0") String nome,
			@RequestParam(value="categorias", defaultValue="0") String categorias,
			@RequestParam(value="page", defaultValue="0") Integer page,
			@RequestParam(value="linesPerPage", defaultValue="24") Integer linesPerPage,
			@RequestParam(value="orderBy", defaultValue="nome") String orderBy,
			@RequestParam(value="direction", defaultValue="ASC") String direction){
		
		String nomeDecoded = URL.decodeParam(nome);
		List<Integer> ids = URL.decodeIntList(categorias);
		
		Page<Produto> list = service.search(nomeDecoded, ids, page, linesPerPage, orderBy, direction);
		
		/*ESTE METODO TEM A MESMA FUNCAO DO search, POREM USA O PADRAO DE NOME JPA*/
		//Page<Produto> list = service.findDistinctByNomeContainingAndCategoriasIn(nomeDecoded, ids, page, linesPerPage, orderBy, direction);		

		Page<ProdutoDTO> listDto = list.map(obj -> new ProdutoDTO(obj));		
		return ResponseEntity.ok().body(listDto);		
	}
}
