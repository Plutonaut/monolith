package com.game.utils.enums;

import lombok.Getter;
import lombok.experimental.Accessors;
import org.lwjgl.assimp.Assimp;

import java.util.List;

@Accessors(fluent = true)
@Getter
public enum EMaterialTexture {
  NON(Assimp.aiTextureType_NONE, null),
  DIF(Assimp.aiTextureType_DIFFUSE, EUniform.TEXTURE_SAMPLER),
  NRM(Assimp.aiTextureType_NORMALS, EUniform.NORMAL_SAMPLER),
  HGT(Assimp.aiTextureType_HEIGHT, EUniform.HEIGHT_SAMPLER),
  RGH(Assimp.aiTextureType_DIFFUSE_ROUGHNESS, EUniform.ROUGHNESS_SAMPLER),
  AO(Assimp.aiTextureType_AMBIENT_OCCLUSION, EUniform.AO_SAMPLER),
  MTL(Assimp.aiTextureType_METALNESS, EUniform.METALLIC_SAMPLER),
  EMS(Assimp.aiTextureType_EMISSIVE, EUniform.EMISSION_SAMPLER);

  private final int value;
  private final EUniform uniform;

  EMaterialTexture(int value, EUniform uniform) {
    this.value = value;
    this.uniform = uniform;
  }

  public static List<EMaterialTexture> types() {
    return List.of(DIF, NRM, HGT, RGH, AO, MTL, EMS);
  }

  public static EUniform getUniformByType(int type) {
    return switch (type) {
      case Assimp.aiTextureType_DIFFUSE -> DIF.uniform;
      case Assimp.aiTextureType_NORMALS -> NRM.uniform;
      case Assimp.aiTextureType_HEIGHT -> HGT.uniform;
      case Assimp.aiTextureType_DIFFUSE_ROUGHNESS -> RGH.uniform;
      case Assimp.aiTextureType_AMBIENT_OCCLUSION -> AO.uniform;
      case Assimp.aiTextureType_METALNESS -> MTL.uniform;
      case Assimp.aiTextureType_EMISSIVE -> EMS.uniform;
      default -> NON.uniform;
    };
  }
}
