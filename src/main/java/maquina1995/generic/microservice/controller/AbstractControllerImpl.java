package maquina1995.generic.microservice.controller;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;

import maquina1995.generic.microservice.dto.AbstractDto;
import maquina1995.generic.microservice.dto.DtoWrapper;
import maquina1995.generic.microservice.exception.EntityNotFoundException;
import maquina1995.generic.microservice.service.AbstractService;

/**
 * Clase abstracta para la creación de webservices de nivel 3 HATETOAS
 *
 * @param <K> clave primaria de la entidad
 * @param <D> dto
 */
public abstract class AbstractControllerImpl<K extends Serializable, D extends AbstractDto<K>>
        implements AbstractController<K, D> {

	@Autowired
	protected AbstractService<K, D> service;

	@Override
	@PostMapping
	public ResponseEntity<DtoWrapper<D>> create(D dto) {
		D createdDto = service.create(dto);

		return ResponseEntity.status(HttpStatus.CREATED)
		        .body(new DtoWrapper<>(createdDto));
	}

	@Override
	@GetMapping({ "/", "/{id}" })
	public ResponseEntity<DtoWrapper<D>> find(Optional<K> id) throws EntityNotFoundException {

		List<D> dtos = id.isPresent() ? Arrays.asList(service.find(id.get())) : service.findAll();

		ResponseEntity<DtoWrapper<D>> response;
		if (dtos.isEmpty()) {
			response = ResponseEntity.noContent()
			        .build();
		} else {
			response = ResponseEntity.ok(new DtoWrapper<>(dtos));
		}

		return response;
	}

	@Override
	@PatchMapping
	public ResponseEntity<DtoWrapper<D>> update(D dto) throws EntityNotFoundException {
		D updatedDto = service.update(dto);

		return ResponseEntity.ok(new DtoWrapper<>(updatedDto));
	}

	@Override
	@DeleteMapping("/{id}")
	public ResponseEntity<DtoWrapper<D>> delete(K id) throws EntityNotFoundException {
		service.delete(id);
		return ResponseEntity.status(HttpStatus.NO_CONTENT)
		        .build();
	}

}
