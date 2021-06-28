package com.happy.hotel.constants;

public interface Msg {

	// common
	public static String INTERNAL_SERVER_ERROR = "Internal Server Error";
	public static String SOMETHING_WENT_WRONG = "Something went wrong!";
	public static String EMPTY_REQUEST = "Empty request!";
	public static String RECORD_NOT_FOUND = "Record not found!";
	public static String NO_RECORD_FOUND = "No record found!";
	public static String RECORD_DELETED = "Record is deleted!";
	public static String REQUEST_BODY_MISSING = "Required request body is missing!";
	public static String CLASS_CAN_NOT_INSTANTIATED = "This is a utility class and cannot be instantiated!";

	// customer
	public static String CUSTOMER_NOT_FOUND = "Customer not found!";

	// user
	public static String USER_NOT_FOUND = "User not found!";
	public static String USERNAME_ALREADY_EXIST = "Username is already exist!";

	// payment
	public static String ONLY_SMALL_PAYMENTS_SUPPORTED = "Only small payments are supported!";

	// room
	public static String CAPACITY_ALREADY_EXIST = "capacity is already exist!";
	public static String ROOM_NOT_FOUND = "Room not found!";
	public static String ROOM_NOT_AVAILABLE = "Room not available!";
	public static String ROOM_BOOKED = "Room already booked!";
	public static String ROOM_UNBOOKED = "Room already unbooked!";

	// booking
	public static String BOOKING_NOT_FOUND = "Booking not found!";

}
