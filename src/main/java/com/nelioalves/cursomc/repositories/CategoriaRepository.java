package com.nelioalves.cursomc.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nelioalves.cursomc.domain.Categoria;

//ANOTAÇÃO PARA IDENTIFICAR CLASSES DE ACESSO A DADOS, PARA ISSO EXTEND JpaRepository
@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Integer>{

}
