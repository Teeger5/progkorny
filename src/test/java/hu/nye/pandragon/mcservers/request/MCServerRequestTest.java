package hu.nye.pandragon.mcservers.request;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class MCServerRequestTest {

	private Validator validator;

	@BeforeEach
	public void setup() {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
	}

	@Test
	public void shouldFailWhenNameIsEmpty() {
		MCServerRequest request = new MCServerRequest(
				"",
				"127.0.0.1",
				"Egy szerver leírása",
				"1.20.4", 25565, 20);
		var expected = "A név nem lehet üres";
		Set<ConstraintViolation<MCServerRequest>> violations = validator.validate(request);
		var result = violations.iterator().next().getMessage();
		assertFalse(violations.isEmpty());
		assertEquals(expected, result);
	}

	@Test
	public void shouldFailWhenAddressIsInvalid() {
		MCServerRequest request = new MCServerRequest(
				"Teszt Szerver",
				"Doragon ga suki desu",
				"Egy szerver leírása",
				"1.20.4", 25565, 20);
		var expected = "Nem érvényes IP-cím vagy domain név";
		Set<ConstraintViolation<MCServerRequest>> violations = validator.validate(request);
		var result = violations.iterator().next().getMessage();
		assertFalse(violations.isEmpty());
		assertEquals(expected, result);
	}


}