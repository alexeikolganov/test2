package heritage.config;

import heritage.sql.Sqlite;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
 
public class Config 
{
	private static Logger log = Logger.getLogger(Config.class.getName());
	
	/**
	 * Получить элемент конфигурации
	 * @param key - ключ в таблице config
	 * @return - значение из таблицы config по ключу
	 */
	public static String getItem( String key ) 
	{
		ArrayList <String[]> result = Sqlite.select( "SELECT config_value FROM config WHERE config_key='" + key + "';" );
		String configValue = "";
		
		if( result.size() == 1 ) 
		{
			configValue = result.get(0)[0];		
	    }
		else if( result.size() == 0 ) 
		{
			IndexOutOfBoundsException ex = new IndexOutOfBoundsException(  );
			log.log( Level.SEVERE, "Invalid Config value: " + key + ". 0 values found", ex );
		}
		else if( result.size() > 1 ) 
		{
			IndexOutOfBoundsException ex = new IndexOutOfBoundsException( "Invalid Config value: " + key + ". More than 1 value found" );
			log.log( Level.SEVERE, "Invalid Config value: " + key + ". More than 1 value found", ex );
		}		
		return configValue;
	}
	
	/**
	 * Получить элемент конфигурации
	 * @param key - ключ в таблице config
	 * @return - значение из таблицы config по ключу
	 */
	public static String getLookup( int key ) 
	{
		ArrayList <String[]> result = Sqlite.select( "SELECT lookup_value FROM lookup WHERE lookup_id='" + key + "';" );
		String configValue = "";
		
		if( result.size() == 1 ) 
		{
			configValue = result.get(0)[0];		
	    }
		else if( result.size() == 0 ) 
		{
			IndexOutOfBoundsException ex = new IndexOutOfBoundsException(  );
			log.log( Level.SEVERE, "Invalid Lookup value: " + key + ". 0 values found", ex );
		}
		else if( result.size() > 1 ) 
		{
			IndexOutOfBoundsException ex = new IndexOutOfBoundsException( "Invalid Config value: " + key + ". More than 1 value found" );
			log.log( Level.SEVERE, "Invalid Lookup value: " + key + ". More than 1 value found", ex );
		}		
		return configValue;
	}
}