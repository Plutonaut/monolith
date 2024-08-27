package com.game.engine.scene.generators;

import com.game.engine.render.models.Model;
import com.game.utils.application.values.ValueMap;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.HashMap;

@Accessors(fluent = true)
@Getter
public class ModelGenerator {
  protected final HashMap<String, AbstractModelGenerator> generators;
  protected final GeneratorParameterBuilder builder;

  public ModelGenerator() {
    generators = new HashMap<>();
    builder = new GeneratorParameterBuilder();
  }

  protected AbstractModelGenerator getGenerator(String type) {
    if (type == null) type = "";
    return generators.computeIfAbsent(type, t -> switch (t) {
      case "sprite" -> new SpriteResourceModelGenerator();
      case "text" -> new TextModelGenerator();
      case "terrain" -> new ProceduralTerrainModelGenerator();
      case "object" -> new ObjectResourceModelGenerator();
      case "billboard" -> new BillboardModelGenerator();
      default -> null;
    });
  }

  protected Model generateModel(String type, ValueMap map) {
    return getGenerator(type).generateModel(map);
  }

  public Model generate(ValueMap map) {
    return generateModel(map.get("type"), map);
  }
}
