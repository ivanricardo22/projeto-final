package com.meli.projetofinal.model;

import com.meli.projetofinal.dto.EstadioRequestDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "estadio")
public class Estadio {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    private String nomeEstadio;

    public Estadio(EstadioRequestDTO estadioRequestDTO) {
        this.nomeEstadio = estadioRequestDTO.getNomeEstadio();
    }
}
