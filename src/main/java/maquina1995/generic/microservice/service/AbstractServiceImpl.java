package maquina1995.generic.microservice.service;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;

import maquina1995.generic.microservice.dto.AbstractDto;
import maquina1995.generic.microservice.entity.Persistible;
import maquina1995.generic.microservice.exception.EntityNotFoundException;
import maquina1995.generic.microservice.mapper.AbstractMapper;

/**
 * 
 * @param <K> Clave primaria de la entidad y del Dto
 * @param <E> Entidad
 * @param <D> Dto
 */
public abstract class AbstractServiceImpl<K extends Serializable, E extends Persistible<K>, D extends AbstractDto<K>>
        implements AbstractService<K, D> {

	@Autowired
	protected JpaRepository<E, K> repository;
	@Autowired
	protected AbstractMapper<E, D> mapper;

	@Override
	public D create(D dto) {

		E entity = mapper.dtoToEntity(dto);

		E entityPersisted = repository.save(entity);
		return mapper.entityToDto(entityPersisted);
	}

	@Override
	public D find(K id) throws EntityNotFoundException {

		E entity = repository.findById(id)
		        .orElseThrow(EntityNotFoundException::new);

		return mapper.entityToDto(entity);
	}

	@Override
	public List<D> findAll() {

		return repository.findAll()
		        .stream()
		        .map(mapper::entityToDto)
		        .collect(Collectors.toList());
	}

	@Override
	public D update(D dto) throws EntityNotFoundException {

		E entity = mapper.dtoToEntity(dto);

		E entityToUpdate = repository.findById(entity.getId())
		        .orElseThrow(EntityNotFoundException::new);

		entity.setId(entityToUpdate.getId());

		return mapper.entityToDto(repository.saveAndFlush(entity));
	}

	@Override
	public void delete(K id) throws EntityNotFoundException {

		E entity = repository.findById(id)
		        .orElseThrow(EntityNotFoundException::new);

		repository.deleteById(entity.getId());
	}

}