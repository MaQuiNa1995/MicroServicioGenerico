package maquina1995.generic.microservice.exception;

public class EntityNotFoundException extends Exception {

	public EntityNotFoundException() {
		super("The entity was not found");
	}

}
