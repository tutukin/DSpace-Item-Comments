package ru.isuct.dspace.content;

import java.sql.SQLException;
import java.util.Date;
import java.util.Map;

import org.dspace.core.Context;
import org.dspace.storage.rdbms.TableRow;
import org.dspace.storage.rdbms.TableRowIterator;
import org.dspace.storage.rdbms.DatabaseManager;
import org.dspace.eperson.EPerson;

public class Comment {

    static private final String T_table = "Comments";

    private Context ourContext;
    private TableRow commentRow;


    public Comment (Context context, TableRow row)
    {
        ourContext = context;
        commentRow = row;
    }

    // TODO: deprecated
    static public Comment create (Context context, Map<String, String> map) 
            throws SQLException, Exception
            {
        throw new Exception("method deprecated!");
        /*
		Comment comment = create(
			context,
			Integer.parseInt(map.get("item_id")),
			Integer.parseInt(map.get("eperson_id")),
			map.get("comment")
		);

		return comment;
         */
            }

    static public Comment create (
            Context context,
            int itemID,
            int epersonID,
            String sComment
            ) 
                    throws SQLException 
                    {
        TableRow row = DatabaseManager.create(context, "comments");

        try {
            row.setColumn("item_id", itemID);
            row.setColumn("submitter_id", epersonID);
            row.setColumn("submitted", new Date());
            row.setColumn("text", sComment);		

            DatabaseManager.update(context, row);
            // Don't do context.complete() - it renders context unusable for other aspects somehow %-/
            //context.complete();
        }
        catch (SQLException e) {
            DatabaseManager.delete(context, row);
            throw e;
        }

        Comment comment = new Comment(context, row);

        return comment;
                    }


    public static CommentIterator findAllByItemID(Context context, int itemID)
            throws SQLException
            {
        String q = "SELECT * FROM Comments where item_id=? ORDER BY submitted";
        TableRowIterator rows = DatabaseManager.queryTable(context, T_table, q, itemID);
        return new CommentIterator(context, rows);
            }

    public EPerson submitter()
            throws SQLException
            {
        return EPerson.find(ourContext, commentRow.getIntColumn("submitter_id"));
            }

    public Date submitted()
    {
        return commentRow.getDateColumn("submitted");
    }

    public String text()
    {
        return commentRow.getStringColumn("text");
    }
}
