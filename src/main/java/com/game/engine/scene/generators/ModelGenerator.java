package com.game.engine.scene.generators;

import com.game.caches.GlobalCache;
import com.game.engine.render.mesh.FontMeshInfo;
import com.game.engine.render.models.Model;
import com.game.engine.scene.generators.data.ProceduralTerrainGenerationData;
import com.game.engine.scene.generators.data.ResourceGenerationData;
import com.game.engine.scene.generators.data.SpriteGenerationData;
import com.game.engine.scene.generators.data.TextGenerationData;
import com.game.engine.scene.terrain.procedural.ProceduralNoiseData;
import com.game.graphics.texture.TextureMapData;
import com.game.utils.engine.entity.StaticEntityData;
import com.game.utils.enums.EModel;
import com.game.utils.enums.ESprite;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.awt.*;

@Accessors(fluent = true)
@Getter
public class ModelGenerator {
  protected final ProceduralTerrainModelGenerator proceduralTerrain;
  protected final ObjectResourceModelGenerator model3D;
  protected final SpriteResourceModelGenerator model2D;
  protected final TextModelGenerator modelText;

  public ModelGenerator() {
    proceduralTerrain = new ProceduralTerrainModelGenerator();
    model3D = new ObjectResourceModelGenerator();
    model2D = new SpriteResourceModelGenerator();
    modelText = new TextModelGenerator();
  }

  /**
   * --------------------------------------------------------------------------------------------------------------------------------------------
   *
   * @param model
   *
   * @return Model object containing vertex and material shader data
   */
  public Model generate3DModel(EModel model) {
    return generate3DModel(model.name(), model.path(), model.animated());
  }

  public Model generate3DModel(String name, String path) {
    return generate3DModel(name, path, false);
  }

  public Model generate3DModel(String name, String path, boolean animated) {
    return model3D.generateModel(new ResourceGenerationData(name, path, animated));
  }

  /**
   * --------------------------------------------------------------------------------------------------------------------------------------------
   *
   * @param sprite
   *
   * @return Model object containing vertex and material shader data
   */
  public Model generate2DModel(ESprite sprite) {
    return generate2DModel(sprite.atlasName(), sprite.path());
  }

  public Model generate2DModel(String id, String path) {
    return generate2DModel(id, path, false);
  }

  public Model generate2DModel(String id, String path, boolean animated) {
    return generate2DModel(id, path, animated, false);
  }

  public Model generate2DModel(
    String id,
    String path,
    boolean animated,
    boolean clamped
  ) {
    return model2D.generateModel(new SpriteGenerationData(id, path, animated, clamped));
  }

  /**
   * --------------------------------------------------------------------------------------------------------------------------------------------
   *
   * @param id
   * @param text
   *
   * @return Model object containing vertex and material shader data
   */
  public Model generateText(String id, String text) {
    return generateText(id, text, "Arial", 20);
  }

  public Model generateText(String id, String text, String font, int size) {
    Font f = GlobalCache.instance().getFont(font, size);
    return generateText(id, text, f);
  }

  public Model generateText(String id, String text, Font font) {
    return generateText(id, text, font, Color.white);
  }

  public Model generateText(String id, String text, Font font, Color color) {
    return generateText(id, text, font, color, false);
  }

  public Model generateText(String id, String text, Font font, Color color, boolean antiAlias) {
    return generateText(id, text, font, color, color, antiAlias);
  }

  public Model generateText(
    String id,
    String text,
    Font font,
    Color color,
    Color materialColor,
    boolean antiAlias
  ) {
    return modelText.generateModel(new TextGenerationData(
      id,
      font,
      color,
      materialColor,
      antiAlias,
      text
    ));
  }

  /**
   * @param id
   * @param text
   * @param font
   *
   * @return mesh info object containing position, texture coordinate, and index vertex data.
   */
  public FontMeshInfo regenerateText(String id, String text, Font font) {
    return regenerateText(id, text, font, Color.white);
  }

  public FontMeshInfo regenerateText(String id, String text, Font font, Color color) {
    return regenerateText(id, text, font, color, color, false);
  }


  public FontMeshInfo regenerateText(
    String id, String text, Font font, Color color, Color materialColor, boolean antiAlias
  ) {
    return modelText.generateMeshInfo(new TextGenerationData(
      id,
      font,
      color,
      materialColor,
      antiAlias,
      text
    ));
  }

  /**
   * --------------------------------------------------------------------------------------------------------------------------------------------
   *
   * @param id
   * @param textureMap
   * @param width
   * @param height
   *
   * @return Model object containing vertex and material shader data
   */
  public Model generateProceduralTerrain(
    String id,
    TextureMapData textureMap,
    int width,
    int height
  ) {
    return generateProceduralTerrain(id, textureMap, width, height, 0f, 0.1f);
  }

  public Model generateProceduralTerrain(
    String id,
    TextureMapData textureMap,
    int width,
    int height,
    float minVertexHeight,
    float maxVertexHeight
  ) {
    return generateProceduralTerrain(
      id,
      textureMap,
      width,
      height,
      minVertexHeight,
      maxVertexHeight,
      1.0f
    );
  }

  public Model generateProceduralTerrain(
    String id,
    TextureMapData textureMap,
    int width,
    int height,
    float minVertexHeight,
    float maxVertexHeight,
    float scale
  ) {
    return generateProceduralTerrain(
      id,
      textureMap,
      width,
      height,
      minVertexHeight,
      maxVertexHeight,
      scale,
      0
    );
  }

  public Model generateProceduralTerrain(
    String id,
    TextureMapData textureMap,
    int width,
    int height,
    float minVertexHeight,
    float maxVertexHeight,
    float scale,
    int seed
  ) {
    return generateProceduralTerrain(
      id,
      textureMap,
      width,
      height,
      minVertexHeight,
      maxVertexHeight,
      StaticEntityData.PROCEDURAL_NOISE.copy().scale(scale).seed(seed)
    );
  }

  public Model generateProceduralTerrain(
    String id,
    TextureMapData textureMapData,
    int width,
    int height,
    float minVertexHeight,
    float maxVertexHeight,
    ProceduralNoiseData noise
  ) {
    return proceduralTerrain.generateModel(new ProceduralTerrainGenerationData(
      id,
      width,
      height,
      minVertexHeight,
      maxVertexHeight,
      textureMapData,
      noise
    ));
  }
}
