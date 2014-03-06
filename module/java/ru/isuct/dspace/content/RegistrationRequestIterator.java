package ru.isuct.dspace.content;

import java.sql.SQLException;

import org.dspace.core.Context;
import org.dspace.storage.rdbms.TableRow;
import org.dspace.storage.rdbms.TableRowIterator;

public class RegistrationRequestIterator {
	private Context ourContext;
	private TableRowIterator rows;
	
	public RegistrationRequestIterator(Context context, TableRowIterator rows) {
		this.ourContext = context;
		this.rows = rows;
	}
	
	public boolean hasNext()
		throws SQLException
	{
		if ( rows != null ) {
			return rows.hasNext();
		}
		
		return false;
	}
	
	public RegistrationRequest next()
		throws SQLException
	{
		if ( hasNext() ) {
			TableRow row = rows.next();
			return new RegistrationRequest(ourContext, row);
		}
		
		return null;
	}
	
	public void close()
	{
		rows.close();
	}
}
