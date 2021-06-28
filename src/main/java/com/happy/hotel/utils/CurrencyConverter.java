package com.happy.hotel.utils;

import com.happy.hotel.constants.Msg;

public class CurrencyConverter {

	private CurrencyConverter() {
		throw new UnsupportedOperationException(Msg.CLASS_CAN_NOT_INSTANTIATED);
	}

	private static final double USD_TO_EUR_RATE = 0.85;

	public static double toEuro(double dollarAmount) {
		return dollarAmount * USD_TO_EUR_RATE;
	}

}
