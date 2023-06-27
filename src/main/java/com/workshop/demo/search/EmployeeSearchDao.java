package com.workshop.demo.search;

import com.workshop.demo.models.Employee;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class EmployeeSearchDao {

    private final EntityManager em;

    public List<Employee> findAllBySimpleQuery(
            String firstname,
            String lastname,
            String email
    ){
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Employee> criteriaQuery = criteriaBuilder.createQuery(Employee.class);

        // SELECT * FROM employee
        Root<Employee> root = criteriaQuery.from(Employee.class);
        // prepare WHERE clause
        // WHERE firstname like '%ali%'
        Predicate firstnamePredicate = criteriaBuilder.like(root.get("firstname"),  "%" + firstname + "%");
        Predicate lastnamePredicate = criteriaBuilder.like(root.get("lastname"),  "%" + lastname + "%");
        Predicate emailPredicate = criteriaBuilder.like(root.get("email"), "%" + email + "%");

        Predicate firstnameOrLastnamePredicate = criteriaBuilder.or(firstnamePredicate,lastnamePredicate,emailPredicate);

        // final query ==> SELECT * from employee WHERE firstname like '%ali'
        // or lastname like '%ali%' and email like '%email%'
        Predicate andEmailPredicate = criteriaBuilder.and(firstnameOrLastnamePredicate, emailPredicate);
        criteriaQuery.where(andEmailPredicate);

        TypedQuery<Employee> query = em.createQuery(criteriaQuery);
        return query.getResultList();
    }

    public List<Employee> findAllByCriteria(
            SearchRequest searchRequest
    ){
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Employee> criteriaQuery = criteriaBuilder.createQuery(Employee.class);

        List<Predicate> predicates = new ArrayList<>();

        //SELECT from employee
        Root<Employee> root = criteriaQuery.from(Employee.class);
        if (searchRequest.getFirstname()!= null){
            Predicate firstnamePredicate = criteriaBuilder.like(root.get("firstname"), "%"+searchRequest.getFirstname()+"%");
            predicates.add(firstnamePredicate);
        }
        if (searchRequest.getLastname()!= null){
            Predicate lastnamePredicate = criteriaBuilder.like(root.get("lastname"), "%"+searchRequest.getLastname()+"%");
            predicates.add(lastnamePredicate);
        }
        if (searchRequest.getEmail()!= null){
            Predicate emailPredicate = criteriaBuilder.like(root.get("firstname"), "%"+searchRequest.getEmail()+"%");
            predicates.add(emailPredicate);
        }

        criteriaQuery.where(
                criteriaBuilder.or(predicates.toArray(new Predicate[0]))
        );

        TypedQuery<Employee> query = em.createQuery(criteriaQuery);
        return query.getResultList();
    }
}
