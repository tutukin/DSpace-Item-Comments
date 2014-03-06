package ru.isuct.dspace.content;

import java.sql.SQLException;
import java.util.Date;
import org.dspace.content.Item;
import org.dspace.core.Context;
import org.dspace.storage.rdbms.TableRow;
import org.dspace.storage.rdbms.TableRowIterator;
import org.dspace.storage.rdbms.DatabaseManager;


public class CommentStash {
	static private final String T_table = "Comments_Stash";
	
	private Context ourContext;
	private TableRow stashRow;
	

	public CommentStash (Context context, TableRow row)
	{
		ourContext = context;
		stashRow = row;
	}
	
	
	static public CommentStash create (
		Context context,
		int itemID,
		int submitter_id,
		String sComment
	) 
		throws SQLException 
	{
			TableRow row = DatabaseManager.create(context, T_table);
			
			try {
				row.setColumn("item_id", itemID);
				row.setColumn("submitted", new Date());
				row.setColumn("text", sComment);
				row.setColumn("submitter_id", submitter_id);				
				DatabaseManager.update(context, row);
			}
			catch (SQLException e) {
				DatabaseManager.delete(context, row);
				throw e;
			}
			
			CommentStash comment = new CommentStash(context, row);
			
			return comment;
	}
	
	public static void delete (Context context, int id) throws SQLException
	{
		String q = "DELETE FROM comments_stash WHERE comments_stash_id = ?";
		DatabaseManager.updateQuery(context, q, id);
	}
	
	public void delete() throws SQLException
	{
		CommentStash.delete(ourContext, getId());
	}
	
	public static CommentStashIterator findBySubmitterId(Context context, int submitter_id) throws SQLException {
		String q = "SELECT * FROM comments_stash WHERE submitter_id=?";
		TableRowIterator stash_rows = DatabaseManager.queryTable(context, T_table, q, submitter_id);
		return new CommentStashIterator(context, stash_rows);
	}
	
	public int getId()
	{
		return stashRow.getIntColumn("comments_stash_id");
	}
	
	public int getItemId()
	{
		return stashRow.getIntColumn("item_id");
	}

	public Date submitted()
	{
		return stashRow.getDateColumn("submitted");
	}
	
	public String text()
	{
		return stashRow.getStringColumn("text");
	}
	
	public Item getItem() throws SQLException
	{
		int item_id = getItemId();
		Item item = Item.find(ourContext,  item_id);
		return item;
	}

}
