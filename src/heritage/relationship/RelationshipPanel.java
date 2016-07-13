package heritage.relationship;

import heritage.config.ApplicationColors;
import heritage.config.Config;
import heritage.contact.Contact;
import heritage.contact.ContactRelationship;
import heritage.controls.HScrollBar;
import heritage.ui.block.AddContactBlock;
import heritage.ui.block.Block;

import java.awt.Color;
import java.awt.Dimension;
import java.io.IOException;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;

public class RelationshipPanel 
{
	private static int selectedContactId = Relation.getPrimaryContact();
	
	private static int WIDTH  = 1400;
	private static int HEIGHT = 800;
	private static final int BLOCK_WIDTH  			= Block.BLOCK_WIDTH;
	private static final int BLOCK_MAX_HEIGHT 		= Block.BLOCK_MAX_HEIGHT;
	
	private static final int MARGIN		 			= Integer.parseInt( Config.getItem( "block_margin" ) );
	public final static int BORDER_WIDTH = 2;
	
	public final static int Y_START = BLOCK_MAX_HEIGHT * 2 + MARGIN * 3;//HEIGHT / 2;
	
	private static int N0 = -1;

	private static JPanel innerPanel;
	private static JPanel mainPanel;
	private static JScrollPane scrollFrame;
	
	private static int rightMostX  = 0;
	private static int bottomMostY = Y_START + BLOCK_MAX_HEIGHT + MARGIN;
	
	private static final double COMPRESSION_RATIO = 1;
	
	public static JPanel createPanel( int width, int height ) throws IOException 
	{
		WIDTH  = width-4;
		HEIGHT = height;
		
		mainPanel = new JPanel();
		mainPanel.setPreferredSize( new Dimension( WIDTH, HEIGHT ) );
		//mainPanel.setLayout( null );
		
		innerPanel = new JPanel( );
		innerPanel.setBackground( ApplicationColors.LINKED_PANEL_BACKGROUND_COLOR );
        innerPanel.setLayout( null );

        scrollFrame = new JScrollPane( innerPanel );
        scrollFrame.setBorder( null );
        scrollFrame.setPreferredSize( new Dimension( WIDTH, HEIGHT ) );
        scrollFrame.setVerticalScrollBar( new HScrollBar( JScrollBar.VERTICAL ) );
        scrollFrame.setHorizontalScrollBar( new HScrollBar( JScrollBar.HORIZONTAL ) );
        scrollFrame.setBounds( 0, 0, WIDTH, HEIGHT );
        mainPanel.add( scrollFrame );

        drawTree();   
        return mainPanel;
	}
	
	public static void drawTree( )
	{
		resetPanel();
		
		if( selectedContactId > 0 )
		{
			// 1. Contact
	        Contact contact = Relation.getContact( selectedContactId );
	        contact.setCoordinates( getNextX( 0 ), Y_START );
	        drawContact( contact );
	        
	        drawParent( contact, 0, "M" );
	        drawParent( contact, 0, "F" );
	        
	        // 2. Spouse(s)
	        List<Contact> spouses = Relation.getSpouses( selectedContactId ); 
	        if( spouses != null )
	        {
	     	    for( int i=0;i<spouses.size();i++ )
	     	    {     	    	
		     	    int x = getNextX( 0 );
		     	    rightMostX = ( x > rightMostX ) ? x : rightMostX;
	     	    	spouses.get(i).setCoordinates( x, Y_START );
		 	        drawContact( spouses.get(i) );
		 	        
		 	        linkContacts( ( i==0 ) ? contact : spouses.get( i-1 ), spouses.get(i) );
		 	        
		 	        drawParent( spouses.get(i), 0, "M" );
		 	        drawParent( spouses.get(i), 0, "F" );
		 	        
		 	        drawCommonChildren( ( i==0 ) ? contact : spouses.get( i-1 ), spouses.get(i) );
	     	    } 
	        }
	        int x = rightMostX + BLOCK_WIDTH + MARGIN;
	        rightMostX = x;
	        
	        // "add spouse" widget
	        Contact spouse = new Contact( "Add Spouse", !contact.isMasculine() );
	        spouse.setCoordinates( x, Y_START );
	        
	        ContactRelationship[] reln = new ContactRelationship[1];
	        reln[0] = new ContactRelationship( selectedContactId, spouse.id, 0 ); 
		    drawContact( spouse, reln );
		    
		    linkContacts( ( spouses == null || spouses.size() == 0 ) ? contact : spouses.get(spouses.size()-1), spouse );
		    
		    scrollFrame.setViewportView(innerPanel);	    
		    innerPanel.setPreferredSize( new Dimension( rightMostX + BLOCK_WIDTH + MARGIN, bottomMostY ) );
		}
		else
		{
			Contact contact = new Contact( "Add Contact", true );
			contact.setCoordinates( getNextX( 0 ), Y_START );
	        drawContact( contact );
		}
	}
	
