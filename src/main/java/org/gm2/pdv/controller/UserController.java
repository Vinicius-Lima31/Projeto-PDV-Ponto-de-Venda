package org.gm2.pdv.controller;

import org.gm2.pdv.dto.ResponseDTO;
import org.gm2.pdv.entity.User;
import org.gm2.pdv.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    // SELECT * FROM
    @GetMapping
    public ResponseEntity getAll() {
        try{
            return new ResponseEntity<>(userService.findAll(), HttpStatus.OK);
        }
        catch (Exception error) {
            return new ResponseEntity<>(error.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // INSERT INTO
    @PostMapping()
    public ResponseEntity post(@Valid @RequestBody User user) {
        try {
            user.setEnabled(true);
            return new ResponseEntity<>(userService.save(user), HttpStatus.OK);
        } catch (Exception error) {
            return new ResponseEntity<>(error.getMessage(), HttpStatus.OK);
        }
    }

    // UPDATE (Método update do UserService)
    @PutMapping
    public ResponseEntity put(@Valid @RequestBody User user) {
        try {
            return new ResponseEntity<>(userService.update(user), HttpStatus.OK);
        }

        catch (Exception error) {
            return new ResponseEntity<>(new ResponseDTO(error.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // DELETE
    @DeleteMapping("{id}")
    public ResponseEntity delete(@PathVariable long id) {

        // Temos 2 tipos de delete, o fisico e o Lógico. Irei explicar melhor no Notion

        try{
            // deleteById não possui retorno, por isso defini ela aqui
            userService.deleteById(id);
            return new ResponseEntity<>(new ResponseDTO("Usuario " + id + " Removido com sucesso"), HttpStatus.OK);
        }
        catch (Exception error) {
            return new ResponseEntity(new ResponseDTO(error.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }
}
