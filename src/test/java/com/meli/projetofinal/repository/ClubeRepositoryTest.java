package com.meli.projetofinal.repository;

import com.meli.projetofinal.model.Clube;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class ClubeRepositoryTest {

    @Autowired
    private ClubeRepository clubeRepository;

    @DisplayName(value = "teste para verificar a presenca do clube e a quantidade de elementos total e por pagina  ")
    @Test
    void testDePaginacaoFindAllCase1() {
        LocalDate dataDeCriacao = LocalDate.of(2003, 1, 23);

        // Populando ao menos 10 elementos
        clubeRepository.save(new Clube(null, "Clube A", "PE", dataDeCriacao, true));
        clubeRepository.save(new Clube(null, "Clube B", "CE", dataDeCriacao, true));
        clubeRepository.save(new Clube(null, "Clube C", "PA", dataDeCriacao, true));
        clubeRepository.save(new Clube(null, "Clube D", "SU", dataDeCriacao, true));
        clubeRepository.save(new Clube(null, "Clube E", "BA", dataDeCriacao, true));
        clubeRepository.save(new Clube(null, "Clube F", "SE", dataDeCriacao, true));
        clubeRepository.save(new Clube(null, "Clube G", "RJ", dataDeCriacao, true));
        clubeRepository.save(new Clube(null, "Clube H", "SP", dataDeCriacao, true));
        clubeRepository.save(new Clube(null, "Clube I", "MG", dataDeCriacao, true));
        clubeRepository.save(new Clube(null, "Clube J", "ES", dataDeCriacao, true));

        Pageable pageable = PageRequest.of(0, 6);
        Page<Clube> clubePage = clubeRepository.findAll(pageable);

        // Verificações
        assertNotNull(clubePage);
        assertEquals(6, clubePage.getNumberOfElements(), "Total de elementos na página deveria ser 6");
        assertEquals(10, clubePage.getTotalElements(), "Total de elementos esperado deveria ser 10");
    }
}
