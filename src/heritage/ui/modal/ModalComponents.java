package heritage.ui.modal;

import heritage.config.ApplicationColors;
import heritage.config.Config;
import heritage.controls.HScrollBar;
import heritage.controls.buttons.HMenuButton;
import heritage.controls.filechooser.HFileChooser;
import heritage.controls.inputs.DatePicker;
import heritage.controls.inputs.HCheckBox;
import heritage.controls.inputs.HComboBox;
import heritage.controls.inputs.HTextArea;
import heritage.controls.inputs.HTextField;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.logging.Logger;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.filechooser.FileNameExtensionFilter;

public class ModalComponents 
{
	private final static String TEXT_FONT_NAME		= Config.getItem( "app_font_name" );
	private final static int TEXT_FONT_SIZE			= Integer.parseInt( Config.getItem( "text_font_size" ) );
	
	private static Logger log = Logger.getLogger(ModalComponents.class.getName());
	
	/**
	 * Создаем чекбокс 
	 * @param x - позиция X
	 * @param y - позиция Y
	 * @param w - ширина
	 * @param h - высота
	 * @param text - текст
	 * @return чекбокс
	 */
	protected static HCheckBox buildCheckBox( int x, int y, int w, int h )
	{
		HCheckBox checkbox = new HCheckBox( );
		checkbox.setBounds( x, y, w, h );
		return checkbox;
	}
	
	/**
	 * Создаем описание поля 
	 * @param x - позиция X
	 * @param y - позиция Y
	 * @param w - ширина
	 * @param h - высота
	 * @param text - текст
	 * @return описание поля
	 */
	protected static JLabel buildLabel( int x, int y, int w, int h, String text )
	{
		JLabel label = new JLabel( text );
		label.setBounds( x, y, w, h );
		label.setFont( new Font( TEXT_FONT_NAME, Font.PLAIN, TEXT_FONT_SIZE ) );	
		return label;
	}
	
	/**
	 * Создаем текстовое поле с указанием выбранного поля
	 * @param x - позиция X
	 * @param y - позиция Y
	 * @param w - ширина
	 * @param h - высота
	 * @param text - текст-подсказка
	 * @return текстовое поле с указанием выбранного поля
	 */
	protected static JPanel buildTextField( int x, int y, int w, int h, HTextField textField, boolean hasHint )
	{
		int focusWidth  = 5;
		int fieldMargin = 5;
		
		JPanel fieldContainer = new JPanel();
	    fieldContainer.setBounds( x, y, w, h );
	    fieldContainer.setLayout( null );
	    fieldContainer.setOpaque( false );
	       
	    if( hasHint )	    	
	    {
		    final JLabel label = new JLabel( );
		    label.setBounds( 0, 0, focusWidth, h );
		    label.setOpaque( true );
		    label.setBackground( ApplicationColors.FIELD_SELECTION_HINT_COLOR );
		    label.setVisible( false );
		    fieldContainer.add( label );
		    
		    textField.addFocusListener( new FocusListener() 
		    {
		        public void focusGained(FocusEvent e) 
		        {
		        	label.setVisible( true );
		        }

		        public void focusLost(FocusEvent e) 
		        {
		        	label.setVisible( false );
		        }
		    });
	    }    

	    textField.setBounds( focusWidth + fieldMargin, 0, w - ( focusWidth + fieldMargin ), h );
	    fieldContainer.add( textField );
	    
	    return fieldContainer;
	}
	
	protected static JPanel buildDatePicker( int x, int y, int w, int h, DatePicker datePicker, boolean hasHint )
	{
		int focusWidth  = 5;
		int fieldMargin = 5;
		
		JPanel fieldContainer = new JPanel();
	    fieldContainer.setBounds( x, y, w, h );
	    fieldContainer.setLayout( null );
	    fieldContainer.setOpaque( false );
	       
	    if( hasHint )	    	
	    {
		    final JLabel label = new JLabel( );
		    label.setBounds( 0, 0, focusWidth, h );
		    label.setOpaque( true );
		    label.setBackground( ApplicationColors.FIELD_SELECTION_HINT_COLOR );
		    label.setVisible( false );
		    fieldContainer.add( label );
		    
		    datePicker.getDateField().addFocusListener( new FocusListener() 
		    {
		        public void focusGained(FocusEvent e) 
		        {
		        	label.setVisible( true );
		        }

		        public void focusLost(FocusEvent e) 
		        {
		        	label.setVisible( false );
		        }
		    });
	    }    

	    datePicker.setBounds( focusWidth + fieldMargin, 0, w - ( focusWidth + fieldMargin ), h );
	    datePicker.reSize( w - ( focusWidth + fieldMargin ), h );
	    fieldContainer.add( datePicker );
	    
	    return fieldContainer;
	}
	
