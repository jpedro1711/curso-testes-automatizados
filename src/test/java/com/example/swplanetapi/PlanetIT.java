package com.example.swplanetapi;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;

import com.example.swplanetapi.domain.Planet;

import static com.example.swplanetapi.common.PlanetConstants.PLANET;
import static com.example.swplanetapi.common.PlanetConstants.TATOOINE;
import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("it")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Sql(scripts = {"/import_planets.sql"}, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = {"/remove_planets.sql"}, executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
public class PlanetIT {

  @Autowired
  private TestRestTemplate restTemplate;

  @Test
  public void createPlanet_ReturnsCreated() {
    ResponseEntity<Planet> sut = restTemplate.postForEntity("/planets", PLANET, Planet.class);
    assertThat(sut.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    assertThat(sut.getBody().getId()).isNotNull();
    assertThat(sut.getBody().getName()).isEqualTo(PLANET.getName());
    assertThat(sut.getBody().getClimate()).isEqualTo(PLANET.getClimate());
    assertThat(sut.getBody().getTerrain()).isEqualTo(PLANET.getTerrain());
  }

  @Test
  public void getPlanet_ReturnsPlanet() {
    ResponseEntity<Planet> sut = restTemplate.getForEntity("/planets/1", Planet.class);
    assertThat(sut.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(sut.getBody().getName()).isEqualTo(TATOOINE.getName());
  }

  @Test
  public void getPlanetByName_ReturnsPlanet() {
    ResponseEntity<Planet> sut = restTemplate.getForEntity("/planets/name/" + TATOOINE.getName(), Planet.class);
    assertThat(sut.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(sut.getBody().getName()).isEqualTo(TATOOINE.getName());
  }

  @Test
  public void listPlanets_ReturnsAllPlanets() {
    ResponseEntity<Planet[]> sut = restTemplate.getForEntity("/planets", Planet[].class);
    assertThat(sut.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(sut.getBody()).hasSize(3);
    assertThat(sut.getBody()[0].getName()).isEqualTo(TATOOINE.getName());
  }

  @Test
  public void listPlanets_byClimate_ReturnsPlanets() {
    ResponseEntity<Planet[]> sut = restTemplate.getForEntity("/planets?climate=" + TATOOINE.getClimate(), Planet[].class);
    assertThat(sut.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(sut.getBody()).hasSize(1);
    assertThat(sut.getBody()[0].getName()).isEqualTo(TATOOINE.getName());
  }

  @Test
  public void listPlanets_byTerrain_ReturnsPlanets() {
    ResponseEntity<Planet[]> sut = restTemplate.getForEntity("/planets?terrain=" + TATOOINE.getTerrain(), Planet[].class);
    assertThat(sut.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(sut.getBody()).hasSize(1);
    assertThat(sut.getBody()[0].getName()).isEqualTo(TATOOINE.getName());
  }

  @Test
  public void removePlanet_ReturnsNoContent() {
    ResponseEntity<Void> sut = restTemplate.exchange("/planets/1", HttpMethod.DELETE, null, Void.class);
    assertThat(sut.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
  }
}
