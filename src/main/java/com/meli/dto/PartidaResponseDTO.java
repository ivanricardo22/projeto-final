package com.meli.dto;

import com.meli.model.Clube;
import com.meli.model.Partida;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PartidaResponseDTO {

    private Integer id;

    private Clube clubeCasa;
    private Clube clubeFora;
    private int golsClubeCasa;
    private int golsClubeFora;
    private String estadio;
    private LocalDate dataInicio;
    private LocalTime horaInicio;


    public PartidaResponseDTO(Partida partida) {
        this.id = partida.getId();
        this.clubeCasa = partida.getClubeCasa();
        this.clubeFora = partida.getClubeFora();
        this.golsClubeCasa = partida.getGolsClubeCasa();
        this.golsClubeFora = partida.getGolsClubeFora();
        this.estadio = partida.getEstadio();
        this.dataInicio = partida.getDataPartida();
        this.horaInicio = partida.getHoraPartida();
    }

}
