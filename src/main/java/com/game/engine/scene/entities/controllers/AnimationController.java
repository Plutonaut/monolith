package com.game.engine.scene.entities.controllers;

import com.game.engine.scene.entities.animations.Animation;
import com.game.utils.enums.EController;
import org.joml.Matrix4f;

import java.util.HashMap;

public class AnimationController extends AbstractController {
  protected final HashMap<String, Animation> animations;
  protected Animation animation;

  protected int speed;

  public AnimationController() {
    this.animations = new HashMap<>();

    animation = get();
  }

  @Override
  public void onUpdate() {
    animation.move(speed);
  }

  @Override
  public String type() {
    return EController.ANIM.getValue();
  }

  public void add(Animation... animations) {
    for (Animation animation : animations) {
      this.animations.put(animation.name(), animation);
    }
  }

  public void play() {
    speed = 1;
  }

  public void reverse() {
    speed = -1;
  }

  public void stop() {
    speed = 0;
  }

  public Matrix4f[] frameBoneMatrices() {
    return animation.frame().boneMatrices();
  }

  protected Animation get() {
    String animation = animations.keySet().stream().findFirst().orElse(null);

    return get(animation);
  }

  protected Animation get(String animation) {
    return animation != null ? animations.getOrDefault(animation, null) : null;
  }
}
