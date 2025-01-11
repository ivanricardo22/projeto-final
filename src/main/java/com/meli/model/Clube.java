package com.meli.model;

import com.meli.dto.ClubeDTORequest;
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




    public Clube(ClubeDTORequest clubeDTORequest) {
        this.nome = clubeDTORequest.getNome();
        this.siglaDeEstado = clubeDTORequest.getSiglaDeEstado();
        this.dataDeCriacao = clubeDTORequest.getDataDeCriacao();
        this.ativo = clubeDTORequest.isAtivo();
    }
}
