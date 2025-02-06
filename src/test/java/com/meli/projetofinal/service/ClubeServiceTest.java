package com.meli.projetofinal.service;

import com.meli.projetofinal.dto.ClubeRequestDTO;
import com.meli.projetofinal.dto.ClubeResponseDTO;
import com.meli.projetofinal.dto.PartidaRequestDTO;
import com.meli.projetofinal.exeption.ClubeInexistenteException;
import com.meli.projetofinal.exeption.ConflitosDeDataException;
import com.meli.projetofinal.exeption.ConflitosDeNomesDeClubes;
import com.meli.projetofinal.exeption.ValidacaoException;
import com.meli.projetofinal.model.Clube;
import com.meli.projetofinal.model.Partida;
import com.meli.projetofinal.repository.ClubeRepository;
import com.meli.projetofinal.repository.PartidaRepository;
import com.meli.projetofinal.service.specification.ClubeSpecification;
import com.meli.projetofinal.service.specification.ClubeSpecificationMatcher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.*;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


class ClubeServiceTest {

    @Mock
    private ClubeRepository clubeRepository;
    @Mock
    private PartidaRepository partidaRepository;

    @InjectMocks
    private ClubeService clubeService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    @DisplayName("sucesso do retorno dos dados cadastrados")
    void getClubesComSucesso() {

        LocalDate date = LocalDate.of(2020, 1, 1);

        Clube clube = new Clube(1, "Ivan", "BA", date, true);
        Clube clube1 = new Clube(2, "Ricardo", "PE", date, true);
        Clube clube2 = new Clube(3, "Santos", "CE", date, true);

        List<Clube> clubes = List.of(clube, clube1, clube2);
        List<ClubeResponseDTO> listClubesDTO = List.of(new ClubeResponseDTO(clube), new ClubeResponseDTO(clube1), new ClubeResponseDTO(clube2));

        when(clubeRepository.findAll()).thenReturn(clubes);

        List<ClubeResponseDTO> clubesDTO = clubeService.getClubes();

        assertNotNull(clubesDTO);
        assertEquals(clubes.size(), clubesDTO.size());
        assertEquals(clubes.get(0).getId(), clubesDTO.get(0).getId());
        assertEquals(clubes.get(1).getId(), clubesDTO.get(1).getId());
        assertEquals(clubes.get(2).getId(), clubesDTO.get(2).getId());
        assertEquals(listClubesDTO, clubesDTO);

    }

    @Test
    void getSpecificationComSucesso() {

        ClubeSpecification d = new ClubeSpecification("Clube A", "SP", true);

        List<Clube> clubes = Collections.singletonList(new Clube());
        Page<Clube> pageResult = new PageImpl<>(clubes);
        when(clubeRepository.findAll(eq(d), any(Pageable.class)))
                .thenReturn(pageResult);

        clubeService.getSpecification(1, 3, d, Sort.DEFAULT_DIRECTION);

        verify(clubeRepository, times(1)).findAll(argThat(new ClubeSpecificationMatcher("Clube A", "SP", true)), any(Pageable.class));
    }

    @Test
    void getClubeIdComSucesso() {
        Clube clube = new Clube(1, "Ivan", "BA", LocalDate.of(2020, 1, 1), true);

        when(clubeRepository.findById(1)).thenReturn(Optional.of(clube));

        ClubeResponseDTO oPT = clubeService.getClubeId(1);
        assertNotNull(oPT);
        assertEquals(clube.getId(), oPT.getId());

    }

    @Test
    void getClubeIdComErro() {
        when(clubeRepository.findById(1)).thenReturn(Optional.empty());
        ConflitosDeDataException ex = assertThrows(ConflitosDeDataException.class, () -> clubeService.getClubeId(1));
        assertEquals("Sem resultado", ex.getMessage());
    }

    @Test
    void cadastrarClubeComSucessoCadastro() {

        ClubeRequestDTO cadastro = new ClubeRequestDTO("Santa Cruz", "PE", LocalDate.of(2020, 1, 1), true);
        Clube clube = new Clube(cadastro);
        clube.setId(1);
        when(clubeRepository.save(any(Clube.class))).thenReturn(clube);

        ClubeResponseDTO result = clubeService.cadastrarClube(cadastro);
        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals(clube.getId(), result.getId());
        assertEquals(cadastro.getNome(), result.getNome());
        assertEquals(cadastro.getSiglaDeEstado(), result.getSiglaDeEstado());
        assertEquals(cadastro.getDataDeCriacao(), result.getDataDeCriacao());
    }

