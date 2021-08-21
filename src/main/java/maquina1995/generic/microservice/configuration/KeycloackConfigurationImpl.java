package maquina1995.generic.microservice.configuration;

import org.keycloak.adapters.KeycloakConfigResolver;
import org.keycloak.adapters.springboot.KeycloakSpringBootConfigResolver;
import org.keycloak.adapters.springsecurity.KeycloakConfiguration;
import org.keycloak.adapters.springsecurity.authentication.KeycloakAuthenticationProvider;
import org.keycloak.adapters.springsecurity.config.KeycloakWebSecurityConfigurerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.core.authority.mapping.SimpleAuthorityMapper;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;

@Profile("!dev")
@KeycloakConfiguration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class KeycloackConfigurationImpl extends KeycloakWebSecurityConfigurerAdapter implements KeyCloakConfiguration {

	@Override
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder authenticationManagerBuilder) {
		KeycloakAuthenticationProvider keycloakAuthenticationProvider = keycloakAuthenticationProvider();
		keycloakAuthenticationProvider.setGrantedAuthoritiesMapper(this.simpleAuthoritymapper());
		authenticationManagerBuilder.authenticationProvider(keycloakAuthenticationProvider);
	}

	@Bean
	@Override
	public GrantedAuthorityDefaults grantedAuthorityDefaults() {
		return new GrantedAuthorityDefaults("");
	}

	@Bean
	@Override
	public KeycloakConfigResolver keycloakConfigResolver() {
		return new KeycloakSpringBootConfigResolver();
	}

	/**
	 * Método usado para la creación del bean encargado de las sesiones de usuario y
	 * sus eventos asociados
	 * 
	 * @see {@link SessionRegistryImpl}
	 */
	@Bean
	@Override
	protected SessionAuthenticationStrategy sessionAuthenticationStrategy() {
		return new RegisterSessionAuthenticationStrategy(new SessionRegistryImpl());
	}

	/**
	 * Método encargado de la configuración de los endpoint que estarán securizados
	 * indicando el rol que necesite
	 * <p>
	 * Es necesario llamar al método padre porque este tiene configuración
	 * predefinida que nos es conveniente
	 * {@link KeycloakWebSecurityConfigurerAdapter#configure(HttpSecurity)}
	 * <p>
	 * Adicionalmente en vez de tener que especificar los endpoint y sus permisos,
	 * añadiendo a esta clase de configuración las siguientes anotaciones:
	 * <p>
	 * <li>{@link EnableGlobalMethodSecurity}<code>(prePostEnabled = true)</code>
	 * </li>
	 * <p>
	 * Podemos usar la anotación en nuestro endpoint de cualquier controller:
	 * {@link org.springframework.security.access.prepost.PreAuthorize}<code>("hasAuthority('nombreRol')")</code>
	 */
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		super.configure(http);
		http.authorizeRequests()
		        .anyRequest()
		        .denyAll();
	}

	private SimpleAuthorityMapper simpleAuthoritymapper() {
		SimpleAuthorityMapper simpleAuthorityMapper = new SimpleAuthorityMapper();
		simpleAuthorityMapper.setPrefix("ROLE_");
		return simpleAuthorityMapper;
	}
}
