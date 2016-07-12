package heritage.sql;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import heritage.config.Config;
import heritage.contact.Contact;
import heritage.contact.ContactRelationship;

/**
 * Connect to SQLite
 * @author Alexei Kolganov
 *
 */
public class Sqlite 
{
	private static Connection c;
	private static final String DB_NAME = "heritage.db";
	
	private static Logger log = Logger.getLogger( Sqlite.class.getName() );
	
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
			c.createStatement().execute("PRAGMA foreign_keys = ON");
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
				contacts.add( buildContact( rs, true ) );		
			}
	    	rs.close();
	    	stmt.close();
	    } 
	    catch ( Exception ex ) 
	    {
	    	log.log( Level.SEVERE, "Failed to get contacts from DB using '" + query + "': ", ex );
	    }
	    disconnect();
	    return contacts;
	}
	
	public static Contact getSelectedContact( String query )
	{
		connect();
		Contact contact = null;
		Statement stmt  = null;
	    ResultSet rs    = null;

	    try 
	    {
	    	stmt = c.createStatement();
	    	rs = stmt.executeQuery( query );
	    	
	    	while ( rs.next() ) 
			{
				contact = buildContact( rs, false );			
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

	public static int insertContact( Contact contact )
	{
		connect();

		PreparedStatement stmt = null;
		int contactId = -1;

		try {
			stmt = c.prepareStatement(  
					"INSERT INTO `contacts` (" +
					"`contact_name`," +
					"`contact_surname`," +
					"`contact_gender`," +
					"`contact_date_of_birth`," +
					"`contact_date_of_death`," +
					"`contact_place_of_birth`," +
					"`contact_place_of_living`," +
					"`contact_place_of_death`," +
					"`contact_nationality`," +
					"`contact_is_dead`," +
					"`contact_avatar`," +
					"`contact_maiden_name`" +
					") VALUES (" +
					"?," +
					"?," +
					"?," +
					"?," +
					"?," +
					"?," +
					"?," +
					"?," +
					"?," +
					"?," +
					"?," +
					"?" +
					");", Statement.RETURN_GENERATED_KEYS );
			stmt.setString( 1,  ( contact.firstName == null ) 		? "" : contact.firstName );
			stmt.setString( 2,  ( contact.lastName == null ) 		? "" : contact.lastName );
			stmt.setString( 3,  ( contact.gender ) 					? "M" : "F" );
			stmt.setString( 4,  ( contact.dateOfBirth == null ) 	? "" : contact.dateOfBirth );
			stmt.setString( 5,  ( contact.dateOfDeath == null ) 	? "" : contact.dateOfDeath );
			stmt.setString( 6,  ( contact.placeOfBirth == null ) 	? "" : contact.placeOfBirth );
			stmt.setString( 7,  ( contact.placeOfLiving == null ) 	? "" : contact.placeOfLiving );
			stmt.setString( 8,  ( contact.placeOfDeath == null ) 	? "" : contact.placeOfDeath );
			stmt.setString( 9,  ( contact.nationality == null ) 	? "" : contact.nationality );
			stmt.setInt( 10,    ( (contact.isDead) 					? 1 : 0 ) );
			stmt.setString( 11, ( contact.avatar == null ) 			? "" : contact.avatar );
			stmt.setString( 12, ( contact.maidenName == null ) 		? "" : contact.maidenName );

			stmt.executeUpdate();
			
			try( ResultSet generatedKeys = stmt.getGeneratedKeys() ) 
			{
	            if( generatedKeys.next() ) 
	            {
	            	contactId = (int) generatedKeys.getLong(1);
	            }
	            else 
	            {
	                throw new SQLException( "Creating user failed, no ID obtained." );
	            }
	        }
		} 
		catch( SQLException ex ) 
		{
			log.log( Level.SEVERE, "Failed to insert contact into DB: ", ex );
		}
		disconnect();
		
		return contactId;
	}
	
	public static void updateContact( Contact contact )
	{
		connect();

		PreparedStatement stmt = null;
		try {
			stmt = c.prepareStatement(  
					"UPDATE `contacts` SET " +
					"`contact_name`= ?," +
					"`contact_surname` = ?," +
					"`contact_gender` = ?," +
					"`contact_date_of_birth` = ?," +
					"`contact_date_of_death` = ?," +
					"`contact_place_of_birth` = ?," +
					"`contact_place_of_living` = ?," +
					"`contact_place_of_death` = ?," +
					"`contact_nationality` = ?," +
					"`contact_is_dead` = ?," +
					"`contact_avatar` = ?," +
					"`contact_maiden_name` = ?" +
					" WHERE " +
					"contact_id = ?;" );
			stmt.setString( 1,  ( contact.firstName == null ) 		? "" : contact.firstName );
			stmt.setString( 2,  ( contact.lastName == null ) 		? "" : contact.lastName );
			stmt.setString( 3,  ( contact.gender ) 					? "M" : "F" );
			stmt.setString( 4,  ( contact.dateOfBirth == null ) 	? "" : contact.dateOfBirth );
			stmt.setString( 5,  ( contact.dateOfDeath == null ) 	? "" : contact.dateOfDeath );
			stmt.setString( 6,  ( contact.placeOfBirth == null ) 	? "" : contact.placeOfBirth );
			stmt.setString( 7,  ( contact.placeOfLiving == null ) 	? "" : contact.placeOfLiving );
			stmt.setString( 8,  ( contact.placeOfDeath == null ) 	? "" : contact.placeOfDeath );
			stmt.setString( 9,  ( contact.nationality == null ) 	? "" : contact.nationality );
			stmt.setInt( 10,    ( (contact.isDead) 					? 1 : 0 ) );
			stmt.setString( 11, ( contact.avatar == null ) 			? "" : contact.avatar );
			stmt.setString( 12, ( contact.maidenName == null ) 		? "" : contact.maidenName );
			stmt.setInt( 13,    contact.id );

			stmt.executeUpdate();

		} 
		catch( SQLException ex ) 
		{
			log.log( Level.SEVERE, "Failed to update contact with ID " + contact.id + ": ", ex );
		}
		disconnect();
	}
	
	public static void deleteContact( Contact contact )
	{
		connect();

		PreparedStatement stmt = null;
		try {
			stmt = c.prepareStatement(  
					"DELETE FROM `contacts` " +
					"WHERE `contact_id` = ?;" );
			stmt.setInt( 1, contact.id );

			stmt.executeUpdate();

		} 
		catch( SQLException ex ) 
		{
			log.log( Level.SEVERE, "Failed to delete contact with '" + contact.id + "' from into DB: ", ex );
		}
		disconnect();
	}
	
	public static void setPrimaryContact( Contact contact )
	{
		connect();

		PreparedStatement stmt = null;
		try {
			stmt = c.prepareStatement(  
					"UPDATE `contacts` SET " +
					"`contact_is_primary` = 0;" );

			stmt.executeUpdate();
			
			stmt = c.prepareStatement(  
					"UPDATE `contacts` SET " +
					"`contact_is_primary` = 1" +	
					" WHERE " +
					"`contact_id` = ?;" );
			stmt.setInt( 1, contact.id );
			stmt.executeUpdate();
		} 
		catch( SQLException ex ) 
		{
			log.log( Level.SEVERE, "Failed to update lifeline for contact with ID " + contact.id +": ", ex );
		}
		disconnect();
	}
	
	public static void insertRelationship( ContactRelationship reln )
	{
		connect();

		PreparedStatement stmt = null;
		try {
			stmt = c.prepareStatement(  
					"INSERT INTO contact_reln (" +
							"subject_contact_id," +
							"object_contact_id," +
							"lookup_id" +
							") VALUES (" +
							"?," +
							"?," +
							"( SELECT lookup_id " +
							"  FROM lookup " +
							"  WHERE lookup_level = ? " +
							"  AND lookup_gender = (SELECT contact_gender FROM contacts WHERE contact_id = ? ) )" +
							");" );
			stmt.setInt( 1, reln.objectContactId );
			stmt.setInt( 2, reln.subjectContactId );
			stmt.setInt( 3, reln.lookupLevel );
			stmt.setInt( 4, reln.subjectContactId );

			stmt.executeUpdate();

		} 
		catch( SQLException ex ) 
		{
			log.log( Level.SEVERE, "Failed to insert note into DB: ", ex );
		}
		disconnect();
	}
	
	public static void deleteRelationship( Contact contact )
	{
		connect();

		PreparedStatement stmt = null;
		try {
			stmt = c.prepareStatement(  
					"DELETE FROM `contact_reln` " +
					"WHERE `object_contact_id` = ? OR `subject_contact_id` = ?;" );
			stmt.setInt( 1, contact.id );
			stmt.setInt( 2, contact.id );

			stmt.executeUpdate();

		} 
		catch( SQLException ex ) 
		{
			log.log( Level.SEVERE, "Failed to delete contact with '" + contact.id + "' from into DB: ", ex );
		}
		disconnect();
	}
	
	public static void insertNote( Contact contact )
	{
		connect();

		PreparedStatement stmt = null;
		try {
			stmt = c.prepareStatement(  
					"INSERT INTO `notes` (" +
					"`note_text`," +
					"`contact_id`" +
					") VALUES (" +
					"?," +
					"?" +
					");" );
			stmt.setString( 1, ( contact.notes == null ) 		? "" : contact.notes );
			stmt.setInt(    2, contact.id );

			stmt.executeUpdate();

		} 
		catch( SQLException ex ) 
		{
			log.log( Level.SEVERE, "Failed to insert note into DB: ", ex );
		}
		disconnect();
	}
	
	public static void updateNote( Contact contact )
	{
		connect();

		PreparedStatement stmt = null;
		try {
			stmt = c.prepareStatement(  
					"UPDATE `notes` SET " +
					"`note_text` = ?" +	
					" WHERE " +
					"`contact_id` = ?;" );
			stmt.setString( 1, ( contact.notes == null ) 		? "" : contact.notes );
			stmt.setInt(    2, contact.id );

			stmt.executeUpdate();

		} 
		catch( SQLException ex ) 
		{
			log.log( Level.SEVERE, "Failed to update note for contact with ID " + contact.id +": ", ex );
		}
		disconnect();
	}
	
	public static void insertLifeline( Contact contact )
	{
		connect();

		PreparedStatement stmt = null;
		try {
			stmt = c.prepareStatement(  
					"INSERT INTO `lifeline` (" +
					"`lifeline_text`," +
					"`contact_id`" +
					") VALUES (" +
					"?," +
					"?" +
					");" );
			stmt.setString( 1, ( contact.lifeline == null ) 		? "" : contact.lifeline );
			stmt.setInt(    2, contact.id );

			stmt.executeUpdate();

		} 
		catch( SQLException ex ) 
		{
			log.log( Level.SEVERE, "Failed to insert lifeline into DB: ", ex );
		}
		disconnect();
	}
	
	public static void updateLifeline( Contact contact )
	{
		connect();

		PreparedStatement stmt = null;
		try {
			stmt = c.prepareStatement(  
					"UPDATE `lifeline` SET " +
					"`lifeline_text` = ?" +	
					" WHERE " +
					"`contact_id` = ?;" );
			stmt.setString( 1, ( contact.notes == null ) ? "" : contact.notes );
			stmt.setInt(    2, contact.id );

			stmt.executeUpdate();

		} 
		catch( SQLException ex ) 
		{
			log.log( Level.SEVERE, "Failed to update lifeline for contact with ID " + contact.id +": ", ex );
		}
		disconnect();
	}
	
	private static Contact buildContact( ResultSet rs, boolean statusRequired )
	{
		Contact contact = null;
		try 
		{		
			int contactId 			= rs.getInt("contact_id");
			String firstName 		= ( rs.getString( "contact_name" ) == null ) 			? "" : rs.getString( "contact_name" );
			String lastName 		= ( rs.getString( "contact_surname" ) == null ) 		? "" : rs.getString( "contact_surname" );
			String maidenName 		= ( rs.getString( "contact_maiden_name" ) == null ) 	? "" : rs.getString( "contact_maiden_name" );			
			String nationality 		= ( rs.getString( "contact_nationality" ) == null )   	? "" : rs.getString( "contact_nationality" );
			String dateOfBirth 		= ( rs.getString( "contact_date_of_birth" ) == null )   ? "" : rs.getString( "contact_date_of_birth" );
			String dateOfDeath 		= ( rs.getString( "contact_date_of_death" ) == null )   ? "" : rs.getString( "contact_date_of_death" );
			String placeOfBirth 	= ( rs.getString( "contact_place_of_birth" ) == null )  ? "" : rs.getString( "contact_place_of_birth" );
			String placeOfLiving 	= ( rs.getString( "contact_place_of_living" ) == null ) ? "" : rs.getString( "contact_place_of_living" );
			String placeOfDeath 	= ( rs.getString( "contact_place_of_death" ) == null )  ? "" : rs.getString( "contact_place_of_death" );
					
			String notes		 	= ( rs.getString( "note_text" ) == null ) ? "" : rs.getString( "note_text" );
			String lifeline 		= ( rs.getString( "lifeline_text" ) == null ) ? "" : rs.getString( "lifeline_text" );
						
			boolean gender			= ( rs.getString( "contact_gender" ) != null && rs.getString( "contact_gender" ).equals("F") ) ? false : true;
			boolean isDead 			= ( rs.getString( "contact_is_dead" ) != null && rs.getString( "contact_is_dead" ).equals("0") ) ? false : true;
			boolean isPrimary 		= ( rs.getString( "contact_is_primary" ) != null && rs.getString( "contact_is_primary" ).equals("0") ) ? false : true;
			
			String status			= (statusRequired) ?  ( rs.getString( "lookup_value" ) == null ) ? "" : rs.getString( "lookup_value" ) : "";
			String avatar			= ( rs.getString( "contact_avatar" ) == null || rs.getString( "contact_avatar" ).isEmpty() ) ? ( gender ? (Config.getItem( "icons_path" ) + "/" + Config.getItem( "no_avatar_man" )) : (Config.getItem( "icons_path" ) + "/" + Config.getItem( "no_avatar_woman" )) ) : rs.getString( "contact_avatar" );
			
			contact = new Contact( contactId, firstName, lastName, maidenName, gender, nationality, dateOfBirth, dateOfDeath, placeOfBirth, placeOfLiving, placeOfDeath, isDead, avatar, notes, lifeline, status, isPrimary );
		} 
		catch( SQLException ex ) 
		{
			log.log( Level.SEVERE, "Failed to build contact from DB using: ", ex );
		}
		return contact;
	}
}