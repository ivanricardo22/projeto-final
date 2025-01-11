package com.meli.controller;

import com.meli.ConflitosDeNomesDeClubes;
import com.meli.ValidacaoException;
import com.meli.dto.ClubeDTO;
import com.meli.dto.ClubeDTORequest;
import com.meli.service.ClubeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("clube")
public class ClubeController {



    private final ClubeService clubeService;

    @Autowired
    public ClubeController(ClubeService clubeService) {
        this.clubeService = clubeService;
    }


    @GetMapping("/{id}")
    public ResponseEntity<Optional<ClubeDTO>> getClube(@PathVariable Integer id) {
        return ResponseEntity.status(HttpStatus.OK).body(clubeService.getClube(id));

    }



    @GetMapping
    public ResponseEntity<List<ClubeDTO>> getClubes() {
        return ResponseEntity.status(HttpStatus.OK).body(clubeService.getClubes());

    }


    @PostMapping()
    public ResponseEntity<?> cadastrarClube(@RequestBody ClubeDTORequest clubeDTORequest) {

        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(clubeService.cadastrarClube(clubeDTORequest));

        } catch (ValidacaoException  e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (ConflitosDeNomesDeClubes e ) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }


    }




}
