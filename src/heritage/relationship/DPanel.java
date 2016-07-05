package heritage.relationship;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

public class DPanel extends JPanel {

	private static final long serialVersionUID = 2719823709014168604L;
	private final List<Line> lines = new ArrayList<Line>();

    public void addLine( double x1, double y1, double x2, double y2 ) 
    {
        lines.add(new Line( (int)x1, (int)y1, (int)x2, (int)y2));
    }

    public void paintComponent(Graphics g) 
    {
        for(final Line r : lines) 
        {
            r.paint(g);
        }
    }
    @Override
    public void paint( Graphics g ) 
    {
        super.paint( g );
    }
}

