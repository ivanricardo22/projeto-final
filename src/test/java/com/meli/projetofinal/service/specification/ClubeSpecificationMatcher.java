package com.meli.projetofinal.service.specification;

import org.mockito.ArgumentMatcher;
import org.springframework.data.jpa.domain.Specification;
import com.meli.projetofinal.model.Clube;

public class ClubeSpecificationMatcher implements ArgumentMatcher<Specification<Clube>> {

    private final String nome;
    private final String siglaDeEstado;
    private final Boolean ativo;

    public ClubeSpecificationMatcher(String nome, String siglaDeEstado, Boolean ativo) {
        this.nome = nome;
        this.siglaDeEstado = siglaDeEstado;
        this.ativo = ativo;
    }

    @Override
    public boolean matches(Specification<Clube> specification) {
        if (specification instanceof ClubeSpecification clubeSpecification) {
            return nome.equals(clubeSpecification.getNome())
                    && (siglaDeEstado == null ? clubeSpecification.getSiglaDeEstado() == null : siglaDeEstado.equals(clubeSpecification.getSiglaDeEstado()))
                    && (ativo == null ? clubeSpecification.getAtivo() == null : ativo.equals(clubeSpecification.getAtivo()));
        }
        return false;
    }
}
