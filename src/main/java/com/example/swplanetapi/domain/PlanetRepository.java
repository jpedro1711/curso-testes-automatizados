package com.example.swplanetapi.domain;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PlanetRepository extends JpaRepository<Planet, Long>{
  Optional<Planet> findByName(String name);
}
