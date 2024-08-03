package com.game.engine.scene.entities;

import com.game.utils.enums.EModifier;
import com.game.utils.enums.EProjection;
import com.game.utils.enums.ERenderer;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.ArrayList;

@Accessors(fluent = true)
@Data
public class EntityRenderParameters {
  protected final ArrayList<EModifier> modifiers;
  protected ERenderer shader;
  protected EProjection projection;
  protected int glParamFlags;

  public EntityRenderParameters() {
    this(ERenderer.SCENE, EProjection.PERSPECTIVE, 0);
  }

  public EntityRenderParameters(ERenderer shader, EProjection projection, int glParamFlags) {
    this.shader = shader;
    this.glParamFlags = glParamFlags;
    this.projection = projection;
    modifiers = new ArrayList<>();
  }

  public void toggleModifier(EModifier modifier) {
    if (isModifierActive(modifier)) modifiers.remove(modifier);
    else modifiers.add(modifier);
  }

  public boolean isFlagActive(int flag) {
    return (glParamFlags & flag) == flag;
  }

  public boolean hasShader(ERenderer shader) {
    return this.shader == shader;
  }

  public boolean isModifierActive(EModifier modifier) {
    return modifiers.contains(modifier);
  }
}
