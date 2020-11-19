package se.ifmo.pepe.coursage.util;

import org.springframework.data.jpa.domain.Specification;
import se.ifmo.pepe.coursage.model.Trademarks;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;

public class TrademarkDataTableFilter implements Specification<Trademarks> {
    String userQuery;

    public TrademarkDataTableFilter(String userQuery) {
        this.userQuery = userQuery;
    }

    @Override
    public Predicate toPredicate(Root<Trademarks> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
        ArrayList<Predicate> predicates = new ArrayList<>();
        if (userQuery != null && !userQuery.equals("")) {
            predicates.add(criteriaBuilder.like(root.get("name"), '%' + userQuery + '%'));
            predicates.add(criteriaBuilder.like(root.get("doze"), '%' + userQuery + '%'));
            predicates.add(criteriaBuilder.like(root.get("release_price"), '%' + userQuery + '%'));
            predicates.add(criteriaBuilder.like(root.get("drug_id"), '%' + userQuery + '%'));
            predicates.add(criteriaBuilder.like(root.get("company_id"), '%' + userQuery + '%'));
            predicates.add(criteriaBuilder.like(root.get("patent_id"), '%' + userQuery + '%'));
        }
        return (!predicates.isEmpty() ?
                criteriaBuilder.or(predicates.toArray(new Predicate[predicates.size()])) : null);
    }
}
