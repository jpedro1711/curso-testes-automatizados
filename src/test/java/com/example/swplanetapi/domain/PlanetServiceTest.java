package com.example.swplanetapi.domain;

import static com.example.swplanetapi.common.PlanetConstants.PLANET;
import static com.example.swplanetapi.common.PlanetConstants.INVALID_PLANET;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

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
}
