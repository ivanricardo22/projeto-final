package com.meli.dto;

import com.meli.model.Clube;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ClubeDTO {

    private Integer id;
    private String nome;
    private String siglaDeEstado;
    private LocalDate dataDeCriacao;
    private  boolean ativo;

    public ClubeDTO(Clube clube) {
        this.id = clube.getId();
        this.nome = clube.getNome();
        this.siglaDeEstado = clube.getSiglaDeEstado();
        this.dataDeCriacao = clube.getDataDeCriacao();
        this.ativo = clube.isAtivo();
    }
}
