package heritage.ui.block;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.Timer;

import heritage.config.Config;
import heritage.contact.Contact;
import heritage.contact.ContactRelationship;
import heritage.controls.buttons.HMenuButton;
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
	private final static String TEXT_FONT_NAME 		= Config.getItem( "app_font_name" );
	
	private final static Font LARGE_FONT 			= new Font( TEXT_FONT_NAME, Font.BOLD, TITLE_FONT_SIZE );
	
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
	public AddContactBlock( final Contact contact, final ContactRelationship[] reln )
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
		final String BUTTON_LABEL = Config.getItem( "new_contact_label" );
		HMenuButton newAccountButton = new HMenuButton( BUTTON_LABEL, "icons/plus.png" );
		newAccountButton.setBounds( 0, 0, BLOCK_WIDTH, BLOCK_HEIGHT / 2 );
			
		newAccountButton.addActionListener( new ActionListener() 
		{
			public void actionPerformed( ActionEvent ev ) 		
			{
				Contact temp = new Contact( "", true );
				new ModalEdit( BUTTON_LABEL, temp );				
			}			
		});
		menuPanel.add( newAccountButton );
		
		// 2.б. Меню "Выбрать существующий контакт"
		HMenuButton addExistingLbl = new HMenuButton( Config.getItem( "existing_contact" ), "icons/link.png" );
		addExistingLbl.setBounds( 0, BLOCK_HEIGHT / 2, BLOCK_WIDTH, BLOCK_HEIGHT / 2 );		
		addExistingLbl.addActionListener( new ActionListener() 
		{
			public void actionPerformed( ActionEvent ev ) 		
			{
				//Contact temp = new Contact( "", true );
				//new ModalEdit( Config.getItem( "add_existing_contact" ), temp );				
			}		
		});
		menuPanel.add( addExistingLbl );
	}	
}
