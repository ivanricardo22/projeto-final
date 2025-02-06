package com.meli.projetofinal.controller;


import com.meli.projetofinal.dto.EstadioRequestDTO;
import com.meli.projetofinal.dto.EstadioResponseDTO;
import com.meli.projetofinal.exeption.EstadioExisteException;
import com.meli.projetofinal.exeption.EstadioNaoExisteException;
import com.meli.projetofinal.exeption.EstadioSemResultadoException;
import com.meli.projetofinal.exeption.ValidacaoException;
import com.meli.projetofinal.service.EstadioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("clube")
public class EstadioController {
    private final EstadioService estadioService;

    @Autowired
    public EstadioController(EstadioService estadioService) {
        this.estadioService = estadioService;
    }
    @PostMapping("estadio")
    public ResponseEntity<Object>criarEstadio(@RequestBody EstadioRequestDTO estadioRequestDTO){
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(estadioService.criarEstadio(estadioRequestDTO));
        } catch (ValidacaoException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }
    @PutMapping("/estadio/{id}")
    public ResponseEntity<Object>editarEstadio(@PathVariable Integer id,@RequestBody EstadioRequestDTO estadioRequestDTO){
        try {
            return ResponseEntity.status(HttpStatus.OK).body(estadioService.editarEstadio(id, estadioRequestDTO));
        } catch (ValidacaoException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }catch (EstadioExisteException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (EstadioNaoExisteException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
    @GetMapping("/estadio/{id}")
    public ResponseEntity<Object>buscarEstadioId(@PathVariable Integer id){
        try{
            return ResponseEntity.status(HttpStatus.OK).body(estadioService.buscarEstadioId(id));
        } catch (EstadioSemResultadoException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
    @GetMapping("/estadio")
    public ResponseEntity<List<EstadioResponseDTO>> buscarTodosEstadio() {
           return ResponseEntity.status(HttpStatus.OK).body(estadioService.buscarTodosEstadio());
    }


}
