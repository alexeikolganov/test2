package heritage.ui.modal;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.Timer;

import heritage.config.ApplicationColors;
import heritage.config.Config;
import heritage.controls.HDialog;
import heritage.ui.GalleryBlock;


public class ReadOnlyModal 
{			
	private final int MODAL_WIDTH  			= Integer.parseInt( Config.getItem( "modal_width" ) );
	private final int MODAL_HEIGHT 			= Integer.parseInt( Config.getItem( "modal_height" ) );
	private static Logger log 				= Logger.getLogger( ReadOnlyModal.class.getName() );

	private final static int BORDER_THICKNESS		= Integer.parseInt( Config.getItem( "border_thickness" ) );
	
	public static final int PREVIOUS = 0;
	private static final int NO_MOVE = 1;
	private static final int NEXT = 2;
	
	private JPanel panel;
	
	static GalleryBlock mainImage;
	static GalleryBlock leftImage;
	static GalleryBlock rightImage;
	
	static JButton prev;
	static JButton next;
	
	static String photoFolder  = "photos";
	static int folderId = 1;
	static int currentPhotoId = 1;
	static File[] listOfFiles;
	static JPanel leftPane;
	
	static Timer t;
	
	public ReadOnlyModal( String title )
	{
		HDialog dialog = new HDialog( new JFrame(), title, MODAL_WIDTH, MODAL_HEIGHT );	
		
		panel = (JPanel) dialog.getContentPane().getComponents()[1];
		
		//panel.add( buildMainSection() );
		//panel.add( buildLeftSection() );
		
		addComponentsToPane( ) ;
		
		dialog.setVisible( true );
	}
	
	public void addComponentsToPane( ) 
	{
		File path = new File( photoFolder  + "/"  + folderId  );
	    listOfFiles  = path.listFiles();

		//int  width  = 700;
		int  leftSectionWidth  = 130; 
		int  blockWidth  = 50;
		int  blockHeight  = 50;
		int  margin  = 10;
	
		JPanel rightPane  = new  JPanel( );
	    rightPane.setBounds( leftSectionWidth, 0, MODAL_WIDTH  - leftSectionWidth, MODAL_WIDTH  );
	    rightPane.setLayout( null  );
	    rightPane.setBackground( new  Color( 225, 200, 175 ) );
	    JLayeredPane mainImage  = buildMainImage( );
	    rightPane.add( mainImage  );
	
	    leftPane  = new  JPanel();
	    leftPane.setBounds( 0, 0, leftSectionWidth, MODAL_WIDTH  );
	    leftPane.setLayout( null  );
	    leftPane.setBackground( Color.green  );
	  
	    int  yPos  = margin;
	    for( int  i=0;i<listOfFiles.length;i++ )
	    {
	        int  xPos  = ( ( i  % 2) == 0 ) ? margin  : ( margin  + blockWidth  + margin  ); 
	        GalleryBlock test  = new  GalleryBlock( listOfFiles[i].toString(), i, (currentPhotoId==i) );
	        test.setLocation( xPos, yPos  );
	        leftPane.add( test  );
	        if( ( ( i  % 2) > 0 ) && ( i  > 0 ) )
	        {
	        	yPos  += margin  + blockHeight;
	        }
	    }

	    panel.add( leftPane  );
	    panel.add( rightPane  );
	}
	
