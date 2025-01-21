package com.meli.repository;

import com.meli.model.Partida;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PartidaRepository extends JpaRepository<Partida, Integer> {


    List<Partida> findAllByClubeCasaId( Integer clubeId);

    @Query("SELECT p from Partida p WHERE p.clubeFora.id = :clubeId")
    List<Partida> findByClubeForaId(@Param("clubeId") Integer clubeId);




}
