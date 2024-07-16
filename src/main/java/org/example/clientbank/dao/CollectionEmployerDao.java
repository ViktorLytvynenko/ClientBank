package org.example.clientbank.dao;

import org.example.clientbank.entity.Employer;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CollectionEmployerDao implements EmployerDao{

    private final List<Employer> employers;
    private long currentId = 1;

    public CollectionEmployerDao(List<Employer> employers) {
        this.employers = employers;
    }

    @Override
    public void save(Employer employer) {
        employer.setId(currentId);
        currentId++;
        employers.add(employer);
    }

    @Override
    public boolean delete(Employer employer) {
        return employers.remove(employer);
    }

    @Override
    public void deleteAll(List<Employer> list) {
        employers.removeAll(list);
    }

    @Override
    public void saveAll(List<Employer> list) {
        for (Employer employer : list) {
            save(employer);
        }
    }

    @Override
    public List<Employer> findAll() {
        return new ArrayList<>(employers);
    }

    @Override
    public boolean deleteById(long id) {
        if (!employers.isEmpty()) {
            return employers.removeIf(customer -> customer.getId().equals(id));
        }
        return false;
    }

    @Override
    public Optional<Employer> getOne(long id) {
        return employers.stream().filter(customer -> customer.getId().equals(id))
                .findFirst();
    }

    @Override
    public Optional<Employer> getOne(Employer employer) {
        return employers.stream().filter(c -> c.equals(employer))
                .findFirst();
    }
}
