package com.example.apifix.repository;

import com.example.apifix.model.Fix;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FixRepository extends JpaRepository<Fix, Long> { }
