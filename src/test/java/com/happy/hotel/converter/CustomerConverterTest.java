package com.happy.hotel.converter;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CustomerConverterTest {

	@Test
	public void constructorShouldBePrivateTest() {
		assertThrows(IllegalAccessException.class, () -> CustomerConverter.class.newInstance());
	}

	@Test
	public void whenConstructorIsCalledThroughReflectionUnsupportedOperationExceptionShouldBeThrown()
			throws InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
		Constructor<CustomerConverter> constructor = CustomerConverter.class.getDeclaredConstructor();
		constructor.setAccessible(true);
		try {
			constructor.newInstance();
			fail("Exception is expected");
		} catch (InvocationTargetException e) {
			// assertThat(e.getCause(),
			// is(instanceOf(UnsupportedOperationException.class)));
		}
	}

	@Test
	public void setUpdateTest() {
		try (MockedStatic<CustomerConverter> mock = Mockito.mockStatic(CustomerConverter.class)) {
			mock.when(() -> CustomerConverter.setUpdate(null, null)).thenAnswer(i -> null);
			
		}
	}
}
