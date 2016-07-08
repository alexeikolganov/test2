package heritage.contact;

public class ContactRelationship 
{
	// selected contact's id
	public int objectContactId;
	// related contact's id
	public int subjectContactId;
	// level:
	//   - 1 - parent
	//   0 - spouse
	//   1 - child
	public int lookupLevel;
	
	public ContactRelationship( int oId, int sId, int level )
	{
		this.objectContactId  = oId;
		this.subjectContactId = sId;
		this.lookupLevel 	  = level;
	}
}