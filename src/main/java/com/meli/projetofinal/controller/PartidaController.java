package com.meli.projetofinal.controller;


import com.meli.projetofinal.dto.PartidaRequestDTO;
import com.meli.projetofinal.dto.PartidaResponseDTO;
import com.meli.projetofinal.exeption.ClubeInativoExeption;
import com.meli.projetofinal.exeption.ConflitosDeDataException;
import com.meli.projetofinal.exeption.PartidaInexistenteException;
import com.meli.projetofinal.exeption.ValidacaoException;
import com.meli.projetofinal.model.Partida;
import com.meli.projetofinal.service.PartidaService;
import com.meli.projetofinal.service.specification.PartidaSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("clube")
public class PartidaController {

    final PartidaService partidaService;

    @Autowired
    public PartidaController(PartidaService partidaService) {
        this.partidaService = partidaService;
    }

    @GetMapping("/partida/{id}")
    public ResponseEntity<Optional<PartidaResponseDTO>> readPartidaId(@PathVariable Integer id) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(Optional.ofNullable(partidaService.readPartidaId(id)));
        } catch (ClubeInativoExeption e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Optional.empty());
        }
    }

    @GetMapping("/partida")
    public ResponseEntity<List<PartidaResponseDTO>> listPartida() {
        return ResponseEntity.status(HttpStatus.OK).body(partidaService.listPartida());
    }

    @GetMapping("/buscarpartida")
    public ResponseEntity<Page<Partida>> getSpecificationPartida(@RequestParam int paginacao, @RequestParam int itens , PartidaSpecification specification) {
        return ResponseEntity.status(HttpStatus.OK).body(partidaService.getSpecificationPartida(paginacao,itens,specification));

    }

    @PostMapping("/partida")
    public ResponseEntity<Object> createPartida(@RequestBody PartidaRequestDTO partidaRequestDTO) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(partidaService.createPartida(partidaRequestDTO));

        } catch (ValidacaoException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (ConflitosDeDataException | ClubeInativoExeption e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @PutMapping("/partida/{id}")
    public ResponseEntity<Object> updatePartida(@PathVariable Integer id, @RequestBody PartidaRequestDTO partidaRequestDTO) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(partidaService.updatePartida(id, partidaRequestDTO));
        } catch (ValidacaoException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (ClubeInativoExeption | ConflitosDeDataException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (PartidaInexistenteException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }

    }

    @DeleteMapping("/partida/{id}")
    public ResponseEntity<Void> deletePartida(@PathVariable Integer id) {
        try {
            partidaService.deletarPartida(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (PartidaInexistenteException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }


    }

}
