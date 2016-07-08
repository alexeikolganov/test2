package heritage.controls.inputs;

import heritage.config.ApplicationColors;
import heritage.config.Config;
import heritage.controls.buttons.HButton;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.util.Calendar;

/**
 * Date Picker
 * @author akolganov
 *
 */
public class DatePicker extends JPanel
{
	private static final long serialVersionUID 	= 2793154058073858045L;
	
	private static String DEFAULT_DATE_FORMAT 	= "dd/MM/yyyy";
    private static final int DIALOG_WIDTH  		= Integer.parseInt( Config.getItem( "field_width" ) );
    private static final int DIALOG_HEIGHT 		= 200;
    private static final int BORDER_THICKNESS  	= 2;
    
	private final int FIELD_HEIGHT 				= Integer.parseInt( Config.getItem( "field_height" ) );
	private final int TEXT_FONT_SIZE			= 16;//Integer.parseInt( Config.getItem( "text_font_size" ) );
	private final String TEXT_FONT_NAME			= Config.getItem( "app_font_name" );
    

    private SimpleDateFormat dateFormat;
    private DatePanel datePanel = null;
    private JDialog dateDialog  = null;
	
	private HTextField dateField;
	private JButton btn;
	private JDialog dialog;
	
	/**
	 * Constructor
	 * 
	 * textfield
	 * button
	 * wrapped by a panel
	 * 
	 * date picker appears on click
	 */
	public DatePicker( String deft, String value )
	{
		setSize( DIALOG_WIDTH, FIELD_HEIGHT );
		setPreferredSize( new Dimension( DIALOG_WIDTH, FIELD_HEIGHT ) );	
		setBorder( new LineBorder( ApplicationColors.FIELD_BORDER_FOCUSED, BORDER_THICKNESS ) );
		setLayout( null );
		
		dateField = new HTextField( deft, value );
		dateField.setBounds( 0, 0, DIALOG_WIDTH - FIELD_HEIGHT, FIELD_HEIGHT );

		add( dateField );
		
		btn = new HButton( "..." );
		btn.setBounds( DIALOG_WIDTH - FIELD_HEIGHT, 0, FIELD_HEIGHT, FIELD_HEIGHT );
		btn.setBorder( null );
		add(btn);
				
		setCursor( new Cursor( Cursor.HAND_CURSOR ) );
        addListeners( );
        
	}

	public void reSize( int w, int h )
	{

		setSize( w, h );
		setPreferredSize( new Dimension( w, h ) );
		
		dateField.setBounds( 0, 0, w - h, h );
		btn.setBounds( w - h, 0, h, h );
		
		//dialog.setSize( w, DIALOG_HEIGHT );

	}
	
	/**
	 * Add listener to invoke a date picker on mouse click
	 * in text field and button
	 */
	private void addListeners() 
	{
        btn.addMouseListener( new MouseAdapter() 
        {
            public void mouseClicked( MouseEvent paramMouseEvent ) 
            {
                if( datePanel == null ) 
                {
                    datePanel = new DatePanel();
                }
                Point point = getLocationOnScreen();
                point.y = point.y + FIELD_HEIGHT - BORDER_THICKNESS;
                showDateDialog( datePanel, point );
            }
        });
        /*dateField.addMouseListener( new MouseAdapter() 
        {
            public void mouseClicked( MouseEvent paramMouseEvent ) 
            {
                if( datePanel == null ) 
                {
                    datePanel = new DatePanel();
                }
                Point point = getLocationOnScreen();
                point.y = point.y + FIELD_HEIGHT - BORDER_THICKNESS;
                showDateDialog( datePanel, point );
            }
        });*/
    }

