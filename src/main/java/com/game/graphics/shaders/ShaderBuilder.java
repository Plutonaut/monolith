package com.game.graphics.shaders;

import com.game.utils.application.PathSanitizer;
import com.game.utils.engine.ShaderUtils;
import com.game.utils.enums.EDynamicShader;
import com.game.utils.logging.LogPrintStreamFactory;
import lombok.experimental.Accessors;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

@Accessors(fluent = true)
public class ShaderBuilder {
  protected final HashMap<EDynamicShader, StringBuilder> contents;

  public ShaderBuilder() {
    contents = new HashMap<>();

    mainFn().append("\nvoid main() {\n");
  }

  public ShaderBuilder addAttribute(String type, String name, int location) {
    layouts()
      .append("\nlayout (location = ")
      .append(location)
      .append(") in ")
      .append(type)
      .append(
        " ")
      .append(name)
      .append(";\n");
    return this;
  }

  public ShaderBuilder addUniform(String type, String name) {
    uniforms().append("\nuniform ").append(type).append(" ").append(name).append(
      ";\n");
    return this;
  }

  public ShaderBuilder addLocalSizing(int x, int y) {
    layouts().append("\nlayout (local_size_x = ").append(x).append(
      ", local_size_y = ").append(y).append(") in;\n");
    return this;
  }

  public ShaderBuilder addBufferBinding(
    String bindingType,
    int binding,
    String name,
    HashMap<String, String> data
  ) {
    layouts()
      .append("\nlayout (")
      .append(bindingType)
      .append(", binding = ")
      .append(binding)
      .append(") buffer ")
      .append(name).append(" {");
    data.forEach((k, v) -> layouts().append("\n\t")
                                    .append(k)
                                    .append(" ")
                                    .append(v)
                                    .append(";"));
    layouts().append("\n};\n");
    return this;
  }

  public ShaderBuilder addUniformBinding(
    String bindingType,
    int binding,
    String dataType,
    String name
  ) {
    layouts()
      .append("\nlayout (")
      .append(bindingType)
      .append(", binding = ")
      .append(binding)
      .append(") uniform ")
      .append(dataType)
      .append(" ")
      .append(name)
      .append(";\n");
    return this;
  }

  public ShaderBuilder addStruct(String name, Map<String, String> data) {
    structs().append("\nStruct ").append(name).append("{");
    data.forEach((k, v) -> structs()
      .append("\n\t")
      .append(k)
      .append(" ")
      .append(v)
      .append(";"));
    builder(EDynamicShader.STRUCT).append("\n};\n");
    return this;
  }

  public ShaderBuilder addFn(String name, String contents) {
    return addFn("void", name, contents);
  }

  public ShaderBuilder addFn(String value, String name, String contents) {
    functions()
      .append("\n")
      .append(value)
      .append(" ")
      .append(name)
      .append("() {\n")
      .append(contents)
      .append("\n}\n");
    return this;
  }

  public ShaderBuilder addToMainFn(String line) {
    mainFn().append(line).append("\n");
    return this;
  }

  StringBuilder structs() {
    return builder(EDynamicShader.STRUCT);
  }

  StringBuilder layouts() {
    return builder(EDynamicShader.LAYOUT);
  }

  StringBuilder functions() {
    return builder(EDynamicShader.FUNC);
  }

  StringBuilder uniforms() {
    return builder(EDynamicShader.UNIFORM);
  }

  StringBuilder mainFn() {
    return builder(EDynamicShader.MAIN);
  }

  StringBuilder builder(EDynamicShader type) {
    return contents.computeIfAbsent(type, (t) -> new StringBuilder());
  }

  public Shader build(String name) {
    String[] s = name.split("\\.");
    String fileName = s[0];
    String fileType = s[1];
    int shaderType = ShaderUtils.shaderTypeFromFileType(fileType);
    String parentDir = ShaderUtils.parentDirFromShaderType(shaderType);
    String fullDir = PathSanitizer.sanitizeFilePath("shaders/" + parentDir);
    try (PrintStream stream = LogPrintStreamFactory.create(fileName, fullDir, fileType)) {
      stream.print(structs());
      stream.print(layouts());
      stream.print(uniforms());
      stream.print(functions());
      stream.print(mainFn().append("\n}"));
    }

    return new Shader(name);
  }
}
