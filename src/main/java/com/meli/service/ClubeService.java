package com.meli.service;

import com.meli.dto.ClubeRequestDTO;
import com.meli.dto.ClubeResponseDTO;
import com.meli.enums.SiglasEstadoBrasil;
import com.meli.exeption.ClubeInexistenteException;
import com.meli.exeption.ConflitosDeDataException;
import com.meli.exeption.ConflitosDeNomesDeClubes;
import com.meli.exeption.ValidacaoException;
import com.meli.model.Clube;
import com.meli.model.Partida;
import com.meli.repository.ClubeRepository;
import com.meli.repository.PartidaRepository;
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
    private final PartidaRepository partidaRepository;
    private static final LocalDate dataAtual = LocalDate.now();

    @Autowired
    public ClubeService(ClubeRepository clubeRepository, PartidaRepository partidaRepository) {

        this.clubeRepository = clubeRepository;
        this.partidaRepository = partidaRepository;
    }

    public List<ClubeResponseDTO> getClubes() {
        List<Clube> clubes = clubeRepository.findAll();
        List<ClubeResponseDTO> listclubesDTO = new ArrayList<>();

        if (clubes.isEmpty()) {
            return listclubesDTO;
        }
        for (Clube clube : clubes) {
            ClubeResponseDTO clubeDTO = new ClubeResponseDTO(clube);
            listclubesDTO.add(clubeDTO);
        }
        return listclubesDTO;
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
        if (clubeRequestDTO.getNome() != null && !clubeRequestDTO.getNome().isEmpty() &&
                clubeRequestDTO.getNome().length() > 1 &&
                clubeRequestDTO.getDataDeCriacao() != null && !dataDeCadastro.isAfter(dataAtual) &&
                clubeRequestDTO.getSiglaDeEstado() != null && clubeRequestDTO.getSiglaDeEstado().length() > 1 &&
                clubeRequestDTO.isAtivo()) {

            for (Clube clube : clubeRepository.findAll()) {
                if (clubeRequestDTO.getNome().equals(clube.getNome())) {
                    throw new ConflitosDeNomesDeClubes(" Já existe país cadastrados com este nome "
                            + clube.getNome());
                }
            }
            for (SiglasEstadoBrasil sigla : SiglasEstadoBrasil.values()) {
                if (!Objects.equals(clubeRequestDTO.getSiglaDeEstado(), sigla)) {
                    throw new ValidacaoException("País incorreto ");
                }
            }
        }
    }

    public Optional<ClubeResponseDTO> alterarClube(Integer id, ClubeRequestDTO clubeRequestDTO) {
        LocalDate dataDeCadastro = clubeRequestDTO.getDataDeCriacao();
        if (clubeRequestDTO.getNome().length() < 2 || dataDeCadastro.isAfter(dataAtual)) {
            throw new ValidacaoException("Dados invalidos");
        }
        for (SiglasEstadoBrasil sigla : SiglasEstadoBrasil.values()) {
            if (!Objects.equals(clubeRequestDTO.getSiglaDeEstado(), sigla)) {
                throw new ValidacaoException("Dados invalidos");
            }
        }
        Optional<Partida> partida = partidaRepository.findById(id);

        if (partida.isPresent() && clubeRequestDTO.getDataDeCriacao().isAfter(partida.get().getDataPartida())) {
            throw new ConflitosDeDataException("A alteração da data de criação não pode ser após a partida já marcada");
        }
        Optional<Clube> clube = clubeRepository.findById(id);
        if (clube.isPresent() && clube.get().getNome().equals(clubeRequestDTO.getNome()) && clube.get().getSiglaDeEstado().equals(clubeRequestDTO.getSiglaDeEstado())) {
            throw new ConflitosDeDataException("Nome do clube já existe para o mesmo estado");
        }
        Clube clubeAtualizado = new Clube(clubeRequestDTO);
        clubeRepository.save(clubeAtualizado);
        return Optional.of(new ClubeResponseDTO(clubeAtualizado));
    }

    public void  excluirClube(Integer id) {

        Optional<Clube> clube = clubeRepository.findById(id);
        if (clube.isPresent()) {
            Clube clubeParaInativar = clube.get();
            clubeParaInativar.setAtivo(false);
            clubeRepository.save(clubeParaInativar);

        } else {
            throw new ClubeInexistenteException("Clube não encontrado");
        }
    }
}

