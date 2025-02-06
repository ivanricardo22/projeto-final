package com.meli.projetofinal.dto;

import com.meli.projetofinal.model.Clube;
import lombok.*;

import java.time.LocalDate;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ClubeResponseDTO {
    private Integer id;
    private String nome;
    private String siglaDeEstado;
    private LocalDate dataDeCriacao;
    private  boolean ativo;

    public ClubeResponseDTO(Clube clube) {
        this.id = clube.getId();
        this.nome = clube.getNome();
        this.siglaDeEstado = clube.getSiglaDeEstado();
        this.dataDeCriacao = clube.getDataDeCriacao();
        this.ativo = clube.isAtivo();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ClubeResponseDTO that = (ClubeResponseDTO) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