	/**
	 * Create Date Picker dialog
	 * which is closed when picker loses focus
	 * @param dateChooser - date picker panel
	 * @param position - cursor position
	 */
    private void showDateDialog( DatePanel dateChooser, Point position ) 
    {
        
        if( SwingUtilities.getWindowAncestor( DatePicker.this ) instanceof JDialog )
        {
        	JDialog owner = (JDialog) SwingUtilities.getWindowAncestor( DatePicker.this );
        	if( dateDialog == null || dateDialog.getOwner() != owner ) 
            {
                dateDialog = createDateDialog( owner, dateChooser );
                dateDialog.setLocation( getAppropriateLocation( owner, position ) );
            }
        	
        }
        else
        {
        	Frame owner = (Frame) SwingUtilities.getWindowAncestor( DatePicker.this );
        	if( dateDialog == null || dateDialog.getOwner() != owner ) 
            {
                dateDialog = createDateDialog( owner, dateChooser );
                dateDialog.setLocation( getAppropriateLocation( owner, position ) );
            }
        }
        
        dateDialog.setModal( false );
       
        dateDialog.setVisible( true );
        dateDialog.addWindowFocusListener( new WindowFocusListener() 
        {
            public void windowGainedFocus (WindowEvent e ) 
            {
            
            }

            public void windowLostFocus( WindowEvent e ) 
            {
            	dateDialog.setVisible( false );
            }
        });
    }

    /**
     * Create Date Picker dialog
     * @param owner - parent frame
     * @param contentPanel - Date Picker panel to be injected into dialog
     * @return Date Picker dialog
     */
    private JDialog createDateDialog( Frame owner, JPanel contentPanel ) 
    {
    	dialog = new JDialog( owner, "Date Selected", true );
        dialog.setUndecorated (true );
        dialog.getContentPane( ).add( contentPanel, BorderLayout.CENTER );
        dialog.pack();
        dialog.setSize( getPreferredSize().width, DIALOG_HEIGHT );
        return dialog;
    }
    
    /**
     * Create Date Picker dialog
     * @param owner - parent frame
     * @param contentPanel - Date Picker panel to be injected into dialog
     * @return Date Picker dialog
     */
    private JDialog createDateDialog( JDialog owner, JPanel contentPanel ) 
    {
    	JDialog dialog = new JDialog( owner, "Date Selected", true );
        dialog.setUndecorated (true );
        dialog.getContentPane( ).add( contentPanel, BorderLayout.CENTER );
        dialog.pack();
        dialog.setSize( getPreferredSize().width, DIALOG_HEIGHT );
        return dialog;
    }

    /**
     * Calculate Date Picker position
     * to be displayed right under calendar field / button
     * @param owner
     * @param position
     * @return
     */
    private Point getAppropriateLocation( Frame owner, Point position ) 
    {
        Point result = new Point( position );
        Point p 	 = owner.getLocation();
        int offsetX  = ( position.x + DIALOG_WIDTH ) - ( p.x + owner.getWidth( ) );
        int offsetY  = ( position.y + DIALOG_HEIGHT ) - ( p.y + owner.getHeight( ) );

        if( offsetX > 0 ) 
        {
            result.x -= offsetX;
        }

        if( offsetY > 0 ) 
        {
            result.y -= offsetY;
        }
        return result;
    }
    
    /**
     * Calculate Date Picker position
     * to be displayed right under calendar field / button
     * @param owner
     * @param position
     * @return
     */
    private Point getAppropriateLocation( JDialog owner, Point position ) 
    {
        Point result = new Point( position );
        Point p 	 = owner.getLocation();
        int offsetX  = ( position.x + DIALOG_WIDTH ) - ( p.x + owner.getWidth( ) );
        int offsetY  = ( position.y + DIALOG_HEIGHT ) - ( p.y + owner.getHeight( ) );

        if( offsetX > 0 ) 
        {
            result.x -= offsetX;
        }

        if( offsetY > 0 ) 
        {
            result.y -= offsetY;
        }
        return result;
    }

    public HTextField getDateField()
    {
    	return dateField;
    }
    
    /** 
     * Get format
     * @return
     */
    private SimpleDateFormat getDefaultDateFormat() 
    {
        if( dateFormat == null ) 
        {
            dateFormat = new SimpleDateFormat( DEFAULT_DATE_FORMAT );
        }
        return dateFormat;
    }

    /**
     * Set text
     */
    public void setText( Date date ) 
    {
        setDate( date, false );
    }

    
    /**
     * Set date
     */
    public void setDate( Date date, boolean spinnerClicked ) 
    {
    	dateField.setText( getDefaultDateFormat( ).format( date ) );
    	if( !spinnerClicked )
    	{
	    	dateField.grabFocus();
	    	dateField.requestFocus();
    	}
    }

