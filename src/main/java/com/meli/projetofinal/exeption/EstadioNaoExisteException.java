package com.meli.projetofinal.exeption;

public class EstadioNaoExisteException extends RuntimeException {
    public EstadioNaoExisteException(String message) {
        super(message);
    }
}
