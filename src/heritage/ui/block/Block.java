package heritage.ui.block;

import heritage.config.ApplicationColors;
import heritage.config.Config;
import heritage.config.MenuItem;
import heritage.contact.Contact;
import heritage.controls.HPopupMenu;
import heritage.listener.ClickListener;
import heritage.listener.PopupListener;
import heritage.ui.HeritageUI;
import heritage.ui.modal.ModalEdit;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.Timer;

/**
 * Блоковое отображение человека
 * @author Home
 *
 */
public class Block extends JPanel
{
	private static final long serialVersionUID 		= -137686843148695848L;
	
	private static Color blockColor;
	private static boolean personIsDead  			= false;
	private static boolean personIsSelected			= false;
	
	
	private static final int BLOCK_WIDTH  			= Integer.parseInt( Config.getItem( "block_width" ) );
	private static final int BLOCK_HEIGHT 			= Integer.parseInt( Config.getItem( "block_height" ) );
	private static final int BLOCK_MAX_HEIGHT 		= Integer.parseInt( Config.getItem( "block_max_height" ) );
		
	private final static int TEXT_FONT_SIZE	 		= Integer.parseInt( Config.getItem( "text_font_size" ) );
	private final static int TITLE_FONT_SIZE		= Integer.parseInt( Config.getItem( "title_font_size" ) );
	private final static String TEXT_FONT_NAME 		= Config.getItem( "app_font_name" );
	
	private final static Font LARGE_FONT 			= new Font( TEXT_FONT_NAME, Font.BOLD, TITLE_FONT_SIZE );
	private final static Font MIDDLE_FONT 			= new Font( TEXT_FONT_NAME, Font.PLAIN, TEXT_FONT_SIZE );
	private final static Font MIDDLE_ITALIC_FONT 	= new Font( TEXT_FONT_NAME, Font.ITALIC, TEXT_FONT_SIZE );
	private final static String DECEASED_ICON		= "icons/deceased.png";
	
	private final static int BORDER_THICKNESS		= 4;
	private final static Color BORDER_COLOR			= new Color( 150, 150, 150 );
	
	private Timer timer;
	
	private static Logger log = Logger.getLogger( Block.class.getName() );
		
	/**
	 * Основной метод, стоящий блок
	 * @param contact - объект "контакт"
	 * @param isSelected - выбран ли этот контакт
	 * @return - JPanel блок
	 */
	public Block( final Contact contact, boolean isSelected )
	{ 
		blockColor 		 = ( contact.isMasculine() ) ? ApplicationColors.MAN_COLOR : ApplicationColors.WOMAN_COLOR;
		personIsDead 	 = contact.isDead;
		personIsSelected = isSelected;
		
		setLocation( (int)contact.x, (int)contact.y );
		setSize( new Dimension( BLOCK_WIDTH, BLOCK_HEIGHT ) );
		setLayout( null );
		setBackground( blockColor );
		
		if( personIsSelected )
		{
			setBorder( BorderFactory.createLineBorder( BORDER_COLOR, BORDER_THICKNESS ) );
		}
		
		/*List<MenuItem> menuItems = new ArrayList<MenuItem>();
		menuItems.add( new MenuItem( "Контакт", true ) );
		menuItems.add( new MenuItem( "Брак", true ) );
		menuItems.add( new MenuItem( "|", false ) );
		menuItems.add( new MenuItem( "Add 1", false ) );
		menuItems.add( new MenuItem( "Add 2", false ) );
		menuItems.add( new MenuItem( "Add 3", false ) );
		
		HPopupMenu popup = new HPopupMenu( menuItems );
		
		PopupListener pl = new PopupListener( popup );
		addMouseListener( pl );*/
				
		add( setAvatar( contact.avatar ) );
		add( setDetails( contact ) );
		
		addMouseListener( new ClickListener()  
		{  	
			public void singleClick( MouseEvent e )
            {
				final Block block = (Block)e.getComponent();
				if( block.getHeight()> BLOCK_HEIGHT )
				{		
					Thread one = new Thread( ) 
					{
					    public void run() 
					    {
					        timer = new Timer( 1, new ActionListener() 
							{
								public void actionPerformed( ActionEvent e ) 
								{
									// collapse
									block.setSize( BLOCK_WIDTH , block.getHeight() - 20 );
									block.getParent().getParent().revalidate();
									block.getParent().getParent().repaint();
							        if( block.getHeight() <= BLOCK_HEIGHT ) 
							        {
							        	((Timer) e.getSource()).stop( );
							        	block.setSize( BLOCK_WIDTH , BLOCK_HEIGHT );
							        } 
								}
							 });
							 timer.start();
					    }  
					};
					one.start();
				 }
				 else
				 {
					 Thread one = new Thread( ) 
						{
						    public void run() 
						    {
						    	timer = new Timer( 10, new ActionListener() 
						        {
									public void actionPerformed( ActionEvent e ) 
									{
										// expand
										block.setSize( BLOCK_WIDTH , block.getHeight() + 5 );
						                if( block.getHeight() >= BLOCK_MAX_HEIGHT ) 
						                {
						                	((Timer) e.getSource()).stop( );    
						                	block.setSize( BLOCK_WIDTH , BLOCK_MAX_HEIGHT );
						                }
									}
						         });
								 //timer.setInitialDelay( 300 );
								 timer.start();
						    }  
						};
						one.start();
				 }
            }

            public void doubleClick( MouseEvent e )
            {
            	new ModalEdit( contact.firstName + " " + contact.lastName, contact );
            }
			
						
		});
	}
	
