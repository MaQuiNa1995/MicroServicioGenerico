package maquina1995.generic.microservice.service;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import maquina1995.generic.microservice.configuration.HibernateConfiguration;
import maquina1995.generic.microservice.configuration.TestConfiguration;
import maquina1995.generic.microservice.dto.AbstractDto;
import maquina1995.generic.microservice.entity.AbstractAuditable;
import maquina1995.generic.microservice.entity.Persistible;
import maquina1995.generic.microservice.exception.EntityNotFoundException;
import maquina1995.generic.microservice.mapper.AbstractMapper;
import maquina1995.generic.microservice.service.AbstractService;
import maquina1995.generic.microservice.utils.AbstractReflectionTestUtils;

@Transactional
@SpringBootTest(classes = { HibernateConfiguration.class, TestConfiguration.class })
abstract class AbstractGenericServiceTest<K extends Serializable,
        E extends AbstractAuditable & Persistible<K>,
        D extends AbstractDto<K>> extends AbstractReflectionTestUtils {

	@PersistenceContext
	protected EntityManager entityManager;
	@Autowired
	protected AbstractService<K, D> sut;
	@Autowired
	protected AbstractMapper<E, D> mapper;

	@Test
	void createTest() {
		// given
		D dto = (D) super.generateObjectFromGeneric(2, this.getClass());

		// when
		D persistedDto = sut.create(dto);

		// then
		Assertions.assertNotNull(persistedDto.getId());
		Assertions.assertEquals(dto, persistedDto);
	}

	@Test
	void getTest() throws EntityNotFoundException {
		// given
		E entity = (E) super.generateObjectFromGeneric(1, this.getClass());

		entity.setId(null);
		entityManager.persist(entity);

		// when
		D dto = sut.find(entity.getId());

		// then
		E dtoToEntity = mapper.dtoToEntity(dto);
		Assertions.assertEquals(dtoToEntity, entity);
	}

	@Test
	void getAllTest() {
		// given
		for (int i = 0; i < 2; i++) {
			this.saveEntity();
		}

		// when
		List<D> dtos = sut.findAll();

		// then
		Assertions.assertEquals(2, dtos.size());
	}

	@Test
	void patchTest() throws EntityNotFoundException {
		// given
		D dto = (D) super.generateObjectFromGeneric(2, getClass());

		dto.setId(this.saveEntity()
		        .getId());

		// when
		D updatedDto = sut.update(dto);

		// then
		Assertions.assertEquals(dto, updatedDto);
	}

	@Test
	void deleteTest() throws EntityNotFoundException {
		// given
		E entity = this.saveEntity();

		// when
		sut.delete(entity.getId());

		// then
		Assertions.assertTrue(sut.findAll()
		        .isEmpty());
	}

	private E saveEntity() {
		E entity = (E) super.generateObjectFromGeneric(1, this.getClass());
		entity.setId(null);
		entityManager.persist(entity);
		return entity;
	}

}
