package com.meli.controller;

import com.meli.dto.ClubeRequestDTO;
import com.meli.dto.ClubeResponseDTO;
import com.meli.exeption.ClubeInativoExeption;
import com.meli.exeption.ClubeInexistenteException;
import com.meli.exeption.ConflitosDeNomesDeClubes;
import com.meli.exeption.ValidacaoException;
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

  @GetMapping("/clube/{id}")
  public ResponseEntity<ClubeResponseDTO> getClubeId(@PathVariable Integer id) throws ValidacaoException {
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
    public ResponseEntity<Void> excluirClube(@PathVariable Integer id) {
        try {
            clubeService.excluirClube(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (ClubeInativoExeption e) {
            return ResponseEntity.notFound().build();
        }
    }

}
