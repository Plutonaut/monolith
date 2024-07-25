package com.game.engine.scene.entities;

import com.game.engine.render.IRenderable;
import com.game.engine.render.mesh.DynamicMesh;
import com.game.engine.render.mesh.FontMeshInfo;
import com.game.engine.render.mesh.vertices.VertexBufferObject;
import com.game.engine.scene.entities.transforms.ModelTransform;
import com.game.utils.engine.loaders.FontResourceLoader;
import lombok.Data;
import lombok.experimental.Accessors;

import java.awt.*;
import java.util.HashMap;

@Accessors(fluent = true)
@Data
public class TextEntity implements IRenderable {
  private final ModelTransform transform;
  private final DynamicMesh mesh;
  private final String name;
  private final HashMap<String, VertexBufferObject> vboAttributeKeyMap;
  private Font font;
  private String text;
  private String updatedText;

  public TextEntity(String name, DynamicMesh mesh, String text, Font font) {
    this.transform = new ModelTransform();
    this.name = name;
    this.mesh = mesh;
    this.text = text;
    this.font = font;

    updatedText = text;
    vboAttributeKeyMap = new HashMap<>();
  }

  public TextEntity move(float x, float y) {
    transform.position().set(x, y, 0f);
    return this;
  }

  public TextEntity scale(float s) {
    transform.scale(s);
    return this;
  }

  public void redraw(String text) {
    if (text == null || this.text.equals(text)) return;
    FontMeshInfo info = FontResourceLoader.build(name, text, font);
    mesh.redraw(info, null);
    this.text = text;
  }
}
