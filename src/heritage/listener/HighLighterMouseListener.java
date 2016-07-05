package heritage.listener;

import heritage.controls.HPopupMenu;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class HighLighterMouseListener extends MouseAdapter
{
      private HPopupMenu menu;
      private Color originalColor;
      private Color highlightColor;
      
      public HighLighterMouseListener(HPopupMenu menu, Color originalColor, Color highlightColor)
      {
            this.menu = menu;
            this.highlightColor = highlightColor;
            this.originalColor = originalColor;
      }

      public void mouseEntered(MouseEvent arg0)
      {
            menu.setBackground(highlightColor);
      }      
      public void mouseExited(MouseEvent arg0)
      {
            menu.setBackground(originalColor);
      }            
}

