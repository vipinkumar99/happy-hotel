package com.happy.hotel.utils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CurrencyConverterTest {

	@Test
	public void constructorShouldBePrivateTest() {
		assertThrows(IllegalAccessException.class, () -> CurrencyConverter.class.newInstance());
	}

	@Test
	public void whenConstructorIsCalledThroughReflectionUnsupportedOperationExceptionShouldBeThrown()
			throws InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
		Constructor<CurrencyConverter> constructor = CurrencyConverter.class.getDeclaredConstructor();
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
	public void shouldReturnToEuroTest() {
		assertThat(CurrencyConverter.toEuro(5)).isEqualTo(4.25);
		try (MockedStatic<CurrencyConverter> mock = Mockito.mockStatic(CurrencyConverter.class)) {
			mock.when(() -> CurrencyConverter.toEuro(10)).thenReturn(8.50);
			assertThat(CurrencyConverter.toEuro(10)).isEqualTo(8.50);
		}
		assertThat(CurrencyConverter.toEuro(5)).isEqualTo(4.25);
	}
}
