package org.example.clientbank.dao;

import org.example.clientbank.entity.Employer;

import java.util.Optional;

public interface EmployerDao extends Dao<Employer>{
    Optional<Employer> getOne(Employer employer);
}
