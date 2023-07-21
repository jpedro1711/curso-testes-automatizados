package com.example.swplanetapi.domain;
import static com.example.swplanetapi.common.PlanetConstants.PLANET;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Optional;

import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

@DataJpaTest
public class PlanetRepositoryTest {

  @Autowired
  private PlanetRepository planetRepository;

  @Autowired
  private TestEntityManager testEntityManager;

  @AfterEach
  public void AfterEach() {
    PLANET.setId(null);
  }

  @Test
  public void createPlanetWithValidData_ReturnsPlantet() {
    Planet planet = planetRepository.save(PLANET); // Retorna o planeta criado
    Planet sut = testEntityManager.find(Planet.class, planet.getId()); // Buscar o planeta criado
    assertThat(sut).isNotNull();
    assertThat(sut.getName()).isEqualTo(PLANET.getName());
    assertThat(sut.getClimate()).isEqualTo(PLANET.getClimate());
    assertThat(sut.getTerrain()).isEqualTo(PLANET.getTerrain());
  }

  @Test
  public void createPlanetWithInvalidData_ThrowsException(){
    Planet emptyPlanet = new Planet();
    Planet invalidPlanet = new Planet("", "", "");

    assertThatThrownBy(() -> planetRepository.save(emptyPlanet)).isInstanceOf(RuntimeException.class);
    assertThatThrownBy(() -> planetRepository.save(invalidPlanet)).isInstanceOf(RuntimeException.class);
  }

  @Test
  public void createPlanet_WithExistingName_ThrowsException() {
    Planet planet = testEntityManager.persistFlushFind(PLANET); // Salvar, atualizar no banco e recuperar
    testEntityManager.detach(planet); // O entity manager deixa de monitorar o planeta
    planet.setId(null); // Zera o ID, quando tentar salvar o JPA vai saber que é pra salvar
    
    assertThatThrownBy(() ->planetRepository.save(planet)).isInstanceOf(RuntimeException.class);;
  }

  @Test
  public void getPlanet_ByExistingId_ReturnsPlanet(){
    Planet planet = testEntityManager.persistFlushFind(PLANET);
    Optional<Planet> planetOpt = planetRepository.findById(planet.getId());
    assertThat(planetOpt).isNotEmpty();
    assertThat(planetOpt.get()).isEqualTo(planet);
  }

  @Test
  public void getPlanet_ByUnexistingId_ReturnsEmpty(){
    Planet planet = new Planet("name", "climate", "terrain");
    planet.setId(999L);
    Planet sut = testEntityManager.find(Planet.class, planet.getId()); // Buscar o planeta criado
    assertThat(sut).isNull();
  }
}
