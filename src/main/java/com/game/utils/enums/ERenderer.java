package com.game.utils.enums;

import lombok.Getter;
import lombok.experimental.Accessors;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Accessors(fluent = true)
@Getter
public enum ERenderer {
  BASIC("render", "basic", "basic.vert", "basic.frag", null, null),
  GUI("render", "gui", "gui.vert", "gui.frag", null, null),
  PARTICLE("render", "instanced", "instanced.vert","instanced.frag", null, null),
  SPRITE("render", "sprite", "sprite.vert", "sprite.frag", null, null),
  MESH("render", "mesh", "mesh.vert", "mesh.frag", null, null),
  SCENE("render", "scene", "scene.vert", "scene.frag", null, null),
  FONT("render", "font", "font.vert", "font.frag", null, null),
  SKYBOX("render", "skybox", "skybox.vert", "skybox.frag", null, null);

  private final String type;
  private final String key;
  private final String vertex;
  private final String fragment;
  private final String geometry;
  private final String compute;

  ERenderer(String type, String key, String vertex, String fragment, String geometry, String compute) {
    this.type = type;
    this.key = key;
    this.vertex = vertex;
    this.fragment = fragment;
    this.geometry = geometry;
    this.compute = compute;
  }

  public List<String> paths() {
    List<String> paths = new ArrayList<>();

    if (vertex != null) paths.add(path(vertex));
    if (fragment != null) paths.add(path(fragment));
    if (geometry != null) paths.add(path(geometry));
    if (compute != null) paths.add(path(compute));

    return paths;
  }

  public String vertexPath() {
    return path(vertex);
  }

  public String fragmentPath() {
    return path(fragment);
  }

  String path(String shader) {
    if (shader == null || shader.isEmpty()) return shader;

    return type + File.separator + key + File.separator + shader;
  }
}
