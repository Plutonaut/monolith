package com.game.engine.render.pipeline;

import com.game.caches.GlobalCache;
import com.game.engine.render.mesh.InstancedMesh;
import com.game.engine.render.mesh.Mesh;
import com.game.engine.render.mesh.MeshInfo;
import com.game.engine.render.mesh.vertices.VertexBufferObject;
import com.game.engine.render.mesh.vertices.VertexInfo;
import com.game.engine.render.models.Model;
import com.game.engine.render.pipeline.packets.RenderPacket;
import com.game.engine.scene.entities.Entity;
import com.game.engine.scene.entities.animations.Animation;
import com.game.engine.scene.entities.controllers.AnimationController;
import com.game.graphics.materials.Material;
import com.game.utils.application.LambdaCounter;
import com.game.utils.enums.EModifier;
import com.game.utils.enums.ERenderer;
import org.lwjgl.opengl.GL46;

import java.util.ArrayList;
import java.util.List;

public class RenderPipeline {
  public RenderPacket pipe(ERenderer shader, Model... models) {
    RenderPacket packet = new RenderPacket(shader);
    for (Model model : models) packet.add(pipe(model));
    return packet;
  }

  Entity pipe(Model model) {
    // Pipe meshes
    List<Mesh> meshes = model.meshInfo().stream().map(this::pipe).toList();

    // Recollect meshes
    String entityName = GlobalCache.instance().resolveEntityName(model.name());
    Entity entity = new Entity(entityName);
    List<Animation> animations = model.animations();
    if (!animations.isEmpty()) {
      AnimationController controller = new AnimationController();
      for (Animation animation : animations)
        controller.add(animation);
      entity.addController(controller);
      entity.toggleModifier(EModifier.ANIMATED);
    }
    entity.addMeshes(meshes);
    return entity;
  }

  Mesh pipe(MeshInfo info) {
    Mesh mesh = info.isInstanced()
                ? new InstancedMesh(info.name(), info.instances())
                : new Mesh(info.name());
    mesh.vertexCount(info.vertexCount());
    mesh.isComplex(!info.indices().isEmpty());

    if (info.material() != null) {
      Material material = GlobalCache.instance().material(info.material());
      mesh.material(material);
    }

    mesh.bind();
    info.vertices().forEach(vertex -> {
      VertexRecord vertexRecord = pipe(vertex);
      mesh.vbos().add(vertexRecord.vbo());
      mesh.vaas().addAll(vertexRecord.vaas());
    });
    mesh.unbind();
    return mesh;
  }

  VertexRecord pipe(VertexInfo info) {
    VertexBufferObject vbo = new VertexBufferObject();

    if (info.glType() == GL46.GL_INT) vbo.buffer(info.vertices().asIntArray(), info.glUsage());
    else vbo.buffer(info
                      .vertices()
                      .asArray(), info.glUsage());
    List<String> vaas = new ArrayList<>();
    LambdaCounter lambdaOffset = new LambdaCounter();
    int stride = info.stride();
    int offset = lambdaOffset.value();
    info.attributes().values().forEach(attribInfo -> {
      String attribute = attribInfo.key();



      vaas.add(attribute);
    });
    return new VertexRecord(vbo, vaas);
  }

  record VertexRecord(VertexBufferObject vbo, List<String> vaas) {}
}
