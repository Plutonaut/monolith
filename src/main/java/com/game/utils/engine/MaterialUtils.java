package com.game.utils.engine;

import com.game.graphics.materials.Material;
import com.game.utils.enums.EMaterialColor;
import com.game.utils.enums.EMaterialTexture;
import org.lwjgl.PointerBuffer;
import org.lwjgl.assimp.*;
import org.lwjgl.system.MemoryStack;

import java.io.File;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

public class MaterialUtils {
  public static final String DFLT = "default";

  public static List<Material> process(String path, AIScene scene) {
    List<Material> materials = new ArrayList<>();
    PointerBuffer aiMaterials = scene.mMaterials();

    if (aiMaterials != null) {
      File file = new File(path);
      String directory = file.getParent();
      int materialCount = scene.mNumMaterials();
      for (int i = 0; i < materialCount; i++) {
        AIMaterial aiMaterial = AIMaterial.create(aiMaterials.get(i));
        Material material = process(aiMaterial, i, directory);
        materials.add(material);
      }
    }

    return materials;
  }

  public static Material process(AIMaterial aiMaterial, int index, String directory) {
    Material material = new Material(directory + "_" + index);

    try (MemoryStack stack = MemoryStack.stackPush()) {
      AIColor4D color = AIColor4D.create();
      EMaterialColor.types().forEach(type -> {
        int result = color(aiMaterial, type.getValue(), color);
        if (result == Assimp.aiReturn_SUCCESS) material.color(type.getValue(), color);
      });
      float reflectance = reflectance(aiMaterial);
      material.reflectance(reflectance);

      EMaterialTexture.types().forEach(type -> {
        AIString aiPath = AIString.calloc(stack);
        int result = texture(aiMaterial, type.getValue(), aiPath);
        if (result == Assimp.aiReturn_SUCCESS) {
          String texturePath = directory + File.separatorChar + aiPath.dataString();
          material.texture(type.getValue(), texturePath);
        }
      });
    }

    return material;
  }

  static int color(AIMaterial aiMaterial, String type, AIColor4D color) {
    return Assimp.aiGetMaterialColor(aiMaterial, type, Assimp.aiTextureType_NONE, 0, color);
  }

  static int texture(AIMaterial aiMaterial, Integer type, AIString aiPath) {
    return Assimp.aiGetMaterialTexture(aiMaterial, type, 0, aiPath, (IntBuffer) null, null, null, null, null, null);
  }

  static float reflectance(AIMaterial aiMaterial) {
    float reflectance = 0.f;
    float[] shininessFactor = new float[]{0.f};
    int[] pMax = new int[]{1};

    int result = Assimp.aiGetMaterialFloatArray(aiMaterial,
                                                Assimp.AI_MATKEY_SHININESS_STRENGTH,
                                                EMaterialTexture.NON.getValue(),
                                                0,
                                                shininessFactor,
                                                pMax);

    if (result != Assimp.aiReturn_SUCCESS) reflectance = shininessFactor[0];

    return reflectance;
  }
}
