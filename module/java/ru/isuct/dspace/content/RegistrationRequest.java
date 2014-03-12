package ru.isuct.dspace.content;

import java.sql.SQLException;

import org.dspace.authorize.AuthorizeException;
import org.dspace.core.Context;
import org.dspace.eperson.EPerson;
import org.dspace.storage.rdbms.DatabaseManager;
import org.dspace.storage.rdbms.TableRow;
import org.dspace.storage.rdbms.TableRowIterator;

public class RegistrationRequest {
    private static final String T_table = "comments_registration_req";
    private Context ourContext;
    private TableRow row;

    public RegistrationRequest (Context context, TableRow row)
    {
        this.ourContext = context;
        this.row = row;
    }

    public static RegistrationRequest create(Context context, String email, String firstname, String lastname)
            throws SQLException
            {
        TableRow row = DatabaseManager.create(context, T_table);
        try {
            row.setColumn("email", email.toLowerCase());
            row.setColumn("firstname", firstname);
            row.setColumn("lastname", lastname);
            DatabaseManager.update(context, row);
        }
        catch (SQLException e) {
            DatabaseManager.delete(context, row);
            throw e;
        }

        RegistrationRequest rr = new RegistrationRequest(context, row);
        return rr;
            }

    public static RegistrationRequestIterator findAll(Context context) throws SQLException
    {
        String q = "SELECT * FROM comments_registration_req ORDER BY email";
        TableRowIterator rows = DatabaseManager.queryTable(context, T_table, q);
        return new RegistrationRequestIterator(context, rows);
    }

    public static RegistrationRequest find(Context context, int id) throws SQLException
    {
        TableRow row = DatabaseManager.find(context, T_table, id);
        if ( row == null ) return null;
        return new RegistrationRequest(context, row);
    }

    public static RegistrationRequest findByEmail(Context context, String email)
            throws SQLException
            {
        if ( email == null ) {
            return null;
        }
        TableRow row = DatabaseManager.findByUnique(context, T_table, "email", email.toLowerCase());
        if ( row == null ) {
            return null;
        }
        return new RegistrationRequest(context, row);
            }

    public static RegistrationRequest findOrCreate(Context context, String email, String firstname, String lastname)
            throws SQLException
            {
        RegistrationRequest rr = findByEmail(context, email);
        if ( rr == null ) {
            rr = create(context, email, firstname, lastname);
        }
        return rr;
            }

    public void delete() throws SQLException
    {
        String q = "DELETE FROM comments_registration_req WHERE comments_registration_req_id = ?";
        deleteStash();
        DatabaseManager.updateQuery(ourContext, q, getId());
    }

    public void deleteStash() throws SQLException
    {
        CommentStashIterator csi = getStash();
        while ( csi.hasNext() ) {
            csi.next().delete();
        }
    }

    public CommentStashIterator getStash() throws SQLException
    {
        return CommentStash.findBySubmitterId(ourContext, getId());
    }

    public int getId() 
    {
        return row.getIntColumn("comments_registration_req_id");
    }

    public String email()
    {
        return row.getStringColumn("email");
    }

    public String firstName()
    {
        return row.getStringColumn("firstname");
    }

    public String lastName()
    {
        return row.getStringColumn("lastname");
    }

    public String fullName()
    {
        return firstName() + " " + lastName();
    }

    public CommentStash addToStash(int itemID, String comment) throws SQLException
    {
        int submitter_id = row.getIntColumn("comments_registration_req_id");
        CommentStash stash = CommentStash.create(ourContext, itemID, submitter_id, comment);
        return stash;
    }

    public EPerson createNewEPerson() throws SQLException, AuthorizeException
    {
        EPerson eperson = EPerson.create(ourContext);
        eperson.setEmail( email() );
        eperson.setFirstName( firstName() );
        eperson.setLastName( lastName() );
        eperson.setCanLogIn(true);
        eperson.setSelfRegistered(false);
        eperson.update();

        return eperson;
    }

    public void stashToComments(EPerson owner) throws SQLException
    {
        CommentStashIterator csi = getStash();

        while ( csi.hasNext() ) {
            CommentStash stash = csi.next();
            Comment.create(ourContext, stash.getItemId(), owner.getID(), stash.text());
            stash.delete();
        }
    }
}
