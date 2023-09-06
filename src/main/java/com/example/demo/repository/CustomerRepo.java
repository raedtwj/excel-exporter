package com.example.demo.repository;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Customer;

@Repository
public interface CustomerRepo extends CrudRepository<Customer, Long> {
}