    /**
     * Get date
     */
    public Date getDate() 
    {
        try 
        {
            return getDefaultDateFormat( ).parse( dateField.getText( ) );
        } 
        catch( ParseException ex ) 
        {
            return new Date();
        }
    }
    
    /**
     * Get text
     */
    public String getText() 
    {
    	return dateField.getText( );
    }

    /**
     * Date widget panel
     * @author akolganov
     *
     */
    private class DatePanel extends JPanel implements ChangeListener 
    {

		private static final long serialVersionUID = 1909445351334061891L;
		int startYear = 1000;
        int lastYear = 2050;

        Color backGroundColor  	= Color.white;
        Color palletTableColor 	= Color.white;
        Color todayBackColor 	= Color.orange;
        Color weekFontColor 	= Color.black;
        Color dateFontColor 	= Color.black;
        Color weekendFontColor 	= Color.red;

        Color controlLineColor 	= ApplicationColors.FIELD_BORDER_UNFOCUSED;
        Color controlTextColor 	= Color.black;
        Color borderColor  		= ApplicationColors.FIELD_BORDER_UNFOCUSED;

        JSpinner yearSpin;
        JSpinner monthSpin;
        JButton[][] daysButton = new JButton[6][7];

        DatePanel() 
        {
            setLayout( new BorderLayout() );
            setBorder( new LineBorder( borderColor, 2) );
            setBackground( backGroundColor );
            setFont( new Font( TEXT_FONT_NAME, Font.PLAIN, TEXT_FONT_SIZE ) );

            JPanel topYearAndMonth = createYearAndMonthPanel();
            add(topYearAndMonth, BorderLayout.NORTH);
            JPanel centerWeekAndDay = createWeekAndDayPanel();
            add(centerWeekAndDay, BorderLayout.CENTER);

            reflushWeekAndDay();
        }

        private JPanel createYearAndMonthPanel() 
        {
            Calendar cal 	 = getCalendar();
            int currentYear  = cal.get( Calendar.YEAR );
            int currentMonth = cal.get( Calendar.MONTH ) + 1;

            JPanel panel = new JPanel();
            panel.setLayout( new FlowLayout() );
            panel.setBackground( controlLineColor );

            yearSpin = new JSpinner( new SpinnerNumberModel( currentYear, startYear, lastYear, 1 ) );
            yearSpin.setFont( new Font( TEXT_FONT_NAME, Font.PLAIN, TEXT_FONT_SIZE ) );
            yearSpin.setPreferredSize( new Dimension(56, 24) );
            yearSpin.setName("Year");
            yearSpin.setBorder( null );
            yearSpin.setEditor(new JSpinner.NumberEditor( yearSpin, "####") );
            yearSpin.addChangeListener(this);
            panel.add(yearSpin);

            JLabel yearLabel = new JLabel("Year");
            yearLabel.setForeground(controlTextColor);
            yearLabel.setFont( new Font( TEXT_FONT_NAME, Font.PLAIN, TEXT_FONT_SIZE ) );
            panel.add(yearLabel);

            monthSpin = new JSpinner(new SpinnerNumberModel(currentMonth, 1, 12, 1));
            monthSpin.setFont( new Font( TEXT_FONT_NAME, Font.PLAIN, TEXT_FONT_SIZE ) );
            monthSpin.setPreferredSize(new Dimension(35, 24));
            monthSpin.setName("Month");
            monthSpin.setBorder( null );
            monthSpin.addChangeListener(this);
            panel.add(monthSpin);

            JLabel monthLabel = new JLabel("Month");
            monthLabel.setForeground(controlTextColor);
            monthLabel.setFont( new Font( TEXT_FONT_NAME, Font.PLAIN, TEXT_FONT_SIZE ) );
            panel.add(monthLabel);

            return panel;
        }

