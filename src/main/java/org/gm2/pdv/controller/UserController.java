package org.gm2.pdv.controller;

import org.gm2.pdv.entity.User;
import org.gm2.pdv.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    // SELECT * FROM
    @GetMapping
    public ResponseEntity getAll() {
        return new ResponseEntity<>(userRepository.findAll(), HttpStatus.OK);
    }

    // INSERT INTO
    @PostMapping()
    public ResponseEntity post(@RequestBody User user) {
        try {
            user.setEnabled(true);
            return new ResponseEntity<>(userRepository.save(user), HttpStatus.OK);
        } catch (Exception error) {
            return new ResponseEntity<>(error.getMessage(), HttpStatus.OK);
        }
    }

    // UPDATE
    @PutMapping
    public ResponseEntity put(@RequestBody User user) {
        // Optional<> é uma List com diferentes métodos
        Optional<User> userToEdit = userRepository.findById(user.getId());

        // isPresent vai ver se existe algo com aquele ID
        if (userToEdit.isPresent()) {
            return new ResponseEntity<>(userRepository.save(user), HttpStatus.OK);
        }

        return ResponseEntity.notFound().build();
    }

    // DELETE
    @DeleteMapping("{id}")
    public ResponseEntity delete(@PathVariable long id) {

        // Temos 2 tipos de delete, o fisico e o Lógico. Irei explicar melhor no Notion

        try{
            // deleteById não possui retorno, por isso defini ela aqui
            userRepository.deleteById(id);
            return new ResponseEntity<>("Usuario Removido com sucesso", HttpStatus.OK);
        }
        catch (Exception error) {
            return new ResponseEntity(error.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
