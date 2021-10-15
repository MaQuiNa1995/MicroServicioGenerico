package maquina1995.generic.microservice.controller;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import maquina1995.generic.microservice.dto.AbstractDto;
import maquina1995.generic.microservice.dto.DtoWrapper;
import maquina1995.generic.microservice.exception.EntityNotFoundException;

public interface AbstractController<K extends Serializable, D extends AbstractDto<K>> {

	/**
	 * Endpoint que se encarga de la creacion de un {@link D}
	 * <p>
	 * Si va bien la operación devuelve un {@link HttpStatus#CREATED}
	 * 
	 * @param dto {@link D} representa el dto a crear
	 * 
	 * @return {@link ResponseEntity} con {@link DtoWrapper} de cuerpo y una
	 *         {@link List} de {@link D}
	 */
	ResponseEntity<DtoWrapper<D>> create(@RequestBody D dto);

	/**
	 * Endpoint que se encarga de la busqueda de un {@link D} por {@link K}
	 * <p>
	 * Si va bien la operación devuelve al menos un objeto devuelve un
	 * {@link HttpStatus#OK}
	 * <p>
	 * Si la operación devuelve una lista vacía se devovlerá un
	 * {@link HttpStatus#NO_CONTENT}
	 * <p>
	 * Si al contrario no encuentra la entidad especificada por su ID {@link K}
	 * devuelve un {@link HttpStatus#NOT_FOUND}
	 * <p>
	 * 
	 * @see <b>Lecciones Aprendidas:</b> El parámetro del método debe tener el mismo
	 *      nombre que el que hayamos puesto en el path
	 * 
	 * @param id {@link Optional} de {@link K} que representa e pasado por parámetro
	 * 
	 * @return {@link ResponseEntity} con {@link DtoWrapper} de cuerpo y una
	 *         {@link List} de {@link D}
	 * 
	 * @throws EntityNotFoundException Representa la situación en la que no se
	 *                                 encuentra el recurso especificado por el
	 *                                 parámetro id
	 *                                 <p>
	 *                                 Es controlada por
	 *                                 {@link maquina1995.generic.microservice.controller.exception.ControllerExceptionHandler}
	 */
	ResponseEntity<DtoWrapper<D>> find(@PathVariable Optional<K> id) throws EntityNotFoundException;

	/**
	 * Endpoint que se encarga de la actualización de una entidad de base de datos
	 * <p>
	 * Si va bien la operación devuelve {@link HttpStatus#OK}
	 * <p>
	 * Si el la clave primaria {@link K} del {@link D} proporcionado no se encuentra
	 * en BD se devuelve un {@link HttpStatus#NOT_FOUND}
	 * 
	 * @param dto {@link D} representa el dto a crear
	 * 
	 * @return {@link ResponseEntity} con {@link DtoWrapper} de cuerpo y una
	 *         {@link List} de {@link D}
	 * @throws EntityNotFoundException Representa la situación en la que no se
	 *                                 encuentra el recurso especificado por el
	 *                                 {@link K} del {@link D}
	 *                                 <p>
	 *                                 Es controlada por
	 *                                 {@link maquina1995.generic.microservice.controller.exception.ControllerExceptionHandler}
	 * 
	 */
	ResponseEntity<DtoWrapper<D>> update(@RequestBody D dto) throws EntityNotFoundException;

	/**
	 * Endpoint que se encarga de la busqueda y eliminación de un {@link D} por
	 * {@link K}
	 * <p>
	 * Si va bien la operación tiene éxito devuelve un {@link HttpStatus#NO_CONTENT}
	 * <p>
	 * Si la operación no encuentra el recursos a borrar se devolverá un
	 * {@link HttpStatus#NOT_FOUND}
	 * 
	 * @param id {@link K} representa el id pasado por parámetro
	 * 
	 * @return {@link ResponseEntity} con {@link DtoWrapper} de cuerpo y una
	 *         {@link List} de {@link D}
	 * 
	 * @throws EntityNotFoundException Representa la situación en la que no se
	 *                                 encuentra el recurso especificado por el
	 *                                 {@link K} del {@link D}
	 *                                 <p>
	 *                                 Es controlada por
	 *                                 {@link maquina1995.generic.microservice.controller.exception.ControllerExceptionHandler}
	 */
	ResponseEntity<DtoWrapper<D>> delete(@PathVariable K id) throws EntityNotFoundException;

}