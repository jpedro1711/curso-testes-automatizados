package com.example.swplanetapi.domain;

import static com.example.swplanetapi.common.PlanetConstants.PLANET;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

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
}
