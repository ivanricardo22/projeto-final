package com.meli.projetofinal.repository;


import com.meli.projetofinal.model.Clube;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;


@Repository
public interface ClubeRepository extends JpaRepository<Clube, Integer>, JpaSpecificationExecutor<Clube> {
  Page<Clube> findAll(Pageable pageable);
}
