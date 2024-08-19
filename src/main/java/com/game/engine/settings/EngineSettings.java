package com.game.engine.settings;

import com.game.loaders.ini.INIFileModel;
import com.game.utils.application.values.ValueMap;
import com.game.utils.enums.EFont;
import lombok.Data;
import lombok.experimental.Accessors;
import org.joml.Vector3f;

@Accessors(fluent = true)
@Data
public class EngineSettings {
  static final String PATH = "properties/ini/settings.ini";
  private int width;
  private int height;
  private int targetFPS;
  private int targetUPS;
  private int antiAliasSamples;
  private boolean maximized;
  private boolean vsync;
  private float stepSize;
  private boolean threaded;
  private boolean debug;
  private boolean diagnostics;
  private int maxLogFiles;
  private EFont fontType;
  private String logic;
  private Vector3f cameraPosition;
  private float movementSpeed;
  private float fov;
  private float zNear;
  private float zFar;
  private float mouseSensitivity;
  private float hudBorder;
  private float hudTextScale;
  private Vector3f hudTextColor;

  public EngineSettings() {
    load();
  }

  public EngineSettings load() {
    INIFileModel model = INIFileModel.load(PATH);
    ValueMap window = model.modelSection("window");
    antiAliasSamples = window.getInt("antiAliasSamples");
    targetFPS = window.getInt("targetFPS");
    targetUPS = window.getInt("targetUPS");
    maximized = window.getBool("maximized");
    stepSize = window.getFloat("stepSize");
    height = window.getInt("height");
    width = window.getInt("width");
    vsync = window.getBool("vsync");
    ValueMap engine = model.modelSection("engine");
    maxLogFiles = engine.getInt("maxLogFiles");
    fontType = engine.getEFont("fontType");
    threaded = engine.getBool("threaded");
    debug = engine.getBool("debug");
    diagnostics = engine.getBool("diagnostics");
    logic = engine.getELogic("logic");
    ValueMap camera = model.modelSection("camera");
    cameraPosition = camera.getVector3f("cameraPosition");
    movementSpeed = camera.getFloat("movementSpeed");
    zNear = camera.getFloat("zNear");
    zFar = camera.getFloat("zFar");
    fov = camera.getFloat("fov");
    ValueMap controls = model.modelSection("controls");
    mouseSensitivity = controls.getFloat("mouseSensitivity");
    ValueMap hud = model.modelSection("hud");
    hudBorder = hud.getFloat("hudBorder");
    hudTextScale = hud.getFloat("hudTextScale");
    hudTextColor = hud.getVector3f("hudTextColor");
    return this;
  }
}
