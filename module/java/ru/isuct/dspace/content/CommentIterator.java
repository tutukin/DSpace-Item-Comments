package ru.isuct.dspace.content;

import java.sql.SQLException;

import org.dspace.core.Context;
import org.dspace.storage.rdbms.TableRow;
import org.dspace.storage.rdbms.TableRowIterator;



public class CommentIterator {
    private Context ourContext;
    private TableRowIterator commentRows;

    public CommentIterator(Context context, TableRowIterator rows) {
        ourContext = context;
        commentRows = rows;
    }

    public boolean hasNext()
            throws SQLException
            {
        if ( commentRows != null ) {
            return commentRows.hasNext();
        }

        return false;
            }

    public Comment next()
            throws SQLException
            {
        if ( hasNext() ) {
            TableRow row = commentRows.next();
            return new Comment(ourContext, row);
        }

        return null;
            }

    public void close()
    {
        commentRows.close();
    }
}
