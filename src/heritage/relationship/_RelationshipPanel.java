package heritage.relationship;

import java.awt.Dimension;
import java.util.List;

import heritage.config.Config;
import heritage.contact.Contact;
import heritage.ui.block.AddContactBlock;
import heritage.ui.block.Block;

public class _RelationshipPanel 
{
	private static int WIDTH  				= 1400;
	private static int HEIGHT 				= 800;
	private static final int BLOCK_WIDTH  	= Integer.parseInt( Config.getItem( "block_width" ) );
	private static final int BLOCK_HEIGHT 	= Integer.parseInt( Config.getItem( "block_height" ) );
	private static final int MARGIN		 	= Integer.parseInt( Config.getItem( "block_margin" ) );
	
	public static int X_START = WIDTH / 2;
	public final static int Y_START = BLOCK_HEIGHT * 3 + MARGIN * 2;//HEIGHT / 2;
	
	/**
	 * Создаение панели, на которой размещены контакты и связи между ними
	 * 
	 * __________											         __________
	 * | x3, y4 |											         | x6, y4 |
	 * ----------  __________						     __________  ----------				
	 * 			   | x2, y2 |						     | x5, y2 |				  
	 * __________  ----------                            ----------  __________  
	 * | x3, y5 |                                                    | x6, y5 |
	 * ----------              __________    __________              ----------
	 *                         | x1, y1 |    | x4, y1 |
	 * __________              ----------    ----------              __________
	 * | x3, y6 |                                                    | x6, y6 |
	 * ----------  __________                            __________  ----------
	 *             | x2, y3 |                            | x5, y3 |
	 * __________  ----------                            ----------  __________
	 * | x3, y7 |                                                    | x6, y7 |
	 * ----------                                                    ----------
	 * @param width - ширина панели
	 * @param height - высота палени
	 * @return
	 */
	public static DPanel createPanel( int width, int height )
	{
		WIDTH 	= width;
		HEIGHT 	= height;	
		X_START = WIDTH / 2;
		
		DPanel mainPanel = new DPanel();
	    mainPanel.setPreferredSize( new Dimension( WIDTH, HEIGHT ) );
	    mainPanel.setSize( new Dimension( WIDTH, HEIGHT ) );
	    mainPanel.setLayout( null );

	    int selectedContact = 1;
	       
	    double x1 = X_START - MARGIN / 2 - BLOCK_WIDTH;
	    double x2 = X_START - MARGIN * 1.5 - BLOCK_WIDTH * 2;
	    double x3 = X_START - MARGIN * 2.5 - BLOCK_WIDTH * 3;
	    double x4 = X_START + MARGIN / 2;
	    double x5 = X_START + MARGIN * 1.5 + BLOCK_WIDTH;
	    double x6 = X_START + MARGIN * 2.5 + BLOCK_WIDTH * 2;
       
	    double y1 = Y_START - BLOCK_HEIGHT * 0.5;
        double y2 = Y_START - BLOCK_HEIGHT * 2 - MARGIN;// * 2;
        double y3 = Y_START + BLOCK_HEIGHT + MARGIN;// * 2;
        double y4 = Y_START - BLOCK_HEIGHT * 3 - MARGIN;// * 3;
        double y5 = Y_START - BLOCK_HEIGHT - MARGIN;
        double y6 = Y_START + MARGIN;
        double y7 = Y_START + BLOCK_HEIGHT * 2 + MARGIN;// * 3;
         
        // contact
        Contact contact = Relation.getContact( selectedContact );
        contact.setCoordinates( x1, y1 );
        drawContact( contact, mainPanel );
       
        // father
        Contact father = Relation.getFather(selectedContact); 
        if( father != null )
        {
        	father.status = Config.getLookup( 2 );
        	
        	drawLine( contact, mainPanel, "left" ); // contact needs a left line to link with father
    	   
    	    father.setCoordinates( x2, y2 );
	        drawContact( father, mainPanel );  
	        drawLine( father, mainPanel, "right" );
	        linkContacts( contact, father, mainPanel, "left" );
	            
	        // father's parents
	        Contact fatherFather = Relation.getFather(father.id); 
	        if( fatherFather != null )
	        {
	        	fatherFather.status = Config.getLookup( 7 );
	        	
	        	drawLine( father, mainPanel, "left" ); // father needs a left line to link with his own father
	    	   
	    	    fatherFather.setCoordinates( x3, y4 );
		        drawContact( fatherFather, mainPanel );
		        drawLine( fatherFather, mainPanel, "right" );
		        linkContacts( father, fatherFather, mainPanel, "left" );
	        }
	        else
	        {
	        	drawLine( father, mainPanel, "left" ); // father needs a left line to link with his own father
	        	Contact addFatherFather = new Contact( Config.getItem( "add_father" ), father.lastName, true ); 	
	        	addFatherFather.setCoordinates( x3, y4 );
		        drawContact( addFatherFather, mainPanel );  
		        drawLine( addFatherFather, mainPanel, "right" );
		        linkContacts( father, addFatherFather, mainPanel, "left" );
	        }
	        Contact fatherMother = Relation.getMother(father.id);
	        if( fatherMother != null )
	        {   
	        	fatherMother.status = Config.getLookup( 8 );
	        	
	    	    drawLine( father, mainPanel, "left" ); // father needs a left line to link with his own mother
	    	   
	    	    fatherMother.setCoordinates( x3, y5 );
		        drawContact( fatherMother, mainPanel ); 
		        drawLine( fatherMother, mainPanel, "right" );
		        linkContacts( father, fatherMother, mainPanel, "left" );
	        }
	        else
	        {
	        	drawLine( father, mainPanel, "left" ); // father needs a left line to link with his own mother
	        	Contact addFatherMother = new Contact( Config.getItem( "add_mother" ), father.lastName, false );
	        	addFatherMother.setCoordinates( x3, y5 );
		        drawContact( addFatherMother, mainPanel );  
		        drawLine( addFatherMother, mainPanel, "right" );
		        linkContacts( father, addFatherMother, mainPanel, "left" );
	        }
        }
        else
        {
        	drawLine( contact, mainPanel, "left" ); // contact needs a left line to link with father
        	Contact addFather = new Contact( Config.getItem( "add_father" ), contact.lastName, true );
        	addFather.setCoordinates( x2, y2 );
	        drawContact( addFather, mainPanel );  
	        drawLine( addFather, mainPanel, "right" );
	        linkContacts( contact, addFather, mainPanel, "left" );
        }
       
       // mother 
       Contact mother = Relation.getMother(selectedContact); 
       if( mother != null )
       {
    	   mother.status = Config.getLookup( 1 );
    	   
    	   drawLine( contact, mainPanel, "left" ); // contact needs a left line to link with father

	       mother.setCoordinates( x2, y3 );
	       drawContact( mother, mainPanel );  
	       drawLine( mother, mainPanel, "right" );
	       linkContacts( contact, mother, mainPanel, "left" );
	       
	       // mother's parents
	       Contact motherFather = Relation.getFather(mother.id); 
	       if( motherFather != null )
	       {
	    	   motherFather.status = Config.getLookup( 7 );
	    	   
	    	   drawLine( mother, mainPanel, "left" ); // mother needs a left line to link with her father
	    	   
	    	   motherFather.setCoordinates( x3, y6 );
		       drawContact( motherFather, mainPanel );
		       drawLine( motherFather, mainPanel, "right" );
		       linkContacts( mother, motherFather, mainPanel, "left" );
	       }
	       else
	       {
	    	    drawLine( mother, mainPanel, "left" ); // mother needs a left line to link with her father
	    	    Contact addMotherFather = new Contact( Config.getItem( "add_father" ), mother.lastName, true );
	       		addMotherFather.setCoordinates( x3, y6 );
		        drawContact( addMotherFather, mainPanel );  
		        drawLine( addMotherFather, mainPanel, "right" );
		        linkContacts( mother, addMotherFather, mainPanel, "left" );
	       }
	       
	       Contact motherMother = Relation.getMother(mother.id); 
	       if( motherMother != null )
	       {
	    	   motherMother.status = Config.getLookup( 8 );
	    	   
	    	   drawLine( mother, mainPanel, "left" ); // mother needs a left line to link with her mother
	    	   
	    	   motherMother.setCoordinates( x3, y7 );
		       drawContact( motherMother, mainPanel ); 
		       drawLine( motherMother, mainPanel, "right" );
		       linkContacts( mother, motherMother, mainPanel, "left" );
	       }
	       else
	       {
	    	    drawLine( mother, mainPanel, "left" ); // mother needs a left line to link with her mother
	    	    Contact addMotherMother = new Contact( Config.getItem( "add_mother" ), mother.lastName, false );
	       		addMotherMother.setCoordinates( x3, y7 );
		        drawContact( addMotherMother, mainPanel );  
		        drawLine( addMotherMother, mainPanel, "right" );
		        linkContacts( mother, addMotherMother, mainPanel, "left" );
	       }
       }
       else
       {
       		Contact addMother = new Contact( Config.getItem( "add_mother" ), contact.lastName, false );
       		addMother.setCoordinates( x2, y3 );
	        drawContact( addMother, mainPanel );  
	        drawLine( addMother, mainPanel, "right" );
	        linkContacts( contact, addMother, mainPanel, "left" );
       }
       
       // spouse
       Contact spouse = Relation.getSpouse(selectedContact); 
       if( spouse != null )
       {
    	   spouse.status = Config.getLookup( contact.isMasculine() ? 11 : 3 );
    	   
    	   drawLine( contact, mainPanel, "right" ); // contact needs a right line to link with the spouse
    	   spouse.setCoordinates( x4, y1 );
	       drawContact( spouse, mainPanel );
	       drawLine( spouse, mainPanel, "left" );
	       	       
	       // spouse father
	       Contact spouseFather = Relation.getFather(spouse.id); 
	       if( spouseFather != null )
	       {
	    	   spouseFather.status = Config.getLookup( 2 );
	    	   
	    	   drawLine( spouse, mainPanel, "right" ); // spouse needs a right line to link with the father
	    	   
	    	   spouseFather.setCoordinates( x5, y2 );
		       drawContact( spouseFather, mainPanel );
		       drawLine( spouseFather, mainPanel, "left" );
		       linkContacts( spouse, spouseFather, mainPanel, "right" );
		       	              
		       // spouse's father parents
		       Contact spouseFatherFather = Relation.getFather(spouseFather.id); 
		       if( spouseFatherFather != null )
		       {
		    	   spouseFatherFather.status = Config.getLookup( 7 );
		    	   
		    	   drawLine( spouseFather, mainPanel, "right" ); // spouse father needs a right line to link with the father
		    	   
		    	   spouseFatherFather.setCoordinates( x6, y4 );
			       drawContact( spouseFatherFather, mainPanel );   
			       drawLine( spouseFatherFather, mainPanel, "left" );
			       linkContacts( spouseFather, spouseFatherFather, mainPanel, "right" );
		       }
		       else
		       {
		        	drawLine( spouseFather, mainPanel, "right" ); // father needs a left line to link with his own mother
		        	Contact addSpouseFatherFather = new Contact( Config.getItem( "add_father" ), spouseFather.lastName, true );
		        	addSpouseFatherFather.setCoordinates( x6, y4 );
				    drawContact( addSpouseFatherFather, mainPanel );
				    drawLine( addSpouseFatherFather, mainPanel, "left" );
				    linkContacts( spouseFather, addSpouseFatherFather, mainPanel, "right" );
		       }
		       
		       Contact spouseFatherMother = Relation.getMother(spouseFather.id); 
		       if( spouseFatherMother != null )
		       {
		    	   spouseFatherMother.status = Config.getLookup( 8 );
		    	   
		    	   drawLine( spouseFather, mainPanel, "right" ); // spouse father needs a right line to link with the mother
		    	  
			       spouseFatherMother.setCoordinates( x6, y5 );
			       drawContact( spouseFatherMother, mainPanel ); 
			       drawLine( spouseFatherMother, mainPanel, "left" );
			       linkContacts( spouseFather, spouseFatherMother, mainPanel, "right" );
		       }
		       else
		       {
		        	drawLine( spouseFather, mainPanel, "right" ); // father needs a left line to link with his own mother
		        	Contact addSpouseFatherMother = new Contact( Config.getItem( "add_mother" ), spouseFather.lastName, false );
		        	addSpouseFatherMother.setCoordinates( x6, y5 );
				    drawContact( addSpouseFatherMother, mainPanel );
				    drawLine( addSpouseFatherMother, mainPanel, "left" );
				    linkContacts( spouseFather, addSpouseFatherMother, mainPanel, "right" );
		       }
	       }
	       else
	       {
	        	drawLine( spouse, mainPanel, "right" ); // father needs a left line to link with his own mother
	        	Contact addSpouseFather = new Contact( Config.getItem( "add_father" ), spouse.lastName, true );
	        	addSpouseFather.setCoordinates( x5, y2 );
			    drawContact( addSpouseFather, mainPanel );
			    drawLine( addSpouseFather, mainPanel, "left" );
			    linkContacts( spouse, addSpouseFather, mainPanel, "right" );
	       }
	       	       
	       // spouse mother
	       Contact spouseMother = Relation.getMother(spouse.id); 
	       if( spouseMother != null )
	       {
	    	   spouseMother.status = Config.getLookup( 1 );
	    	   
	    	   drawLine( spouse, mainPanel, "right" ); // spouse needs a right line to link with the father
	    	 
		       spouseMother.setCoordinates( x5, y3 );
		       drawContact( spouseMother, mainPanel ); 
		       drawLine( spouseMother, mainPanel, "left" );
		       linkContacts( spouse, spouseMother, mainPanel, "right" );
		       
		       // spouse's mother parents
		       Contact spouseMotherFather = Relation.getFather(spouseMother.id);
		       if( spouseMotherFather != null )
		       {
		    	   spouseMotherFather.status = Config.getLookup( 7 );
		    	   
		    	   drawLine( spouseMother, mainPanel, "right" ); // spouse mother needs a right line to link with the father
		    	 
			       spouseMotherFather.setCoordinates( x6, y6 );
			       drawContact( spouseMotherFather, mainPanel );   
			       drawLine( spouseMotherFather, mainPanel, "left" );
			       linkContacts( spouseMother, spouseMotherFather, mainPanel, "right" );
		       }
		       else
		       {
		        	drawLine( spouseMother, mainPanel, "right" ); // father needs a left line to link with his own mother
		        	Contact addSpouseMotherFather = new Contact( Config.getItem( "add_father" ), spouseMother.lastName, true );
		        	addSpouseMotherFather.setCoordinates( x6, y6 );
				    drawContact( addSpouseMotherFather, mainPanel );
				    drawLine( addSpouseMotherFather, mainPanel, "left" );
				    linkContacts( spouseMother, addSpouseMotherFather, mainPanel, "right" );
		       }
		       
		       Contact spouseMotherMother = Relation.getMother(spouseMother.id); 
		       if( spouseMotherMother != null )
		       {
		    	   spouseMotherMother.status = Config.getLookup( 8 );
		    	   
		    	   drawLine( spouseMother, mainPanel, "right" ); // spouse mother needs a right line to link with the mother
		    	 
		    	   spouseMotherMother.setCoordinates( x6, y7 );
			       drawContact( spouseMotherMother, mainPanel );   
			       drawLine( spouseMotherMother, mainPanel, "left" );
			       linkContacts( spouseMother, spouseMotherMother, mainPanel, "right" );
		       }
		       else
		       {
		        	drawLine( spouseMother, mainPanel, "right" ); // father needs a left line to link with his own mother
		        	Contact addSpouseMotherMother = new Contact( Config.getItem( "add_mother" ), spouseMother.lastName, false );
		        	addSpouseMotherMother.setCoordinates( x6, y7 );
				    drawContact( addSpouseMotherMother, mainPanel );
				    drawLine( addSpouseMotherMother, mainPanel, "left" );
				    linkContacts( spouseMother, addSpouseMotherMother, mainPanel, "right" );
		       }
	       }
	       else
	       {
	        	drawLine( spouse, mainPanel, "right" ); // father needs a left line to link with his own mother
	        	Contact addSpouseMother = new Contact( Config.getItem( "add_mother" ), spouse.lastName, false );
	        	addSpouseMother.setCoordinates( x5, y3 );
			    drawContact( addSpouseMother, mainPanel );
			    drawLine( addSpouseMother, mainPanel, "left" );
			    linkContacts( spouse, addSpouseMother, mainPanel, "right" );
	       }
	       drawCommonChildren( contact, mainPanel );
       }
       else
       {
    	    drawLine( contact, mainPanel, "right" ); // contact needs a right line to link with the spouse
    	    Contact addSpouse = new Contact( Config.getItem( "add_spouse" ), "", true );
       		drawLine( addSpouse, mainPanel, "right" ); // contact needs a right line to link with the spouse
       		addSpouse.setCoordinates( x4, y1 );
 	        drawContact( addSpouse, mainPanel );
 	        drawLine( addSpouse, mainPanel, "left" );
       }
       return mainPanel;
	}
		
