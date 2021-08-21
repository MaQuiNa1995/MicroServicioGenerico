package maquina1995.generic.microservice.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import maquina1995.generic.microservice.entity.AbstractAuditable;
import maquina1995.generic.microservice.entity.Persistible;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false,
        onlyExplicitlyIncluded = true)
@ToString
public class TestClass extends AbstractAuditable implements Persistible<Long> {

	@GeneratedValue(generator = "sequence",
	        strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "sequence",
	        allocationSize = 1)
	@Id
	private Long id;
	@EqualsAndHashCode.Include
	private String name;
	@EqualsAndHashCode.Include
	private Boolean active;

}