    @Test
    void cadastroDeClubeComoErroNomeExistenteCase1() {
        ClubeRequestDTO nomeExistente = new ClubeRequestDTO("Botafogo", "PE", LocalDate.of(2020, 1, 1), true);
        Clube clube = new Clube(nomeExistente);
        when(clubeRepository.findAll()).thenReturn(List.of(clube));

        ConflitosDeNomesDeClubes e = assertThrows(ConflitosDeNomesDeClubes.class, () -> clubeService.cadastrarClube(nomeExistente));
        assertEquals("Já existe clube cadastrados com este nome " + clube.getNome(), e.getMessage());
    }

    @Test
    void cadastroDeClubeComErroSiglaIncorretaCase2() {

        ClubeRequestDTO siglaIncorreta = new ClubeRequestDTO("Santa Cruz", "XX", LocalDate.of(2020, 1, 1), true);
        ValidacaoException e = assertThrows(ValidacaoException.class, () -> clubeService.cadastrarClube(siglaIncorreta));
        assertEquals("Sigla de estado incorreto " + siglaIncorreta.getSiglaDeEstado(), e.getMessage());
    }

    @Test
    void cadastroDeClubeComErroNomeNullCase3() {
        ClubeRequestDTO nomeNull = new ClubeRequestDTO(null, "PE", LocalDate.of(2020, 1, 1), true);
        ValidacaoException ex = assertThrows(ValidacaoException.class, () -> clubeService.cadastrarClube(nomeNull));
        assertEquals("Nome errado" + nomeNull.getNome(), ex.getMessage());
    }

    @Test
    void cadastroDeClubeComErroNomeMenorQueDuasLetraCase4() {
        ClubeRequestDTO nomeMenorQueDuasLetra = new ClubeRequestDTO("F", "PE", LocalDate.of(2020, 1, 1), true);
        ValidacaoException ex = assertThrows(ValidacaoException.class, () -> clubeService.cadastrarClube(nomeMenorQueDuasLetra));
        assertEquals("Nome errado" + nomeMenorQueDuasLetra.getNome(), ex.getMessage());
    }


    @Test
    void cadastroDeClubeComErroDataAposAtualCase6() {
        ClubeRequestDTO dataAposAtual = new ClubeRequestDTO("Flamengo", "PE", LocalDate.of(2050, 1, 31), true);
        ValidacaoException e = assertThrows(ValidacaoException.class, () -> clubeService.cadastrarClube(dataAposAtual));
        assertEquals("Data de criaço errado" + dataAposAtual.getDataDeCriacao(), e.getMessage());
    }

    @Test
    void cadastrarClubeComInatividade() {
        ClubeRequestDTO ativoFalse = new ClubeRequestDTO("Flamengo", "PE", LocalDate.of(2025, 1, 30), false);
        ValidacaoException e = assertThrows(ValidacaoException.class, () -> clubeService.cadastrarClube(ativoFalse));
        assertEquals("Ativo errado" + false, e.getMessage());
    }

    @Test
    void alterarClubeIDComSucesso() {

        ClubeRequestDTO alteracao = new ClubeRequestDTO("Flamengo2", "RJ", LocalDate.of(2020, 1, 1), true);
        Optional<Clube> clube = Optional.of(new Clube(1, "Flamengo", "PE", LocalDate.of(2020, 1, 1), true));
        Clube clubeResult = new Clube(1, "Flamengo2", "RJ", LocalDate.of(2020, 1, 1), true);

        when(clubeRepository.findById(1)).thenReturn(clube);
        when(clubeRepository.save(any())).thenReturn(clubeResult);

        ClubeResponseDTO result = clubeService.alterarClube(1, alteracao);

        assertEquals(1, result.getId());
        assertEquals(alteracao.getNome(), result.getNome());
        assertEquals(alteracao.getSiglaDeEstado(), result.getSiglaDeEstado());
    }

    @Test
    void alterarClubeIdComErro() {
        ClubeRequestDTO alteracao = new ClubeRequestDTO("Flamengo2", "RJ", LocalDate.of(2020, 1, 1), true);
        when(clubeRepository.findById(1)).thenReturn(Optional.empty());
        ClubeInexistenteException e = assertThrows(ClubeInexistenteException.class, () -> clubeService.alterarClube(1, alteracao));
        assertEquals("Sem resultado", e.getMessage());
    }

    @Test
    void alterarClubenomeMenorQueDuasLetrasComErro() {
        ClubeRequestDTO nomeMenorQueDuasLetras = new ClubeRequestDTO("a", "PE", LocalDate.of(2020, 1, 1), true);
        Optional<Clube> clube = Optional.of(new Clube(1, "Flamengo", "PE", LocalDate.of(2020, 1, 1), true));
        Clube clubeResult = new Clube(1, "a", "RJ", LocalDate.of(2020, 1, 1), true);
        when(clubeRepository.findById(1)).thenReturn(clube);
        when(clubeRepository.save(any())).thenReturn(clubeResult);

        ValidacaoException ex = assertThrows(ValidacaoException.class, () -> clubeService.alterarClube(1, nomeMenorQueDuasLetras));
        assertEquals("é necessario no minimo duas letras para clube " + nomeMenorQueDuasLetras.getNome(), ex.getMessage());
    }

