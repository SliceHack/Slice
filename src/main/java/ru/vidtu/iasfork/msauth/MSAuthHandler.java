package ru.vidtu.iasfork.msauth;

public interface MSAuthHandler {
	void setState(String state);

	void error(Throwable t);

	void success(String name, String uuid, String token, String refresh);

	void cancellble(boolean b);
}
