package com.meli.projetofinal.dto;

import com.meli.projetofinal.model.Clube;
import com.meli.projetofinal.model.Estadio;
import com.meli.projetofinal.model.Partida;
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
    private Estadio estadio;
    private int golsClubeCasa;
    private int golsClubeFora;
    private LocalDate dataInicio;
    private LocalTime horaInicio;


    public PartidaResponseDTO(Partida partida) {
        this.id = partida.getId();
        this.clubeCasa = partida.getClubeCasa();
        this.clubeFora = partida.getClubeFora();
        this.estadio = partida.getEstadio();
        this.golsClubeCasa = partida.getGolsClubeCasa();
        this.golsClubeFora = partida.getGolsClubeFora();

        this.dataInicio = partida.getDataPartida();
        this.horaInicio = partida.getHoraPartida();
    }

}
