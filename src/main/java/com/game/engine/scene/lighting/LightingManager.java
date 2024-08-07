package com.game.engine.scene.lighting;

import com.game.engine.scene.lighting.lights.DirectionalLight;
import com.game.engine.scene.lighting.lights.Light;
import com.game.engine.scene.lighting.lights.PointLight;
import com.game.engine.scene.lighting.lights.SpotLight;
import com.game.utils.enums.ELighting;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.HashMap;
import java.util.List;

@Accessors(fluent = true)
@Data
public class LightingManager {
  protected final HashMap<String, Light> lights;

  public LightingManager() {
    lights = new HashMap<>();
  }

  public boolean hasAmbientLight() {
    return lights.containsKey(ELighting.AMBIENT.value());
  }

  public boolean hasDirectionalLight() {
    return lights.containsKey(ELighting.DIRECTIONAL.value());
  }

  public boolean hasPointLights() {return lights.keySet().stream().anyMatch(ELighting.POINT::isType);}

  public boolean hasSpotLights() {return lights.keySet().stream().anyMatch(ELighting.SPOT::isType);}

  public Light ambientLight() {return lights.computeIfAbsent(ELighting.AMBIENT.value(), k -> new Light());}

  public LightingManager addAmbientLight() {
    ambientLight();
    return this;
  }

  public DirectionalLight directionalLight() {
    return (DirectionalLight) lights.computeIfAbsent(ELighting.DIRECTIONAL.value(), k -> new DirectionalLight());
  }

  public LightingManager addDirectionalLight() {
    directionalLight();
    return this;
  }

  public List<PointLight> pointLights() {
    return lights.keySet().stream().filter(ELighting.POINT::isType).map(this::pointLight).toList();
  }

  public PointLight pointLight(String key) {
    return (PointLight) lights.get(ELighting.POINT.prefix(key));
  }

  public LightingManager addPointLight(String key) {
    lights.putIfAbsent(ELighting.POINT.prefix(key), new PointLight());
    return this;
  }

  public List<SpotLight> spotLights() {
    return lights.keySet().stream().filter(ELighting.SPOT::isType).map(this::spotLight).toList();
  }

  public SpotLight spotLight(String key) {
    return (SpotLight) lights.get(ELighting.SPOT.prefix(key));
  }

  public LightingManager addSpotLight(String key) {
    lights.putIfAbsent(ELighting.SPOT.prefix(key), new SpotLight());
    return this;
  }
}
