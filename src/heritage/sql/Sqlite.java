package heritage.sql;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import heritage.config.Config;
import heritage.contact.Contact;

/**
 * Connect to SQLite
 * @author Alexei Kolganov
 *
 */
public class Sqlite 
{
	private static Connection c;
	private static final String DB_NAME = "heritage.db";
	
	private static Logger log = Logger.getLogger(Sqlite.class.getName());
	
	/**
	 * Connect to correct DB
	 */
	private static void connect()
	{
		c = null;	
		try 
		{     
			Class.forName( "org.sqlite.JDBC" );
			c = DriverManager.getConnection( "jdbc:sqlite:" + DB_NAME );
			c.setAutoCommit( true );
		} 
		catch( Exception ex ) 
		{
			log.log( Level.SEVERE, "Failed to connect to DB: ", ex );
		}
			
	}
	
	/**
	 * Disconnect using the provided connection descriptor
	 */
	private static void disconnect()
	{
		try 
		{
			c.close();
		} 
		catch( SQLException ex ) 
		{
			log.log( Level.SEVERE, "Failed to disconnect from DB: ", ex );
		}
	}

	/**
	 * Select data from DB
	 * using SELECT query
	 * @param query - 'Select ... From...' String
	 * @return ArrayList
	 */
	public static ArrayList <String[]> select( String query ) 
	{
		connect();
		
		Statement stmt = null;
		ArrayList <String[]> result = new ArrayList<String[]>();
		
		try
		{			
			stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery( query );
			int columnCount = rs.getMetaData().getColumnCount();
			
			while(rs.next())
			{
				String[] row = new String[columnCount];
			    for (int i=0; i <columnCount ; i++)
			    {
			       row[i] = rs.getString(i + 1);
			    }
			    result.add(row);
			}
		}
		catch( SQLException ex ) 
		{	
			log.log( Level.SEVERE, "Failed to select data from DB using '" + query + "': ", ex );
		}
		
		disconnect();
			
		return result;

	}
	
	public static List<Contact> getContacts( String query )
	{
		connect();
		List<Contact> contacts = new ArrayList<Contact>();

		Statement stmt = null;
	    ResultSet rs   = null;
	    try 
	    {
	    	stmt = c.createStatement();
	    	rs = stmt.executeQuery( query );
	    	while ( rs.next() ) 
			{
	    		int contactId 			= rs.getInt("contact_id");
				String firstName 		= ( rs.getString( "contact_name" ) == null ) 			? "" : rs.getString( "contact_name" );
				String lastName 		= ( rs.getString( "contact_surname" ) == null ) 		? "" : rs.getString( "contact_surname" );
				String maidenName 		= ( rs.getString( "contact_maiden_name" ) == null ) 	? "" : rs.getString( "contact_maiden_name" );
				boolean gender			= ( rs.getString( "contact_gender" ) != null && rs.getString( "contact_gender" ).equals("F") ) ? false : true;
				String nationality 		= ( rs.getString( "contact_nationality" ) == null )   	? "" : rs.getString( "contact_nationality" );
				String dateOfBirth 		= ( rs.getString( "contact_date_of_birth" ) == null )   ? "" : rs.getString( "contact_date_of_birth" );
				String dateOfDeath 		= ( rs.getString( "contact_date_of_death" ) == null )   ? "" : rs.getString( "contact_date_of_death" );
				String placeOfBirth 	= ( rs.getString( "contact_place_of_birth" ) == null )  ? "" : rs.getString( "contact_place_of_birth" );
				String placeOfLiving 	= ( rs.getString( "contact_place_of_living" ) == null ) ? "" : rs.getString( "contact_place_of_living" );
				String placeOfDeath 	= ( rs.getString( "contact_place_of_death" ) == null )  ? "" : rs.getString( "contact_place_of_death" );
				
				boolean isDead 			= ( rs.getString( "contact_is_dead" ) != null && rs.getString( "contact_is_dead" ).equals("0") ) ? false : true;
				String avatar			= ( rs.getString( "contact_avatar" ) == null || rs.getString( "contact_avatar" ).isEmpty() ) ? ( gender ? (Config.getItem( "icons_path" ) + "/" + Config.getItem( "no_avatar_man" )) : (Config.getItem( "icons_path" ) + "/" + Config.getItem( "no_avatar_woman" )) ) : rs.getString( "contact_avatar" );
				
				String notes		 	= ( rs.getString( "note_text" ) == null ) ? "" : rs.getString( "note_text" );
				String lifeline 		= ( rs.getString( "lifeline_text" ) == null ) ? "" : rs.getString( "lifeline_text" );

				contacts.add( new Contact( contactId, firstName, lastName, maidenName, gender, nationality, dateOfBirth, dateOfDeath, placeOfBirth, placeOfLiving, placeOfDeath, isDead, avatar, notes, lifeline ) );
			}
	    	rs.close();
	    	stmt.close();
	    	//c.close();
	    } 
	    catch ( Exception ex ) 
	    {
	    	log.log( Level.SEVERE, "Failed to get contacts from DB using '" + query + "': ", ex );
	    }
	    disconnect();
	    
	    return contacts;
	}
	
