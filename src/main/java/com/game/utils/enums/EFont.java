package com.game.utils.enums;

import lombok.Getter;
import lombok.experimental.Accessors;

// frog move to settings file
@Accessors(fluent = true)
@Getter
public enum EFont {
  FESTER("fester/Fester-ExtraLight.otf"),
  FIRA_BLD("fira/FiraCode-Bold.ttf"),
  FIRA_LHT("fira/FiraCode-Light.ttf"),
  FIRA_MED("fira/FiraCode-Medium.ttf"),
  FIRA_REG("fira/FiraCode-Regular.ttf"),
  FIRA_RET("fira/FiraCode-Retina.ttf"),
  FIRA_SEM_BLD("fira/FiraCode-SemiBold.ttf"),
  KNY_BLO("kenney/Kenney Blocks.ttf"),
  KNY_BLD("kenney/Kenney Bold.ttf"),
  KNY_FTR("kenney/Kenney Future.ttf"),
  KNY_FTR_NRW("kenney/Kenney Future Narrow.ttf"),
  KNY_FTR_SQR("kenney/Kenney Future Square.ttf"),
  KNY_HGH("kenney/Kenney Hight.ttf"),
  KNY_HGH_SQR("kenney/Kenney High Square.ttf"),
  KNY_MIN("kenney/Kenney Mini.ttf"),
  KNY_MIN_SQR("kenney/Kenney Mini Square.ttf"),
  KNY_PXL("kenney/Kenney Pixel.ttf"),
  KNY_RKT("kenney/Kenney Rocket.ttf"),
  KNY_RKT_SQR("kenney/Kenney Rocket Square.ttf"),
  KNY_SPC("kenney/Kenney Space.ttf"),
  MPLUS_BLA("mplus/MPLUSRounded1c-Black.ttf"),
  MPLUS_BLD("mplus/MPLUSRounded1c-Bold.ttf"),
  MPLUS_EXT_BLD("mplus/MPLUSRounded1c-ExtraBold.ttf"),
  MPLUS_LHT("mplus/MPLUSRounded1c-Light.ttf"),
  MPLUS_MED("mplus/MPLUSRounded1c-Medium.ttf"),
  MPLUS_REG("mplus/MPLUSRounded1c-Regular.ttf"),
  MPLUS_THN("mplus/MPLUSRounded1c-Thin.ttf"),
  PXL_GMR("pixel/Gamer.ttf"),
  PXL_LND("pixel/Pixeland.ttf"),
  PXL_LTD("pixel/Pixelated.ttf"),
  PXL_TYP("pixel/Pixeltype.ttf");

  private final String value;

  EFont(String value) {
    this.value = value;
  }
}
