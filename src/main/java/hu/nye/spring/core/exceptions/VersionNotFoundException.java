package hu.nye.spring.core.exceptions;

public class VersionNotFoundException extends RuntimeException {
	public VersionNotFoundException(String version) {
		super(String.format("Version '%s' not present in the database", version));
	}
}
