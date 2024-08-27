package com.game.engine.scene.generators;

import com.game.utils.application.values.ValueMap;
import com.game.utils.application.values.ValueStore;
import org.apache.commons.lang3.StringUtils;
import org.joml.Vector3f;

import java.awt.*;

// Not much of a builder...
public class GeneratorParameterBuilder {
  protected final ValueMap map;

  public GeneratorParameterBuilder() {
    map = new ValueMap();
  }

  public GeneratorParameterBuilder id(String id) {
    map.set("id", id);
    return this;
  }

  public GeneratorParameterBuilder instances(int instances) {
    map.setInt("instances", instances);
    return this;
  }

  public GeneratorParameterBuilder type(String type) {
    map.set("type", type);
    return this;
  }

  public GeneratorParameterBuilder asObject() {
    return type("object");
  }

  public GeneratorParameterBuilder asSprite() {
    return type("sprite");
  }

  public GeneratorParameterBuilder asBillboard() {
    return type("billboard");
  }

  public GeneratorParameterBuilder asText() {
    return type("text");
  }

  public GeneratorParameterBuilder asTerrain() {
    return type("terrain");
  }

  public GeneratorParameterBuilder path(String path) {
    map.set("path", path);
    return this;
  }

  public GeneratorParameterBuilder text(String text) {
    map.set("text", text);
    return this;
  }

  public GeneratorParameterBuilder animated(boolean animated) {
    map.setBool("animated", animated);
    return this;
  }

  public GeneratorParameterBuilder clamped(boolean clamped) {
    map.setBool("clamped", clamped);
    return this;
  }

  public GeneratorParameterBuilder fontName(String fontName) {
    map.set("fontName", fontName);
    return this;
  }

  public GeneratorParameterBuilder fontSize(int fontSize) {
    map.setInt("fontSize", fontSize);
    return this;
  }

  public GeneratorParameterBuilder fontColor(Color fontColor) {
    map.setColor("fontColor", fontColor);
    return this;
  }

  public GeneratorParameterBuilder antiAlias(boolean antiAlias) {
    map.setBool("antiAlias", antiAlias);
    return this;
  }

  public GeneratorParameterBuilder position(Vector3f position) {
    map.setVector3f("position", position);
    return this;
  }

  public GeneratorParameterBuilder positions(ValueStore store) {
    return positions(store.asArray());
  }

  public GeneratorParameterBuilder positions(float... positions) {
    map.setFloats("positions", positions);
    return this;
  }

  public GeneratorParameterBuilder diffuseColor(Color diffuseColor) {
    map.setColor("diffuseColor", diffuseColor);
    return this;
  }

  public GeneratorParameterBuilder heightMapTexturePath(String heightMapTexturePath) {
    map.set("heightMapTexturePath", heightMapTexturePath);
    return this;
  }

  public GeneratorParameterBuilder diffuseTexturePath(String diffuseTexturePath) {
    map.set("diffuseTexturePath", diffuseTexturePath);
    return this;
  }

  public GeneratorParameterBuilder normalTexturePath(String normalTexturePath) {
    map.set("normalTexturePath", normalTexturePath);
    return this;
  }

  public GeneratorParameterBuilder width(int width) {
    map.setInt("width", width);
    return this;
  }

  public GeneratorParameterBuilder height(int height) {
    map.setInt("height", height);
    return this;
  }

  public GeneratorParameterBuilder minVertexHeight(float minVertexHeight) {
    map.setFloat("minVertexHeight", minVertexHeight);
    return this;
  }

  public GeneratorParameterBuilder maxVertexHeight(float maxVertexHeight) {
    map.setFloat("maxVertexHeight", maxVertexHeight);
    return this;
  }

  public GeneratorParameterBuilder useNoise() {
    return strategy("noise");
  }

  public GeneratorParameterBuilder useTexture() {
    return strategy("texture");
  }

  public GeneratorParameterBuilder strategy(String strategy) {
    map.set("strategy", strategy);
    return this;
  }

  public GeneratorParameterBuilder spawners(String... spawners) {
    map.setArray("spawners", spawners);
    return this;
  }

  public ValueMap build() {
    ValueMap results = new ValueMap();
    if (StringUtils.isEmpty(map.get("type"))) asObject();
    if (StringUtils.isEmpty(map.get("id")) && !StringUtils.isEmpty(map.get("path")))
      id(map.get("path"));
    results.copy(map);
    map.clear();
    return results;
  }
}
