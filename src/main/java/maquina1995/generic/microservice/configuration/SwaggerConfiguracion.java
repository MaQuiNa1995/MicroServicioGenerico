package maquina1995.generic.microservice.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.fasterxml.classmate.TypeResolver;

import maquina1995.generic.microservice.constant.SwaggerConstants;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * Configuración de swagger
 */
@Configuration
@Profile("testing")
public class SwaggerConfiguracion {

	/**
	 * Creación del bean usado para configurar swagger
	 * 
	 * @param typeResolver {@link TypeResolver} usado por swagger para resolver los
	 *                     tipos y darle un esquema al usuario en swagger del Dto
	 * @return {@link Docket} bean de configuración de swagger
	 */
	@Bean
	public Docket swaggerApi(TypeResolver typeResolver) {
		return new Docket(DocumentationType.SWAGGER_2).select()
		        .apis(RequestHandlerSelectors.basePackage(SwaggerConstants.PROJECT_CONTROLLER_PATH))
		        .paths(PathSelectors.any())
		        .build()
		        .apiInfo(this.apiInfo());
	}

	/**
	 * Creación del contacto para mostrar en swagger
	 * 
	 * @return {@link ApiInfo} con la info del contacto
	 */
	private ApiInfo apiInfo() {

		final Contact contacto = new Contact(SwaggerConstants.PROJECT_CONTACT_NAME, SwaggerConstants.CONTACT_URL,
		        SwaggerConstants.CONTACT_EMAIL);

		return new ApiInfoBuilder().title(SwaggerConstants.PROJECT_TITLE)
		        .version(SwaggerConstants.PROJECT_API_VERSION)
		        .contact(contacto)
		        .build();
	}
}
