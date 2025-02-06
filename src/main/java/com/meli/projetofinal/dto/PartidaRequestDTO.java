package com.meli.projetofinal.dto;


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
public class PartidaRequestDTO {


    private int clubeCasaId;
    private int clubeForaId;

    private int partidaId;

    private int golsClubeCasa;
    private int golsClubeFora;
    private String estadio;
    private LocalDate dataPartida;
    private LocalTime horaPartida;


}
