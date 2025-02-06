package com.meli.projetofinal.service.specification;


import com.meli.projetofinal.model.Clube;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.Specification;
import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
public class ClubeSpecification implements Specification<Clube> {

    private String nome;
    private String siglaDeEstado;
    private  Boolean ativo;


    public ClubeSpecification(String nome, String siglaDeEstado, Boolean ativo) {
        this.nome = nome;
        this.siglaDeEstado = siglaDeEstado;
        this.ativo = ativo;
    }

    @Override
    public Predicate toPredicate(Root<Clube> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<>();

        if(nome != null && !nome.isEmpty()) {
            predicates.add(criteriaBuilder.equal(root.get("nome"), nome));
        }
        if (siglaDeEstado != null && !siglaDeEstado.isEmpty()) {
            predicates.add(criteriaBuilder.equal(root.get("siglaDeEstado"), siglaDeEstado));
        }
        if(ativo != null) {
            predicates.add(criteriaBuilder.equal(root.get("ativo"), ativo));
        }
        return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
    }
}
