package maquina1995.generic.microservice.configuration;

import org.keycloak.adapters.KeycloakConfigResolver;
import org.keycloak.adapters.springsecurity.config.KeycloakWebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.core.GrantedAuthorityDefaults;

/**
 * Clase encargada de la creación de la configuración relacionada con
 * <a href="https://www.keycloak.org/">Keycloak</a>
 * 
 */
public interface KeyCloakConfiguration {

	/**
	 * Método encargado de la creación del control sobre la autenticación en este
	 * caso como usamos keycloak:
	 * {@link KeycloakWebSecurityConfigurerAdapter#keycloakAuthenticationProvider}
	 * 
	 * @param authenticationManagerBuilder {@link AuthenticationManagerBuilder}
	 *                                     usado para la creación del tipo de
	 *                                     autenticación que vamos a usar
	 */
	void configureGlobal(AuthenticationManagerBuilder authenticationManagerBuilder);

	/**
	 * Método encargado de la creación del bean que resuelve una determinada config
	 * en formato JSON
	 * <p>
	 * En este caso obtiene el del servidor keycloak
	 * 
	 * @return {@link KeycloakConfigResolver} encargado de la resolución de
	 *         configuraciones de Json
	 */
	KeycloakConfigResolver keycloakConfigResolver();

	/**
	 * Método usado para eliminar el prefijo <code>ROLE_</code> que añade siempre
	 * Spring security
	 * 
	 * @return {@link GrantedAuthorityDefaults} encargado del mapeo de los roles
	 */
	GrantedAuthorityDefaults grantedAuthorityDefaults();

}