	/**
	 * Добавляем аватар
	 * Если человека нет в живых, добавлям траурную ленту на аватаре
	 * @param avatar - название файла
	 */
	private JLayeredPane setAvatar( String avatar )
	{
		int avatarMargin 		= 4;
		int avatarDoubleMargin 	= BLOCK_HEIGHT - avatarMargin * 2;
		int avatarLayerId 		= 2;
		int avatarDeadLayerId 	= 3;
		
		JLayeredPane avatarPane = new JLayeredPane();
		avatarPane.setBounds( avatarMargin, avatarMargin, avatarDoubleMargin, avatarDoubleMargin );
		
		ImageIcon icon = new ImageIcon( ( (new ImageIcon( avatar ) ).getImage() ).getScaledInstance( avatarDoubleMargin, 
																									avatarDoubleMargin, 
																									java.awt.Image.SCALE_SMOOTH ) );
		JLabel lbl = new JLabel( icon );
		lbl.setBounds( 0, 0, avatarDoubleMargin, avatarDoubleMargin );
		avatarPane.add( lbl, avatarLayerId, 0 );
		
		if( personIsDead )
		{
			ImageIcon deceasedIcon = new ImageIcon( ( (new ImageIcon( DECEASED_ICON ) ).getImage() ).getScaledInstance( avatarDoubleMargin, 
																														avatarDoubleMargin, 
																														java.awt.Image.SCALE_SMOOTH ));
			JLabel deceasedLbl = new JLabel( deceasedIcon );
			deceasedLbl.setBounds( 0, 0, avatarDoubleMargin, avatarDoubleMargin );
			avatarPane.add( deceasedLbl, avatarDeadLayerId, 0 );
		}
		
		return avatarPane;
	}
	
	/**
	 * Пишем детали
	 * @param details - массив деталей пользователя ( аватар, имя, фамилия, место жительства, дата рождения, родственный статус относительно выбранного контакта )
	 */
	private JPanel setDetails( Contact contact )
	{
		int textWidth 	= BLOCK_WIDTH - BLOCK_HEIGHT - BORDER_THICKNESS * 2;
		int yPos 		= 1;
		int margin 		= 4;
		
		JPanel detailPanel = new JPanel( );
		detailPanel.setBounds( BLOCK_HEIGHT + 1, BORDER_THICKNESS, textWidth, BLOCK_HEIGHT - BORDER_THICKNESS * 2 );
		detailPanel.setBackground( blockColor );
		detailPanel.setLayout( null );

		int rowHeight = (int) ( BLOCK_HEIGHT - BORDER_THICKNESS * 2 ) / 4;
				
		// имя
		detailPanel.add( createLabel( margin, yPos, textWidth, rowHeight, contact.firstName, LARGE_FONT ) );
		yPos += rowHeight;
		
		// фамилия
		detailPanel.add( createLabel( margin, yPos, textWidth, rowHeight, contact.lastName, LARGE_FONT ) ); 
		yPos += rowHeight;
		
		// место жительства
		detailPanel.add( createLabel( margin, yPos, textWidth, rowHeight, contact.placeOfLiving, MIDDLE_FONT ) ); 
		yPos += rowHeight;
		
		// дата рождения
		detailPanel.add( createLabel( margin, yPos, textWidth / 2, rowHeight, contact.dateOfBirth, MIDDLE_FONT ) );
		
		// статус
		if( !personIsSelected )
		{
			JLabel lbl = createLabel( textWidth / 2 , yPos, textWidth / 2, rowHeight, contact.status, MIDDLE_ITALIC_FONT );
			lbl.setHorizontalAlignment( SwingConstants.RIGHT );
			detailPanel.add( lbl );
		}
		
		return detailPanel;
	}
	
	/**
	 * Создаем текст
	 * @param x - координата X
	 * @param y - координата Y
	 * @param w - ширина
	 * @param h - высота
	 * @param text - текст
	 * @param font - шрифт
	 * @return JLabel
	 */
	private static JLabel createLabel( int x, int y, int w, int h, String text, Font font )
	{
		JLabel field = new JLabel( text );
		field.setBounds( x, y, w, h );
		field.setFont( font );
		return field;
	}
}
