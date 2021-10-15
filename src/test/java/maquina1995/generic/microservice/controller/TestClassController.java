package maquina1995.generic.microservice.controller;

import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import maquina1995.generic.microservice.constant.TestConstants;
import maquina1995.generic.microservice.dto.DtoWrapper;
import maquina1995.generic.microservice.dto.TestClassDto;
import maquina1995.generic.microservice.exception.EntityNotFoundException;

@RestController
@Api(tags = "TestClass")
@RequestMapping(path = "/testClass")
public class TestClassController extends AbstractControllerImpl<Long, TestClassDto> {

	@Override
	@PreAuthorize(TestConstants.HAS_AUTHORITY_TEST_ROLE)
	public ResponseEntity<DtoWrapper<TestClassDto>> create(@Validated @RequestBody TestClassDto dto) {
		return super.create(dto);
	}

	@Override
	@PreAuthorize(TestConstants.HAS_AUTHORITY_TEST_ROLE)
	public ResponseEntity<DtoWrapper<TestClassDto>> find(Optional<Long> id) throws EntityNotFoundException {
		return super.find(id);
	}

	@Override
	@PreAuthorize(TestConstants.HAS_AUTHORITY_TEST_ROLE)
	public ResponseEntity<DtoWrapper<TestClassDto>> update(@Validated @RequestBody TestClassDto dto)
	        throws EntityNotFoundException {
		return super.update(dto);
	}

	@Override
	@PreAuthorize(TestConstants.HAS_AUTHORITY_TEST_ROLE)
	public ResponseEntity<DtoWrapper<TestClassDto>> delete(Long id) throws EntityNotFoundException {
		return super.delete(id);
	}

}
