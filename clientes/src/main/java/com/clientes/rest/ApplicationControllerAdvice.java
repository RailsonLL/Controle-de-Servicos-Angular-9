package com.clientes.rest;

import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import com.clientes.rest.exception.ApiErrors;

@RestControllerAdvice
public class ApplicationControllerAdvice {

	@ExceptionHandler(MethodArgumentNotValidException.class) //método interceptador de errors
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ApiErrors handleValidationErrors( MethodArgumentNotValidException ex) {
		BindingResult bindingResult = ex.getBindingResult(); //recebe os argumentos da exception
		
		List<String> messages = bindingResult.getAllErrors() //retorna uma lista das mensagens
				.stream()
				.map(objectError -> objectError.getDefaultMessage())
				.collect(Collectors.toList());
		
		return new ApiErrors(messages);
	}
	
	@ExceptionHandler(ResponseStatusException.class)
	public ResponseEntity handleResponseStatusException(ResponseStatusException ex) {
		String mensagemErro = ex.getMessage();
		HttpStatus codigoStatus = ex.getStatus();
		
		ApiErrors apiErrors = new ApiErrors(mensagemErro);
		return new ResponseEntity(apiErrors, codigoStatus);
	}
	
}