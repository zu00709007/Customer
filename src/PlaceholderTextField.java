import java.awt.*;
import java.awt.geom.Rectangle2D;

import javax.swing.*;

@SuppressWarnings("serial")
public class PlaceholderTextField extends JTextField 
{
    private String placeholder;   

    protected void paintComponent(final Graphics pG) 
    {
        super.paintComponent(pG);

        if (placeholder.length() == 0 || getText().length() > 0) 
        {
            return;
        }

        final Graphics2D g = (Graphics2D) pG;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);        
        g.setColor(Color.BLACK);
        //g.drawString(placeholder, getInsets().left, pG.getFontMetrics().getMaxAscent() + getInsets().top +5);
        
        
        FontMetrics fm = g.getFontMetrics();
        Rectangle2D r = fm.getStringBounds(placeholder, g);
        int xx = (this.getWidth() - (int) r.getWidth()) / 2;
        int yy = (this.getHeight() - (int) r.getHeight()) / 2 + fm.getAscent();		
		g.drawString(placeholder, xx, yy);
    }

    public void setPlaceholder(final String s) 
    {
        placeholder = s;
    }
}