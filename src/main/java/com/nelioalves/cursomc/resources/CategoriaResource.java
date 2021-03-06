package com.nelioalves.cursomc.resources;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.nelioalves.cursomc.domain.Categoria;
import com.nelioalves.cursomc.dto.CategoriaDTO;
import com.nelioalves.cursomc.services.CategoriaService;

@RestController
@RequestMapping(value="/categorias")
public class CategoriaResource {
	
	
	@Autowired
	private CategoriaService service;
	
	@RequestMapping(value="/listar", method=RequestMethod.GET)
	public ResponseEntity<List<CategoriaDTO>> findAll(){
		List<Categoria> list = service.findAll();
		//conversao de Categoria para CategoriaDTO usando o construtor do dto e montando a lista com o recurso de stream
		List<CategoriaDTO> listDto = list.stream().map(obj -> new CategoriaDTO(obj)).collect(Collectors.toList());		
		return ResponseEntity.ok().body(listDto);		
	}
	
	/*
	 * FOI INCLUÍDO O PARÂMETRO id PARA REALIZAR A BUSCA E FOI ACRESCENTADA A ANOTAÇÃO @PathVariable PARA QUE O VALOR DO ID SEJA PASSADO 
	 * PARA O MÉTODO COM PARÂMETRO 
	*/
	@RequestMapping(value="/{id}", method=RequestMethod.GET)
	public ResponseEntity<Categoria> find(@PathVariable Integer id){
		Categoria obj = service.find(id);
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
	//anotacao utilizada para restringir o perfil que pode acessar este metodo
	@PreAuthorize("hasAnyRole('ADMIN')")
	public ResponseEntity<Void> insert(@Valid @RequestBody CategoriaDTO objDTO){
		Categoria obj = service.fromDTO(objDTO);
		obj = service.insert(obj);		
		//pega a uri do recurso request atual
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
				.path("/{id}").buildAndExpand(obj.getId()).toUri();
		//retorna a uri acima no ResposeEntity
		return ResponseEntity.created(uri).build();
	}
	
	@RequestMapping(value="/{id}", method=RequestMethod.PUT)
	@PreAuthorize("hasAnyRole('ADMIN')")
	public ResponseEntity<Void> update(@Valid @RequestBody CategoriaDTO objDto, @PathVariable Integer id){
		Categoria obj =  service.fromDTO(objDto);
		obj.setId(id);
		obj = service.update(obj);
		return ResponseEntity.noContent().build();
	}
	
	@RequestMapping(value="/{id}", method=RequestMethod.DELETE)
	@PreAuthorize("hasAnyRole('ADMIN')")
	public ResponseEntity<Void> delete(@PathVariable Integer id){
		service.delete(id);
		return ResponseEntity.noContent().build();
	}
	
	/*
	 * metodo que retorna a lista PAGINADA de categorias de acordo com os parametros passados no GET
	*/
	@RequestMapping(value="/page", method=RequestMethod.GET)
	public ResponseEntity<Page<CategoriaDTO>> findPage(
			@RequestParam(value="page", defaultValue="0") Integer page,
			@RequestParam(value="linesPerPage", defaultValue="24") Integer linesPerPage,
			@RequestParam(value="orderBy", defaultValue="nome") String orderBy,
			@RequestParam(value="direction", defaultValue="ASC") String direction){
		
		Page<Categoria> list = service.findPage(page, linesPerPage, orderBy, direction);		
		Page<CategoriaDTO> listDto = list.map(obj -> new CategoriaDTO(obj));		
		return ResponseEntity.ok().body(listDto);		
	}
}

