package com.meli.projetofinal.repository;

import com.meli.projetofinal.model.Partida;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface PartidaRepository extends JpaRepository<Partida,Integer>, JpaSpecificationExecutor<Partida > {

    Page<Partida> findAll(Pageable pageable);

    List<Partida> findAllByClubeCasaId( Integer clubeId);

    Optional<Partida> findAByEstadioIdAndDataPartida(Integer estadioId, LocalDate date);

    @Query("SELECT p from Partida p WHERE p.clubeFora.id = :clubeId")
    List<Partida> findByClubeForaId(@Param("clubeId") Integer clubeId);




}
