package heritage.controls;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JScrollBar;
import javax.swing.plaf.metal.MetalScrollBarUI;

public class HScrollBar extends JScrollBar
{
	private static final long serialVersionUID = 4712232372117115270L;
	private final String ICONS_PATH = "icons/"; 
	private String thumbFileV = ICONS_PATH + "thumb_vertical.png";
    private String trackFileV = ICONS_PATH + "track_vertical.png";
    private String thumbFileH = ICONS_PATH + "thumb_horizontal.png";
    private String trackFileH = ICONS_PATH + "track_horizontal.png";
	
	private int scrollbarOrientation;
	
	private static Logger log = Logger.getLogger( HScrollBar.class.getName() );
	
	public HScrollBar( )
	{		
		super( JScrollBar.VERTICAL );
	    setPreferredSize( new Dimension( 12, 12 ) );
	    setUI( new HScrollBarUI() );
	    
	    scrollbarOrientation = JScrollBar.VERTICAL;
	}
	
	public HScrollBar( int orientation )
	{		
		super( orientation );
	    setPreferredSize( new Dimension( 12, 12 ) );
	    setUI( new HScrollBarUI() );
	    
	    scrollbarOrientation = orientation;
	}
	
	class HScrollBarUI extends MetalScrollBarUI 
	{
	    private Image imageThumbV, imageTrackV, imageThumbH, imageTrackH;
	    	    
	    HScrollBarUI( )
	    {
	        try 
	        {
	            imageThumbV = ImageIO.read( new File( thumbFileV ) );
	            imageTrackV = ImageIO.read( new File( trackFileV ) );
	            imageThumbH = ImageIO.read( new File( thumbFileH ) );
	            imageTrackH = ImageIO.read( new File( trackFileH ) );
	        } 
	        catch( IOException ex )
	        {
	        	log.log( Level.SEVERE, "Failed to load scrollbar: ", ex );
	        }
	    }

	    @Override
	    protected void paintThumb( Graphics g, JComponent c, Rectangle thumbBounds ) 
	    {        
	    	Image imageThumb = ( scrollbarOrientation == JScrollBar.VERTICAL ) ? imageThumbV : imageThumbH;
	    	
	    	g.translate( thumbBounds.x, thumbBounds.y );
	        g.drawRect( 0, 0, thumbBounds.width - 2, thumbBounds.height - 1 );
	        AffineTransform transform = AffineTransform.getScaleInstance( (double) thumbBounds.width / imageThumb.getWidth( null ), 
	        															  (double) thumbBounds.height / imageThumb.getHeight( null ) );
	        ((Graphics2D)g).drawImage( imageThumb, transform, null );
	        g.translate( -thumbBounds.x, -thumbBounds.y );
	    }

	    @Override
	    protected void paintTrack( Graphics g, JComponent c, Rectangle trackBounds ) 
	    {        
	    	Image imageTrack = ( scrollbarOrientation == JScrollBar.VERTICAL ) ? imageTrackV : imageTrackH;
	    	g.translate( trackBounds.x, trackBounds.y );
	        ((Graphics2D)g).drawImage( imageTrack, AffineTransform.getScaleInstance( (double)trackBounds.width / imageTrack.getWidth( null ), (double)trackBounds.height / imageTrack.getHeight( null ) ), null );
	        g.translate( -trackBounds.x, -trackBounds.y );
	    }
	    
	    @Override
	    protected JButton createDecreaseButton( int orientation ) 
	    {
	        return createZeroButton( );
	    }

	    @Override    
	    protected JButton createIncreaseButton( int orientation ) 
	    {
	        return createZeroButton( );
	    }

	    private JButton createZeroButton() 
	    {
	        JButton jbutton = new JButton( );
	        jbutton.setPreferredSize( new Dimension( 0, 0 ) );
	        jbutton.setMinimumSize( new Dimension( 0, 0 ) );
	        jbutton.setMaximumSize( new Dimension( 0, 0 ) );
	        return jbutton;
	    }

	}
}
