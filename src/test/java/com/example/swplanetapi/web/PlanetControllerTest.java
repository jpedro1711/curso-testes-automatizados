package com.example.swplanetapi.web;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static com.example.swplanetapi.common.PlanetConstants.PLANET;
import static com.example.swplanetapi.common.PlanetConstants.TATOOINE;
import static com.example.swplanetapi.common.PlanetConstants.PLANETS;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.example.swplanetapi.domain.Planet;
import com.example.swplanetapi.domain.PlanetService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mysql.cj.x.protobuf.MysqlxCrud.Collection;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@WebMvcTest(PlanetController.class)
public class PlanetControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockBean
  private PlanetService planetService;
  
  @Test
  public void createPlanet_WithValidData_ReturnsCreated() throws Exception {
    when(planetService.create(PLANET)).thenReturn(PLANET);

    mockMvc.perform(post("/planets").content(objectMapper.writeValueAsString(PLANET)).contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isCreated())
      .andExpect(jsonPath("$").value(PLANET));
  }

  @Test
  public void createPlanet_withInvalidData_ReturnsBadRequest() throws Exception{
    Planet emptyPlanet = new Planet();
    Planet invalidPlanet = new Planet("", "", "");

    mockMvc.perform(post("/planets").content(objectMapper.writeValueAsString(emptyPlanet))
      .contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isUnprocessableEntity());

    mockMvc.perform(post("/planets").content(objectMapper.writeValueAsString(invalidPlanet))
      .contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isUnprocessableEntity());
  }

  @Test
  public void createPlanet_withExistingName_ReturnsConflict() throws Exception{
    when(planetService.create(PLANET)).thenThrow(DataIntegrityViolationException.class);
    mockMvc.perform(post("/planets").content(objectMapper
      .writeValueAsString(PLANET))
      .contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isConflict());
  }

  @Test
  public void getPlanet_ByExistingId_ReturnsPlanet() throws Exception{
    when(planetService.getById(anyLong())).thenReturn(Optional.of(PLANET));
    mockMvc.perform(get("/planets/99"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$").value(PLANET));
  }

  @Test
  public void getPlanet_ByUnexistingId_ReturnsNotFound() throws Exception{
    when(planetService.getById(anyLong())).thenReturn(Optional.empty());
    mockMvc.perform(get("/planets/99"))
      .andExpect(status().isNotFound());
  }

  @Test
  public void getPlanet_ByExistingName_ReturnsPlanet() throws Exception {
    when(planetService.getByName("tatooine")).thenReturn(Optional.of(PLANET));
    mockMvc.perform(get("/planets/name/tatooine"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$").value(PLANET));
  }

  @Test
  public void getPlanet_ByUnexistingName_ReturnsNotFound() throws Exception{
    when(planetService.getByName("tatooine")).thenReturn(Optional.empty());
    mockMvc.perform(get("/planets/name/tatooine"))
      .andExpect(status().isNotFound());
  }

  @Test
  public void listPlanets_ReturnsFilteredPlanets() throws Exception {
    when(planetService.list(null, null)).thenReturn(PLANETS);
    when(planetService.list(TATOOINE.getTerrain(), TATOOINE.getClimate())).thenReturn(List.of(TATOOINE));
    mockMvc.perform(get("/planets"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$", hasSize(3)));

      mockMvc.perform(get("/planets?" + String.format("terrain=%s&climate=%s", TATOOINE.getTerrain(), TATOOINE.getClimate())))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$", hasSize(1))) // Assuming only one planet matches the filtering criteria
      .andExpect(jsonPath("$[0].name").value(TATOOINE.getName())) // Verify the name property of the first (and only) planet in the list
      .andExpect(jsonPath("$[0].terrain").value(TATOOINE.getTerrain())) // Verify the terrain property
      .andExpect(jsonPath("$[0].climate").value(TATOOINE.getClimate())); // Verify the climate property
  }

  @Test
  public void listPlanets_ReturnsNoPlanets() throws Exception {
    when(planetService.list(null, null)).thenReturn(Collections.emptyList());
    mockMvc.perform(get("/planets"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$", hasSize(0)));
  }
}