	private static int getNextX( int level )
	{
		//return (int) ( ( ( BLOCK_WIDTH + MARGIN ) * 2 + ( BLOCK_WIDTH + MARGIN ) * 6 * ++N0 ) * COMPRESSION_RATIO + MARGIN );	
		return (int) ( ( ( BLOCK_WIDTH + MARGIN ) * 1.5 + ( BLOCK_WIDTH + MARGIN ) * 4 * ++N0 ) * COMPRESSION_RATIO + MARGIN );
	}
	
	private static void drawParent( Contact contact, int level, String parentGender )
	{
		// offset between levels
		// levels = 2: 0=1, -1=0.5
		// levels = 3: 0=2, -1=1, -2=0.5
		// levels = 4: 0=4, -1=2, -2=1, -3=0.5
		// ...
		double[] offsets = { 1, 0.5 };
		
		if( level >= -1 )
		{
			Contact parent;
			ContactRelationship[] reln = new ContactRelationship[1];
			double x = 0;
			
			String contraGender = parentGender.equals( "M" ) ? "F" : "M";
			if( parentGender.equals( "M" ) )
			{
				parent = Relation.getFather( contact.id );
				if( parent == null )
				{
					parent = new Contact( "Add Father", true );
					reln[0] = new ContactRelationship( contact.id, parent.id, -1 );
				}
				x = contact.x - ( BLOCK_WIDTH + MARGIN ) * offsets[Math.abs(level)] * COMPRESSION_RATIO;
				rightMostX = ( x > rightMostX ) ? (int)x : rightMostX;
			}
			else
			{
				parent = Relation.getMother( contact.id );
				if( parent == null )
				{
					parent = new Contact( "Add Mother", false );
					reln[0] = new ContactRelationship( contact.id, parent.id, -1 );
				}
				x = contact.x + ( BLOCK_WIDTH + MARGIN ) * offsets[Math.abs(level)] * COMPRESSION_RATIO;	
				rightMostX = ( x > rightMostX ) ? (int)x : rightMostX;
			}
			if( parent != null )
			{
				parent.setCoordinates( x, contact.y - BLOCK_MAX_HEIGHT - MARGIN  );
				//System.out.println( parent.x + ", " + parent.y );
			    drawContact( parent, reln );
			    linkContacts( contact, parent );
			    
			    if( parent.id != -1 )
			    {
			    	drawParent( parent, level - 1, parentGender );
			    	drawParent( parent, level - 1, contraGender );
			    }
			}
		}
	}
		
	private static void drawContact( Contact contact, ContactRelationship[] reln )
	{
		if( contact.id > 0 )
		{
			innerPanel.add( new Block( contact, false ) );
		}
		else
		{	
			innerPanel.add( new AddContactBlock( contact, reln ) );
		}
	}
	
	private static void drawContact( Contact contact )
	{
		if( contact.id > 0 )
		{
			innerPanel.add( new Block( contact, ( contact.id == selectedContactId ) ) );
		}
		else
		{	
			innerPanel.add( new AddContactBlock( contact, null ) );
		}
	}
	
	private static void linkContacts( Contact c1, Contact c2 )
	{
		if( c1.x > c2.x ) // c1 is to the right of c2
		{
			if( c1.y > c2.y ) // c1 is below c2
			{
				//System.out.println("c2 bottom-left-c1 top");
				drawLine( c2.lineX, c2.bottomLineY1, c2.lineX, c2.bottomLineY2 );
				drawLine( c2.lineX, c2.bottomLineY2, c1.lineX, c1.topLineY1 );				
				drawLine( c1.lineX, c1.topLineY1, c1.lineX, c1.topLineY2 );
			}
			else if( c1.y == c2.y ) // c1 and c2 are on the same level
			{
				//System.out.println("left-right");
				drawLine( c2.rightLineX1, c2.lineY, c1.leftLineX2, c1.lineY );
			}
			else // c1 is above c2
			{
				//System.out.println("bottom-left");
			}		
		}
		else if( c1.x < c2.x )
		{
			if( c1.y > c2.y ) // c1 is below c2
			{
				//System.out.println("c1 top-right-c2 bottom");
				drawLine( c2.lineX, c2.bottomLineY1, c2.lineX, c2.bottomLineY2 );
				drawLine( c1.lineX, c2.bottomLineY2, c2.lineX, c1.topLineY1 );
				drawLine( c1.lineX, c1.topLineY1, c1.lineX, c1.topLineY2 );
			}
			else if( c1.y == c2.y ) // c1 and c2 are on the same level
			{
				//System.out.println("c1 right-c2 left");
				drawLine( c1.rightLineX1, c1.lineY, c2.leftLineX2, c2.lineY );
			}
			else // c1 is above c2
			{
				//System.out.println("bottom-left");
			}	
		}
	}
	
