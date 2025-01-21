package com.meli.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;


@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class ClubeRequestDTO {

    private String nome;
    private String siglaDeEstado;
    private LocalDate dataDeCriacao;
    private  boolean ativo;
}
