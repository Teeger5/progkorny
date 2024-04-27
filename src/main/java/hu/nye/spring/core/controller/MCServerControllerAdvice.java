package hu.nye.spring.core.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;

/**
 * Ez az osztály hibakezelésre van,
 * elsősorban a MethodArgumentNotValidException miatt,
 * hogy a hiba részleteit is tartalmazza a válasz,
 * amikor validációs hiba történik a beérkezett adatokkal
 * Azaz a validációs annotációk kivételt okoznak
 */
@RestControllerAdvice
public class MCServerControllerAdvice extends ResponseEntityExceptionHandler {

	/**
	 * Ez a metódus módosítja a választ, ami hibás kérési adatok esetén keletkezik
	 * Az alap válaszban csak a hibakód (400) van,
	 * ez részleteket ad hozzá, azaz, hogy mivel mi volt a probléma
	 * Bár Azaz a validációs annotációkban megadott üzeneteket párosítva az adattag nevével,
	 * ami megegyezik a JSON-ben lévővel
	 * @param ex the exception to handle
	 * @param headers the headers to be written to the response
	 * @param status the selected response status
	 * @param request the current request
	 * @return mező nevek -> hibaüzenet
	 */
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(
			MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatusCode status,
			WebRequest request) {
		Map<String, String> errors = new HashMap<>();
		ex.getBindingResult().getAllErrors().forEach((error) -> {
			String fieldName = ((FieldError) error).getField();
			String errorMessage = error.getDefaultMessage();
			if (fieldName.equalsIgnoreCase("address") && errorMessage.contains("kifejezés")) {
				errorMessage = "Nem érvényes IP-cím vagy domain név";
			}
			errors.put(fieldName, errorMessage);
		});
		return new ResponseEntity(errors, HttpStatus.BAD_REQUEST);
	}
}
