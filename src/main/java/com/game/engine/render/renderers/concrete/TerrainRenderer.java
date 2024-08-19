package com.game.engine.render.renderers.concrete;

import com.game.engine.render.IRenderable;
import com.game.engine.render.mesh.InstancedMesh;
import com.game.engine.render.renderers.AbstractLitRenderer;
import com.game.engine.scene.Scene;
import com.game.engine.scene.entities.Entity;
import com.game.engine.scene.lighting.LightingManager;
import com.game.utils.engine.terrain.procedural.TerrainChunk;
import com.game.utils.enums.ERenderer;
import com.game.utils.enums.EUniform;
import org.joml.Matrix4f;

import java.util.HashMap;

public class TerrainRenderer extends AbstractLitRenderer {
  @Override
  public ERenderer type() { return ERenderer.TERRAIN; }

  @Override
  protected void render(IRenderable item, Scene scene) {
    Entity entity = (Entity) item;
    Matrix4f viewMatrix = scene.camera().view3D();

    program.uniforms().set(EUniform.PROJECTION.value(), scene.projectionMat(entity.projection()));
    program.uniforms().set(EUniform.VIEW.value(), viewMatrix);
    program.uniforms().set(EUniform.MODEL.value(), entity.transform().worldModelMat());
    LightingManager lighting = scene.lighting();
    setLightingUniforms(lighting, viewMatrix);
    HashMap<Integer, TerrainChunk> activeTerrainChunkMap = entity.controllers().terrain().active();
    entity.meshes().forEach(mesh -> {
      int meshId = mesh.glId();
      if (activeTerrainChunkMap.containsKey(meshId)) {
        setMaterialUniform(mesh.material());
        TerrainChunk chunk = activeTerrainChunkMap.get(meshId);
        program.uniforms().set(EUniform.TERRAIN_CHUNK_MODEL.value(), chunk.terrainModel());
        program.uniforms().set(EUniform.IS_INSTANCED.value(), mesh instanceof InstancedMesh);
        program.uniforms().set(EUniform.SELECTED.value(), mesh.selected());
        mesh.render();
      }
    });
  }
}
