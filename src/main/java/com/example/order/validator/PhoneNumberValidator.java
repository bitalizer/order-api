package com.example.order.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class PhoneNumberValidator implements ConstraintValidator<PhoneNumber, String> {

	private static final String PHONE_NUMBER_PATTERN = "^[+]?[(]?\\d{3}[)]?[-\\s.]?\\d{3}[-\\s.]?\\d{4,6}$";

	@Override
	public boolean isValid(String phoneNumber, ConstraintValidatorContext context) {
		if (phoneNumber == null) {
			return true; // Null values are considered valid, handle separately if needed
		}

		// Check if the phone number matches the regex pattern
		return Pattern.matches(PHONE_NUMBER_PATTERN, phoneNumber);
	}

}