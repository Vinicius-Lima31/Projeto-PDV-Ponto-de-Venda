package org.gm2.pdv.controller;

import org.gm2.pdv.dto.ResponseDTO;
import org.gm2.pdv.dto.SaleDTO;
import org.gm2.pdv.service.SaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@Controller
@RequestMapping("/sale")
public class SaleController {

    @Autowired
    private SaleService saleService;

    @GetMapping
    public ResponseEntity getAll() {
        return new ResponseEntity(saleService.findAll(), HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity getById(@Valid @PathVariable long id) {
        try {
            return new ResponseEntity(saleService.getById(id), HttpStatus.OK);
        }
        catch (Exception error) {
            return new ResponseEntity<>(error.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping
    public ResponseEntity post(@Valid @RequestBody SaleDTO saleDTO) {
        try{
            long id = saleService.save(saleDTO);
            return new ResponseEntity<>(new ResponseDTO("Venda realizada com sucesso!"), HttpStatus.CREATED);
        }
        catch (Exception error) {
            return new ResponseEntity<>(error.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
