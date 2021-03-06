package heritage.ui;
import heritage.config.ApplicationColors;
import heritage.config.Config;
import heritage.controls.HTitleBar;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;

public class HeritageUI 
{
	private final String APPLICATION_NAME 	= Config.getItem( "app_title" );
	private final int HORIZONTAL_OFFSET	  	= Integer.parseInt( Config.getItem( "horizontal_offset" ) );
	private final int VERTICAL_OFFSET 		= Integer.parseInt( Config.getItem( "vertical_offset" ) );
	private final int APP_WIDTH				= getScreenWidth()  - Integer.parseInt( Config.getItem( "margin" ) );
	private final int APP_HEIGHT			= getScreenHeight() - Integer.parseInt( Config.getItem( "margin" ) );
	private final int BORDER_THICKNESS		= Integer.parseInt( Config.getItem( "border_thickness" ) );
	private final String ICON_NAME			= Config.getItem( "icon_name" );
	
	private static JFrame frame;
	private JLayeredPane layeredPane;
	private JPanel layoutPanel;
	private JPanel linkedPanel;
	
	private static final String LOG_CONFIG = "config/logging.properties";
	private static Logger log = Logger.getLogger( HeritageUI.class.getName() );
	
	// ������ ��������� �� �� ������
	private static HashMap<String, Component> componentMap;
	
	/**
	 * ������� ���� ����������
	 */
	private void createFrame()
	{
		frame = new JFrame( );
		frame.setUndecorated( true );
		frame.setBounds( HORIZONTAL_OFFSET, VERTICAL_OFFSET, APP_WIDTH, APP_HEIGHT );
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setBackground( ApplicationColors.APP_BACKGROUND_COLOR );
		frame.setResizable( false );
		frame.setVisible( true );
        frame.getContentPane().setLayout( null );
        frame.setIconImage( new ImageIcon( "icons/" + ICON_NAME ).getImage() );
        frame.setTitle( APPLICATION_NAME );
	}
	
	/**
	 * ������� ������ ����, �� ������� ����� ������������� ��� ������
	 */
	private void createPanelLayout( )
	{
		layoutPanel = new JPanel();
		layoutPanel.setBounds(0, 0, APP_WIDTH, APP_HEIGHT );
		layoutPanel.setBorder( BorderFactory.createMatteBorder( BORDER_THICKNESS, BORDER_THICKNESS, BORDER_THICKNESS, BORDER_THICKNESS, Color.black ) );
		layoutPanel.setLayout(null);
		layoutPanel.setBackground( ApplicationColors.LINKED_PANEL_BACKGROUND_COLOR );
		frame.getContentPane().add( layoutPanel );
	}
	
	/**
	 * ������� ��� ������
	 */
	private void createSections( )
	{
		layeredPane = new JLayeredPane();
	    layeredPane.setBounds( BORDER_THICKNESS, BORDER_THICKNESS, APP_WIDTH-BORDER_THICKNESS*2, APP_HEIGHT-BORDER_THICKNESS*2 ); // ��� ����������� �������
	    	    
	    layoutPanel.add( layeredPane );
	    	    
	    HTitleBar titleBar = new HTitleBar( APPLICATION_NAME, getFrameWidth(), frame, true, true );
	    layeredPane.add( titleBar, 10, 1 );

	    layeredPane.add( TopPanel.create( getFrameWidth() ), 10, 1 );
		
	    linkedPanel = LinkedPanel.create( getFrameWidth(), getFrameHeight() );
	    layeredPane.add( linkedPanel, 0, 1 );
		
	    layeredPane.add( UnlinkedPanel.create( getFrameWidth(), getFrameHeight() ), 10, 1 );
	   		
	  	layeredPane.add( BottomPanel.create( getFrameWidth(), getFrameHeight() ), 10, 1 );
	}
		
	/**
	 * ��������� ������������ ������� ����������
	 */
	private static void refreshLogger( )
	{
		try 
		{
			 LogManager.getLogManager().readConfiguration( new FileInputStream( LOG_CONFIG ) );
		} 
		catch( Exception ex ) 
		{
			ex.printStackTrace( );
		} 
	}
	
	/** ================== ��������������� ������ ================== **/
	
	/**
	 * ��������� ������ ��������
	 * @return ������ ��������
	 */
	private int getScreenWidth()
	{
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();		
		return (int) screenSize.getWidth();
	}
	
	/**
	 * ��������� ������ ��������
	 * @return ������ ��������
	 */
	private int getScreenHeight()
	{
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();		
		return (int)screenSize.getHeight();
	}
	
	/**
	 * ��������� ������ ��������� ���� ����������
	 * @return ������ ��������� ���� ����������
	 */
	private int getFrameWidth()
	{
		return frame.getContentPane().getWidth();
	}
	
	/**
	 * ��������� ������ ��������� ���� ����������
	 * @return ������ ��������� ���� ����������
	 */
	private int getFrameHeight()
	{
		return frame.getContentPane().getHeight();
	}
	
	/**
	 * ����� ����������� ��� ������ ���������� � �������� ������ ���� ������������� ���������
	 */
	private void createComponentMap() 
	{
		componentMap = new HashMap<String,Component>();
        Component[] components = layeredPane.getComponents();
        for( int i=0;i<components.length;i++ ) 
        {
        	componentMap.put( components[i].getName(), components[i] );
        }
	}
	
	/**
	 * �������� ������� �� ��� �����
	 * @param name - ���
	 * @return - ��������� � �������� ������
	 */
	public static Component getComponentByName( String name ) 
	{
        if( componentMap.containsKey(name) ) 
        {
        	return (Component) componentMap.get(name);
        }
        else 
        {
        	return null;
        }
	}
	
	public static JFrame getFrame()
	{
		return frame;
	}

	/**
	 * Launch the application.
	 */
	public static void main( String[] args ) 
	{
		refreshLogger( );
		
		EventQueue.invokeLater(new Runnable() 
		{
			@SuppressWarnings("static-access")
			public void run() {
				try {
					HeritageUI window = new HeritageUI();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public HeritageUI() 
	{
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() 
	{
		log.info( "Application starts..." );
		
		createFrame( );
		createPanelLayout( );
		createSections( );
		
		log.info( "... Application started" );
		
		createComponentMap( );
	}
}
