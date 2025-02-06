package com.meli.projetofinal.model;

import com.meli.projetofinal.dto.PartidaRequestDTO;
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

    @ManyToOne
    @JoinColumn(name = "estadio_id", nullable = false)
    private Estadio estadio;

    private int golsClubeCasa;
    private int golsClubeFora;
    private LocalDate dataPartida;
    private LocalTime horaPartida;


    public Partida(PartidaRequestDTO partidaRequestDTO, Clube clubeCasa, Clube clubeFora, Estadio estadio) {


        this.clubeCasa = clubeCasa;
        this.clubeFora = clubeFora;
        this.estadio = estadio;
        this.golsClubeCasa = partidaRequestDTO.getGolsClubeCasa();
        this.golsClubeFora = partidaRequestDTO.getGolsClubeFora();
        this.dataPartida = partidaRequestDTO.getDataPartida();
        this.horaPartida = partidaRequestDTO.getHoraPartida();


    }




}
