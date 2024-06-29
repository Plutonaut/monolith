package com.game.engine.render.mesh.animations;

import lombok.Value;
import lombok.experimental.Accessors;
import org.joml.Matrix4f;

import java.util.ArrayList;
import java.util.List;

@Accessors(fluent = true)
@Value
public class Node {
  List<Node> children;
  Node parent;
  String name;
  Matrix4f transform;

  public Node(String name, Node parent, Matrix4f transform) {
    this.name = name;
    this.parent = parent;
    this.transform = transform;

    this.children = new ArrayList<>();
  }

  public void addChild(Node node) {children.add(node);}
}
