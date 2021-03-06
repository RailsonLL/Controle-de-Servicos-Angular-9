package com.clientes.rest;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.clientes.model.entity.Cliente;
import com.clientes.model.repository.ClienteRepository;


//@CrossOrigin("http://localhost:4200") //o CrossOrigin é para habilitar a comunicação de entre back-end porta:8080 com o front-end porta:4200
@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

	@Autowired
	private ClienteRepository repository;
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Cliente salvar(@RequestBody @Valid Cliente cliente) { // o RequestBody é necessário para ler o paramatro JSON do Body da requisição http
		return repository.save(cliente);
	}
	
	@GetMapping("{id}")
	public Cliente acharPorId(@PathVariable Integer id) {
		return repository.findById(id).orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não encontrado.") );
	}
	
	@GetMapping
	public List<Cliente> obterTodos() {
		return repository.findAll();
	}
	
	@DeleteMapping("{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deletar(@PathVariable Integer id) {
		if ( !repository.existsById(id) ) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não encontrado.");
		
		try {
			Cliente cliente = repository.findById(id).get();
			if (cliente != null) repository.delete(cliente);
		} catch (Exception e) {
			e.printStackTrace();
		}	
		
		//método utilizando lambda function
	//	repository.findById(id).map( cliente -> {
	//								   repository.delete(cliente);
	//								   return Void.TYPE;
	//							   })
	//							   .orElseThrow( () -> 
	//							   new ResponseStatusException(HttpStatus.NOT_FOUND));
	}
	
	@PutMapping("{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void atualizar(@PathVariable Integer id, @RequestBody @Valid Cliente clienteAtualizado) {

		repository.findById(id).map( cliente -> {
									   cliente.setNome(clienteAtualizado.getNome());
									   cliente.setCpf(clienteAtualizado.getCpf());
									   return repository.save(cliente);
								   })
								   .orElseThrow( () -> 
								   new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não encontrado."));
	}
	
}
