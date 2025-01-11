package com.meli.service;

import com.meli.ConflitosDeNomesDeClubes;
import com.meli.ValidacaoException;
import com.meli.dto.ClubeDTO;
import com.meli.dto.ClubeDTORequest;
import com.meli.model.Clube;
import com.meli.repository.ClubeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;


@Service
public class ClubeService {

    private final ClubeRepository clubeRepository;

    @Autowired
    public ClubeService(ClubeRepository clubeRepository) {

        this.clubeRepository = clubeRepository;
    }

    private final String[] SIGLAS_ESTADO_BRASIL = {"AC", "AL", "AP", "AM", "BA", "CE", "DF", "ES", "GO", "MA", "MT",
            "MS", "MG", "PA", "PB", "PR", "PE", "PI", "RJ", "RN", "RS", "RO", "RR", "SC", "SP", "SE", "TO"};


    public List<ClubeDTO> getClubes() {
        List<Clube> clubes = clubeRepository.findAll();
        List<ClubeDTO> clubesDTO = new ArrayList<>();
        for (Clube clube : clubes) {
            ClubeDTO clubeDTO = new ClubeDTO(clube);
            clubesDTO.add(clubeDTO);
        }
        return clubesDTO;
    }

    public Optional<ClubeDTO> getClube(Integer id) {
        Optional<Clube> clube = clubeRepository.findById(id);
        if (clube.isPresent()) {
            return Optional.of(new ClubeDTO(clube.get()));
        } else {
            return Optional.of(new ClubeDTO());
        }

    }


    public ClubeDTO cadastrarClube(ClubeDTORequest clubeDTORequest) {

        LocalDate dataDeCadastro = clubeDTORequest.getDataDeCriacao();
        LocalDate dataAtual =LocalDate.now();


        if (clubeDTORequest.getNome() != null && !clubeDTORequest.getNome().isEmpty() &&
                clubeDTORequest.getNome().length() > 1 &&
                clubeDTORequest.getDataDeCriacao() != null && !dataDeCadastro.isAfter(dataAtual) &&
                clubeDTORequest.getSiglaDeEstado() != null && clubeDTORequest.getSiglaDeEstado().length() > 1 &&
                clubeDTORequest.isAtivo() ) {

            for (Clube clube : clubeRepository.findAll()) {
                if (clubeDTORequest.getNome().equals(clube.getNome())) {
                    throw new ConflitosDeNomesDeClubes(" Já existe país cadastrados com este nome " + clube.getNome());
                }
            }

            for (String sigla : SIGLAS_ESTADO_BRASIL) {
                if (!Objects.equals(clubeDTORequest.getSiglaDeEstado(), sigla)) {
                    throw new ValidacaoException("País incorreto ");
                }
            }

            Clube clube = new Clube(clubeDTORequest);
            Clube novo = clubeRepository.save(clube);

            ClubeDTO clubeDTO = new ClubeDTO();

            clubeDTO.setId(novo.getId());
            clubeDTO.setNome(novo.getNome());
            clubeDTO.setDataDeCriacao(novo.getDataDeCriacao());
            clubeDTO.setSiglaDeEstado(novo.getSiglaDeEstado());
            clubeDTO.setAtivo(novo.isAtivo());

            return clubeDTO;

        }


        throw new ValidacaoException("Preencha todos os dados!");
    }




}
