package org.gm2.pdv.controller;

import org.gm2.pdv.dto.ResponseDTO;
import org.gm2.pdv.exceptions.InvalidOperationException;
import org.gm2.pdv.exceptions.NoItemException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

// Usar RestController, só quero retornar o dado apenas, sem view nem nada
@RestControllerAdvice
public class ApplicationAdviceController {

    // Aqui passamos esse @ExceptionHandler para estar capturando o método da exceção
    @ExceptionHandler(NoItemException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)

    // Vamos retornar o ReponseDTO, passando como parametro o erro para tratar.
    public ResponseDTO handleNoItemException(NoItemException ex){
        String messageError = ex.getMessage();
        return new ResponseDTO(messageError);
    }



    // Aqui passamos esse @ExceptionHandler para estar capturando o método da exceção
    @ExceptionHandler(InvalidOperationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)

    // Vamos retornar o ReponseDTO, passando como parametro o erro para tratar.
    public ResponseDTO handleInvalidOperationException(InvalidOperationException ex){
        String messageError = ex.getMessage();
        return new ResponseDTO(messageError);
    }

    // Validação de dados das Entidades, se foi preenchida ou não
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseDTO handleValidationException(MethodArgumentNotValidException ex) {
        // Vou retornar uma List pq eu posso não preencher mais de um campo
        List<String> erros = new ArrayList<>();

        // Iterando sobre a List de erros, e queremos fazer um .add na nossa List <String> acima
        ex.getBindingResult().getAllErrors().forEach(error -> {

            // Acessando a String do error
            String errorMessage = error.getDefaultMessage();

            erros.add(errorMessage);
        });

        return new ResponseDTO(erros);
    }
}
