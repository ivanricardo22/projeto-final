package com.meli.projetofinal.model;

import com.meli.projetofinal.dto.ClubeRequestDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;


@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name= "club")
public class Clube implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY )
    private Integer id;

    @Column(nullable = false)
    private String nome;
    @Column(nullable = false)
    private String siglaDeEstado;
    @Column(nullable = false)
    private LocalDate dataDeCriacao;
    @Column(nullable = false)
    private  boolean ativo;


    public Clube(ClubeRequestDTO clubeRequestDTO) {
        this.nome = clubeRequestDTO.getNome();
        this.siglaDeEstado = clubeRequestDTO.getSiglaDeEstado();
        this.dataDeCriacao = clubeRequestDTO.getDataDeCriacao();
        this.ativo = clubeRequestDTO.isAtivo();
    }


}
