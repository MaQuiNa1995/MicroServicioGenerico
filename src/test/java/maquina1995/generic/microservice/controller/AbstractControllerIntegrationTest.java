package maquina1995.generic.microservice.controller;

import java.beans.Introspector;
import java.io.Serializable;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import maquina1995.generic.microservice.configuration.HibernateConfiguration;
import maquina1995.generic.microservice.configuration.TestConfiguration;
import maquina1995.generic.microservice.dto.AbstractDto;
import maquina1995.generic.microservice.dto.DtoWrapper;
import maquina1995.generic.microservice.entity.AbstractAuditable;
import maquina1995.generic.microservice.entity.Persistible;
import maquina1995.generic.microservice.exception.ErrorMessage;
import maquina1995.generic.microservice.service.AbstractService;
import maquina1995.generic.microservice.utils.AbstractReflectionTestUtils;

/**
 * Test abstracto del controller
 * 
 * @param <K> Clave primaria
 * @param <E> Entidad
 * @param <D> Dto
 */

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT,
        classes = { TestConfiguration.class, HibernateConfiguration.class })
abstract class AbstractControllerIntegrationTest<K extends Serializable,
        E extends AbstractAuditable & Persistible<K>,
        D extends AbstractDto<K>> extends AbstractReflectionTestUtils {

	@LocalServerPort
	protected Integer localPort;

	@Autowired
	protected TestRestTemplate testRestTemplate;
	@Autowired
	protected AbstractService<K, D> service;
	@Autowired
	protected JpaRepository<E, K> repository;

	@BeforeEach
	public void setUp() {
		repository.deleteAll();
	}

	/**
	 * Creación exitosa
	 */
	@Test
	void createTest() {
		// given
		D dto = (D) super.generateObjectFromGeneric(2, this.getClass());
		HttpEntity<D> httpEntity = new HttpEntity<>(dto);

		// when
		ResponseEntity<DtoWrapper<D>> responseEntity = testRestTemplate.exchange(this.getPathOfParametizedController()
		        .toString(), HttpMethod.POST, httpEntity, createParametyzedTypeReference());

		// then
		DtoWrapper<D> body = responseEntity.getBody();
		Assertions.assertNotNull(body);

		List<D> receivedDtos = body.getAbstractHatetoasDtos();
		Assertions.assertEquals(HttpStatus.CREATED.value(), responseEntity.getStatusCodeValue());
		Assertions.assertEquals(dto, receivedDtos.get(0));
	}

	/**
	 * Obtención exitosa
	 */
	@Test
	void getTest() {
		// given
		E entity = saveEntity();
		ParameterizedTypeReference<DtoWrapper<D>> parametyzedTypeReference = this.createParametyzedTypeReference();

		// when
		ResponseEntity<DtoWrapper<D>> responseBody = testRestTemplate.exchange(
		        this.getPathOfParametizedController(entity.getId()), HttpMethod.GET, null, parametyzedTypeReference,
		        entity.getId());

		// then
		Assertions.assertEquals(HttpStatus.OK, responseBody.getStatusCode());

		List<D> dto = responseBody.getBody()
		        .getAbstractHatetoasDtos();

		Assertions.assertNotNull(dto);
		Assertions.assertEquals(entity.getId(), dto.get(0)
		        .getId());
	}

	/**
	 * Obtención de algo que no existe en Base de datos
	 */
	@Test
	void getNotFoundTest() {
		// given
		K id = (K) super.generateObjectFromGeneric(0, this.getClass());
		String pathToAttack = this.getPathOfParametizedController(id);

		// when
		ResponseEntity<DtoWrapper<D>> responseBody = testRestTemplate.exchange(pathToAttack, HttpMethod.GET, null,
		        this.createParametyzedTypeReference(), id);

		// then
		Assertions.assertEquals(HttpStatus.NOT_FOUND, responseBody.getStatusCode());
	}

	/**
	 * Obtención de todos exitosa
	 */
	@Test
	void getAllTest() {
		// given
		for (int i = 0; i < 3; i++) {
			this.saveEntity();
		}

		// when
		ResponseEntity<DtoWrapper<D>> responseEntity = testRestTemplate.exchange(
		        this.getPathOfParametizedController() + "/", HttpMethod.GET, null,
		        this.createParametyzedTypeReference());

		// then
		Assertions.assertEquals(HttpStatus.OK.value(), responseEntity.getStatusCodeValue());
		DtoWrapper<D> body = responseEntity.getBody();
		Assertions.assertNotNull(body);
		Assertions.assertEquals(3, body.getAbstractHatetoasDtos()
		        .size());
		body.getAbstractHatetoasDtos()
		        .forEach(Assertions::assertNotNull);
	}

	/**
	 * Obtención de todos sin que haya nada en base de datos
	 */
	@Test
	void getAllNotFoundTest() {
		// when
		ResponseEntity<DtoWrapper<D>> responseEntity = testRestTemplate.exchange(
		        this.getPathOfParametizedController() + "/", HttpMethod.GET, null,
		        this.createParametyzedTypeReference());

		// then
		Assertions.assertEquals(HttpStatus.NO_CONTENT.value(), responseEntity.getStatusCodeValue());
		Assertions.assertNull(responseEntity.getBody());
	}

	/**
	 * Actualización exitosa
	 */
	@Test
	void patchTest() {
		// given
		E entity = saveEntity();

		D dto = (D) super.generateObjectFromGeneric(2, this.getClass());
		dto.setId(entity.getId());
		HttpEntity<D> httpEntity = new HttpEntity<>(dto);

		// when
		ResponseEntity<DtoWrapper<D>> responseEntity = testRestTemplate.exchange(this.getPathOfParametizedController()
		        .toString(), HttpMethod.PATCH, httpEntity, this.getWrapperFromDto());

		// then
		DtoWrapper<D> body = responseEntity.getBody();
		Assertions.assertNotNull(body);

		Assertions.assertEquals(HttpStatus.OK.value(), responseEntity.getStatusCodeValue());

		List<D> persistedDtos = body.getAbstractHatetoasDtos();
		Assertions.assertEquals(1, persistedDtos.size());

		D persistedDto = persistedDtos.get(0);

		Assertions.assertEquals(dto, persistedDto);
		Assertions.assertEquals(entity.getId(), persistedDto.getId());
	}

	/**
	 * Actualizacion de algo que no existe en Base de datos
	 */
	@Test
	void patchNotFoundTest() {
		// given
		D dto = (D) super.generateObjectFromGeneric(2, this.getClass());
		K id = (K) super.generateObjectFromGeneric(0, this.getClass());
		dto.setId(id);

		HttpEntity<D> httpEntity = new HttpEntity<>(dto);

		// when
		ResponseEntity<ErrorMessage> pathOperation = testRestTemplate.exchange(this.getPathOfParametizedController()
		        .toString(), HttpMethod.PATCH, httpEntity, ErrorMessage.class);

		// then
		Assertions.assertEquals(HttpStatus.NOT_FOUND, pathOperation.getStatusCode());
	}

	/**
	 * Eliminación exitosa
	 */
	@Test
	void deleteTest() {
		// given
		K id = this.saveEntity()
		        .getId();

		String pathToAttack = this.getPathOfParametizedController(id);

		// when
		ResponseEntity<DtoWrapper<D>> responseEntity = testRestTemplate.exchange(pathToAttack, HttpMethod.DELETE, null,
		        this.getWrapperFromDto());

		// then
		Assertions.assertEquals(HttpStatus.NO_CONTENT.value(), responseEntity.getStatusCodeValue());
	}

	/**
	 * Eliminación de algo que no existe en Base de datos
	 */
	@Test
	void deleteNotFoundTest() {
		// given
		K id = (K) super.generateObjectFromGeneric(0, this.getClass());

		String pathToExecute = this.getPathOfParametizedController(id);
		HttpEntity<K> httpEntity = new HttpEntity<>(id);

		// when
		ResponseEntity<DtoWrapper<D>> responseEntity = testRestTemplate.exchange(pathToExecute, HttpMethod.DELETE,
		        httpEntity, this.getWrapperFromDto(), id);

		// then
		Assertions.assertEquals(HttpStatus.NOT_FOUND.value(), responseEntity.getStatusCodeValue());
	}

	/**
	 * Obtiene un endpoint del controller construido a partir del nombre de la clase
	 * de dominio asociada {@link E}
	 * 
	 * @return {@link String} ruta raiz del controller
	 */
	protected StringBuilder getPathOfParametizedController() {
		String entityName = super.getClassFromIndex(this.getClass(), 1).getSimpleName();

		return new StringBuilder("http://localhost:").append(localPort)
		        .append("/")
		        .append(Introspector.decapitalize(entityName));
	}

	/**
	 * Obtiene un endpoint del controller construido a partir del nombre de la clase
	 * de dominio asociada {@link E} y su clave primaria {@link K} en formato
	 * {@link String}
	 * 
	 * @return {@link String} ruta raiz del controller
	 */
	protected String getPathOfParametizedController(K id) {
		return this.getPathOfParametizedController()
		        .append("/")
		        .append(id)
		        .toString();
	}

	/**
	 * Obtiene la {@link Class} de un {@link DtoWrapper} que alberga un {@link D}
	 * 
	 * @return {@link Class} de un {@link DtoWrapper} que alberga un {@link D}
	 */
	protected Class<DtoWrapper<D>> getWrapperFromDto() {
		return (Class<DtoWrapper<D>>) new DtoWrapper<D>().getClass();
	}

	/**
	 * Crea {@link ParameterizedTypeReference} de un {@link DtoWrapper} que alberga
	 * un {@link D}
	 * 
	 * @return {@link ParameterizedTypeReference} de un {@link DtoWrapper} que
	 *         alberga un {@link D}
	 */
	private ParameterizedTypeReference<DtoWrapper<D>> createParametyzedTypeReference() {
		return new ParameterizedTypeReference<DtoWrapper<D>>() {
		};
	}

	private E saveEntity() {
		E entity = (E) super.generateObjectFromGeneric(1, this.getClass());
		entity.setId(null);
		repository.save(entity);
		return entity;
	}

}
