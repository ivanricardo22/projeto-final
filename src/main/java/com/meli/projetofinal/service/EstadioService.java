package com.meli.projetofinal.service;

import com.meli.projetofinal.dto.EstadioRequestDTO;
import com.meli.projetofinal.dto.EstadioResponseDTO;
import com.meli.projetofinal.exeption.EstadioExisteException;
import com.meli.projetofinal.exeption.EstadioNaoExisteException;
import com.meli.projetofinal.exeption.EstadioSemResultadoException;
import com.meli.projetofinal.exeption.ValidacaoException;
import com.meli.projetofinal.model.Estadio;
import com.meli.projetofinal.repository.EstadioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@Service
public class EstadioService {



    private final EstadioRepository estadioRepository;

    @Autowired
    public EstadioService(EstadioRepository estadioRepository) {
        this.estadioRepository = estadioRepository;
    }

    public EstadioResponseDTO criarEstadio(EstadioRequestDTO estadioRequestDTO) {
        verificarDadosParaCriar(estadioRequestDTO);
        Estadio estadio = new Estadio(estadioRequestDTO);
        estadioRepository.save(estadio);
        return new EstadioResponseDTO(estadio);
    }

    public void verificarDadosParaCriar(EstadioRequestDTO estadioRequestDTO) {
        if (estadioRequestDTO.getNomeEstadio() == null || estadioRequestDTO.getNomeEstadio().length() < 3) {
            throw new ValidacaoException("Dados invalidos");
        }
        List<Estadio> estadio = estadioRepository.findAll();
        for (Estadio estadio1 : estadio) {
            if (estadioRequestDTO.getNomeEstadio().equals(estadio1.getNomeEstadio())) {
                throw new ValidacaoException("Estádio já existente");
            }
        }
    }

    public EstadioResponseDTO editarEstadio(Integer id, EstadioRequestDTO estadioRequestDTO) {

        verificarDadosParaEditar(id, estadioRequestDTO);
        Estadio estadio = new Estadio(estadioRequestDTO);
        estadioRepository.save(estadio);
        return new EstadioResponseDTO(estadio);
    }

    public void verificarDadosParaEditar(Integer id, EstadioRequestDTO estadioRequestDTO) {
        if (estadioRequestDTO.getNomeEstadio().length() < 3) {
            throw new ValidacaoException("Dados invalidos para editar");
        }
        Optional<Estadio> estadioID = estadioRepository.findById(id);
        if (estadioID.isEmpty()) {
            throw new EstadioNaoExisteException("Estadio não encontrado");
        }

        Estadio estadioAtual = estadioID.get();
        List<Estadio> listaEstadio = estadioRepository.findAll();
        for (Estadio estadio : listaEstadio) {
            if (!estadio.getId().equals(estadioID.get().getId()) && estadio.getNomeEstadio().equals(estadioRequestDTO.getNomeEstadio())) {
                throw new EstadioExisteException("Estadio já existente");
            }
        }
        new EstadioResponseDTO(estadioAtual);

    }

    public Optional<EstadioResponseDTO> buscarEstadioId(Integer id) {
        Optional<Estadio> estadio = estadioRepository.findById(id);
        if (estadio.isPresent()) {
          return Optional.of(new EstadioResponseDTO(estadio.get()));
        } else {
            throw new EstadioSemResultadoException("Sem resultado para buscar");
        }
    }

    public List<EstadioResponseDTO> buscarTodosEstadio(){
        List<Estadio> estadio = estadioRepository.findAll();
        List<EstadioResponseDTO> listaEstadio = new ArrayList<>();

        for (Estadio novo : estadio) {
            listaEstadio.add(new EstadioResponseDTO(novo));
        }
        return listaEstadio;
    }


}
