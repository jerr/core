package org.jboss.forge.addon.text.highlight.encoder;

import java.awt.Color;
import java.io.OutputStream;
import java.util.Map;

import org.jboss.forge.addon.text.highlight.Encoder;
import org.jboss.forge.addon.text.highlight.Theme;
import org.jboss.forge.addon.text.highlight.TokenType;

public class TerminalEncoder extends Encoder.AbstractEncoder implements Encoder
{

   public TerminalEncoder(OutputStream out, Theme theme, Map<String, Object> options)
   {
      super(out, theme, options);
      write(TerminalString.RESET); // reset terminal colors
   }

   @Override
   public void textToken(String text, TokenType type)
   {
      Color color = color(type);
      if (color != null)
      {
         write(TerminalString.of(color, text));
      }
      else
      {
         write(text);
      }
   }

   @Override
   public void beginGroup(TokenType type)
   {
   }

   @Override
   public void endGroup(TokenType type)
   {
   }

   @Override
   public void beginLine(TokenType type)
   {
   }

   @Override
   public void endLine(TokenType type)
   {
   }

   public static class TerminalString
   {

      public static final String START_COLOR = "\u001B[38;5;";
      public static final String END = "m";
      public static final String RESET = "\u001B[0" + END;

      public static String of(Color color, String text)
      {
         StringBuilder sb = new StringBuilder();
         sb.append(START_COLOR)
                  .append(from(color))
                  .append(END);
         sb.append(text);
         sb.append(RESET);
         return sb.toString();
      }

      public static String from(Color color)
      {
         return String.valueOf(
                  rgbToAnsi(
                           color.getRed(),
                           color.getGreen(),
                           color.getBlue()));
      }

      private static int rgbToAnsi(int red, int green, int blue)
      {
         return 16 + (getAnsiScale(red) * 36) + (getAnsiScale(green) * 6) + getAnsiScale(blue);
      }

      public static int getAnsiScale(int color)
      {
         int space = 256 / 5;
         if (color == 0)
         {
            return 0;
         }
         if (color < space * 1)
         {
            return 1;
         }
         if (color > space * 1 && color < space * 2)
         {
            return 2;
         }
         if (color > space * 2 && color < space * 3)
         {
            return 3;
         }
         if (color > space * 3 && color < space * 4)
         {
            return 4;
         }
         if (color > space * 4)
         {
            return 5;
         }
         return 0;
      }

      public static void main(String[] args) throws Exception
      {
         System.out.println(from(Color.white));
      }
   }
}