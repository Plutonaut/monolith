package com.game.engine.render.renderers.concrete;

import com.game.caches.GlobalCache;
import com.game.engine.render.IRenderable;
import com.game.engine.render.mesh.InstancedMesh;
import com.game.engine.render.mesh.Mesh;
import com.game.engine.render.renderers.AbstractLitRenderer;
import com.game.engine.scene.Scene;
import com.game.engine.scene.entities.Entity;
import com.game.engine.scene.lighting.LightingManager;
import com.game.engine.scene.terrain.TerrainChunk;
import com.game.utils.enums.ERenderer;
import com.game.utils.enums.EUniform;
import org.joml.Matrix4f;

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
    TerrainChunk[] sortedTerrainChunks = scene.terrain().getSortedChunkArray();

    for (TerrainChunk chunk : sortedTerrainChunks) {
      Mesh mesh = GlobalCache.instance().mesh(chunk.coordinates().toString());
      if (mesh.material() != null) setMaterialUniform(mesh.material());
      program.uniforms().set(EUniform.IS_INSTANCED.value(), mesh instanceof InstancedMesh);
      program.uniforms().set(EUniform.SELECTED.value(), mesh.selected());
      program.uniforms().set(EUniform.USE_TERRAIN_MODEL.value(), true);
      program.uniforms().set(EUniform.TERRAIN_CHUNK_MODEL.value(), chunk.getWorldModel());
      mesh.render();
    }
  }
}
