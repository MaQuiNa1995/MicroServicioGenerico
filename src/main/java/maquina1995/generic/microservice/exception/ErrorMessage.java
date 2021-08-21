package maquina1995.generic.microservice.exception;

import java.time.OffsetDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ErrorMessage {
	private Integer statusCode;
	@JsonFormat(shape = JsonFormat.Shape.STRING,
	        pattern = "uuuu-MM-dd'T'HH:mm:ss.SSSXXXX")
	private OffsetDateTime timestamp;
	private String message;

}
