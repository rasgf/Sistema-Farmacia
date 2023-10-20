package com.generation.sistemafarmacia.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import com.generation.sistemafarmacia.model.Remedio;

public interface RemedioRepository extends JpaRepository<Remedio, Long> {

    public List<Remedio> findAllByNomeContainingIgnoreCase(@Param("nome") String nome);

}
