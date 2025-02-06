package com.meli.projetofinal.dto;

import com.meli.projetofinal.model.Estadio;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class EstadioResponseDTO {
    private Integer id;
    private String nomeEstadio;

    public EstadioResponseDTO(Estadio estadio) {
        this.id = estadio.getId();
        this.nomeEstadio = estadio.getNomeEstadio();
    }

}
