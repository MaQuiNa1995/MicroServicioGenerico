package maquina1995.generic.microservice.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import maquina1995.generic.microservice.dto.AbstractDto;

@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TestClassDto extends AbstractDto<Long> {

	private String name;
	private Boolean active;

}
