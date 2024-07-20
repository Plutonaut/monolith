package com.game.engine.logic;

import com.game.caches.GlobalCache;
import com.game.engine.render.mesh.Mesh;
import com.game.engine.render.mesh.definitions.Quad;
import com.game.engine.render.mesh.vertices.AttribInfo;
import com.game.engine.settings.EngineSettings;
import com.game.graphics.shaders.Program;
import com.game.utils.enums.ERenderer;
import lombok.extern.slf4j.Slf4j;
import org.lwjgl.opengl.GL46;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

@Slf4j
public class TestRenderLogic extends AbstractLogic {
  AttribInfo positionAttrib = new AttribInfo("position", 3, 1);
  AttribInfo colorAttrib = new AttribInfo("color", 3, 1);
  Program program;
  Mesh mesh;
  int numVertices;
  int positionVbo;
  int colorVbo;
  int indexVbo;

  public TestRenderLogic(EngineSettings settings) {
    super(settings);
    log.info("Test Render Logic");
  }

  private void setupVAO() {
    Quad quad = new Quad();
    GlobalCache.instance().cacheItem(quad.createMeshInfo());
//    MeshInfo info = GlobalCache.instance().meshInfo(quad.name(), n -> quad.create());
//    mesh = info.create();
    mesh = new Mesh(quad.name());
    mesh.bind();
  }

  // Enabling and disabling vertex attributes does not break anything, but it is also not necessary for this test.
  private void drawVao() {
    mesh.bind();
    mesh.drawComplex(GL46.GL_TRIANGLES, numVertices);
    mesh.unbind();
  }

  private int setupVBO(AttribInfo info, float[] data) {
    int vbo = GL46.glGenBuffers();
    FloatBuffer vertexData = MemoryUtil.memAllocFloat(data.length).put(data).flip();
    GL46.glBindBuffer(GL46.GL_ARRAY_BUFFER, vbo);
    GL46.glBufferData(GL46.GL_ARRAY_BUFFER, vertexData, GL46.GL_STATIC_DRAW);
    MemoryUtil.memFree(vertexData);
//    program.attributes().enable(info.key());
//    program.attributes().point(List.of(info), GL46.GL_FLOAT); // Replaced with VertexAttributeArray
    return vbo;
  }

  private void setupIndexVBO() {
    indexVbo = GL46.glGenBuffers();
    int size = Quad.INDICES.length;
    IntBuffer indices = MemoryUtil.memAllocInt(size).put(Quad.INDICES).flip();
    GL46.glBindBuffer(GL46.GL_ELEMENT_ARRAY_BUFFER, indexVbo);
    GL46.glBufferData(GL46.GL_ELEMENT_ARRAY_BUFFER, indices, GL46.GL_STATIC_DRAW);
    numVertices = size;
    MemoryUtil.memFree(indices);
  }

  private void setupShader() {
    program = new Program(ERenderer.BASIC);
  }

  private void setupGl() {
    GL46.glEnable(GL46.GL_BLEND);
    GL46.glBlendFunc(GL46.GL_SRC_ALPHA, GL46.GL_ONE_MINUS_SRC_ALPHA);
  }

  @Override
  public String windowTitle() {
    return "TEST Render Logic";
  }

  @Override
  public void onStart() {
    setupGl();
    setupShader();
    setupVAO();
    positionVbo = setupVBO(positionAttrib, Quad.POSITIONS);
    colorVbo = setupVBO(colorAttrib, Quad.COLORS);
    setupIndexVBO();
    mesh.unbind();
  }

  @Override
  public void input() {

  }

  @Override
  public void update() {

  }

  @Override
  public void render(float fps) {
    GL46.glClear(GL46.GL_COLOR_BUFFER_BIT | GL46.GL_DEPTH_BUFFER_BIT);
    GL46.glViewport(0, 0, 800, 600);
    program.bind();
    drawVao();
    program.unbind();
    scene.window().update();
    scene.window().poll();
  }

  @Override
  public void onEnd() {
    mesh.dispose();
    GL46.glDeleteBuffers(positionVbo);
    GL46.glDeleteBuffers(colorVbo);
    GL46.glDeleteBuffers(indexVbo);
    program.dispose();
    scene.dispose();
    renderer.dispose();
  }
}
