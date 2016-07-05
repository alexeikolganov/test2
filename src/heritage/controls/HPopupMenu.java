package heritage.controls;

import heritage.config.ApplicationColors;
import heritage.config.Config;
import heritage.config.MenuItem;
import heritage.listener.MenuPanelListener;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;

/**
 * Контекстное меню
 * @author Home
 *
 */
public class HPopupMenu extends JPopupMenu
{
	private static final long serialVersionUID = 4902600238078017300L;
	
	private final int BORDER_THICKNESS 	 = Integer.parseInt( Config.getItem( "border_thickness" ) );
	
	private final int CONTEXT_WIDTH 	 = 160;
	
	private final int TEXT_FONT_SIZE	 = Integer.parseInt( Config.getItem( "text_font_fize" ) );
	private final String TEXT_FONT_NAME	 = Config.getItem( "app_font_name" );
	
	private final Font FONT = new Font( TEXT_FONT_NAME, Font.PLAIN, TEXT_FONT_SIZE );
	private final Font EDIT_FONT = new Font( TEXT_FONT_NAME, Font.PLAIN, 9 );

	public HPopupMenu( List<MenuItem> items )
	{
		super();
		
		UIManager.put( "MenuItem.background", ApplicationColors.POPUP_BACKGROUND_COLOR );
		
		JMenuItem item;
		for( int i=0;i<items.size();i++ )
		{
			if( items.get(i).getMenuItem().equals( "|" ) )
			{
				add( buildSeparator( ) );
			}
			else
			{				
				/*
				item = new JMenuItem( items[i] );
				item.setFont( FONT );
				item.addActionListener( buildActionListener() );
				add( item );
				*/
				// TODO: добавить возможность добавлять два пункта в одну строку (просмотр/редактирование)
				// на данном этапе это JPanel c двумя JLabel
				
				item = new JMenuItem( );				
				item.add( buildMenuItem( items.get(i) ) );
				add( item );
			}
		}
		setPopupSize( CONTEXT_WIDTH, items.size()*24 );
		setBorder( new LineBorder( ApplicationColors.POPUP_BORDER_COLOR, BORDER_THICKNESS ) );
		setBackground( ApplicationColors.POPUP_BACKGROUND_COLOR );
	}
	
	/**
	 * Создаем свой сепаратор
	 * @return
	 */
	private JSeparator buildSeparator( )
	{
		JSeparator sep = new JSeparator( ) 
		{
			private static final long serialVersionUID = 1L;

			public void paintComponent( Graphics g )
			{
				g.setColor( ApplicationColors.POPUP_BORDER_COLOR );
				g.drawLine( 2, 3, CONTEXT_WIDTH - 8, 3 );
			}
		};
		return sep;
	}
	
	private JPanel buildMenuItem( MenuItem item )
	{
		int textWidth = 120;
		int margin	  = 4;
		int height	  = 24;
		
		JPanel menu = new JPanel( );
		menu.setBackground( ApplicationColors.POPUP_BACKGROUND_COLOR );
		menu.setLayout( null );
		menu.addMouseListener( new MenuPanelListener( ) );
		menu.setPreferredSize( new Dimension( CONTEXT_WIDTH, height ) );

		JLabel menuItem = new JLabel( item.getMenuItem() );
		menuItem.setBackground( ApplicationColors.POPUP_BACKGROUND_COLOR );
		menuItem.setFont( FONT );
		menuItem.setBounds( margin, 0, textWidth, height );	
		menuItem.addMouseListener( buildMouseAdapter() );
		menu.add( menuItem );
		
		if( item.isEditable() )
		{
			JLabel editItem = new JLabel( Config.getItem( "default_edit_label" ) );
			editItem.setBackground( ApplicationColors.POPUP_BACKGROUND_COLOR );
			editItem.setFont( EDIT_FONT );
			editItem.setBounds( textWidth, 0, CONTEXT_WIDTH - textWidth, height );
			editItem.setForeground( Color.BLUE );
			editItem.addMouseListener( buildMouseAdapter() );
			menu.add( editItem );
		}
		
		return menu;
	}
	
	private ActionListener buildActionListener( )
	{
		ActionListener al = new ActionListener() 
		{
			public void actionPerformed( ActionEvent e ) 
			{
				//System.out.println( ((JLabel) e.getSource()).getText() );
			}
		};
		return al;
	}

	private MouseAdapter buildMouseAdapter( )
	{
		MouseAdapter ma = new MouseAdapter()  
		{  
			private final Color ROLLOVER_COLOR 	 = new Color( 200, 200, 200 );
			private final Color BACKGROUND_COLOR = Color.white;
			
			public void mouseEntered( MouseEvent e )
			{
				JLabel lbl = (JLabel) e.getSource();
				JPanel pnl = (JPanel) lbl.getParent();
				pnl.setBackground( ROLLOVER_COLOR );
			}
			
			public void mouseExited( MouseEvent e )
			{
				JLabel lbl = (JLabel) e.getSource();
				JPanel pnl = (JPanel) lbl.getParent();
				pnl.setBackground( BACKGROUND_COLOR );
			}
			public void mouseClicked( MouseEvent e )  
		    {  
		    	//System.out.println( ((JLabel) e.getSource()).getText() );
		    }  
		};
		return ma;
	}
}