package com.clientes.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.clientes.model.entity.ServicoPrestado;


public interface ServicoPrestadoRepository extends JpaRepository<ServicoPrestado, Integer> {

	/**
	 * Método para buscar os Serviços do cliente filtrados pelo nome do cliente e mês
	 */
	@Query(" select s from ServicoPrestado s join s.cliente c " + // o Query faz a pesquisa baseado nos atributos da classe e não das tabelas do banco
	       " where upper(c.nome) like CONCAT('%', upper(:nome), '%') and MONTH(s.data) = :mes ")
	List<ServicoPrestado> findByNomeClienteAndMes(@Param("nome") String nome, @Param("mes") Integer mes); // o Param atribui o valor de um parametro utilizado dentro da query

}
