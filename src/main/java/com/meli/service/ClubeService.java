package com.meli.service;

import com.meli.dto.ClubeDTO;
import com.meli.dto.ClubeDTORequest;
import com.meli.model.Clube;
import com.meli.repository.ClubeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class ClubeService {

    private final ClubeRepository clubeRepository;

    @Autowired
    public ClubeService(ClubeRepository clubeRepository) {
        this.clubeRepository = clubeRepository;
    }


    public List<ClubeDTO> getClubes() {
        List<Clube> clubes = clubeRepository.findAll();
        List<ClubeDTO> clubeDTOS = new ArrayList<>();
        for (Clube clube : clubes) {
            ClubeDTO clubeDTO = new ClubeDTO(clube);
            clubeDTOS.add(clubeDTO);
        }
        return clubeDTOS;
    }

    public Optional<ClubeDTO> getClube(Integer id) {
        Optional<Clube> clube = clubeRepository.findById(id);
        //return clube.map(ClubeDTO::new).orElseGet(ClubeDTO::new);

        if (clube.isPresent()) {
            return Optional.of(new ClubeDTO(clube.get()));
        } else {
            return Optional.of(new ClubeDTO());
        }


    }


    public ClubeDTO cadastrarClube(ClubeDTORequest clubeDTORequest) {
        Clube clube = new Clube(clubeDTORequest);
        Clube novo = clubeRepository.save(clube);

        ClubeDTO clubeDTO = new ClubeDTO();

        clubeDTO.setId(novo.getId());
        clubeDTO.setNome(novo.getNome());
        clubeDTO.setDataDeCriacao(novo.getDataDeCriacao());
        clubeDTO.setSiglaDeEstado(novo.getSiglaDeEstado());

        return clubeDTO;
    }




}
