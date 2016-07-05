package heritage.controls;

import heritage.config.ApplicationColors;
import heritage.config.Config;
import heritage.controls.buttons.HContolButton;
import heritage.controls.filechooser.HFileChooser;
import heritage.ui.HeritageUI;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;

public class HTitleBar extends JLayeredPane
{
	private static final long serialVersionUID 	= 72294827535122744L;
	private final int TITLE_BAR_HEIGHT 			= Integer.parseInt( Config.getItem( "title_bar_height" ) );
	
	private final String APPLICATION_FONT_NAME	= Config.getItem( "app_font_name" );	
	private final String ICON_NAME				= Config.getItem( "icon_name" );
	private final int FONT_SIZE	  				= Integer.parseInt( Config.getItem( "title_font_size" ) );
	
	private final String CLOSE_ICON				= "icons/close_16.png";
	private final String MINIMIZE_ICON			= "icons/minimize_16.png";
	
	public static JLayeredPane titleBar;
	
	private static Logger log = Logger.getLogger( HTitleBar.class.getName() );
	
	private static Point point = new Point();
	
	private Component parentWindow;
	
	
	/**
	 * Создаем замену стандартному title bar с кастомным дизайном 	
	 * @param frameWidth - ширина окна приложения
	 * @param frame - окно приложения
	 * @return JLayeredPane - кастомный title bar
	 */
	public HTitleBar( String titleLabel, int frameWidth, Component frame, boolean showClose, boolean showMinimize )
	{		
		this.parentWindow = frame;
		
		createPanel( this, frameWidth );
		makePanelDraggable( this, frame );
		createTitle( this, titleLabel );
		
		if( showClose )
		{
			createCloseButton( this, frameWidth );
		}
		
		if( showMinimize )
		{
			createMinimizeButton( this, frameWidth );
		}
		
		log.info( "Title Bar created..." );
	}
		
	/**
	 * Создаем панель заголовка приложения
	 */
	private void createPanel( HTitleBar titleBar, int frameWidth )
	{
		int x = 0;
		int y = 0;
		int w = frameWidth;
		int h = TITLE_BAR_HEIGHT;
		
		titleBar.setBounds( x, y, w, h );
		titleBar.setSize( w, h );
		titleBar.setPreferredSize( new Dimension( w, h ) );
		titleBar.setBackground( ApplicationColors.TITLE_BACKGROUND_COLOR );
		titleBar.setLayout( null );
		titleBar.setOpaque( true );
		titleBar.setName( "titleBar" );
	}
	
	/**
	 * Делаем панель заголовка приложения подвижной
	 */
	private void makePanelDraggable( HTitleBar titleBar, final Component frame )
	{
		titleBar.addMouseListener( new MouseAdapter() 
		{
			public void mousePressed( MouseEvent e ) 
		    {
		        point.x = e.getX();
		        point.y = e.getY();
		    }
		});
		titleBar.addMouseMotionListener( new MouseMotionAdapter() 
		{
		    public void mouseDragged( MouseEvent e ) 
		    {
		        Point p = frame.getLocation();
		        frame.setLocation( p.x + e.getX() - point.x, p.y + e.getY() - point.y );
		        //System.out.println(frame.getParent());
		    }
		});
	}
	
	/**
	 * Создаем заголовок приложения
	 */
	private void createTitle( HTitleBar titleBar, String titleLabel )
	{		
		ImageIcon titleBarIcon = new ImageIcon( "icons/" + ICON_NAME );		
		JLabel icon = new JLabel( titleBarIcon );	
		icon.setBounds( 10, 														// x
						( TITLE_BAR_HEIGHT - titleBarIcon.getIconHeight() ) / 2, 	// y
						titleBarIcon.getIconWidth(), 								// w
						titleBarIcon.getIconHeight() );								// h
		titleBar.add( icon );
		
		JLabel title = new JLabel( titleLabel );
		title.setFont( new Font( APPLICATION_FONT_NAME, Font.PLAIN, FONT_SIZE ) );
		title.setBounds( 10 + titleBarIcon.getIconWidth() + 10, 					// x
						 ( TITLE_BAR_HEIGHT - FONT_SIZE ) / 2, 						// y
						 200, 														// w
						 FONT_SIZE );												// h
		title.setForeground( ApplicationColors.TITLE_FOREGROUND_COLOR );	
		titleBar.add( title );
		
	}
	
	/**
	 * Создаем кнопку закрытия приложения	
	 */
	private void createCloseButton( HTitleBar titleBar, int frameWidth )
	{
		HContolButton closeButton = new HContolButton( this.exitAction );
		closeButton.setBounds( frameWidth - 16 - 12, 12, 16, 16 );
		
		try 
		{
			Image img = ImageIO.read( new FileInputStream( CLOSE_ICON ) );
			closeButton.setIcon( new ImageIcon(img) );
		} 
		catch( IOException ex ) 
		{
			log.log( Level.SEVERE, "Failed to find '" + CLOSE_ICON + "': ", ex );
		}
		
		titleBar.add( closeButton );
	}
	
	/**
	 * Создаем кнопку сворачивания приложения	
	 */
	private void createMinimizeButton( HTitleBar titleBar, int frameWidth )
	{
		HContolButton miniButton = new HContolButton( this.minimizeAction );
		//System.out.println(miniButton.getWidth());
        miniButton.setBounds( frameWidth - 16 - 12 - 16 - 12, 12, 16, 16 );
        
        try 
		{
			Image img = ImageIO.read( new FileInputStream( MINIMIZE_ICON ) );
			miniButton.setIcon( new ImageIcon(img) );
		} 
		catch( IOException ex ) 
		{
			log.log( Level.SEVERE, "Failed to find '" + MINIMIZE_ICON + "': ", ex );
		}
        
        
        titleBar.add( miniButton );
	}
	
	/**
	 * Создаем действие для кнопки закрытия приложения	
	 */
	private final Action exitAction = new AbstractAction( )
    {
		private static final long serialVersionUID = 3301887784942202025L;

		@Override
        public void actionPerformed( ActionEvent e )
        {
			if( parentWindow instanceof JFrame )//parentWindow.toString().contains("JFrame")  )
			{
				System.exit(0);
			}
			else if( parentWindow instanceof JDialog ) //parentWindow.toString().contains("JDialog")  )
			{
				JDialog dialog = (JDialog) parentWindow;
				dialog.dispose();
			}
			else if( parentWindow instanceof HFileChooser ) //parentWindow.toString().contains("HFileChooser")  )
			{
				HFileChooser dialog = (HFileChooser) parentWindow;
				dialog.cancelSelection();
			}
			else
			{
				HDialog dialog = (HDialog) parentWindow;
				dialog.dispose();
			}
        }
    };
    /**
	 * Создаем действие для кнопки сворачивания приложения	
	 */
    private final Action minimizeAction = new AbstractAction( )
    {
		private static final long serialVersionUID = 7765837970089196732L;

		@Override
        public void actionPerformed(ActionEvent e)
        {
        	HeritageUI.getFrame().setState( JFrame.ICONIFIED );
        }
    };
}
