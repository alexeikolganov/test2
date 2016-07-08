package heritage.ui.block;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.Timer;

import heritage.config.ApplicationColors;
import heritage.config.Config;
import heritage.contact.Contact;
import heritage.contact.ContactRelationship;
import heritage.listener.ClickListener;
import heritage.ui.modal.ModalEdit;

/**
 * Блок "Добавить <тип_контакта>"
 * @author Home
 *
 */
public class AddContactBlock extends JLayeredPane
{
	private static final long serialVersionUID = 2946765925019144703L;
	private static final int BLOCK_WIDTH  			= Integer.parseInt( Config.getItem( "block_width" ) );
	private static final int BLOCK_HEIGHT 			= Integer.parseInt( Config.getItem( "block_height" ) );

	private final static int TITLE_FONT_SIZE		= Integer.parseInt( Config.getItem( "title_font_size" ) );
	private final static int TEXT_FONT_SIZE			= Integer.parseInt( Config.getItem( "menu_button_font_size" ) );
	private final static String TEXT_FONT_NAME 		= Config.getItem( "app_font_name" );
	
	private final static Font LARGE_FONT 			= new Font( TEXT_FONT_NAME, Font.BOLD, TITLE_FONT_SIZE );
	private final static Font MIDDLE_FONT 			= new Font( TEXT_FONT_NAME, Font.PLAIN, TEXT_FONT_SIZE );
	
	private Timer timer;
	
	private JPanel mainPanel;
	private JPanel menuPanel;
	
	/**
	 * Конструктор, состоящий из:
	 * 1. Основной панели, содержащей текст "Добавить <тип_контакта>"
	 * 2. Выезжающего меню при наведении на блок, в свою очередь состоящего из:
	 * 2.а. Меню "Добавить новый контакт"
	 * 2.б. Меню "Выбрать существующий контакт"
	 * @param contact
	 */
	public AddContactBlock( final Contact contact, final ContactRelationship reln )
	{
		// устанавливаем основные параметры
		setLocation( (int)contact.x, (int)contact.y );
		setSize( new Dimension( BLOCK_WIDTH, BLOCK_HEIGHT ) );
		setBorder( BorderFactory.createDashedBorder( null ) );
		
		// при клике на блок вызывается меню
		addMouseListener( new MouseAdapter( )  
		{  
			@Override
			public void mouseClicked( MouseEvent e )  
			{
				final AddContactBlock block = (AddContactBlock)e.getComponent();
				timer = new Timer( 10, new ActionListener() 
		        {
					public void actionPerformed( ActionEvent e ) 
					{
						// expand
						block.menuPanel.setSize( BLOCK_WIDTH , block.menuPanel.getHeight() + 5 );
		                if( block.menuPanel.getHeight() >= BLOCK_HEIGHT ) 
		                {
		                	((Timer) e.getSource()).stop( );    
		                	block.menuPanel.setSize( BLOCK_WIDTH , BLOCK_HEIGHT );
		                }
					}
		         });
				 timer.setInitialDelay( 300 );
				 timer.start();
			}		
		});
		
		// 1. Основная панель, содержащая текст "Добавить <тип_контакта>"
		mainPanel = new JPanel();
		mainPanel.setSize( new Dimension( BLOCK_WIDTH, BLOCK_HEIGHT ) );
		mainPanel.setOpaque( false );
		add( mainPanel, 1, 1 );
		
		JLabel addLbl = new JLabel( contact.label );
		addLbl.setHorizontalAlignment( SwingConstants.CENTER );
		addLbl.setFont( LARGE_FONT );
		mainPanel.add( addLbl );
		
		// 2. Выезжающее меню при наведении на блок
		menuPanel = new JPanel();
		menuPanel.setSize( new Dimension( BLOCK_WIDTH, 0 ) );
		menuPanel.setLayout( null );
		add( menuPanel, 2, 1 );
		
		// 2.а. Меню "Добавить новый контакт"
		JLabel addNewLbl = new JLabel( Config.getItem( "add_new_contact" ) );
		addNewLbl.setSize( new Dimension( BLOCK_WIDTH, BLOCK_HEIGHT / 2 ) );
		addNewLbl.setHorizontalAlignment( SwingConstants.CENTER );
		addNewLbl.setLocation(0,0);
		addNewLbl.setFont( MIDDLE_FONT );
		addNewLbl.setBorder( BorderFactory.createMatteBorder( 0, 0, 2, 0, ApplicationColors.FIELD_BORDER_UNFOCUSED ) );
		addNewLbl.setBackground( ApplicationColors.PANEL_BACKGROUND_COLOR );
		addNewLbl.setForeground( ApplicationColors.APP_TEXT_COLOR);
		addNewLbl.setOpaque( true );
		menuPanel.add( addNewLbl );
		
		// при одиночном клике на пункт меню прячем меню целиком
		// при двойном клике вызываем форму создания контакта
		addNewLbl.addMouseListener( new ClickListener()  
		{  
			private final Color ROLLOVER_COLOR 	 = new Color( 200, 200, 200 );
			private final Color BACKGROUND_COLOR = ApplicationColors.PANEL_BACKGROUND_COLOR;
			
			public void mouseEntered( MouseEvent e )
			{
				JComponent panel = (JComponent) e.getSource();
				panel.setBackground( ROLLOVER_COLOR );
			}
			
			public void mouseExited( MouseEvent e )
			{
				JComponent panel = (JComponent) e.getSource();
				panel.setBackground( BACKGROUND_COLOR );
			}
				
			public void singleClick( MouseEvent e )
            {
				/*final AddContactBlock block = (AddContactBlock)e.getComponent().getParent().getParent();
				if( block.menuPanel.getHeight()> 0 )
				 {
					timer = new Timer( 1, new ActionListener() 
			        {
						public void actionPerformed( ActionEvent e ) 
						{
							// collapse
							block.menuPanel.setSize( BLOCK_WIDTH , block.menuPanel.getHeight() - 10 );
							block.getParent().getParent().revalidate();
							block.getParent().getParent().repaint();
			                if( block.menuPanel.getHeight() <= 0 ) 
			                {
			                	((Timer) e.getSource()).stop( );  
			                	block.menuPanel.setSize( BLOCK_WIDTH, 0 );
			                } 
						}
			         });
					 timer.start(); 
				 }*/
				new ModalEdit( contact.firstName + " " + contact.lastName, contact, reln );
            }

            public void doubleClick( MouseEvent e )
            {
            	new ModalEdit( contact.firstName + " " + contact.lastName, contact );
            }
			
			
		});
		
		// 2.б. Меню "Выбрать существующий контакт"
		JLabel addExistingLbl = new JLabel( Config.getItem( "add_existing_contact" ) );
		addExistingLbl.setSize( new Dimension( BLOCK_WIDTH, BLOCK_HEIGHT / 2 ) );
		addExistingLbl.setHorizontalAlignment( SwingConstants.CENTER );
		addExistingLbl.setLocation(0,BLOCK_HEIGHT / 2 );
		addExistingLbl.setFont( MIDDLE_FONT );
		addExistingLbl.setBackground( ApplicationColors.PANEL_BACKGROUND_COLOR );
		addExistingLbl.setForeground( ApplicationColors.APP_TEXT_COLOR);
		addExistingLbl.setOpaque( true );
		menuPanel.add( addExistingLbl );

	}
	
	
}
