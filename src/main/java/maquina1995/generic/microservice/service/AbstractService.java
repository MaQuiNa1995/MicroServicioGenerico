package maquina1995.generic.microservice.service;

import java.util.List;

import maquina1995.generic.microservice.dto.AbstractDto;
import maquina1995.generic.microservice.exception.EntityNotFoundException;

/**
 * @param <K> Clave primaria
 * @param <D> Dto
 */
public interface AbstractService<K, D extends AbstractDto<?>> {

	/**
	 * Creación de un {@link D}
	 * 
	 * @param dto
	 * @return {@link D}
	 */
	D create(D dto);

	/**
	 * Obtencion de un {@link D} a traves de su clave primaria {@link K}
	 * 
	 * @param id clave primaria {@link K}
	 * 
	 * @return {@link D} encontrado con esa clave primaria {@link K} pasada por
	 *         parámetro
	 * 
	 * @throws EntityNotFoundException si no encuentra la entidad con la clave
	 *                                 primaria {@link K} pasada por parámetro
	 */
	D find(K id) throws EntityNotFoundException;

	/**
	 * Obtención de todos los {@link D}
	 * 
	 * @return {@link List} de {@link D} en caso de no encontrar nada devuelve una
	 *         instancia vacía de una {@link List}
	 */
	List<D> findAll();

	/**
	 * 
	 * @param dto {@link D} con los datos a modificar y con la clave primaria
	 *            {@link K} de la entidad a atacar
	 * 
	 * @return {@link D} modificada
	 * 
	 * @throws EntityNotFoundException si no encuentra la entidad con la clave
	 *                                 primaria {@link K} del {@link D} pasado por
	 *                                 parámetro
	 */
	D update(D dto) throws EntityNotFoundException;

	/**
	 * 
	 * @param id clave primaria {@link K}
	 * 
	 * @throws EntityNotFoundException si no encuentra la entidad con la clave
	 *                                 primaria {@link K} pasada por parámetro
	 */
	void delete(K id) throws EntityNotFoundException;

}