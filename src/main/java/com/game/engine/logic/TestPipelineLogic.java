package com.game.engine.logic;

import com.game.engine.EngineSettings;
import com.game.engine.render.mesh.definitions.Quad;
import com.game.utils.enums.ERenderer;

public class TestPipelineLogic extends AbstractLogic {
  public TestPipelineLogic(EngineSettings settings) {
    super(settings);
  }

  @Override
  public String windowTitle() {
    return "TEST Render Pipeline";
  }

  @Override
  public void onStart() {
    renderer.cull(true);
    renderer.blend(true);
    renderer.depth(true);

//    Quad quad = new Quad();
//    MeshInfo info = quad.createMeshInfo();
//    ModelBuilder modelBuilder = new ModelBuilder();
//    Model model = modelBuilder.use("quad_model").addMeshData(info).build();
//    Model model = new Quad().createModel("quad_model");
//    RenderPacket packet = renderer.bind(ERenderer.BASIC, model);
//    scene.addPacket(packet);

    scene.bind(ERenderer.BASIC, "quad_model", new Quad());
    renderer.bind(scene);
  }

  @Override
  public void input() {

  }

  @Override
  public void update() {

  }
}
