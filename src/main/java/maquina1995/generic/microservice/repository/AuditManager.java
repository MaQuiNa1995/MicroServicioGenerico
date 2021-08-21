package maquina1995.generic.microservice.repository;

import org.hibernate.Session;
import org.hibernate.tuple.ValueGenerator;

public class AuditManager implements ValueGenerator<String> {

	@Override
	public String generateValue(Session session, Object owner) {
		return "UserMocked";
	}

}
