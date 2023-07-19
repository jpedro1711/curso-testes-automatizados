package com.example.swplanetapi.domain;

import java.util.Optional;

import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

@Service
public class PlanetService {
  private PlanetRepository planetRepository;

  public PlanetService(PlanetRepository planetRepository) {
    this.planetRepository = planetRepository;
  }

  public Planet create(Planet planet) {
    return planetRepository.save(planet);
  }

  public Optional<Planet> getById(Long id) {
    return planetRepository.findById(id);
  }

  public Optional<Planet> getByName(String name) {
    return planetRepository.findByName(name);
  }

  public java.util.List<Planet> list(String terrain, String climate) {
    Example<Planet> query = QueryBuilder.makeQuery(new Planet(terrain, climate));
    return planetRepository.findAll(query);
  }

  public void remove(Long id) {
    planetRepository.deleteById(id);
  }
}
