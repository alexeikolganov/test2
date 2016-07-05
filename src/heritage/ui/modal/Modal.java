package heritage.ui.modal;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import heritage.config.ApplicationColors;
import heritage.config.Config;
import heritage.contact.Contact;
import heritage.controls.HDialog;

/**
 * ��������� ����, ������� ����������
 * @author Alexei Kolganov
 */
public class Modal 
{
	private final int MODAL_WIDTH  			= Integer.parseInt( Config.getItem( "modal_width" ) );
	private final int MODAL_HEIGHT 			= Integer.parseInt( Config.getItem( "modal_height" ) );
	private final int SYS_PANEL_HEIGHT		= Integer.parseInt( Config.getItem( "sys_panel_height" ) );
	private final int BORDER_THICKNESS		= Integer.parseInt( Config.getItem( "border_thickness" ) );
		
	private final int AVATAR_W_H			= Integer.parseInt( Config.getItem( "avatar_w_h" ) );
	private final int MARGIN				= 4;
	private final int LEFT_SECTION_WIDTH 	= MARGIN + AVATAR_W_H + MARGIN;
	
	private JPanel contentPanel;
	JPanel leftPanel;
	private JPanel rightPanel;
		
	private JLabel avatar;
	private JPanel infoSection;
	
	private Contact contact;
	
	protected HDialog dialog;
	
	/**
	 * �����������
	 * @param title - ��������� ���������� ����
	 * @param leftSectionColor - ���� ����� ������
	 * @param rightSectionColor - ���� ������ ������
	 */
	public Modal( String title, Contact contact )
	{		
		this.contact = contact;
		
		installComponents( );
		dialog = new HDialog( new JFrame(), title, MODAL_WIDTH, MODAL_HEIGHT );	
		contentPanel = (JPanel) dialog.getContentPane().getComponents()[1];
		
		contentPanel.add( getLeftSection() );
		contentPanel.add( getRightSection() );
						
		//dialog.setVisible( true );
	}
	
	/**
	 * ��������� ����������
	 */
	private void installComponents( )
	{
		setAvatar( );
		setInfoSection( );
		setLeftSection( );
		setRightSection( );
	}
	
	/**
	 * �����, ����� ����� ������
	 */
	private void setLeftSection( )
	{		
		// �������������
		leftPanel = new JPanel( );
		leftPanel.setBounds( 	BORDER_THICKNESS, 
								SYS_PANEL_HEIGHT, 
								LEFT_SECTION_WIDTH, 
								MODAL_HEIGHT - SYS_PANEL_HEIGHT - BORDER_THICKNESS 
							);
		leftPanel.setBackground( contact.isMasculine() ? ApplicationColors.MAN_COLOR : ApplicationColors.WOMAN_COLOR );
		leftPanel.setLayout( null );
		
		// ����������
		leftPanel.add( getAvatar( ) );
		leftPanel.add( getInfoSection( ) );
	}
	
	/**
	 * ������, �������� ������
	 */
	private void setRightSection( )
	{
		int width = MODAL_WIDTH - LEFT_SECTION_WIDTH - BORDER_THICKNESS * 2;

		rightPanel = new JPanel( );
		rightPanel.setBounds( LEFT_SECTION_WIDTH + BORDER_THICKNESS, SYS_PANEL_HEIGHT - BORDER_THICKNESS, width, MODAL_HEIGHT - SYS_PANEL_HEIGHT );
		rightPanel.setBackground( ApplicationColors.APP_BACKGROUND_COLOR );
		rightPanel.setLayout( null );
	}
		
	/**
	 * ������
	 */
	void setAvatar( )
	{
		avatar = new JLabel( "", JLabel.CENTER );
		avatar.setBounds( 	MARGIN, 
							MARGIN, 
							AVATAR_W_H, 
							AVATAR_W_H 
						);
		
		avatar.setIcon( contact.getAvatar() );
		//avatar.setBorder( BorderFactory.createLineBorder( Color.red ) );  // �������
	}
	
	/**
	 * �������������� ������ � ����� �����
	 */
	private void setInfoSection( )
	{
		int height = MODAL_HEIGHT - ( AVATAR_W_H + MARGIN * 3 + SYS_PANEL_HEIGHT + BORDER_THICKNESS * 2 );
		
		infoSection = new JPanel( );
		infoSection.setBounds( 	MARGIN, 
								MARGIN + AVATAR_W_H + MARGIN, 
								AVATAR_W_H, 
								height 
							);
		infoSection.setOpaque( false );
		//infoSection.setBorder( BorderFactory.createLineBorder(Color.red));  // �������
	}
	
	/**
	 * ���������� ����� ������
	 * @return JPanel
	 */
	public JPanel getLeftSection( )
	{
		return leftPanel;
	}
	
	/**
	 * ���������� ������ ������
	 * @return JPanel
	 */
	public JPanel getRightSection( )
	{
		return rightPanel;		
	}
	
	/**
	 * ���������� ������
	 * @return JLabel
	 */
	public JLabel getAvatar( )
	{		
		return avatar;		
	}
	
	/**
	 * ���������� �������������� ������
	 * @return JLabel
	 */
	public JPanel getInfoSection( )
	{
		return infoSection;		
	}
	
	protected Contact getContactDetails( int id )
	{
		return new Contact( id );
	}
	
	/**
	 * ���������� ��������� ����
	 * @return HDialog
	 */
	public HDialog getDialog( )
	{		
		return dialog;		
	}
}