	/**
	 * Создаем текстовое поле с указанием выбранного поля
	 * @param x - позиция X
	 * @param y - позиция Y
	 * @param w - ширина
	 * @param h - высота
	 * @param text - текст-подсказка
	 * @return текстовое поле с указанием выбранного поля
	 */
	protected static JPanel buildTextArea( int x, int y, int w, int h, HTextArea textArea, boolean hasHint )
	{
		int focusWidth  = 5;
		int fieldMargin = 5;
		
		JPanel fieldContainer = new JPanel();
	    fieldContainer.setBounds( x, y, w, h );
	    fieldContainer.setLayout( null );
	    fieldContainer.setOpaque( false );
	       
	    if( hasHint )	    	
	    {
		    final JLabel label = new JLabel( );
		    label.setBounds( 0, 0, focusWidth, h );
		    label.setOpaque( true );
		    label.setBackground( ApplicationColors.FIELD_SELECTION_HINT_COLOR );
		    label.setVisible( false );
		    fieldContainer.add( label );
		    
		    textArea.addFocusListener( new FocusListener() 
		    {
		        public void focusGained(FocusEvent e) 
		        {
		        	label.setVisible( true );
		        	System.out.println( label.isVisible());
		        }

		        public void focusLost(FocusEvent e) 
		        {
		        	label.setVisible( false );
		        }
		    });
	    }   
	    	    
	   //textArea.setBounds( focusWidth + fieldMargin, 0, w - ( focusWidth + fieldMargin ), h );
	   // fieldContainer.add( textArea );
	    
	    JScrollPane scroll = new JScrollPane( textArea );
	    scroll.setVerticalScrollBar( new HScrollBar( JScrollBar.VERTICAL ) );
	    scroll.setHorizontalScrollBar( new HScrollBar( JScrollBar.HORIZONTAL ) );
	    scroll.setBounds( focusWidth + fieldMargin, 0, w - ( focusWidth + fieldMargin ), h );
	    fieldContainer.add( scroll );
	    
	    return fieldContainer;
	}
	
	/**
	 * Создаем выпадающий список
	 * @param x - позиция X
	 * @param y - позиция Y
	 * @param w - ширина
	 * @param h - высота
	 * @param list - список
	 * @return выпадающий список
	 */
	protected static JPanel buildComboBox( int x, int y, int w, int h, HComboBox comboBox, boolean hasHint )
	{
		int focusWidth  = 5;
		int fieldMargin = 5;
		
		JPanel fieldContainer = new JPanel();
	    fieldContainer.setBounds( x, y, w, h );
	    fieldContainer.setLayout( null );
	    fieldContainer.setOpaque( false );
	       
	    if( hasHint )	    	
	    {
		    JLabel label2 = new JLabel( );
		    label2.setBounds( 0, 0, focusWidth, h );
		    label2.setOpaque( true );
		    fieldContainer.add( label2 );
	    }
	    
	    comboBox.setBounds( focusWidth + fieldMargin, 0, w - ( focusWidth + fieldMargin ), h );
	    fieldContainer.add( comboBox );
	    
	    return fieldContainer;
	}
	
	protected static JPanel buildFileChooser( int x, int y, String icon )
	{
		JPanel fieldContainer = new JPanel();
	    fieldContainer.setBounds( x, y, 32, 32 );
	    fieldContainer.setLayout( null );
	    	    	    
	    HMenuButton btn = new HMenuButton( "", "icons/" + icon );	    
	    btn.setBounds( 0, 0, 32, 32 );
	    btn.addActionListener( new ActionListener()
	    {
		    public void actionPerformed( ActionEvent event )
		    {
		    	HFileChooser avatar = new HFileChooser();
			    FileNameExtensionFilter filter = new FileNameExtensionFilter( "Images", "jpeg", "jpg", "png"  );
			    avatar.setFileFilter( filter );

			    int ret = avatar.showDialog( null, "Select File" );

		    }
	    });
	    fieldContainer.add( btn );
	    
	    return fieldContainer;
	}
}