	private static void drawLine( double x1, double y1, double x2, double y2 )
	{
		JLabel lbl = new JLabel( );
		lbl.setBackground( Color.red );
		lbl.setOpaque( true );
		lbl.setBounds( (int)x1, (int)y1, (int) (x2-x1+BORDER_WIDTH), (int) (y2-y1+BORDER_WIDTH) );
		//System.out.println( lbl.getBounds());
		innerPanel.add( lbl );
	}

	private static void drawCommonChildren( Contact contact, Contact spouse )
	{
		List<Contact> children = Relation.getCommonChildren( contact.id, spouse.id );
		Contact addChild = new Contact( -1, "Add Child" );
		children.add( addChild );
				
		int maxPerRow 	= 3;
		int rows 		= children.size() / maxPerRow; // children rows may have max 6 contacts
		int remainingContacts = children.size() % maxPerRow; // remaining contacts
		double middleX = contact.x + BLOCK_WIDTH + MARGIN + ( spouse.leftLineX1 - contact.rightLineX2 ) / 2; // middle point between spouses
				
		double start =  middleX - ( BLOCK_WIDTH + MARGIN ) * ( maxPerRow ) / 2 + MARGIN / 2;
		int y = Y_START + BLOCK_MAX_HEIGHT + MARGIN ;
		
		for( int i=0;i<rows * maxPerRow;i++ )
		{	
			children.get(i).setCoordinates( start, y );

			if( children.get(i).id > 0 )
			{
				drawContact( children.get(i) );  
			}
			else
			{
				// "add contact" widget
				ContactRelationship[] reln = new ContactRelationship[2];
				reln[0] = new ContactRelationship( contact.id, children.get(i).id, 1 );
				reln[1] = new ContactRelationship( spouse.id, children.get(i).id, 1 );
				drawContact( children.get(i), reln );  
			}
		    
		    drawLine( middleX, contact.lineY, middleX, y - MARGIN / 2 );
		    drawLine( children.get(i).lineX, children.get(i).topLineY1, children.get(i).lineX, children.get(i).topLineY2 );
		    if( middleX > children.get(i).lineX )
		    {
		    	drawLine( children.get(i).lineX, children.get(i).topLineY1, middleX, children.get(i).topLineY1 );  
		    }
		    else
		    {
		    	drawLine( middleX, children.get(i).topLineY1, children.get(i).lineX, children.get(i).topLineY1 );
		    }
		    
		    start += MARGIN + BLOCK_WIDTH;
			if( i != 0 && i % maxPerRow == maxPerRow-1 )
			{			
				y += BLOCK_MAX_HEIGHT + MARGIN;
				start = middleX - ( BLOCK_WIDTH + MARGIN ) * ( maxPerRow ) / 2 + MARGIN / 2;
			}
		    
		}
		if( remainingContacts > 0 )
		{	
			start = middleX - ( BLOCK_WIDTH + MARGIN ) * ( remainingContacts ) / 2 + MARGIN / 2;
			
			for( int i=rows*maxPerRow;i<children.size();i++ )
			{
				children.get(i).setCoordinates( start, y );
			   
				if( children.get(i).id > 0 )
				{
					drawContact( children.get(i) );  
				}
				else
				{
					// "add contact" widget
					ContactRelationship[] reln = new ContactRelationship[2];
					reln[0] = new ContactRelationship( contact.id, children.get(i).id, 1 );
					reln[1] = new ContactRelationship( spouse.id, children.get(i).id, 1 );
					drawContact( children.get(i), reln );  
				}
			    
				drawLine( middleX, contact.lineY, middleX, y - MARGIN / 2 );
			    drawLine( children.get(i).lineX, children.get(i).topLineY1, children.get(i).lineX, children.get(i).topLineY2 );
			    if( middleX > children.get(i).lineX )
			    {
			    	drawLine( children.get(i).lineX, children.get(i).topLineY1, middleX, children.get(i).topLineY1 );  
			    }
			    else
			    {
			    	drawLine( middleX, children.get(i).topLineY1, children.get(i).lineX, children.get(i).topLineY1 );
			    }
				 
			   // drawLine( children.get(i), panel, "top" );
			    
			    start += MARGIN + BLOCK_WIDTH;
			}
		}
		bottomMostY = ( y > bottomMostY ) ? ( y + MARGIN + BLOCK_MAX_HEIGHT ) : bottomMostY;		
	}
	
	public static void setSelectedContactId( int contactId )
	{
		selectedContactId = contactId;
	}
	
	public static int getSelectedContact( )
	{
		return selectedContactId;
	}
	
	public static void resetPanel()
	{
		rightMostX  = 0;
		bottomMostY = Y_START + BLOCK_MAX_HEIGHT + MARGIN;
		N0 			= -1;		
		innerPanel.removeAll();
		innerPanel.revalidate();
	    innerPanel.repaint();
	}
}