    @Test
    void alterarClubeComDataDeCriacaoInvalidaComErro() {
        ClubeRequestDTO dataInvalida = new ClubeRequestDTO("Flamengo2", "RJ", LocalDate.of(2050, 2, 1), true);
        Clube clube = new Clube(1, "Flamengo", "RJ", LocalDate.of(2020, 1, 1), true);

        when(clubeRepository.findById(1)).thenReturn(Optional.of(clube));

        ValidacaoException e = assertThrows(ValidacaoException.class, () -> clubeService.alterarClube(1, dataInvalida));
        assertEquals("Data invalida " + dataInvalida.getDataDeCriacao(), e.getMessage());

    }

    @Test
    void alterarClubeComSiglaIncorretaComErro() {

        ClubeRequestDTO siglaIncorreta = new ClubeRequestDTO("Santa Cruz", "XX", LocalDate.of(2020, 1, 1), true);
        Optional<Clube> clube = Optional.of(new Clube(1, "Flamengo", "RJ", LocalDate.of(2020, 1, 1), true));
        when(clubeRepository.findById(1)).thenReturn(clube);

        ValidacaoException e = assertThrows(ValidacaoException.class, () -> clubeService.alterarClube(1, siglaIncorreta));
        assertEquals("Estado incorreto " + siglaIncorreta.getSiglaDeEstado(), e.getMessage());
    }

    @Test
    void alterarClubeComNomeExistenteComErro() {
        ClubeRequestDTO a = new ClubeRequestDTO("Flamengo", "RJ", LocalDate.of(2020, 1, 1), true);
        Clube clube = new Clube(1, "Flamengo", "RJ", LocalDate.of(2020, 1, 1), true);
        clube.setId(1);

        Clube novo = new Clube(a);

        when(clubeRepository.findById(1)).thenReturn(Optional.of(novo));
        when(clubeRepository.findAll()).thenReturn(List.of(novo));


        ConflitosDeNomesDeClubes ex = assertThrows(ConflitosDeNomesDeClubes.class, () -> clubeService.alterarClube(1, a));
        assertEquals("Nome do clube já existe para o mesmo estado" + a.getNome(), ex.getMessage());

    }

    @Test
    void alterarClubeComDataDeCriacaoAposADataPartidaComErro() {
        PartidaRequestDTO a = new PartidaRequestDTO(1, 2, 1, 3, 2, "sss", LocalDate.of(2020, 1, 1), null);

        ClubeRequestDTO clube1 = new ClubeRequestDTO("Flamengo", "RJ", LocalDate.of(2021, 1, 1), true);
        ClubeRequestDTO clube2 = new ClubeRequestDTO("Sport", "PE", LocalDate.of(2021, 1, 2), true);

        Clube clubeId1 = new Clube(1, "Flamengo", "RJ", LocalDate.of(2019, 1, 1), true);
        clubeId1.setId(1);
        Clube clubeId2 = new Clube(2, "Sport", "PE", LocalDate.of(2019, 1, 1), true);
        clubeId2.setId(2);

        Clube clubeNomeId1 = new Clube(clube1);
        Clube clubeNomeId2 = new Clube(clube2);

        when(clubeRepository.findById(1)).thenReturn(Optional.of(clubeNomeId1));
        when(clubeRepository.findById(2)).thenReturn(Optional.of(clubeNomeId2));

        Partida partida = new Partida(a, clubeId1, clubeId2, null);

        when(partidaRepository.findById(1)).thenReturn(Optional.of(partida));

        ConflitosDeDataException e = assertThrows(ConflitosDeDataException.class, () -> clubeService.alterarClube(1, clube1));
        assertEquals("A alteração da data de criação não pode ser após a partida já marcada" + clube1.getDataDeCriacao(), e.getMessage());

    }


    @Test
    void excluirClubeComSucesso() {
        Optional<Clube> clube = Optional.of(new Clube(1, "Flamengo", "RJ", LocalDate.of(2020, 1, 1), true));

        when(clubeRepository.findById(1)).thenReturn(clube);
        Clube i = clube.get();
        i.setAtivo(false);
        clubeRepository.save(i);

        ClubeResponseDTO response = clubeService.excluirClube(1);
        assertEquals(i.getId(), response.getId());
        assertEquals(response.getNome(), i.getNome());
        assertEquals(response.isAtivo(), i.isAtivo());

    }

    @Test
    void excluirClubeComErro() {

        when(clubeRepository.findById(1)).thenReturn(Optional.empty());
        ClubeInexistenteException e = assertThrows(ClubeInexistenteException.class, () -> clubeService.excluirClube(1));
        assertEquals("Clube não encontrado", e.getMessage());

    }

}