	private static void drawCommonChildren( Contact contact, DPanel panel )
	{
		List<Contact> children = Relation.getCommonChildren( contact.id );
		children.add( new Contact( Config.getItem( "add_child" ), "", true ) );
		int maxPerRow = 6;
		int rows = children.size() / maxPerRow; // children rows may have max 6 contacts
		int remainingContacts = children.size() % maxPerRow; // remaining contacts
				
		double marginStart 		= -( maxPerRow / 2 - 0.5 ) * MARGIN;
		double blockWidthStart  = -Math.floor( ( maxPerRow / 2 ) ) * BLOCK_WIDTH;
		int y = Y_START + BLOCK_HEIGHT * 3 + MARGIN * 2;

		for( int i=0;i<rows * maxPerRow;i++ )
		{	
			children.get(i).status = Config.getLookup( children.get(i).isMasculine() ? 5 : 6 );
			children.get(i).setCoordinates( WIDTH /2 + marginStart + blockWidthStart, y );
		    drawContact( children.get(i), panel );   
		    drawLine( children.get(i), panel, "top" );
		    
		    marginStart += MARGIN;
		    blockWidthStart += BLOCK_WIDTH;
			if( i != 0 && i % maxPerRow == maxPerRow-1 )
			{			
				marginStart = -( maxPerRow / 2 - 0.5 ) * MARGIN;
				blockWidthStart = -Math.floor( ( maxPerRow / 2 ) ) * BLOCK_WIDTH;
				panel.addLine( contact.rightLineX2, contact.lineY, contact.rightLineX2, y - MARGIN / 2 );
				panel.addLine( children.get(i-maxPerRow+1).lineX, children.get(i-maxPerRow+1).topLineY1, children.get(i).lineX, children.get(i).topLineY1 );
				y += BLOCK_HEIGHT + MARGIN;
			}  
		}
		if( remainingContacts != 0 )
		{
			double marginStartOdd 		= -Math.floor( ( remainingContacts / 2 ) ) * MARGIN;
			double blockWidthStartOdd 	= -( remainingContacts / 2 + 0.5 ) * BLOCK_WIDTH;
			double marginStartEven 		= -( remainingContacts / 2 - 0.5 ) * MARGIN;
			double blockWidthStartEven 	= -Math.floor( ( remainingContacts / 2 ) ) * BLOCK_WIDTH;
			if( remainingContacts % 2 == 0 )
			{
				marginStart 	= marginStartEven;
				blockWidthStart = blockWidthStartEven;
			}
			else
			{
				marginStart 	= marginStartOdd;
				blockWidthStart = blockWidthStartOdd;
			}
	
			for( int i=rows*maxPerRow;i<children.size();i++ )
			{
				children.get(i).status = Config.getLookup( children.get(i).isMasculine() ? 5 : 6 );
				children.get(i).setCoordinates( X_START + marginStart + blockWidthStart, y );
			    drawContact( children.get(i), panel );   
			    drawLine( children.get(i), panel, "top" );
			    
			    marginStart += MARGIN;
			    blockWidthStart += BLOCK_WIDTH;
			}
	
			panel.addLine( contact.rightLineX2, contact.lineY, contact.rightLineX2, y - MARGIN / 2 );
			panel.addLine( children.get(rows*maxPerRow).lineX, children.get(rows*maxPerRow).topLineY1, children.get(children.size()-1).lineX, children.get(children.size()-1).topLineY1 );
		}
	}
	
