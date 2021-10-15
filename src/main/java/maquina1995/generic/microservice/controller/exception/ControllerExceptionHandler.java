package maquina1995.generic.microservice.controller.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import maquina1995.generic.microservice.constant.SwaggerConstants;
import maquina1995.generic.microservice.exception.EntityNotFoundException;
import maquina1995.generic.microservice.exception.ErrorMessage;

@RestControllerAdvice(basePackages = SwaggerConstants.PROJECT_CONTROLLER_PATH)
public class ControllerExceptionHandler {

	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ExceptionHandler(value = EntityNotFoundException.class)
	public ErrorMessage resourceNotFoundException(EntityNotFoundException exception, WebRequest request) {
		return ErrorMessage.builder()
		        .message(exception.getMessage())
		        .statusCode(HttpStatus.NOT_FOUND.value())
		        .build();
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
		Map<String, String> errors = new HashMap<>();
		ex.getBindingResult()
		        .getAllErrors()
		        .forEach(error -> {
			        String fieldName = ((FieldError) error).getField();
			        String errorMessage = error.getDefaultMessage();
			        errors.put(fieldName, errorMessage);
		        });
		return errors;
	}

}
