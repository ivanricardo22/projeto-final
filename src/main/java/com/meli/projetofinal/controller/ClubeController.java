package com.meli.projetofinal.controller;

import com.meli.projetofinal.service.specification.ClubeSpecification;
import com.meli.projetofinal.dto.ClubeRequestDTO;
import com.meli.projetofinal.dto.ClubeResponseDTO;
import com.meli.projetofinal.exeption.ClubeInexistenteException;
import com.meli.projetofinal.exeption.ConflitosDeNomesDeClubes;
import com.meli.projetofinal.exeption.ValidacaoException;
import com.meli.projetofinal.model.Clube;
import com.meli.projetofinal.service.ClubeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;



@RestController
@RequestMapping("clube")
public class ClubeController {


    private final ClubeService clubeService;

    @Autowired
    public ClubeController(ClubeService clubeService) {
        this.clubeService = clubeService;
    }

    @GetMapping("/clube/{id}")
    public ResponseEntity<ClubeResponseDTO> getClubeId(@PathVariable Integer id) {
        try {
            return new ResponseEntity<>(clubeService.getClubeId(id), HttpStatus.OK);
        } catch (ClubeInexistenteException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @GetMapping("clube")
    public ResponseEntity<List<ClubeResponseDTO>> getClubes() {
        return ResponseEntity.status(HttpStatus.OK).body(clubeService.getClubes());

    }

    @GetMapping
    public ResponseEntity<Page<Clube>> getSpecification(@RequestParam int paginacao, @RequestParam int itens, ClubeSpecification specification, @RequestParam(defaultValue = "ASC") Sort.Direction direction) {
        return ResponseEntity.status(HttpStatus.OK).body(clubeService.getSpecification(paginacao,itens,specification,direction));
    }


    @PostMapping("clube")
    public ResponseEntity<Object> cadastrarClube(@RequestBody ClubeRequestDTO clubeRequestDTO) {

        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(clubeService.cadastrarClube(clubeRequestDTO));

        } catch (ValidacaoException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (ConflitosDeNomesDeClubes e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }


    }


    @PutMapping("/clube/{id}")
    public ResponseEntity<Object> alterarClube(@PathVariable Integer id, @RequestBody ClubeRequestDTO clubeRequestDTO) {

        try {
            return ResponseEntity.ok(clubeService.alterarClube(id, clubeRequestDTO));

        } catch (ValidacaoException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (ClubeInexistenteException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (ConflitosDeNomesDeClubes e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }

    }
    @DeleteMapping("/clube/{id}")
    public ResponseEntity<Object> excluirClube(@PathVariable Integer id) {
        try {
         return ResponseEntity.status(HttpStatus.NO_CONTENT).body(clubeService.excluirClube(id));
        } catch (ClubeInexistenteException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

}
