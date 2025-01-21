package com.meli.model;

import com.meli.dto.PartidaRequestDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "partidas")
public class Partida {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;


    //entidade proprietaria
    //unidirecional sabe da existencia da relação
    @ManyToOne
    @JoinColumn(name = "clube_casa_id", nullable = false)
    private Clube clubeCasa;

    @ManyToOne
    @JoinColumn(name = "clube_fora_id", nullable = false)
    private Clube clubeFora;

    private int golsClubeCasa;
    private int golsClubeFora;


    private String estadio;
    private LocalDate dataPartida;
    private LocalTime horaPartida;


    public Partida(PartidaRequestDTO partidaRequestDTO, Clube clubeCasa, Clube clubeFora) {


        this.clubeCasa = clubeCasa;
        this.clubeFora = clubeFora;
        this.golsClubeCasa = partidaRequestDTO.getGolsClubeCasa();
        this.golsClubeFora = partidaRequestDTO.getGolsClubeFora();
        this.estadio = partidaRequestDTO.getEstadio();
        this.dataPartida = partidaRequestDTO.getDataPartida();
        this.horaPartida = partidaRequestDTO.getHoraPartida();


    }




}
