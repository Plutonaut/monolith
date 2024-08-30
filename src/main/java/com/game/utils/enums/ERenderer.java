package com.game.utils.enums;

import lombok.Getter;
import lombok.experimental.Accessors;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Accessors(fluent = true)
@Getter
public enum ERenderer {
  BASIC("basic", "basic.vert", "basic.frag", null, null),
//  GUI("render", "gui", "gui.vert", "gui.frag", null, null),
  INSTANCED("instanced", "instanced.vert","instanced.frag", null, null),
  PARTICLE("particle", "particle.vert",null, "particle.geom", null),
  SPRITE("sprite", "sprite.vert", "sprite.frag", null, null),
  MESH("mesh", "mesh.vert", "mesh.frag", null, null),
  SCENE("scene", "scene.vert", "scene.frag", null, null),
  TERRAIN("terrain", "terrain.vert", "terrain.frag", null, null),
  FONT("font", "font.vert", "font.frag", null, null),
  BILLBOARD("billboard", "billboard.vert", "billboard.frag", "billboard.geom", null),
  RAYTRACE("raytrace", null, null, null, "raytrace.glsl"),
  FRAMEBUFFER("framebuffer", "framebuffer.vert", "framebuffer.frag", null, null),
  SKYBOX("skybox", "skybox.vert", "skybox.frag", null, null);

  private final String key;
  private final String vertex;
  private final String fragment;
  private final String geometry;
  private final String compute;

  ERenderer(String key, String vertex, String fragment, String geometry, String compute) {
    this.key = key;
    this.vertex = vertex;
    this.fragment = fragment;
    this.geometry = geometry;
    this.compute = compute;
  }

  public List<String> paths() {
    List<String> paths = new ArrayList<>();

    if (vertex != null) paths.add(path(vertex));
    if (geometry != null) paths.add(path(geometry));
    if (fragment != null) paths.add(path(fragment));
    if (compute != null) paths.add(path(compute));

    return paths;
  }

  String path(String shader) {
    if (shader == null || shader.isEmpty()) return shader;

    return File.separator + key + File.separator + shader;
  }
}
