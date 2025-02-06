package com.meli.projetofinal.repository;

import com.meli.projetofinal.model.Estadio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EstadioRepository extends JpaRepository<Estadio, Integer> {
    Optional<Estadio> findById(Integer id);
}
