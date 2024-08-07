package com.game.utils.enums;

import lombok.Getter;

@Getter
public enum EAttribute {
  POS("position"),
  NRM("normal"),
  TAN("tangent"),
  BTA("bitangent"),
  CLR("color"),
  TXC("texcoord"),
  WGT("boneweights"),
  BON("boneindices"),
  ICR("instancecolors"),
  IMX("instancematrices");

  private final String value;

  EAttribute(String value) {
    this.value = value;
  }

}
