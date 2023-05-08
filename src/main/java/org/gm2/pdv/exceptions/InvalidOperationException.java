package org.gm2.pdv.exceptions;

// Exceção de erros de quantidades de sales < quantidades product
public class InvalidOperationException extends RuntimeException{
    public InvalidOperationException(String message) {
        super(message);
    }
}