	private static void drawContact( Contact contact, DPanel panel )
	{
		if( contact.id > 0 )
		{
			panel.add( new Block( contact, false ) );
		}
		else
		{	
			panel.add( new AddContactBlock( contact ) );
		}
	}
	
	private static void drawLine( Contact contact, DPanel panel, String direction )
	{
		if( direction.equals( "left" ) )
		{
			panel.addLine( contact.leftLineX1, contact.lineY, contact.leftLineX2, contact.lineY );
		}
		else if( direction.equals( "right" ) )
		{
			panel.addLine( contact.rightLineX1, contact.lineY, contact.rightLineX2, contact.lineY );
		}
		else if( direction.equals( "top" ) )
		{
			panel.addLine( contact.lineX, contact.topLineY1, contact.lineX, contact.topLineY2 );
		}
		else if( direction.equals( "bottom" ) )
		{
			panel.addLine( contact.lineX, contact.bottomLineY1, contact.lineX, contact.bottomLineY2 );
		}
	}
	
	private static void linkContacts( Contact contact1, Contact contact2, DPanel panel, String direction )
	{
		if( direction.equals( "left" ) )
		{
			panel.addLine( contact1.leftLineX1, contact1.lineY, contact2.rightLineX2, contact2.lineY );
		}
		else if( direction.equals( "right" ) )
		{
			panel.addLine( contact1.rightLineX2, contact1.lineY, contact2.leftLineX1, contact2.lineY );
		}
		else if( direction.equals( "right-top" ) )
		{
			panel.addLine( contact1.rightLineX2, contact1.lineY, contact2.lineX, contact2.topLineY1 );
		}
		else if( direction.equals( "right-right" ) )
		{
			panel.addLine( contact1.rightLineX2, contact1.lineY, contact2.rightLineX2, contact2.lineY );
		}
		else if( direction.equals( "right-left" ) )
		{
			panel.addLine( contact1.rightLineX2, contact1.lineY, contact2.leftLineX1, contact2.lineY );
		}
	}
}