	public static Contact getSelectedContact( int id )
	{
		connect();
		Contact contact = null;
		Statement stmt = null;
	    ResultSet rs   = null;
	    
	    String query =  "SELECT " +
				"contacts.contact_id, " +
				"contacts.contact_name, " +
				"contacts.contact_surname, " +
				"contacts.contact_maiden_name, " +
				"contacts.contact_gender, " +
				"contacts.contact_nationality, " +
				"contacts.contact_date_of_birth, " +
				"contacts.contact_date_of_death, " +
				"contacts.contact_place_of_birth, " +
				"contacts.contact_place_of_living, " +
				"contacts.contact_place_of_death, " +
				"contacts.contact_is_dead, " +
				"contacts.contact_avatar,  " +
				"notes.note_text,  " +
				"lifeline.lifeline_text  " +
				"FROM contacts " +
				"LEFT JOIN notes " +
				"ON contacts.contact_id = notes.contact_id " +
				"LEFT JOIN lifeline " +
				"ON contacts.contact_id = lifeline.contact_id " +
				"WHERE contacts.contact_id = " + id + ";";
	    System.out.println(query);
	    try 
	    {
	    	stmt = c.createStatement();
	    	rs = stmt.executeQuery( query );
	    	while ( rs.next() ) 
			{
	    		int contactId 			= rs.getInt("contact_id");
				String firstName 		= ( rs.getString( "contact_name" ) == null ) ? "" : rs.getString( "contact_name" );
				String lastName 		= ( rs.getString( "contact_surname" ) == null ) ? "" : rs.getString( "contact_surname" );
				String maidenName 		= ( rs.getString( "contact_maiden_name" ) == null ) ? "" : rs.getString( "contact_maiden_name" );
				boolean gender			= ( rs.getString( "contact_gender" ) != null && rs.getString( "contact_gender" ).equals("F") ) ? false : true;
				String nationality 		= ( rs.getString( "contact_nationality" ) == null ) ? "" : rs.getString( "contact_nationality" );
				String dateOfBirth 		= ( rs.getString( "contact_date_of_birth" ) == null ) ? "" : rs.getString( "contact_date_of_birth" );
				String dateOfDeath 		= ( rs.getString( "contact_date_of_death" ) == null ) ? "" : rs.getString( "contact_date_of_death" );
				String placeOfBirth 	= ( rs.getString( "contact_place_of_birth" ) == null ) ? "" : rs.getString( "contact_place_of_birth" );
				String placeOfLiving 	= ( rs.getString( "contact_place_of_living" ) == null ) ? "" : rs.getString( "contact_place_of_living" );
				String placeOfDeath 	= ( rs.getString( "contact_place_of_death" ) == null ) ? "" : rs.getString( "contact_place_of_death" );
				
				boolean isDead 			= ( rs.getString( "contact_is_dead" ) != null && rs.getString( "contact_is_dead" ).equals("0") ) ? false : true;
				String avatar			= ( rs.getString( "contact_avatar" ) == null ) ? ( gender ? (Config.getItem( "icons_path" ) + "/" + Config.getItem( "no_avatar_man" )) : (Config.getItem( "icons_path" ) + "/" + Config.getItem( "no_avatar_woman" )) ) : rs.getString( "contact_avatar" );

				String notes		 	= ( rs.getString( "note_text" ) == null ) ? "" : rs.getString( "note_text" );
				String lifeline 		= ( rs.getString( "lifeline_text" ) == null ) ? "" : rs.getString( "lifeline_text" );
				
				contact = new Contact( contactId, firstName, lastName, maidenName, gender, nationality, dateOfBirth, dateOfDeath, placeOfBirth, placeOfLiving, placeOfDeath, isDead, avatar, notes, lifeline );
				
			}
	    	rs.close();
	    	stmt.close();
	    	//c.close();
	    } 
	    catch ( Exception ex ) 
	    {
	    	log.log( Level.SEVERE, "Failed to get contacts from DB using '" + query + "': ", ex );
	    }
	    disconnect();
	    
	    return contact;
	}
}