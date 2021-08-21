package maquina1995.generic.microservice.mapper;

import org.mapstruct.Mapper;

import maquina1995.generic.microservice.dto.TestClassDto;
import maquina1995.generic.microservice.entity.TestClass;
import maquina1995.generic.microservice.mapper.AbstractMapper;

@Mapper
public interface TestClassMapper extends AbstractMapper<TestClass, TestClassDto> {

}
