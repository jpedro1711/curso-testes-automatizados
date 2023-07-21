package com.example.swplanetapi.common;

import java.util.ArrayList;

import org.hibernate.mapping.List;

import com.example.swplanetapi.domain.Planet;

public class PlanetConstants {
  public static final Planet PLANET = new Planet("name", "climate", "terrain");
  public static final Planet INVALID_PLANET = new Planet("", "", "");

  public static final Planet TATOOINE = new Planet("Tatooine", "arid", "desert");
  public static final Planet ALDERAAN = new Planet("Alderaan", "temperate", "grasslands, mountains");
  public static final Planet YAVINIV = new Planet("Yaviniv", "temperate, tropical", "jungle, rainforest");
  public static final ArrayList<Planet> PLANETS = new ArrayList<>() {
    {
      add(TATOOINE);
      add(ALDERAAN);
      add(YAVINIV);
    }
  };
}
