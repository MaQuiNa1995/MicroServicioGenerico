package maquina1995.generic.microservice.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.stream.Stream;

import lombok.SneakyThrows;

public abstract class AbstractReflectionTestUtils {

	private final Random randomGenerator;

	protected AbstractReflectionTestUtils() {
		this.randomGenerator = new Random();
	}

	protected Object generateObjectFromGeneric(int index, Class<?> clazz) {
		Class<?> paramClazz = getClassFromIndex(clazz, index);
		return this.setValuesToObject(paramClazz);
	}

	protected Class<?> getClassFromIndex(Class<?> clazz, int index) {
		return (Class<?>) ((ParameterizedType) clazz.getGenericSuperclass()).getActualTypeArguments()[index];
	}

	/**
	 * La clase debe estar anotada con: {@link lombok.AllArgsConstructor} y
	 * {@link lombok.NoArgsConstructor}
	 * 
	 * @param constructor constructores de la clase
	 * 
	 * @return constructor de la clase anotado con {@link lombok.AllArgsConstructor}
	 */
	protected Constructor<?> findAllArgsConstructor(Constructor<?>[] constructor) {

		return Stream.of(constructor)
		        .filter(e -> e.getParameterTypes().length >= 1)
		        .findFirst()
		        .orElseThrow(() -> new IllegalArgumentException(
		                "El objeto debe tener el constructor con todos los argumentos"));
	}

	/**
	 * Llama al constructor con todos los argumentos de la clase pasada como
	 * parámetro
	 * <p>
	 * Se ha arreglado un bug por el cual si pasas a este método un Long por ejemplo
	 * al buscar el constructor intentará instanciar el objeto con el siguiente
	 * constructor {@link Long#Long(String)} lo cual al ser una String cuando se
	 * genere el valor random se generará un UUID y dará un
	 * {@link NumberFormatException} ya que una string del tipo
	 * {@link UUID#randomUUID()} no puede ser parseada a un número
	 * <p>
	 * para arreglar esto se ha metido un if para ver si la clase es del core de
	 * java y asi generar un valor de acuerdo a ella en vez de instanciarla por
	 * constructor
	 * 
	 * @param clazz {@link Class} <?> a instanciar
	 * @return un valor valido de la clase en el caso de los objetos del core de
	 *         java o un objeto construido a partir del constructor con todos los
	 *         argumentos
	 */
	@SneakyThrows
	private Object setValuesToObject(Class<?> clazz) {

		List<Object> randomValues = new ArrayList<>();

		if ("java.lang".equals(clazz.getPackage()
		        .getName())) {
			List<Object> values = new ArrayList<>();
			this.generateValueFromClass(values, clazz);
			return values.get(0);
		}

		Constructor<?> constructor = this.findAllArgsConstructor(clazz.getDeclaredConstructors());
		Stream.of(constructor.getParameterTypes())
		        .forEach(this.generateRandomFromClass(randomValues));

		return constructor.newInstance(randomValues.toArray());
	}

	/**
	 * Crea un {@link Consumer} que genera un valor aleatorio dependiendo de la
	 * clase que sea su tipo y los mete a la lista pasada por parámetro:
	 * <p>
	 * <ul>
	 * <li>Short</li>
	 * <li>Integer</li>
	 * <li>Long</li>
	 * <li>BigDecimal</li>
	 * <li>Double</li>
	 * <li>Float</li>
	 * <li>Boolean</li>
	 * </ul>
	 * <p>
	 * 
	 * @param randomValues {@link List}< Object > a ser rellenada por los valores
	 *                     generados
	 * 
	 * @return {@link Consumer} < Class<?> >
	 */

	private Consumer<Class<?>> generateRandomFromClass(List<Object> randomValues) {
		return clazz -> this.generateValueFromClass(randomValues, clazz);
	}

	private void generateValueFromClass(List<Object> randomValues, Class<?> clazz) {
		// Numeros enteros
		if (clazz.equals(Short.class) || clazz.getName()
		        .equals("short")) {
			randomValues.add((short) this.randomGenerator.nextInt(Short.MAX_VALUE + 1));
		} else if (clazz.equals(Integer.class) || clazz.getName()
		        .equals("int")) {
			randomValues.add(Math.abs(this.randomGenerator.nextInt()));
		} else if (clazz.equals(Long.class) || clazz.getName()
		        .equals("long")) {
			randomValues.add(Math.abs(this.randomGenerator.nextLong()));
		} else if (clazz.equals(BigDecimal.class)) {
			randomValues.add(new BigDecimal(Math.abs(this.randomGenerator.nextDouble())));
		}
		// Numeros Decimales
		else if (clazz.equals(Double.class) || clazz.getName()
		        .equals("double")) {
			randomValues.add(Math.abs(this.randomGenerator.nextDouble()));
		} else if (clazz.equals(Float.class) || clazz.getName()
		        .equals("float")) {
			randomValues.add(Math.abs(this.randomGenerator.nextFloat()));
		}
		// Booleano
		else if (clazz.equals(Boolean.class) || clazz.getName()
		        .equals("boolean")) {
			randomValues.add(this.randomGenerator.nextBoolean());
		}
		// Colecciones de datos
		else if (clazz.equals(List.class)) {
			randomValues.add(new ArrayList<>());
		} else if (clazz.equals(Set.class)) {
			randomValues.add(new HashSet<>());
		} else if (clazz.equals(Map.class)) {
			randomValues.add(new HashMap<>());
		}
		// Fechas
		else if (clazz.equals(Date.class)) {
			randomValues.add(new Date());
		} else if (clazz.equals(LocalDateTime.class)) {
			randomValues.add(LocalDateTime.now());
		} else if (clazz.equals(LocalDate.class)) {
			randomValues.add(LocalDate.now());
		} else if (clazz.equals(LocalTime.class)) {
			randomValues.add(LocalTime.now());
		}
		// String
		else if (clazz.equals(String.class)) {
			String string = UUID.randomUUID()
			        .toString();
			randomValues.add(string);
		} else {
			this.createValueToCustomClass(randomValues, clazz);
		}
	}

	/**
	 * @param randomValues
	 * @param clazz
	 * @throws SecurityException
	 */
	private void createValueToCustomClass(List<Object> randomValues, Class<?> clazz) throws SecurityException {
		Constructor<?> constructor = clazz.getConstructors()[0];

		List<Object> randomValuesObject = new ArrayList<>();

		Stream.of(constructor.getParameterTypes())
		        .forEach(this.generateRandomFromClass(randomValuesObject));

		try {
			randomValues.add(constructor.newInstance(randomValuesObject.toArray()));
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
		        | InvocationTargetException exception) {
			exception.printStackTrace();
		}
	}

}
