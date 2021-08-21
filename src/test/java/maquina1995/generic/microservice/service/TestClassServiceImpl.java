package maquina1995.generic.microservice.service;

import org.springframework.stereotype.Service;

import maquina1995.generic.microservice.dto.TestClassDto;
import maquina1995.generic.microservice.entity.TestClass;
import maquina1995.generic.microservice.service.AbstractServiceImpl;

@Service
public class TestClassServiceImpl extends AbstractServiceImpl<Long, TestClass, TestClassDto> {

}
