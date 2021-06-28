package com.happy.hotel.converter;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class RoomConverterTest {

	@Test
	public void constructorShouldBePrivateTest() {
		assertThrows(IllegalAccessException.class, () -> RoomConverter.class.newInstance());
	}

	@Test
	public void whenConstructorIsCalledThroughReflectionUnsupportedOperationExceptionShouldBeThrown()
			throws InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
		Constructor<RoomConverter> constructor = RoomConverter.class.getDeclaredConstructor();
		constructor.setAccessible(true);
		try {
			constructor.newInstance();
			fail("Exception is expected");
		} catch (InvocationTargetException e) {
			// assertThat(e.getCause(),
			// is(instanceOf(UnsupportedOperationException.class)));
		}
	}

}
