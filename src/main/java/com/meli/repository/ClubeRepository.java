package com.meli.repository;


import com.meli.model.Clube;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface ClubeRepository extends JpaRepository<Clube, Integer> {



}
