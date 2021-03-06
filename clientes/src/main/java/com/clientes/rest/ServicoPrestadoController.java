package com.clientes.rest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.clientes.model.entity.Cliente;
import com.clientes.model.entity.ServicoPrestado;
import com.clientes.model.repository.ClienteRepository;
import com.clientes.model.repository.ServicoPrestadoRepository;
import com.clientes.rest.dto.ServicoPrestadoDTO;
import com.clientes.util.BigDecimalConverter;

@RestController
@RequestMapping("/api/servicos")
public class ServicoPrestadoController {

	@Autowired
	private ServicoPrestadoRepository servicoRepository;
	@Autowired
	private ClienteRepository clienteRepository;
	@Autowired
	private BigDecimalConverter bigDecimalConverter;
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ServicoPrestado salvar(@RequestBody @Valid ServicoPrestadoDTO dto) {
		System.out.println("entrou");
		LocalDate data = LocalDate.parse(dto.getData(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
		
		//Optional<Cliente> cliente = clienteRepository.findById(dto.getIdCliente());
		//if (!cliente.isPresent()) { //lança exceção }
		
		Cliente cliente = clienteRepository.findById(dto.getIdCliente())
				                           .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
				                        		                                          "Cliente não existe."));
		
		ServicoPrestado servico = new ServicoPrestado();
		servico.setDescricao(dto.getDescricao());
		servico.setData(data);
		servico.setValor(bigDecimalConverter.converter(dto.getValor()));
		servico.setCliente(cliente);
		
		return servicoRepository.save(servico);
	}
	
	@GetMapping("{id}")
	public ServicoPrestado acharPorId(@PathVariable Integer id) {
		return servicoRepository.findById(id).orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Serviço não encontrado.") );
	}
		
	@DeleteMapping("{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deletar(@PathVariable Integer id) {
		if ( !servicoRepository.existsById(id) ) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Serviço não encontrado.");
		
		try {
			ServicoPrestado servico = servicoRepository.findById(id).get();
			if (servico != null) servicoRepository.delete(servico);
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}
	
	@PutMapping("{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void atualizar(@PathVariable Integer id, @RequestBody @Valid ServicoPrestado servicoAtualizado) {

		servicoRepository.findById(id).map( servico -> {
									   servico.setDescricao(servicoAtualizado.getDescricao());
									   servico.setCliente(servicoAtualizado.getCliente());
									   servico.setValor(servicoAtualizado.getValor());
									   servico.setData(servicoAtualizado.getData());
									   return servicoRepository.save(servico);
								   })
								   .orElseThrow( () -> 
								   new ResponseStatusException(HttpStatus.NOT_FOUND, "Serviço não encontrado."));
	}
	
	@GetMapping
	public List<ServicoPrestado> pesquisar(
			@RequestParam(value = "nome", required = false, defaultValue = "") String nome,
			@RequestParam(value = "mes", required = false) Integer mes
	) {
		return servicoRepository.findByNomeClienteAndMes(nome, mes);
	}
	
}
