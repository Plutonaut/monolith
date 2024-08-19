package com.game.engine.render.renderers;

import com.game.caches.GlobalCache;
import com.game.engine.scene.lighting.Attenuation;
import com.game.engine.scene.lighting.LightingManager;
import com.game.engine.scene.lighting.lights.DirectionalLight;
import com.game.engine.scene.lighting.lights.Light;
import com.game.engine.scene.lighting.lights.PointLight;
import com.game.engine.scene.lighting.lights.SpotLight;
import com.game.graphics.materials.Material;
import com.game.graphics.materials.MaterialTexturePack;
import com.game.graphics.texture.Texture;
import com.game.utils.application.LambdaCounter;
import com.game.utils.enums.EMaterialColor;
import com.game.utils.enums.EMaterialTexture;
import com.game.utils.enums.EUniform;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.util.List;

// TODO: Replace with some utility inner object used by UniformCache.
public abstract class AbstractLitRenderer extends AbstractRenderer {
  protected void setMaterialUniform(Material material) {
    program.uniforms().set(
      EUniform.MATERIAL_AMBIENT.value(),
      material.color(EMaterialColor.AMB.getValue())
    );
    program.uniforms().set(
      EUniform.MATERIAL_DIFFUSE.value(),
      material.color(EMaterialColor.DIF.getValue())
    );
    program.uniforms().set(
      EUniform.MATERIAL_SPECULAR.value(),
      material.color(EMaterialColor.SPC.getValue())
    );
    program.uniforms().set(EUniform.MATERIAL_REFLECTANCE.value(), material.reflectance());

    MaterialTexturePack textures = material.textures();
    if (textures.hasTextures()) setMaterialTextureUniform(textures);
  }

  protected void setMaterialTextureUniform(MaterialTexturePack textures) {
    program.uniforms().set(
      EUniform.MATERIAL_HAS_TEXTURE.value(),
      textures.hasTexture(EMaterialTexture.DIF.value())
    );
    program.uniforms().set(
      EUniform.MATERIAL_HAS_NORMAL_MAP.value(),
      textures.hasTexture(EMaterialTexture.NRM.value())
    );
    program.uniforms().set(
      EUniform.MATERIAL_HAS_HEIGHT_MAP.value(),
      textures.hasTexture(EMaterialTexture.HGT.value())
    );

    LambdaCounter counter = new LambdaCounter();
    textures.pack().forEach((type, path) -> {
      String uniform = EMaterialTexture.getUniformByType(type).value();
      if (program.uniforms().has(uniform)) {
        int index = counter.inc();
        Texture texture = GlobalCache.instance().texture(path);
        if (texture != null) {
          texture.active(index);
          texture.bind();
          program.uniforms().set(uniform, index);
        }
      }
    });
  }

  protected void setLightingUniforms(LightingManager lighting, Matrix4f viewMatrix) {
    if (lighting.hasAmbientLight()) setAmbientLightUniform(lighting.ambientLight());
    if (lighting.hasDirectionalLight()) setDirectionalLightUniform(
      lighting.directionalLight(),
      viewMatrix
    );
    if (lighting.hasPointLights()) setPointLightUniforms(lighting.pointLights(), viewMatrix);
    if (lighting.hasSpotLights()) setSpotLightUniforms(lighting.spotLights(), viewMatrix);
  }

  protected void setAmbientLightUniform(Light light) {
    String uniform = EUniform.AMBIENT_LIGHT.value();
    program.uniforms().set(uniform + EUniform.FACTOR.value(), light.factor());
    program.uniforms().set(uniform + EUniform.COLOR.value(), light.color());
  }

  protected void setDirectionalLightUniform(DirectionalLight light, Matrix4f viewMatrix) {
    String uniform = EUniform.DIRECTIONAL_LIGHT.value();
    Vector4f auxDirection = new Vector4f(light.direction(), 0.0f);
    auxDirection.mul(viewMatrix);
    Vector3f direction = new Vector3f(auxDirection.x, auxDirection.y, auxDirection.z);

    program.uniforms().set(uniform + EUniform.DIRECTION.value(), direction);
    program.uniforms().set(uniform + EUniform.COLOR.value(), light.color());
    program.uniforms().set(uniform + EUniform.INTENSITY.value(), light.intensity());
  }

  protected void setSpotLightUniforms(List<SpotLight> spotLights, Matrix4f viewMatrix) {
    int size = spotLights.size();

    for (int i = 0; i < size; i++)
      setSpotLightUniform(spotLights.get(i), i, viewMatrix);
  }

  protected void setSpotLightUniform(SpotLight spotLight, int index, Matrix4f viewMatrix) {
    String uniform = EUniform.SPOT_LIGHTS.value() + "[" + index + "]";
    setSpotLightUniform(uniform, spotLight, viewMatrix);
  }

  protected void setSpotLightUniform(String uniform, SpotLight spotLight, Matrix4f viewMatrix) {
    PointLight pointLight = null;
    Vector3f coneDirection = new Vector3f();
    float cutoff = 0.0f;

    if (spotLight != null) {
      coneDirection = spotLight.coneDirection();
      cutoff = spotLight.cutoff();
      pointLight = spotLight.pointLight();
    }

    program.uniforms().set(uniform + EUniform.CONE_DIRECTION.value(), coneDirection);
    program.uniforms().set(uniform + EUniform.CUT_OFF.value(), cutoff);
    setPointLightUniform(uniform + EUniform.PL.value(), pointLight, viewMatrix);
  }

  protected void setPointLightUniforms(List<PointLight> pointLights, Matrix4f viewMatrix) {
    int size = pointLights.size();
    for (int i = 0; i < size; i++)
      setPointLightUniform(pointLights.get(i), i, viewMatrix);
  }

  protected void setPointLightUniform(PointLight pointLight, int index, Matrix4f viewMatrix) {
    String uniform = EUniform.POINT_LIGHTS.value() + "[" + index + "]";
    setPointLightUniform(uniform, pointLight, viewMatrix);
  }

  protected void setPointLightUniform(String uniform, PointLight pointLight, Matrix4f viewMatrix) {
    Vector4f aux = new Vector4f();
    Vector3f lightPosition = new Vector3f();
    Vector3f color = new Vector3f();

    float intensity = 0.0f;
    float constant = 0.0f;
    float linear = 0.0f;
    float exponent = 0.0f;

    if (pointLight != null) {
      aux.set(pointLight.position(), 1);
      aux.mul(viewMatrix);
      lightPosition.set(aux.x, aux.y, aux.z);
      color.set(pointLight.color());
      intensity = pointLight.intensity();

      final Attenuation att = pointLight.attenuation();
      constant = att.constant();
      linear = att.linear();
      exponent = att.exponent();
    }

    program.uniforms().set(uniform + EUniform.POSITION.value(), lightPosition);
    program.uniforms().set(uniform + EUniform.COLOR.value(), color);
    program.uniforms().set(uniform + EUniform.INTENSITY.value(), intensity);
    program.uniforms().set(
      uniform + EUniform.ATTENUATION.value() + EUniform.CONSTANT.value(),
      constant
    );
    program.uniforms().set(
      uniform + EUniform.ATTENUATION.value() + EUniform.LINEAR.value(),
      linear
    );
    program.uniforms().set(
      uniform + EUniform.ATTENUATION.value() + EUniform.EXPONENT.value(),
      exponent
    );
  }
}
