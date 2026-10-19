package ru.vidtu.iasfork.msauth;

public interface Account {
	String alias();

	Throwable login();
}
