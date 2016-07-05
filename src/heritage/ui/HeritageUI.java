package heritage.ui;
import heritage.config.ApplicationColors;
import heritage.config.Config;
import heritage.controls.HTitleBar;
import heritage.relationship.DPanel;
import heritage.relationship.RelationshipPanel;
import heritage.relationship._RelationshipPanel;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.io.FileInputStream;
import java.io.IOException;
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
	
	// список элементов по их именам
	private static HashMap<String, Component> componentMap;
	
	/**
	 * Создаем окно приложения
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
	 * Создаем нижний слой, на котором будут располагаться все секции
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
	 * Создаем все секции
	 */
	private void createSections( )
	{
		layeredPane = new JLayeredPane();
	    layeredPane.setBounds( BORDER_THICKNESS, BORDER_THICKNESS, APP_WIDTH-BORDER_THICKNESS*2, APP_HEIGHT-BORDER_THICKNESS*2 ); // для отображения границы
	    	    
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
	 * Обновляем конфигурацию логгера приложения
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
	
	/** ================== ВСПОМОГАТЕЛЬНЫЕ МЕТОДЫ ================== **/
	
	/**
	 * Получение ширины монитора
	 * @return ширина монитора
	 */
	private int getScreenWidth()
	{
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();		
		return (int) screenSize.getWidth();
	}
	
	/**
	 * Получение высоты монитора
	 * @return высота монитора
	 */
	private int getScreenHeight()
	{
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();		
		return (int)screenSize.getHeight();
	}
	
	/**
	 * Получение ширины основного окна приложения
	 * @return ширина основного окна приложения
	 */
	private int getFrameWidth()
	{
		return frame.getContentPane().getWidth();
	}
	
	/**
	 * Получение высоты основного окна приложения
	 * @return высота основного окна приложения
	 */
	private int getFrameHeight()
	{
		return frame.getContentPane().getHeight();
	}
	
	/**
	 * Метод запускается при старте приложения и получает список всех поименованных элементов
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
	 * Получаем элемент по его имени
	 * @param name - имя
	 * @return - компонент с заданным именем
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
	
	private JPanel buildTree( int w, int h)
	{
		JPanel treePane = new JPanel();
		try {
			treePane = RelationshipPanel.createPanel( w, h );
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		return treePane;
	}
	
/*	private BlockTreePane buildTree2(int w, int h)
	{
		String[] details = { "icons/man_128.png", "name1", "surname", "place", "10/10/1900", "husband" };
		Block block1 = new Block( new Contact("1"), false );
	   
	    String[] details2 = { "icons/woman_128.png", "name2", "surname (maiden)", "place", "10/11/1850", "grandmother" };
	    JPanel block2 =  new Block( new Contact("1"), false );
	    
	    String[] details3 = { "icons/man_128.png", "name3", "surname", "place", "10/10/1900", "husband" };
	    JPanel block3 =  new Block( new Contact("1"), false );
	    
	    String[] details4 = { "icons/woman_128.png", "name4", "surname (maiden)", "place", "10/11/1850", "grandmother" };
	    JPanel block4 =  new Block( new Contact("1"), false );
	    
	    String[] details5 = { "icons/man_128.png", "name5", "surname", "place", "10/10/1900", "husband" };
	    JPanel block5 =  new Block( new Contact("1"), false );
	    
	    String[] details6 = { "icons/man_128.png", "name6", "surname", "place", "10/10/1900", "husband" };
	    JPanel block6 =  new Block( new Contact("1"), false );
	    
	    String[] details7 = { "icons/woman_128.png", "name7", "surname (maiden)", "place", "10/11/1850", "grandmother" };
	    JPanel block7 =  new Block( new Contact("1"), false );
	    
	    DefaultTreeForTreeLayout<JPanel> tree = new DefaultTreeForTreeLayout<JPanel>(block1);
	   
	    tree.addChild(block1, block2);
	    tree.addChild(block1, block5);
	    
	    tree.addChild(block2, block3);	    
	    tree.addChild(block2, block6);
	    
	    tree.addChild(block1, block7);
	    
	    tree.addChild(block6, block4);
	    
	    
	    
	    //TreeForTreeLayout<TextInBox> tree = SampleTreeFactory.createSampleTree();
	            
        // setup the tree layout configuration
        double gapBetweenLevels = 20;
        double gapBetweenNodes = 20;
        DefaultConfiguration<JPanel> configuration = new DefaultConfiguration<JPanel>(
                        gapBetweenLevels, gapBetweenNodes);

        // create the NodeExtentProvider for TextInBox nodes
        BlockNodeExtentProvider nodeExtentProvider = new BlockNodeExtentProvider();

        // create the layout
        TreeLayout<JPanel> treeLayout = new TreeLayout<JPanel>(tree,
                        nodeExtentProvider, configuration);

        // Create a panel that draws the nodes and edges and show the panel
        BlockTreePane panel = new BlockTreePane(treeLayout);
        panel.setBounds(9,9, 1200, 700 );
        
        //System.out.println( panel );
	    
	    return panel;
	}
*/
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
