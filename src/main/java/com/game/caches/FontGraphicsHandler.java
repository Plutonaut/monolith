package com.game.caches;

import com.game.utils.application.LoaderUtils;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Slf4j
public class FontGraphicsHandler {
  private final List<String> availableFontFamilyNames;
  private final GraphicsEnvironment env;

  public FontGraphicsHandler() {
    env = GraphicsEnvironment.getLocalGraphicsEnvironment();
    availableFontFamilyNames = Arrays.asList(env.getAvailableFontFamilyNames());
  }

  public boolean isAvailableFontFamilyName(String fontName) {
    return availableFontFamilyNames.contains(fontName);
  }

  public Font getFont(String fontName, int fontSize) {
    Font font;
    if (LoaderUtils.isResourcePath(fontName)) {
      File f = new File(fontName);
      try {
        font = Font.createFont(Font.TRUETYPE_FONT, f);
        if (!isAvailableFontFamilyName(fontName)) env.registerFont(font);
      } catch (FontFormatException | IOException ex) {
        log.error("Could not parse font type {}", fontName);
        throw new RuntimeException(ex);
      }
    } else font = new Font(fontName, Font.PLAIN, fontSize);
    return font;
  }
}