	private  JLayeredPane buildMainImage( )
	{
	    JLayeredPane container  = new  JLayeredPane();
	    container.setLayout( null  );
	    container.setBounds( 10, 10, 350, 350 );
	    
	    JLayeredPane imagePane  = new  JLayeredPane( );
	    imagePane.setLayout( null  );
	    imagePane.setBounds( 0, 0, 350, 350 );
	    container.add( imagePane, 0, 0 );

	    mainImage = new GalleryBlock( listOfFiles[currentPhotoId].toString() );
	    mainImage.setLocation( 0, 0 );
	    imagePane.add( mainImage, 0, 0 );
	    
	    leftImage  = new  GalleryBlock( listOfFiles[currentPhotoId].toString() );
	    leftImage.setLocation( -350, 0 );
	    imagePane.add( leftImage, 1, 0 );
	    
	    rightImage  = new  GalleryBlock( listOfFiles[currentPhotoId].toString() );
	    rightImage.setLocation( 351, 0 );
	    imagePane.add( rightImage, 1, 0 );
	    
	    JPanel buttonPane  = new  JPanel( );
	    buttonPane.setLayout( null  );
	    buttonPane.setBounds( 0, 0, 350, 350 );
	    buttonPane.setOpaque( false  );
	    container.add( buttonPane, 5, 0 );
	    
	    prev  = new  JButton("<<");
	    prev.setBounds( 10, 10, 50, 50 );
	    prev.addActionListener( new  ActionListener()
	    {
		    public  void  actionPerformed( ActionEvent e  )
		    {
			    currentPhotoId--;
			    setCurrentImage( currentPhotoId, PREVIOUS  ); 
		    }
	    });
	    buttonPane.add( prev );

	    next  = new  JButton(">>");
	    next.setBounds( 290, 10, 50, 50 );
	    next.addActionListener( new  ActionListener()
	    {
		    public  void  actionPerformed( ActionEvent e  )
		    {
			    currentPhotoId++;
			    setCurrentImage( currentPhotoId, NEXT  );
		    }
	    });
	    buttonPane.add( next  );
	    
	    return  container;
	}
	
	private  static  boolean  existsPrevious( int  id  )
	{
	    return  ( id  > 0 ) ? true  : false;
	}

	private  static  boolean  existsNext( int  id  )
	{
	    return  ( id  < listOfFiles.length-1 ) ? true  : false;
	}
	
	public static  void  setCurrentImage( int  currentId, int  direction  )
	{
	    System.out.println( currentId  );
	    
	    if( mainImage  != null  )
	    {
	    	currentPhotoId  = currentId;
	    	if( direction  == NO_MOVE  )
	    	{
	    		mainImage.setIcon( GalleryBlock.getScaledIcon(new  ImageIcon( listOfFiles[currentPhotoId].toString() ), 350, 350 ) );
	    	}
	    	else if( direction  == PREVIOUS  )
	    	{
	    		leftImage.setIcon( GalleryBlock.getScaledIcon(new  ImageIcon( listOfFiles[currentPhotoId].toString() ), 350, 350 ) );
	    		runTimer( leftImage, direction  );
		    }
		    else
		    {
			    rightImage.setIcon( GalleryBlock.getScaledIcon(new  ImageIcon( listOfFiles[currentPhotoId].toString() ), 350, 350 ) );
			    runTimer( rightImage, direction  );
		    }
			prev.setVisible( existsPrevious( currentPhotoId  ) );
			next.setVisible( existsNext( currentPhotoId  ) );

			for( Component item  : leftPane.getComponents() )
			{
				if( item  instanceof  GalleryBlock )
				{
					((GalleryBlock) item).setBorder( BorderFactory.createLineBorder( ApplicationColors.FIELD_BORDER_UNFOCUSED, BORDER_THICKNESS  ) );
				}
			}

			((GalleryBlock) leftPane.getComponent( currentPhotoId  )).setBorder( BorderFactory.createLineBorder( ApplicationColors.FIELD_BORDER_FOCUSED, BORDER_THICKNESS  ) );    
	    }
	}
	
	private  static  void  runTimer( final JLabel l, final int  direction  )
	{
	    t  = new  Timer( 1, new  ActionListener( )
	    {
		    int  x  = ( direction  == PREVIOUS  ) ? -350 : 350;
		    int  endPoint  = x;
		    public  void  actionPerformed( ActionEvent ae  )
		    {
		    	x  = ( direction  == PREVIOUS  ) ? (x  + 2) : (x  - 2);
		    	l.setLocation( x, 0 );
		    	if( x  == 0 ) 
		    	{ 
		    		mainImage.setIcon( l.getIcon() );
		    		l.setLocation( endPoint, 0 );
		    		t.stop();
		    	}
		    }
	    });
	    t.setInitialDelay(200);
	    t.start();
	}
}

