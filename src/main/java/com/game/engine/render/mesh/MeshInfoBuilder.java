package com.game.engine.render.mesh;

import com.game.engine.render.mesh.vertices.AttribInfo;
import com.game.engine.render.mesh.vertices.VertexInfo;
import com.game.graphics.materials.Material;
import com.game.utils.application.values.ValueStore;
import com.game.utils.enums.EAttribute;
import com.game.utils.enums.EMaterialColor;
import com.game.utils.enums.EMaterialTexture;
import lombok.extern.slf4j.Slf4j;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL46;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class MeshInfoBuilder {
  protected ArrayList<String> attributes;
  protected List<VertexInfo> vertices;
  protected ValueStore indices;
  protected Material material;
  protected String name;

  public MeshInfoBuilder() {
    attributes = new ArrayList<>();
    vertices = new ArrayList<>();
    indices = new ValueStore();
    material = null;
    name = null;
  }

  public MeshInfoBuilder use(String name) {
    if (this.name != null) {
      log.error("Pipeline currently in use! Please call flush before attempting to use");
      return null;
    }
    this.name = name;
    return this;
  }

  public MeshInfoBuilder material(String materialId) {
    material = new Material(materialId);
    return this;
  }

  public MeshInfoBuilder materialDiffuseTexture(String texturePath) {
    return materialTexture(EMaterialTexture.DIF.value(), texturePath);
  }

  public MeshInfoBuilder materialNormalTexture(String texturePath) {
    return materialTexture(EMaterialTexture.NRM.value(), texturePath);
  }

  public MeshInfoBuilder materialHeightTexture(String texturePath) {
    return materialTexture(EMaterialTexture.HGT.value(), texturePath);
  }

  public MeshInfoBuilder materialTexture(int textureType, String texturePath) {
    if (material != null && texturePath != null && !texturePath.isEmpty())
      material.texture(textureType, texturePath);
    return this;
  }

  public MeshInfoBuilder materialDiffuseColor(Vector4f colorValue) {
    return materialColor(EMaterialColor.DIF.getValue(), colorValue);
  }

  public MeshInfoBuilder materialSpecularColor(Vector4f colorValue) {
    return materialColor(EMaterialColor.SPC.getValue(), colorValue);
  }

  public MeshInfoBuilder materialAmbientColor(Vector4f colorValue) {
    return materialColor(EMaterialColor.AMB.getValue(), colorValue);
  }

  public MeshInfoBuilder materialColor(String colorType, Vector4f colorValue) {
    if (material != null && colorType != null && !colorType.isEmpty() && colorValue != null)
      material.color(colorType, colorValue);
    return this;
  }

  public MeshInfoBuilder positions(float[] values) {
    return positions(values, 3);
  }

  public MeshInfoBuilder positions(float[] values, int size) {
    return vertices(values, size, EAttribute.POS.getValue());
  }

  public MeshInfoBuilder positions(ValueStore store) {
    return positions(store, 3);
  }

  public MeshInfoBuilder positions(ValueStore store, int size) {
    return vertices(store, size, GL46.GL_FLOAT, EAttribute.POS.getValue());
  }

  public MeshInfoBuilder textureCoordinates(float[] values) {
    return vertices(values, 2, EAttribute.TXC.getValue());
  }

  public MeshInfoBuilder textureCoordinates(ValueStore store) {
    return textureCoordinates(store, 2);
  }

  public MeshInfoBuilder textureCoordinates(ValueStore store, int size) {
    return vertices(store, size, GL46.GL_FLOAT, EAttribute.TXC.getValue());
  }

  public MeshInfoBuilder colors(float[] values) {
    return vertices(values, EAttribute.CLR.getValue());
  }

  public MeshInfoBuilder normals(float[] values) {
    return vertices(values, EAttribute.NRM.getValue());
  }

  public MeshInfoBuilder normals(ValueStore store) {
    return vertices(store, 3, GL46.GL_FLOAT, EAttribute.NRM.getValue());
  }

  public MeshInfoBuilder tangents(float[] values) {
    return vertices(values, 2, EAttribute.TAN.getValue());
  }

  public MeshInfoBuilder biTangents(float[] values) {
    return vertices(values, EAttribute.BTA.getValue());
  }

  public MeshInfoBuilder boneIds(int[] values) {
    return vertices(values, 4, EAttribute.BON.getValue());
  }

  public MeshInfoBuilder weights(float[] values) {
    return vertices(values, 4, EAttribute.WGT.getValue());
  }

  public MeshInfoBuilder instanceMatrices(ValueStore store) {
    int instances = store.size() / 16;
    return vertices(store, 16, EAttribute.IMX.getValue(), instances);
  }

  public MeshInfoBuilder instanceMatrices(float[] values) {
    int instances = values.length / 16;
    return vertices(values, 16, EAttribute.IMX.getValue(), instances);
  }

  public MeshInfoBuilder indices(int[] values) {
    indices.set(values);
    return this;
  }

  public MeshInfoBuilder indices(ValueStore store) {
    indices.set(store);
    return this;
  }

  public MeshInfoBuilder vertices(float[] values, String attribute) {
    return vertices(values, 3, attribute);
  }

  public MeshInfoBuilder vertices(int[] values, int size, String attribute) {
    ValueStore store = new ValueStore();
    store.add(values);
    return vertices(store, size, GL46.GL_INT, attribute);
  }

  public MeshInfoBuilder vertices(float[] values, int size, String attribute) {
    ValueStore store = new ValueStore();
    store.add(values);
    return vertices(store, size, GL46.GL_FLOAT, attribute);
  }

  public MeshInfoBuilder vertices(float[] values, int size, String attribute, int instances) {
    ValueStore store = new ValueStore();
    store.add(values);
    return vertices(store, size, attribute, instances);
  }

  public MeshInfoBuilder vertices(ValueStore store, int size, String attribute, int instances) {
    return vertices(store, size, GL46.GL_FLOAT, GL46.GL_STATIC_DRAW, attribute, instances);
  }

  public MeshInfoBuilder vertices(ValueStore store, int size, int glType, String attribute) {
    return vertices(store, size, glType, GL46.GL_STATIC_DRAW, attribute);
  }

  public MeshInfoBuilder vertices(
    ValueStore store,
    int size,
    int glType,
    int glUsage,
    String attribute
  ) {
    return vertices(store, size, glType, glUsage, attribute, 1);
  }

  public MeshInfoBuilder vertices(
    ValueStore store, int size, int glType, int glUsage, String attribute, int instances
  ) {
    if (attributes.contains(attribute)) {
      log.error("{} already contains attribute: {}!", name, attribute);
      return null;
    } else if (!store.isEmpty()) {
      AttribInfo attribInfo = new AttribInfo(attribute, size, instances, 0);
      VertexInfo vertexInfo = new VertexInfo(store, glType, glUsage, attribInfo);
      vertices.add(vertexInfo);
      attributes.add(attribute);
    }
    return this;
  }

  public MeshInfoBuilder vertices(VertexInfo info) {
    vertices.add(info);
    return this;
  }

  public MeshInfo build() {
    final MeshInfo meshInfo = new MeshInfo(name);
    return constructMeshInfo(meshInfo);
  }

  public FontMeshInfo build(String text, Font font) {
    final FontMeshInfo meshInfo = new FontMeshInfo(name, text, font);
    constructMeshInfo(meshInfo);
    return meshInfo;
  }

  MeshInfo constructMeshInfo(MeshInfo meshInfo) {
    vertices.forEach(meshInfo::addVertices);
    meshInfo.indices.set(indices);
    int vertexCount = indices.size();
    if (vertexCount == 0) {
      VertexInfo info = meshInfo.getVerticesByAttribute(EAttribute.POS);
      vertexCount = info.totalVertexCount();
    }
    meshInfo.vertexCount(vertexCount);
    if (material != null) meshInfo.material(material);
    dispose();

    return meshInfo;
  }

  protected void dispose() {
    attributes.clear();
    vertices.clear();
    indices.clear();
    material = null;
    name = null;
  }
}
