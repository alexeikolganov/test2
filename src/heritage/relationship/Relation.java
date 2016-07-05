package heritage.relationship;

import java.util.List;

import heritage.contact.Contact;
import heritage.sql.Sqlite;

public class Relation 
{	
	public static Contact getContact( int contactId )
	{
		Contact contact = Sqlite.getSelectedContact( contactId );
		
		return contact;
	}
	public static List<Contact> getCommonChildren( int contactId )
	{
		String query = 	"SELECT " +
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
						"JOIN contact_reln " +
						"ON contacts.contact_id = contact_reln.object_contact_id " +
						"AND contact_reln.subject_contact_id = " + contactId + " " +
						"AND contact_reln.lookup_id IN ( 5, 6 ) " +
						"JOIN contact_reln spouse_reln " +
						"ON contacts.contact_id = spouse_reln.object_contact_id " +
						"AND spouse_reln.subject_contact_id = ( SELECT  object_contact_id FROM contact_reln WHERE subject_contact_id = " + contactId + " AND contact_reln.lookup_id = 3 LIMIT 1 ) " +
						"AND spouse_reln.lookup_id IN ( 5, 6 )";
		
		List<Contact> contacts = Sqlite.getContacts( query );
		
		return contacts;
	}
	
	public static List<Contact> getCommonChildren( int contactId, int spouseContactId )
	{
		String query = 	"SELECT " +
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
						"JOIN contact_reln " +
						"ON contacts.contact_id = contact_reln.object_contact_id " +
						"AND contact_reln.subject_contact_id = " + contactId + " " +
						"AND contact_reln.lookup_id IN ( 5, 6 ) " +
						"JOIN contact_reln spouse_reln " +
						"ON contacts.contact_id = spouse_reln.object_contact_id " +
						"AND spouse_reln.subject_contact_id = " + spouseContactId + " " +
						"AND spouse_reln.lookup_id IN ( 5, 6 )";

		List<Contact> contacts = Sqlite.getContacts( query );
				
		return contacts;
	}
	
	public static Contact getSpouse( int contactId )
	{
		return getRelatedContact( contactId, 3 );
	}
	
	public static List<Contact> getSpouses( int contactId )
	{
		return getRelatedContacts( contactId, 3 );
	}
	
	public static Contact getMother( int contactId )
	{		
		return getRelatedContact( contactId, 1 );
	}
	public static Contact getFather( int contactId )
	{		
		Contact contact = getRelatedContact( contactId, 2 );
		return contact;
	}
	
	private static Contact getRelatedContact( int contactId, int lookupId )
	{
		String query = prepareQuery( contactId, lookupId );	
		
		List<Contact> contacts = Sqlite.getContacts( query );
		return ( contacts.size() > 0 ) ? contacts.get( 0 ) : null;
	}
	
	private static List<Contact> getRelatedContacts( int contactId, int lookupId )
	{
		String query = prepareQuery( contactId, lookupId );		
		List<Contact> contacts = Sqlite.getContacts( query );
		return contacts;
	}
	
	private static String prepareQuery( int contactId, int lookupId )
	{
		return "SELECT " +
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
				"JOIN contact_reln " + 
				"ON contacts.contact_id = contact_reln.object_contact_id " +
				"JOIN lookup " + 
				"ON contact_reln.lookup_id = lookup.lookup_id " + 
				"AND contact_reln.subject_contact_id = " + contactId + " " +
				"AND contact_reln.lookup_id = " + lookupId + ";";
	}

}
