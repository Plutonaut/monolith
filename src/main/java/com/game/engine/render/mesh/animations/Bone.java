package com.game.engine.render.mesh.animations;

import org.joml.Matrix4f;

public record Bone(int boneId, String boneName, Matrix4f offsetMatrix) {
}