        private JPanel createWeekAndDayPanel() 
        {
            String colname[] = { "S", "M", "T", "W", "T", "F", "S" };
            JPanel panel = new JPanel();
            panel.setFont(new Font(TEXT_FONT_NAME, Font.PLAIN, TEXT_FONT_SIZE));
            panel.setLayout(new GridLayout(7, 7));
            panel.setBackground(backGroundColor);

            for (int i = 0; i < 7; i++) {
                JLabel cell = new JLabel(colname[i]);
                cell.setHorizontalAlignment(JLabel.CENTER);
                cell.setFont(new Font(TEXT_FONT_NAME, Font.PLAIN, TEXT_FONT_SIZE));
                if( i == 0 || i == 6 ) 
                {
                    cell.setForeground( weekendFontColor );
                } 
                else 
                {
                    cell.setForeground( weekFontColor );
                }
                panel.add(cell);
            }

            int actionCommandId = 0;
            for (int i = 0; i < 6; i++)
                for (int j = 0; j < 7; j++) {
                    JButton numBtn = new JButton();
                    numBtn.setFont(new Font(TEXT_FONT_NAME, Font.PLAIN, TEXT_FONT_SIZE));
                    numBtn.setBorder(null);
                    numBtn.setHorizontalAlignment(SwingConstants.CENTER);
                    numBtn.setActionCommand(String
                            .valueOf(actionCommandId));
                    numBtn.setBackground(palletTableColor);
                    numBtn.setForeground(dateFontColor);
                    numBtn.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent event) {
                            JButton source = (JButton) event.getSource();
                            if (source.getText().length() == 0) {
                                return;
                            }
                            dayColorUpdate(true);
                            source.setForeground(todayBackColor);
                            int newDay = Integer.parseInt(source.getText());
                            Calendar cal = getCalendar();
                            cal.set(Calendar.DAY_OF_MONTH, newDay);
                            setDate(cal.getTime(), false);

                            dateDialog.setVisible(false);
                        }
                    });

                    if (j == 0 || j == 6)
                        numBtn.setForeground(weekendFontColor);
                    else
                        numBtn.setForeground(dateFontColor);
                    daysButton[i][j] = numBtn;
                    panel.add(numBtn);
                    actionCommandId++;
                }

            return panel;
        }

        private Calendar getCalendar() {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(getDate());
            return calendar;
        }

        private int getSelectedYear() {
            return ((Integer) yearSpin.getValue()).intValue();
        }

        private int getSelectedMonth() {
            return ((Integer) monthSpin.getValue()).intValue();
        }

        private void dayColorUpdate(boolean isOldDay) {
            Calendar cal = getCalendar();
            int day = cal.get(Calendar.DAY_OF_MONTH);
            cal.set(Calendar.DAY_OF_MONTH, 1);
            int actionCommandId = day - 2 + cal.get(Calendar.DAY_OF_WEEK);
            int i = actionCommandId / 7;
            int j = actionCommandId % 7;
            if (isOldDay) {
                daysButton[i][j].setForeground(dateFontColor);
            } else {
                daysButton[i][j].setForeground(todayBackColor);
            }
        }

        private void reflushWeekAndDay() {
            Calendar cal = getCalendar();
            cal.set(Calendar.DAY_OF_MONTH, 1);
            int maxDayNo = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
            int dayNo = 2 - cal.get(Calendar.DAY_OF_WEEK);
            for (int i = 0; i < 6; i++) {
                for (int j = 0; j < 7; j++) {
                    String s = "";
                    if (dayNo >= 1 && dayNo <= maxDayNo) {
                        s = String.valueOf(dayNo);
                    }
                    daysButton[i][j].setText(s);
                    dayNo++;
                }
            }
            dayColorUpdate(false);
        }

        public void stateChanged(ChangeEvent e) {
            dayColorUpdate(true);

            boolean spinnerClicked = false;
            
            JSpinner source = (JSpinner) e.getSource();
            Calendar cal = getCalendar();
            if (source.getName().equals("Year")) {
                cal.set(Calendar.YEAR, getSelectedYear());
                spinnerClicked = true;
            } else {
                cal.set(Calendar.MONTH, getSelectedMonth() - 1);
                spinnerClicked = true;
            }
            setDate(cal.getTime(), spinnerClicked);
            reflushWeekAndDay();
        }
    }
}
