package com.game.engine;

import com.game.loaders.INIFileModel;
import com.game.utils.enums.EFont;
import com.game.utils.enums.ELogic;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Map;

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
  private int maxLogFiles;
  private EFont fontType;
  private ELogic logic;

  private float movementSpeed;
  private float fov;
  private float zNear;
  private float zFar;

  private float mouseSensitivity;

  public EngineSettings() {
    load();
  }

  public EngineSettings load() {
    INIFileModel model = INIFileModel.load(PATH);

    Map<String, String> window = model.section("window");
    width = Integer.parseInt(window.get("width"));
    height = Integer.parseInt(window.get("height"));
    targetFPS = Integer.parseInt(window.get("targetFPS"));
    targetUPS = Integer.parseInt(window.get("targetUPS"));
    antiAliasSamples = Integer.parseInt(window.get("antiAliasSamples"));
    maximized = Boolean.parseBoolean(window.get("maximized"));
    vsync = Boolean.parseBoolean(window.get("vsync"));
    stepSize = Float.parseFloat(window.get("stepSize"));

    Map<String, String> engine = model.section("engine");
    threaded = Boolean.parseBoolean(engine.get("threaded"));
    debug = Boolean.parseBoolean(engine.get("debug"));
    maxLogFiles = Integer.parseInt(engine.get("maxLogFiles"));
    fontType = Enum.valueOf(EFont.class, engine.get("fontType"));
    logic = Enum.valueOf(ELogic.class, engine.get("logic"));

    Map<String, String> camera = model.section("camera");
    movementSpeed = Float.parseFloat(camera.get("movementSpeed"));
    fov = Float.parseFloat(camera.get("fov"));
    zNear = Float.parseFloat(camera.get("zNear"));
    zFar = Float.parseFloat(camera.get("zFar"));

    Map<String, String> controls = model.section("controls");
    mouseSensitivity = Float.parseFloat(controls.get("mouseSensitivity"));

    return this;
  }
}
