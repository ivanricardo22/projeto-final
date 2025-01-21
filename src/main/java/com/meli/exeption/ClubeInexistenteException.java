package com.meli.exeption;

public class ClubeInexistenteException extends RuntimeException {
    public ClubeInexistenteException(String message) {
        super(message);
    }
}
