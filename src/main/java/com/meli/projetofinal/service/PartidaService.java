package com.meli.projetofinal.service;
import com.meli.projetofinal.dto.PartidaRequestDTO;
import com.meli.projetofinal.dto.PartidaResponseDTO;
import com.meli.projetofinal.exeption.ClubeInativoExeption;
import com.meli.projetofinal.exeption.ConflitosDeDataException;
import com.meli.projetofinal.exeption.PartidaInexistenteException;
import com.meli.projetofinal.exeption.ValidacaoException;
import com.meli.projetofinal.model.Clube;
import com.meli.projetofinal.model.Estadio;
import com.meli.projetofinal.model.Partida;
import com.meli.projetofinal.repository.ClubeRepository;
import com.meli.projetofinal.repository.EstadioRepository;
import com.meli.projetofinal.repository.PartidaRepository;
import com.meli.projetofinal.service.specification.PartidaSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PartidaService {

    private final PartidaRepository partidaRepository;
    private final ClubeRepository clubeRepository;
    private final EstadioRepository estadioRepository;

    private static final LocalDateTime dataAtual = LocalDateTime.now();
    private static final LocalDateTime horaAtualTime = LocalDateTime.now();

    @Autowired
    public PartidaService(PartidaRepository partidaRepository, ClubeRepository clubeRepository, EstadioRepository estadioRepository) {
        this.partidaRepository = partidaRepository;
        this.clubeRepository = clubeRepository;
        this.estadioRepository = estadioRepository;
    }

    public Page<Partida> getSpecificationPartida(int paginacao, int itens, PartidaSpecification partidaSpecification) {
        Pageable pageable = PageRequest.of(paginacao, itens);
        return  partidaRepository.findAll(partidaSpecification, pageable);

    }

    public PartidaResponseDTO readPartidaId(Integer id) {
        Optional<Partida>  partida = partidaRepository.findById(id);
            if (partida.isPresent()) {
                return new PartidaResponseDTO(partida.get());
            } else {
                throw new PartidaInexistenteException("Sem resultado");
            }
    }

    public List<PartidaResponseDTO> listPartida() {
        List<Partida> partida = partidaRepository.findAll();
        List<PartidaResponseDTO> partidaResponse = new ArrayList<>();
        for(Partida partida1 : partida) {
            partidaResponse.add(new PartidaResponseDTO(partida1));
        }
        return partidaResponse;
    }

    public PartidaResponseDTO createPartida(PartidaRequestDTO partidaRequestDTO) {

        Clube clubeCasa = clubeRepository.findById(partidaRequestDTO.getClubeCasaId()).
                orElseThrow(() -> new RuntimeException("ClubeCasa not found create"));

        Clube clubeFora = clubeRepository.findById(partidaRequestDTO.getClubeForaId()).
                orElseThrow(() -> new RuntimeException("ClubeFora not found create"));

        Estadio estadio = estadioRepository.findById(partidaRequestDTO.getPartidaId()).
                orElseThrow(() -> new RuntimeException("Estadio not found create"));

        LocalDateTime dataHoraPartida = LocalDateTime.of(partidaRequestDTO.getDataPartida(), partidaRequestDTO.getHoraPartida());
        validarDadosClubeCriar(partidaRequestDTO, clubeCasa, clubeFora, dataHoraPartida);
        validarDadosPartidaCriar(partidaRequestDTO, clubeCasa, clubeFora, dataHoraPartida,estadio);


        Partida partida = new Partida(partidaRequestDTO, clubeCasa, clubeFora, estadio );
        Partida newPartida = partidaRepository.save(partida);
        return new PartidaResponseDTO(newPartida);
    }

    private void validarDadosPartidaCriar(PartidaRequestDTO partidaRequestDTO, Clube clubeCasa, Clube clubeFora,LocalDateTime dataHoraPartida, Estadio estadio) {

        LocalDate dataPartida = partidaRequestDTO.getDataPartida();
        LocalDateTime data48HorasAntes = dataHoraPartida.minusHours(48);
        LocalDateTime data48HorasDepois = dataHoraPartida.plusHours(48);

        if (partidaRequestDTO.getDataPartida().isBefore(clubeCasa.getDataDeCriacao()) ||
                partidaRequestDTO.getDataPartida().isBefore(clubeFora.getDataDeCriacao())) {
            throw new ConflitosDeDataException("Hora da partida anterior a data de criação do time");
        }

        Optional<Partida> partidaEstadio = partidaRepository.findAByEstadioIdAndDataPartida(estadio.getId(), dataPartida);
        List<Partida> partidasClubeCasa = partidaRepository.findAllByClubeCasaId(clubeCasa.getId());
        List<Partida> partidasClubeFora = partidaRepository.findByClubeForaId(clubeFora.getId());

        if (partidaEstadio.isPresent()) {
            throw new ConflitosDeDataException("Estadio já possui uma partida");
        }


        for (Partida p : partidasClubeCasa) {
            LocalDateTime dataHoraExistente = LocalDateTime.of(p.getDataPartida(), p.getHoraPartida());
            if (dataHoraExistente.isAfter(data48HorasAntes) && dataHoraExistente.isBefore(data48HorasDepois)) {
                throw new ConflitosDeDataException("A partida não pode ser alterada com diferença menor do que 48 horas em relação a outra partida.");
            }
        }

        for (Partida p : partidasClubeFora) {
            LocalDateTime dataHoraExistente = LocalDateTime.of(p.getDataPartida(), p.getHoraPartida());
            if (dataHoraExistente.isAfter(data48HorasAntes) && dataHoraExistente.isBefore(data48HorasDepois)) {
                throw new ConflitosDeDataException("A partida não pode ser alterada com diferença menor do que 48 horas em relação a outra partida.");
            }
        }
    }

    private void validarDadosClubeCriar(PartidaRequestDTO partidaRequestDTO, Clube clubeCasa, Clube clubeFora, LocalDateTime dataHoraPartida) {
        if (clubeCasa == null || clubeFora == null || clubeCasa == clubeFora || partidaRequestDTO.getEstadio().isEmpty() ||
                partidaRequestDTO.getGolsClubeCasa() < 0 || partidaRequestDTO.getGolsClubeFora() < 0 || dataHoraPartida == null ||
                dataHoraPartida.isAfter(dataAtual) || dataHoraPartida.isAfter(horaAtualTime)) {
            throw new ValidacaoException("O cadastro deve conter, no minimo dois clube envolvido(náo pode ser igual), " +
                    "os resultados " +
                    "de gols(não pode ser negativo)," +
                    "nome do estadio, data e hora da partida(não pode ser posterior a data e hora atual");
        }

        if (!clubeCasa.isAtivo()) {
            throw new ClubeInativoExeption("Clube inativo para se criado: " + clubeCasa.getNome());
        }
        if (!clubeFora.isAtivo()) {
            throw new ClubeInativoExeption("Clube inativo para ser criado: " + clubeFora.getNome());

        }
    }

    public PartidaResponseDTO updatePartida(Integer id, PartidaRequestDTO partidaRequestDTO) {

        Clube clubeCasa = clubeRepository.findById(partidaRequestDTO.getClubeCasaId()).
                orElseThrow(() -> new RuntimeException("ClubeCasa not found update"));

        Clube clubeFora = clubeRepository.findById(partidaRequestDTO.getClubeForaId()).
                orElseThrow(() -> new RuntimeException("ClubeFora not found update"));

        Estadio estadio = estadioRepository.findById(partidaRequestDTO.getPartidaId()).
                orElseThrow(() -> new RuntimeException("Partida not found update"));

        Optional<Partida> partidaExiste = partidaRepository.findById(id);
        if (partidaExiste.isEmpty()) {
            throw new PartidaInexistenteException("Partida not found");
        }

        LocalDateTime dataHoraPartida = LocalDateTime.of(partidaRequestDTO.getDataPartida(), partidaRequestDTO.getHoraPartida());
        validarDadosClubeAtualizar(partidaRequestDTO, clubeCasa, clubeFora, dataHoraPartida);
        validarDadosDaPartidaAtualizar(partidaRequestDTO, clubeCasa, clubeFora, dataHoraPartida);

        Partida partida = new Partida(partidaRequestDTO, clubeCasa, clubeFora, estadio);
        Partida partidaAtualizada = partidaRepository.save(partida);
        return new PartidaResponseDTO(partidaAtualizada);
    }

    public void validarDadosClubeAtualizar(PartidaRequestDTO partidaRequestDTO, Clube clubeCasa, Clube clubeFora, LocalDateTime dataHoraPartida) {
        if (clubeCasa == clubeFora || partidaRequestDTO.getEstadio().isEmpty() ||
                partidaRequestDTO.getGolsClubeCasa() < 0 || partidaRequestDTO.getGolsClubeFora() < 0 ||
                dataHoraPartida.isAfter(dataAtual) || dataHoraPartida.isAfter(horaAtualTime)) {
            throw new ValidacaoException("Os clube envolvido(náo pode ser igual), " +
                    "os resultados " +
                    "de gols(não pode ser negativo)," +
                    "nome do estadio, data e hora da partida(não pode ser posterior a data e hora atual");
        }

        if (!clubeCasa.isAtivo()) {
            throw new ClubeInativoExeption("Clube inativo para ser atualizado: " + clubeCasa.getNome());
        }
        if (!clubeFora.isAtivo()) {
            throw new ClubeInativoExeption("Clube inativo para ser atualizado : " + clubeFora.getNome());

        }
    }

    public void validarDadosDaPartidaAtualizar(PartidaRequestDTO partidaRequestDTO, Clube clubeCasa, Clube clubeFora, LocalDateTime dataHoraPartida) {
        LocalDateTime data48HorasAntes = dataHoraPartida.minusHours(48);
        LocalDateTime data48HorasDepois = dataHoraPartida.plusHours(48);


        if (partidaRequestDTO.getDataPartida().isBefore(clubeCasa.getDataDeCriacao()) ||
                partidaRequestDTO.getDataPartida().isBefore(clubeFora.getDataDeCriacao())) {
            throw new ConflitosDeDataException("Hora da partida anterior a data de criação do time para atualizar");

        }
        List<Partida> partidasClubeCasa = partidaRepository.findAllByClubeCasaId(clubeCasa.getId());
        List<Partida> partidasClubeFora = partidaRepository.findByClubeForaId(clubeFora.getId());

        for (Partida p : partidasClubeCasa) {
            LocalDateTime dataHoraExistente = LocalDateTime.of(p.getDataPartida(), p.getHoraPartida());
            if (dataHoraExistente.isAfter(data48HorasAntes) && dataHoraExistente.isBefore(data48HorasDepois)) {
                throw new ConflitosDeDataException("A partida não pode ser marcada com diferença menor do que 48 horas em relação a esta");
            }
        }
        for (Partida p : partidasClubeFora) {
            LocalDateTime dataHoraExistente = LocalDateTime.of(p.getDataPartida(), p.getHoraPartida());
            if (dataHoraExistente.isAfter(data48HorasAntes) && dataHoraExistente.isBefore(data48HorasDepois)) {
                throw new ConflitosDeDataException("A partida não pode ser alterada com diferença menor do que 48 horas em relação a putra partida do clube");
            }
        }
    }

    public void deletarPartida(Integer idPartida) {
        Optional<Partida> partida = partidaRepository.findById(idPartida);
        if (partida.isPresent()) {
            partidaRepository.deleteById(idPartida);
        } else {
            throw new PartidaInexistenteException("Partida nao encontrada");
        }
    }
}




