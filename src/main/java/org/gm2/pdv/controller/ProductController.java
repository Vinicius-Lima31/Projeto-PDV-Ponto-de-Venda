package org.gm2.pdv.controller;

import org.gm2.pdv.entity.Product;
import org.gm2.pdv.entity.User;
import org.gm2.pdv.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    // SELECT * FROM
    @GetMapping
    public ResponseEntity getAll() {
        return new ResponseEntity<>(productRepository.findAll(), HttpStatus.OK);
    }

    // INSERT
    @PostMapping
    public ResponseEntity post(@RequestBody Product product) {
        try {
            return new ResponseEntity(productRepository.save(product), HttpStatus.CREATED);
        }
        catch (Exception error) {
            return new ResponseEntity<>(error.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // UPDATE
    @PutMapping
    public ResponseEntity put(@RequestBody Product product) {
        try {
            return new ResponseEntity<>(productRepository.save(product), HttpStatus.OK);
        }
        catch (Exception error){
            return new ResponseEntity<>(error.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // DELETE
    @DeleteMapping("{id}")
    public ResponseEntity delete(@PathVariable long id) {
        try {
            productRepository.deleteById(id);
            return new ResponseEntity<>("Produto removido com Sucesso!", HttpStatus.OK);
        }
        catch (Exception error) {
            return new ResponseEntity(error.getMessage(), HttpStatus.OK);
        }
    }

}
