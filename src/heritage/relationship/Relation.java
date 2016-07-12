package heritage.relationship;

import java.util.List;

import heritage.contact.Contact;
import heritage.contact.ContactRelationship;
import heritage.sql.Sqlite;

public class Relation 
{	
	public static Contact getContact( int contactId )
	{
		String query =  "SELECT " +
						getFieldSet( ) +
						"WHERE contacts.contact_id = " + contactId + ";";
		Contact contact = Sqlite.getSelectedContact( query );	
		return contact;
	}
	
	public static int getPrimaryContact( )
	{
		String query =  "SELECT " +
						getFieldSet( ) +
						"WHERE contacts.contact_is_primary = 1;";
		Contact contact = Sqlite.getSelectedContact( query );	
		return contact.id;
	}
	
	public static void setPrimaryContact( Contact contact )
	{
		Sqlite.setPrimaryContact( contact );	
	}
	
	public static int insertContact( Contact contact )
	{
		contact.id = Sqlite.insertContact( contact );	
		Sqlite.insertLifeline( contact );
		Sqlite.insertNote( contact );
		return contact.id;
	}
	
	public static void updateContact( Contact contact )
	{
		Sqlite.updateContact( contact );	
		Sqlite.updateLifeline( contact );
		Sqlite.updateNote( contact );
	}
	
	public static void deleteContact( Contact contact )
	{
		Sqlite.deleteContact( contact );
		deleteRelationship( contact );
	}
	
	public static void insertRelationship( ContactRelationship reln )
	{
		// direct relationship
		Sqlite.insertRelationship( reln );	
		// inverse relationship
		int temp 				= reln.objectContactId;
		reln.objectContactId 	= reln.subjectContactId;
		reln.subjectContactId 	= temp;
		reln.lookupLevel 		*= -1;
		Sqlite.insertRelationship( reln );	
	}
	
	public static void deleteRelationship( Contact contact )
	{
		Sqlite.deleteRelationship( contact );
	}
		
	public static List<Contact> getCommonChildren( int contactId, int spouseContactId )
	{
		String query = 	"SELECT " +
						"lookup.lookup_value, " +
						getFieldSet( ) +
						"JOIN contact_reln " +
						"ON contacts.contact_id = contact_reln.object_contact_id " +
						"AND contact_reln.subject_contact_id = " + contactId + " " +
						"AND contact_reln.lookup_id IN ( 5, 6 ) " +
						"JOIN contact_reln spouse_reln " +
						"ON contacts.contact_id = spouse_reln.object_contact_id " +
						"AND spouse_reln.subject_contact_id = " + spouseContactId + " " +
						"AND spouse_reln.lookup_id IN ( 5, 6 ) " +
						"JOIN lookup " +
						"ON contact_reln.lookup_id = lookup.lookup_id;";
		
		List<Contact> contacts = Sqlite.getContacts( query );
		System.out.println( contacts.size() );	
		return contacts;
	}
	
	public static List<Contact> getSpouses( int contactId )
	{
		System.out.println("getting spouses");
		return getRelatedContacts( contactId, "3, 11" );
	}
	
	public static Contact getMother( int contactId )
	{		
		return getRelatedContact( contactId, "1" );
	}
	public static Contact getFather( int contactId )
	{		
		Contact contact = getRelatedContact( contactId, "2" );
		return contact;
	}
	
	private static Contact getRelatedContact( int contactId, String lookupIds )
	{
		String query = prepareQuery( contactId, lookupIds );	
		
		List<Contact> contacts = Sqlite.getContacts( query );
		return ( contacts.size() > 0 ) ? contacts.get( 0 ) : null;
	}
	
	private static List<Contact> getRelatedContacts( int contactId, String lookupIds )
	{
		String query = prepareQuery( contactId, lookupIds );	
		List<Contact> contacts = Sqlite.getContacts( query );
		return contacts;
	}
	
	private static String prepareQuery( int contactId, String lookupIds )
	{
		return "SELECT " +
				"lookup.lookup_value, " +
				getFieldSet( ) +
				"JOIN contact_reln " + 
				"ON contacts.contact_id = contact_reln.object_contact_id " +
				"JOIN lookup " + 
				"ON contact_reln.lookup_id = lookup.lookup_id " + 
				"AND contact_reln.subject_contact_id = " + contactId + " " +
				"AND contact_reln.lookup_id IN ( " + lookupIds + " );";
	}
	
	private static String getFieldSet( )
	{
		return "contacts.contact_id, " +
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
				"contacts.contact_avatar, " +
				"contacts.contact_is_primary, " +
				"notes.note_text,  " +
				"lifeline.lifeline_text " +			
				"FROM contacts " + 
				"LEFT JOIN notes " +
				"ON contacts.contact_id = notes.contact_id " +
				"LEFT JOIN lifeline " +
				"ON contacts.contact_id = lifeline.contact_id ";
	}

}
