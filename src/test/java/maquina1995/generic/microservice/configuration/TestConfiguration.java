package maquina1995.generic.microservice.configuration;

import org.keycloak.adapters.springboot.KeycloakAutoConfiguration;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.test.context.ActiveProfiles;

import com.fasterxml.jackson.annotation.JsonInclude;

import maquina1995.generic.microservice.dto.AbstractDto;
import maquina1995.generic.microservice.entity.TestClass;
import maquina1995.generic.microservice.jackson.AbstractDtoMixIn;

@SpringBootConfiguration
@SpringBootApplication(exclude = {
        org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class,
        org.springframework.boot.actuate.autoconfigure.security.servlet.ManagementWebSecurityAutoConfiguration.class })
@ActiveProfiles(value = "dev")
@EnableJpaRepositories(basePackages = "maquina1995.generic.microservice.repository")
@EntityScan(basePackages = "maquina1995.generic.microservice.domain.entity",
        basePackageClasses = TestClass.class)
@ComponentScan(basePackages = "maquina1995.generic.microservice",
        excludeFilters = @Filter(type = FilterType.ASSIGNABLE_TYPE,
                classes = { SecurityConfiguration.class, KeycloakAutoConfiguration.class,
                        KeycloackConfigurationImpl.class }))
public class TestConfiguration {

	@Bean
	public Jackson2ObjectMapperBuilder jacksonBuilder() {
		return new Jackson2ObjectMapperBuilder().indentOutput(true)
		        .serializationInclusion(JsonInclude.Include.NON_NULL)
		        .serializationInclusion(JsonInclude.Include.NON_EMPTY)
		        .mixIn(AbstractDto.class, AbstractDtoMixIn.class);
	}

}
