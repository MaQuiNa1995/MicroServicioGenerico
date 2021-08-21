package maquina1995.generic.microservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import maquina1995.generic.microservice.entity.TestClass;

@Repository
public interface TestClassRepository extends JpaRepository<TestClass, Long> {

}
