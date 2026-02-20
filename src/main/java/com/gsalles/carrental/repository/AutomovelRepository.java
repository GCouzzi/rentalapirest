package com.gsalles.carrental.repository;

import com.gsalles.carrental.entity.Automovel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AutomovelRepository extends JpaRepository<Automovel, Long> {
    Optional<Automovel> findByPlaca(String placa);
    Page<Automovel> findAll(Pageable pageable);
    Long deleteByPlaca(String placa);
}
