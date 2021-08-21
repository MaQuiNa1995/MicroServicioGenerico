package maquina1995.generic.microservice.dto;

import java.io.Serializable;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
public abstract class AbstractDto<K extends Serializable> implements Serializable {
	private K id;
}
