package com.meli.model;

import com.meli.dto.ClubeRequestDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;


@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name= "club")
public class Clube {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY )
    private Integer id;

    private String nome;
    private String siglaDeEstado;
    private LocalDate dataDeCriacao;
    private  boolean ativo;


    public Clube(ClubeRequestDTO clubeRequestDTO) {
        this.nome = clubeRequestDTO.getNome();
        this.siglaDeEstado = clubeRequestDTO.getSiglaDeEstado();
        this.dataDeCriacao = clubeRequestDTO.getDataDeCriacao();
        this.ativo = clubeRequestDTO.isAtivo();
    }


}
