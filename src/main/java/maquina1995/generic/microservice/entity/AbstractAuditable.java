package maquina1995.generic.microservice.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.MappedSuperclass;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenerationTime;
import org.hibernate.annotations.GeneratorType;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.Getter;
import lombok.Setter;
import maquina1995.generic.microservice.repository.AuditManager;

@Getter
@Setter
@MappedSuperclass
public abstract class AbstractAuditable implements Serializable {

	@GeneratorType(type = AuditManager.class,
	        when = GenerationTime.INSERT)
	protected String creadoPor;

	@CreationTimestamp
	protected LocalDateTime fechaCreacion;

	@GeneratorType(type = AuditManager.class,
	        when = GenerationTime.ALWAYS)
	protected String modificadoPor;

	@UpdateTimestamp
	protected LocalDateTime fechaModificacion;

}
