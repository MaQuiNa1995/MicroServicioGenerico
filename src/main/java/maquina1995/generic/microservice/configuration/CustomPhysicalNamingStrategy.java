package maquina1995.generic.microservice.configuration;

import java.util.Locale;

import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.boot.model.naming.PhysicalNamingStrategy;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;
import org.springframework.stereotype.Component;

/**
 * Procesa los nombres de los elementos de una base de datos de camelCase a
 * snake_case en minúsculas
 */
@Component
public class CustomPhysicalNamingStrategy implements PhysicalNamingStrategy {

	@Override
	public Identifier toPhysicalCatalogName(Identifier name, JdbcEnvironment jdbcEnvironment) {
		return this.convertToLowerSnakeCase(name);
	}

	@Override
	public Identifier toPhysicalSchemaName(Identifier name, JdbcEnvironment jdbcEnvironment) {
		return this.convertToLowerSnakeCase(name);
	}

	@Override
	public Identifier toPhysicalTableName(Identifier name, JdbcEnvironment jdbcEnvironment) {
		return this.convertToLowerSnakeCase(name);
	}

	@Override
	public Identifier toPhysicalSequenceName(Identifier name, JdbcEnvironment jdbcEnvironment) {
		return this.convertToLowerSnakeCase(name);
	}

	@Override
	public Identifier toPhysicalColumnName(Identifier name, JdbcEnvironment jdbcEnvironment) {
		return this.convertToLowerSnakeCase(name);
	}

	/**
	 * Convierte un identificador de la base de datos como puede ser una tabla
	 * esquema etc a <code>snake_case</code>
	 * 
	 * @param nameFromHibernate {@link Identifier} de la base de datos (Tabla,
	 *                          Esquema etc)
	 * 
	 * @return {@link Identifier} convertido a <code>snake_case</code>
	 */
	private Identifier convertToLowerSnakeCase(Identifier nameFromHibernate) {
		Identifier name = null;

		if (nameFromHibernate != null) {
			String transformedName = nameFromHibernate.getText()
			        .replaceAll("([a-z])([A-Z])", "$1_$2")
			        .toLowerCase(Locale.ROOT);

			name = Identifier.toIdentifier(transformedName);
		}
		return name;
	}

}
