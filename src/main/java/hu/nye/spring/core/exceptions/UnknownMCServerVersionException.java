package hu.nye.spring.core.exceptions;

public class UnknownMCServerVersionException extends UnprocessableEntityException {
	public UnknownMCServerVersionException(String version) {
		super(String.format("A '%s' verzió nem támogatott", version));
	}
}
