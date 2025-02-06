package com.meli.projetofinal.service.specification;

import com.meli.projetofinal.model.Partida;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.Specification;
import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter

public class PartidaSpecification implements Specification<Partida>, Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String  clubeCasa;
    private String  clubeFora;


    @Override
    public Predicate toPredicate(Root<Partida> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<>();
        if (clubeCasa  != null) {
        predicates.add(criteriaBuilder.equal(root.get("clubeCasa").get("nome"), clubeCasa));
        }
        if (clubeFora != null) {
            predicates.add(criteriaBuilder.equal(root.get("clubeFora").get("nome"), clubeFora));
        }
        return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
    }





}
