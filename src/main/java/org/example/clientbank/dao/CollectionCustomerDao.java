package org.example.clientbank.dao;

import org.example.clientbank.entity.Customer;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class CollectionCustomerDao implements CustomerDao {

    private final List<Customer> customers;
    private long currentId = 1;

    public CollectionCustomerDao() {
        this.customers = new ArrayList<>();
        Customer customer1 = new Customer(1L, "Max", "qwe@gmail.com", 35);
        customers.add(customer1);
        customers.add(new Customer(2L, "Viktor", "sgdsgdsg@gmail.com", 31));
        customers.add(new Customer(3L, "Andrew", "sdgdsg@gmail.com", 25));
    }

    @Override
    public void save(Customer customer) {
        customer.setId(currentId);
        currentId++;
        customers.add(customer);
    }

    @Override
    public boolean delete(Customer customer) {
        return customers.remove(customer);
    }

    @Override
    public void deleteAll(List<Customer> list) {
        customers.removeAll(list);
    }

    @Override
    public void saveAll(List<Customer> list) {
        for (Customer customer : list) {
            save(customer);
        }
    }

    @Override
    public List<Customer> findAll() {
        return new ArrayList<>(customers);
    }

    @Override
    public boolean deleteById(long id) {
        if (!customers.isEmpty()) {
            return customers.removeIf(customer -> customer.getId().equals(id));
        }
        return false;
    }

    @Override
    public Optional<Customer> getOne(long id) {
        return customers.stream().filter(customer -> customer.getId().equals(id))
                .findFirst();
    }

    @Override
    public Optional<Customer> getOne(Customer customer) {
        return customers.stream().filter(c -> c.equals(customer))
                .findFirst();
    }
}
