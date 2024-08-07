package org.example.clientbank.customer.db;

import org.example.clientbank.customer.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long>{

//    @Override
//    @EntityGraph("graph.CustomerAccountEmployer")
//    List<Customer> findAll();
}
