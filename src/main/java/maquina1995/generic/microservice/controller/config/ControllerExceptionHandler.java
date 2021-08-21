package maquina1995.generic.microservice.controller.config;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import maquina1995.generic.microservice.exception.EntityNotFoundException;
import maquina1995.generic.microservice.exception.ErrorMessage;

@RestControllerAdvice(basePackages = "maquina1995.generic.microservice.controller")
public class ControllerExceptionHandler {

	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ExceptionHandler(value = EntityNotFoundException.class)
	public ErrorMessage resourceNotFoundException(EntityNotFoundException exception, WebRequest request) {
		return ErrorMessage.builder()
		        .message(exception.getMessage())
		        .statusCode(HttpStatus.NOT_FOUND.value())
		        .build();
	}

}
