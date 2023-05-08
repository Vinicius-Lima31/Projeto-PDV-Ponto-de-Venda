package org.gm2.pdv.exceptions;

// Exceção de erros para 0 itens em estoque
public class NoItemException extends RuntimeException{
    public NoItemException(String message) {
        super(message);
    }
}
