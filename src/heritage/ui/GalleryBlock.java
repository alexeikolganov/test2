package heritage.ui;

import heritage.config.ApplicationColors;
import heritage.ui.modal.ReadOnlyModal;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public  class  GalleryBlock extends  JLabel
{
	private  static  final  long  serialVersionUID  = 2446183694107112884L;
	private  final  int  SMALL_WIDTH   		= 50;
	private  final  int  SMALL_HEIGHT  		= 50;
	private  final  int  BIG_WIDTH   		= 350;
	private  final  int  BIG_HEIGHT  		= 350;
	private  final  int  BORDER_THICKNESS  	= 2;

	static  String photoFolder  = "photos";
	public  GalleryBlock( String imagePath, final int  id, boolean  isSelected  )
	{
		super( );
	
		ImageIcon icon  =  new  ImageIcon( imagePath  );
		setIcon( getScaledIcon( icon, SMALL_WIDTH, SMALL_HEIGHT  ) );
		setSize( new  Dimension( SMALL_WIDTH, SMALL_HEIGHT  ) );
	        setBorder( BorderFactory.createLineBorder( ( isSelected  ) ? ApplicationColors.FIELD_BORDER_FOCUSED : ApplicationColors.FIELD_BORDER_UNFOCUSED, BORDER_THICKNESS  ) );  
	        setHorizontalAlignment( SwingConstants.CENTER  );
	        setOpaque( true  );
	        setBackground( ApplicationColors.FIELD_BACKGROUND_COLOR );
	        addMouseListener( new  MouseAdapter() 
	        {
	        	@Override
	        	public  void  mouseClicked( MouseEvent evt  )
	        	{
	        		GalleryBlock block  = (GalleryBlock) evt.getSource();
	        		JPanel parent  = (JPanel) block.getParent();
	        		for( Component item  : parent.getComponents() )
	        		{
	        			if( item  instanceof  GalleryBlock )
	        			{
	        				((GalleryBlock) item).setBorder( BorderFactory.createLineBorder( ApplicationColors.FIELD_BORDER_UNFOCUSED, BORDER_THICKNESS  ) );
	        			}
	        		}

	        		block.setBorder( BorderFactory.createLineBorder( ApplicationColors.FIELD_BORDER_FOCUSED, BORDER_THICKNESS  ) );
	        		ReadOnlyModal.setCurrentImage( id, ReadOnlyModal.PREVIOUS );
	        	}
	        });
	}

	public  GalleryBlock( String iconPath  )
	{
		super( );
		ImageIcon icon  =  new  ImageIcon( iconPath  );
		setIcon( getScaledIcon( icon, BIG_WIDTH, BIG_HEIGHT  ) );
		setSize( new  Dimension( BIG_WIDTH, BIG_HEIGHT  ) );
		        setBorder( BorderFactory.createLineBorder( ApplicationColors.FIELD_BORDER_UNFOCUSED, BORDER_THICKNESS  ) );  
		        setHorizontalAlignment( SwingConstants.CENTER  );
		        setOpaque( true  );
		        setBackground( ApplicationColors.FIELD_BACKGROUND_COLOR );
	}
	
	public  static  ImageIcon getScaledIcon( ImageIcon originalIcon, int  plannedWidth, int  plannedHeight  )
	{
		int  width  = plannedWidth;
		int  height  = plannedHeight;
		double  scale  = 1.0;
		
		Image originalImage  = originalIcon.getImage();
		if( originalIcon.getIconWidth() > originalIcon.getIconHeight() )
		{
			scale  = (double) plannedWidth  / originalIcon.getIconWidth(); 
		}
		else
		{
			scale  = (double) plannedHeight  / originalIcon.getIconHeight();
		}
		width   = (int) (originalIcon.getIconWidth() * scale);
		height  = (int) (originalIcon.getIconHeight() * scale);
		Image scaledImage  = originalImage.getScaledInstance( width, height, Image.SCALE_SMOOTH  );
		return  new  ImageIcon( scaledImage  );
	
	}
}
