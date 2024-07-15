package com.game.engine.render.mesh.definitions;

import com.game.engine.render.mesh.MeshInfo;
import com.game.engine.render.mesh.MeshInfoBuilder;
import com.game.engine.render.models.Model;
import lombok.Data;
import lombok.experimental.Accessors;
import org.joml.Vector4f;

@Accessors(fluent = true)
@Data
public abstract class MeshDefinition {
  protected MeshInfoBuilder builder;
  protected Vector4f diffuseColor;
  protected Vector4f ambientColor;
  protected Vector4f specularColor;
  protected String diffuseTexture;
  protected String normalTexture;
  protected String heightTexture;
  protected float[] positions;
  protected float[] textureCoordinates;
  protected float[] normals;
  protected float[] colors;
  protected float[] tangents;
  protected float[] biTangents;
  protected int[] boneIds;
  protected float[] weights;
  protected int[] indices;

  public MeshDefinition() {
    builder = new MeshInfoBuilder();

    init();
  }

  public abstract String name();

  protected abstract void init();

  protected MeshInfoBuilder builder() {
    return builder
      .use(name())
      .positions(positions)
      .textureCoordinates(textureCoordinates)
      .normals(normals)
      .colors(colors)
      .tangents(tangents)
      .biTangents(biTangents)
      .boneIds(boneIds)
      .weights(weights)
      .indices(indices)
      .materialDiffuseColor(diffuseColor)
      .materialAmbientColor(ambientColor)
      .materialSpecularColor(specularColor)
      .materialDiffuseTexture(diffuseTexture)
      .materialNormalTexture(normalTexture)
      .materialHeightTexture(heightTexture);
  }

  public MeshInfo createMeshInfo() {
    return builder().build();
  }

  public Model createModel() {
    return builder().model(name()).build();
  }
}
