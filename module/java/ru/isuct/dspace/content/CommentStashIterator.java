package ru.isuct.dspace.content;

import java.sql.SQLException;

import org.dspace.core.Context;
import org.dspace.storage.rdbms.TableRow;
import org.dspace.storage.rdbms.TableRowIterator;

public class CommentStashIterator {
    private Context ourContext;
    private TableRowIterator rows;

    public CommentStashIterator(Context context, TableRowIterator rows) {
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

    public CommentStash next()
            throws SQLException
            {
        TableRow row = null;
        CommentStash stash = null;

        if ( hasNext() ) {
            row = rows.next();
            stash = new CommentStash(ourContext, row);
        }

        return stash;
            }


    public void close()
    {
        rows.close();
    }
}
