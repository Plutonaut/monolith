package com.game.utils.engine.entity;

import com.game.engine.render.mesh.animations.Bone;
import com.game.engine.render.mesh.animations.Node;
import com.game.engine.scene.entities.animations.Animation;
import com.game.engine.scene.entities.animations.Animation3D;
import com.game.engine.scene.entities.animations.BoneMatrixFrame;
import com.game.utils.math.MatrixUtils;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.lwjgl.PointerBuffer;
import org.lwjgl.assimp.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AnimationUtils {
  public static final int MAX_BONES = 150;

  public static List<Animation> process(AIScene scene, List<Bone> bones) {
    ArrayList<Animation> animation3DS = new ArrayList<>();
    AINode aiNode = scene.mRootNode();

    if (scene.mNumAnimations() > 0 && aiNode != null) {
      Node root = process(aiNode, null);
      Matrix4f globalInverseTransform = root.transform().invert();

      int count = scene.mNumAnimations();
      PointerBuffer aiAnimations = scene.mAnimations();

      if (aiAnimations == null) return animation3DS;

      for (int i = 0; i < count; i++) {
        long aiAnimationPointer = aiAnimations.get(i);
        AIAnimation aiAnimation = AIAnimation.create(aiAnimationPointer);
        Animation3D animation3D = process(aiAnimation, bones, root, globalInverseTransform);
        animation3DS.add(animation3D);
      }
    }
    return animation3DS;
  }

  static Animation3D process(
    AIAnimation aiAnimation,
    List<Bone> bones,
    Node root,
    Matrix4f globalTransform
  ) {
    int maxFrames = calculateMaxAnimationFrames(aiAnimation);

    List<BoneMatrixFrame> boneMatrixFrames = new ArrayList<>();

    for (int j = 0; j < maxFrames; j++) {
      Matrix4f[] boneTransforms = new Matrix4f[MAX_BONES];
      Arrays.fill(boneTransforms, new Matrix4f());
      BoneMatrixFrame boneMatrixFrame = new BoneMatrixFrame(boneTransforms);

      buildAnimationFrameMatrices(
        aiAnimation,
        bones,
        boneMatrixFrame,
        j,
        root,
        root.transform(),
        globalTransform
      );

      boneMatrixFrames.add(boneMatrixFrame);
    }
    String animationName = aiAnimation.mName().dataString();
    double duration = aiAnimation.mDuration();
    return new Animation3D(animationName, duration, boneMatrixFrames);
  }

  static Node process(AINode aiNode, Node parent) {
    String nodeName = aiNode.mName().dataString();
    Node node = new Node(nodeName, parent, MatrixUtils.convert(aiNode.mTransformation()));

    int count = aiNode.mNumChildren();
    PointerBuffer aiChildren = aiNode.mChildren();

    for (int i = 0; i < count; i++) {
      AINode aiChild = AINode.create(aiChildren.get(i));
      Node child = process(aiChild, node);
      node.addChild(child);
    }

    return node;
  }

  static int calculateMaxAnimationFrames(AIAnimation aiAnimation) {
    int maxFrames = 0;
    final int numNodeAnims = aiAnimation.mNumChannels();

    final PointerBuffer aiChannels = aiAnimation.mChannels();

    for (int i = 0; i < numNodeAnims; i++) {
      final AINodeAnim aiNodeAnim = AINodeAnim.create(aiChannels.get(i));
      final int numFrames = Math.max(
        Math.max(aiNodeAnim.mNumPositionKeys(), aiNodeAnim.mNumScalingKeys()),
        aiNodeAnim.mNumRotationKeys()
      );
      maxFrames = Math.max(maxFrames, numFrames);
    }

    return maxFrames;
  }

  static AINodeAnim findAIAnimNode(AIAnimation aiAnimation, String nodeName) {
    final int count = aiAnimation.mNumChannels();
    final PointerBuffer aiChannels = aiAnimation.mChannels();

    if (aiChannels != null) {
      for (int i = 0; i < count; i++) {
        final AINodeAnim aiNodeAnim = AINodeAnim.create(aiChannels.get(i));
        if (nodeName.equals(aiNodeAnim.mNodeName().dataString())) return aiNodeAnim;
      }
    }

    return null;
  }

  static Matrix4f buildNodeTransformationMatrix(AINodeAnim aiNodeAnim, int frame) {
    final AIVectorKey.Buffer positionKeys = aiNodeAnim.mPositionKeys();
    final AIVectorKey.Buffer scalingKeys = aiNodeAnim.mScalingKeys();
    final AIQuatKey.Buffer rotationKeys = aiNodeAnim.mRotationKeys();

    AIVectorKey aiVecKey;
    AIVector3D vec;

    final Matrix4f nodeTransform = new Matrix4f();
    final int numPositions = aiNodeAnim.mNumPositionKeys();
    if (numPositions > 0) {
      aiVecKey = positionKeys.get(Math.min(numPositions - 1, frame));
      vec = aiVecKey.mValue();
      nodeTransform.translate(vec.x(), vec.y(), vec.z());
    }

    final int numRotations = aiNodeAnim.mNumRotationKeys();
    if (numRotations > 0) {
      final AIQuatKey quatKey = rotationKeys.get(Math.min(numRotations - 1, frame));
      final AIQuaternion aiQuat = quatKey.mValue();
      final Quaternionf quat = new Quaternionf(aiQuat.x(), aiQuat.y(), aiQuat.z(), aiQuat.w());
      nodeTransform.rotate(quat);
    }

    final int numScalingKeys = aiNodeAnim.mNumScalingKeys();
    if (numScalingKeys > 0) {
      aiVecKey = scalingKeys.get(Math.min(numScalingKeys - 1, frame));
      vec = aiVecKey.mValue();
      nodeTransform.scale(vec.x(), vec.y(), vec.z());
    }

    return nodeTransform;
  }

  static void buildAnimationFrameMatrices(
    AIAnimation aiAnimation,
    List<Bone> bones,
    BoneMatrixFrame animatedBoneMatrixFrame,
    int frame,
    Node node,
    Matrix4f parentTransformation,
    Matrix4f globalInverseTransform
  ) {
    final String nodeName = node.name();
    final AINodeAnim aiNodeAnim = findAIAnimNode(aiAnimation, nodeName);

    final Matrix4f nodeTransform = aiNodeAnim != null
                                   ? buildNodeTransformationMatrix(aiNodeAnim, frame)
                                   : node.transform();

    final Matrix4f nodeGlobalTransform = new Matrix4f(parentTransformation).mul(nodeTransform);
    final List<Bone> affectedBones = bones
      .stream()
      .filter(b -> b.boneName().equals(nodeName))
      .toList();

    for (final Bone bone : affectedBones)
      animatedBoneMatrixFrame.boneMatrices()[bone.boneId()] = new Matrix4f(globalInverseTransform)
        .mul(nodeGlobalTransform)
        .mul(bone.offsetMatrix());

    for (final Node child : node.children())
      buildAnimationFrameMatrices(
        aiAnimation,
        bones,
        animatedBoneMatrixFrame,
        frame,
        child,
        nodeGlobalTransform,
        globalInverseTransform
      );

  }
}
