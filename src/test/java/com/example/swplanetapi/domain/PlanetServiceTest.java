package com.example.swplanetapi.domain;

import static com.example.swplanetapi.common.PlanetConstants.PLANET;
import static com.example.swplanetapi.common.PlanetConstants.INVALID_PLANET;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Example;

@ExtendWith(MockitoExtension.class)
public class PlanetServiceTest {

  //@Autowired
  @InjectMocks
  private PlanetService planetService;

  //@MockBean
  @Mock
  private PlanetRepository planetRepository;

  // operacao_estado_return
  @Test
  public void createPlanet_WithValidData_ReturnsPlanet() {
    // AAA
    // Arrange
    when(planetRepository.save(PLANET)).thenReturn(PLANET);
    // system under test

    // Act
    Planet sut = planetService.create(PLANET);

    // Assert
    assertThat(sut).isEqualTo(PLANET);
  }

  @Test
  public void createPlanet_withInvalidData_ThrowsException() {
    when(planetRepository.save(INVALID_PLANET)).thenThrow(RuntimeException.class);

    assertThatThrownBy(() -> planetService.create(INVALID_PLANET)).isInstanceOf(RuntimeException.class);
  }

  @Test
  public void findPlanet_WithAnExistingId_ReturnsPlanet() {
    when(planetRepository.findById(anyLong())).thenReturn(Optional.of(PLANET));
    Optional<Planet> sut = planetService.getById(1L);
    assertThat(sut).isNotEmpty();
    assertThat(sut).get().isEqualTo(PLANET);
  }

  @Test
  public void findPlanet_WithUnexistingId_ReturnsEmpty(){
    when(planetRepository.findById(1L)).thenReturn(Optional.empty());
    Optional<Planet> sut = planetService.getById(1L);
    assertThat(sut).isEmpty();
  }

  @Test
  public void findPlanet_withAnExistingName_ReturnsPlanet() {
    when(planetRepository.findByName("name")).thenReturn(Optional.of(PLANET));
    Optional<Planet> sut = planetService.getByName("name");
    assertThat(sut).isNotEmpty();
    assertThat(sut).get().isEqualTo(PLANET);
  }

  @Test
  public void findPlanet_withAnUnexistingName_ReturnsEmpty() {
    when(planetRepository.findByName("Unexisting name")).thenReturn(Optional.empty());
    Optional<Planet> sut = planetService.getByName("Unexisting name");
    assertThat(sut).isEmpty();
  }

  @Test
  public void listPlanets_ReturnsAllPlanets() {
    List<Planet> planets = new ArrayList<>() {{
      add(PLANET);
    }};
    Example<Planet> query = QueryBuilder.makeQuery(new Planet(PLANET.getTerrain(), PLANET.getClimate()));
    when(planetRepository.findAll(query)).thenReturn(planets);
    List<Planet> sut = planetService.list(PLANET.getTerrain(), PLANET.getClimate());
    assertThat(sut).isNotEmpty();
    assertThat(sut).hasSize(1);
    assertThat(sut.get(0)).isEqualTo(PLANET);
  }

  @Test
  public void listPlanets_ReturnsNoPlanets() {
    Example<Planet> query = QueryBuilder.makeQuery(new Planet(INVALID_PLANET.getTerrain(), INVALID_PLANET.getClimate()));
    when(planetRepository.findAll(query)).thenReturn(Collections.emptyList());
    List<Planet> sut = planetService.list(INVALID_PLANET.getTerrain(), INVALID_PLANET.getClimate());
    assertThat(sut).isEmpty();
  }

  @Test
  public void removePlanet_withExistingId_doesNotThrowException() {
    doNothing().when(planetRepository).deleteById(1L);
    planetService.remove(1L);
    verify(planetRepository, times(1)).deleteById(1L);
  }

  @Test
  public void removePlanet_withNonExistingId_ThrowsException() {
    doThrow(new RuntimeException()).when(planetRepository).deleteById(99L);

    assertThatThrownBy(() -> planetService.remove(99L)).isInstanceOf(RuntimeException.class);

  }
}
