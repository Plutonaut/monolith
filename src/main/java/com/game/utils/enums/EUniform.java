package com.game.utils.enums;

import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
public enum EUniform {
  PROJECTION("projection"),
  MODEL("model"),
  VIEW("view"),
  MODEL_VIEW("modelview"),
  TERRAIN_CHUNK_MODEL("terrainmodel"),
  USE_TERRAIN_MODEL("useTerrainModel"),
  IS_INSTANCED("isInstanced"),
  HAS_INSTANCED_COLORS("hasInstancedColors"),
  TEXTURE_COLOR("textureColor"),
  TEXTURE_SAMPLER("textureSampler"),
  NORMAL_SAMPLER("normalSampler"),
  HEIGHT_SAMPLER("heightSampler"),
  ROUGHNESS_SAMPLER("roughnessSampler"),
  AO_SAMPLER("aoSampler"),
  METALLIC_SAMPLER("metallicSampler"),
  EMISSION_SAMPLER("emissionSampler"),
  TEXTURE_IMAGE("texImage"),
  SPECULAR_POWER("specularPower"),
  HEIGHT_SCALE("heightScale"),
  POSITION(".position"),
  INTENSITY(".intensity"),
  DIRECTION(".direction"),
  CONE_DIRECTION(".conedir"),
  PL(".pl"),
  CUT_OFF(".cutoff"),
  ATTENUATION(".att"),
  CONSTANT(".constant"),
  LINEAR(".linear"),
  EXPONENT(".exponent"),
  COLOR(".color"),
  FACTOR(".factor"),
  MATERIAL_AMBIENT("material.ambient"),
  MATERIAL_DIFFUSE("material.diffuse"),
  MATERIAL_SPECULAR("material.specular"),
  MATERIAL_REFLECTANCE("material.reflectance"),
  MATERIAL_HAS_TEXTURE("material.hasTexture"),
  MATERIAL_HAS_NORMAL_MAP("material.hasNormalMap"),
  MATERIAL_HAS_HEIGHT_MAP("material.hasHeightMap"),
  AMBIENT_LIGHT("ambientLight"),
  DIRECTIONAL_LIGHT("dirLight"),
  POINT_LIGHTS("pointLights"),
  SPOT_LIGHTS("spotLights"),
  MATERIAL("material"),
  SELECTED("isSelected"),
  ANIMATED("isAnimated"),
  BONE_MATRICES("boneMatrices"),
  SCREEN_WIDTH("screenwidth"),
  OUT_IMAGE("outimage"),
  ZOOM("zoom"),
  TIME_STEP("timestep"),
  SIZE("size"),
  PERSISTENCE("persistence"),
  OCTAVES("octaves");

  @Accessors(fluent = true)
  private final String value;

  EUniform(String value) {
    this.value = value;
  }
}
