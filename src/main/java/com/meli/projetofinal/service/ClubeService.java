package com.meli.projetofinal.service;

import com.meli.projetofinal.service.specification.ClubeSpecification;
import com.meli.projetofinal.dto.ClubeRequestDTO;
import com.meli.projetofinal.dto.ClubeResponseDTO;
import com.meli.projetofinal.enums.SiglasEstadoBrasil;
import com.meli.projetofinal.exeption.ClubeInexistenteException;
import com.meli.projetofinal.exeption.ConflitosDeDataException;
import com.meli.projetofinal.exeption.ConflitosDeNomesDeClubes;
import com.meli.projetofinal.exeption.ValidacaoException;
import com.meli.projetofinal.model.Clube;
import com.meli.projetofinal.model.Partida;
import com.meli.projetofinal.repository.ClubeRepository;
import com.meli.projetofinal.repository.PartidaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class ClubeService {

    private final ClubeRepository clubeRepository;
    private final PartidaRepository partidaRepository;
    private static final LocalDate dataAtual = LocalDate.now();

    @Autowired
    public ClubeService(ClubeRepository clubeRepository, PartidaRepository partidaRepository) {

        this.clubeRepository = clubeRepository;
        this.partidaRepository = partidaRepository;
    }
    public List<ClubeResponseDTO> getClubes() {
        List<Clube> clubes = clubeRepository.findAll();
        List<ClubeResponseDTO> listClubesDTO = new ArrayList<>();

        if (clubes.isEmpty()) {
            return listClubesDTO;
        }
        for (Clube clube : clubes) {
            ClubeResponseDTO clubeDTO = new ClubeResponseDTO(clube);
            listClubesDTO.add(clubeDTO);
        }
        return listClubesDTO;
    }
    public Page<Clube> getSpecification(int paginacao, int itens, ClubeSpecification specification, Sort.Direction direcao) {
        Pageable pageable = PageRequest.of(paginacao, itens, Sort.by(direcao, "nome", "siglaDeEstado", "ativo"));
        return clubeRepository.findAll(specification, pageable);
    }

    public ClubeResponseDTO getClubeId(Integer id) {
        Optional<Clube> clube = clubeRepository.findById(id);
        if (clube.isPresent()) {
            return new ClubeResponseDTO(clube.get());
        } else {
            throw new ConflitosDeDataException("Sem resultado");
        }
    }
    public ClubeResponseDTO cadastrarClube(ClubeRequestDTO clubeRequestDTO) {

        validacaoDadosClube(clubeRequestDTO);
        Clube clube = new Clube(clubeRequestDTO);
        Clube novo = clubeRepository.save(clube);
        return new ClubeResponseDTO(novo);
    }
    private void validacaoDadosClube(ClubeRequestDTO clubeRequestDTO) {
        LocalDate dataDeCadastro = clubeRequestDTO.getDataDeCriacao();

        for (Clube clube : clubeRepository.findAll()) {
            if (clubeRequestDTO.getNome().equals(clube.getNome())) {
                throw new ConflitosDeNomesDeClubes("Já existe clube cadastrados com este nome "
                        + clube.getNome());
            }
        }
        boolean siglaEncontrado = false;
        for (SiglasEstadoBrasil sigla : SiglasEstadoBrasil.values()) {
            if (Objects.equals(clubeRequestDTO.getSiglaDeEstado(), sigla.name())) {
                siglaEncontrado = true;
                break;
            }
        }
        if (!siglaEncontrado) {
            throw new ValidacaoException("Sigla de estado incorreto " + clubeRequestDTO.getSiglaDeEstado());
        }

        if (clubeRequestDTO.getNome() == null || clubeRequestDTO.getNome().isEmpty() || clubeRequestDTO.getNome().length() < 2) {
            throw new ValidacaoException("Nome errado" + clubeRequestDTO.getNome());
        } else if (clubeRequestDTO.getDataDeCriacao() == null || dataDeCadastro.isAfter(dataAtual)) {
            throw new ValidacaoException("Data de criaço errado" + clubeRequestDTO.getDataDeCriacao());
        } else if (!clubeRequestDTO.isAtivo()) {
            throw new ValidacaoException("Ativo errado" + false);
        }
    }
    public ClubeResponseDTO alterarClube(Integer id, ClubeRequestDTO clubeRequestDTO) {
        validacaoParaAlterar(id, clubeRequestDTO);
        Clube clube = new Clube(clubeRequestDTO);
        Clube clubeSaved =  clubeRepository.save(clube);
        return new ClubeResponseDTO(clubeSaved);
    }
    private void validacaoParaAlterar(Integer id, ClubeRequestDTO clubeRequestDTO) {
        LocalDate dataDeCadastro = clubeRequestDTO.getDataDeCriacao();
        Optional<Clube> clubeOPT = clubeRepository.findById(id);

        if (clubeOPT.isEmpty()) {
            throw new ClubeInexistenteException("Sem resultado");
        }

        if (clubeRequestDTO.getNome().length() < 2) {
            throw new ValidacaoException("é necessario no minimo duas letras para clube " + clubeRequestDTO.getNome());
        } else if (dataDeCadastro.isAfter(dataAtual)) {
            throw new ValidacaoException("Data invalida " + clubeRequestDTO.getDataDeCriacao());
        }
        boolean siglaEncontrado = false;
        for (SiglasEstadoBrasil sigla : SiglasEstadoBrasil.values()) {
            if (Objects.equals(clubeRequestDTO.getSiglaDeEstado(), sigla.name())) {
                siglaEncontrado = true;
                break;
            }
        }
        if (!siglaEncontrado) {
            throw new ValidacaoException("Estado incorreto " + clubeRequestDTO.getSiglaDeEstado());
        }

        for (Clube clube : clubeRepository.findAll()) {
            if (clubeRequestDTO.getNome().equals(clube.getNome()) && clubeRequestDTO.getSiglaDeEstado().equals(clube.getSiglaDeEstado())) {
                throw new ConflitosDeNomesDeClubes("Nome do clube já existe para o mesmo estado" + clube.getNome());
            }
        }
        Optional<Partida> partida = partidaRepository.findById(id);

        if (partida.isPresent() && clubeRequestDTO.getDataDeCriacao().isAfter(partida.get().getDataPartida())) {
            throw new ConflitosDeDataException("A alteração da data de criação não pode ser após a partida já marcada" + clubeRequestDTO.getDataDeCriacao());
        }
    }
    public ClubeResponseDTO excluirClube(Integer id) {

        Optional<Clube> clube = clubeRepository.findById(id);
        if (clube.isPresent()) {
            Clube clubeParaInativar = clube.get();
            clubeParaInativar.setAtivo(false);
            clubeRepository.save(clubeParaInativar);
            return new ClubeResponseDTO(clubeParaInativar);
        }
        throw new ClubeInexistenteException("Clube não encontrado");
    }
}



