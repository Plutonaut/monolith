package com.game.utils.engine;

import com.game.caches.GlobalCache;
import com.game.graphics.fonts.CharInfo;
import com.game.graphics.fonts.FontInfo;
import com.game.graphics.texture.Texture;
import com.game.utils.application.PathSanitizer;
import com.game.utils.engine.loaders.TextureLoader;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.util.HashMap;

public class FontInfoUtils {
  static final int CHAR_PADDING = 2;
  static final String CHARSET = "ISO-8859-1";

  public static FontInfo process(String fontName) {
    Font font = GlobalCache.instance().getFont(fontName, 20);
    return process(font);
  }

  public static FontInfo process(Font font) {
    return process(font, false, Color.white);
  }

  public static FontInfo process(Font font, boolean antiAlias, Color color) {
    String texturePath = "textures%sfonts%s%s.png".formatted(
      File.separator,
      File.separator,
      font.getFontName()
    );
    texturePath = PathSanitizer.sanitizeFilePath(texturePath);

    return process(font, texturePath, antiAlias, color);
  }

  public static FontInfo process(Font font, String texturePath, boolean antiAlias, Color color) {
    // Get the font metrics for each character for the selected font by using image.
    BufferedImage fontImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
    // Create a temporary image
    Graphics2D fontG2D = fontImage.createGraphics();
    fontG2D.setFont(font);

    // Obtain font metrics from temporary image.
    FontMetrics fontMetrics = fontG2D.getFontMetrics();

    // Create a string containing all valid characters available in font.
    final String allChars = getAllAvailableChars(CHARSET);
    int width = 0;
    final int height = fontMetrics.getHeight();

    final HashMap<Character, CharInfo> chars = new HashMap<>();

    for (final char c : allChars.toCharArray()) {
      // Get the size for each character and update global image size
      final CharInfo info = new CharInfo(width, fontMetrics.charWidth(c));
      chars.put(c, info);

      width += info.width() + CHAR_PADDING;
    }
    fontG2D.dispose();
    int textureWidth = width;

    Texture texture = GlobalCache.instance().texture(texturePath, (p) -> {
      // Create the image associated to the charset
      BufferedImage img = new BufferedImage(textureWidth, height, BufferedImage.TYPE_INT_ARGB);
      Graphics2D g2D = img.createGraphics();
      if (antiAlias)
        g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
      g2D.setFont(font);
      FontMetrics metrics = g2D.getFontMetrics();
      g2D.setColor(color);
      int startX = 0;
      for (final char c : allChars.toCharArray()) {
        final CharInfo charInfo = chars.get(c);
        g2D.drawString("" + c, startX, metrics.getAscent());
        startX += charInfo.width() + CHAR_PADDING;
      }
      g2D.dispose();
      return TextureLoader.load(p, img, true);
    });

    return new FontInfo(font, texture, chars, width, height);
  }

  static String getAllAvailableChars(String charsetName) {
    final CharsetEncoder ce = Charset.forName(charsetName).newEncoder();
    final StringBuilder result = new StringBuilder();
    for (char c = 0; c < Character.MAX_VALUE; c++)
      if (ce.canEncode(c))
        result.append(c);

    return result.toString();
  }
}
