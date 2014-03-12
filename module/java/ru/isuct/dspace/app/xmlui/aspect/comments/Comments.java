package ru.isuct.dspace.app.xmlui.aspect.comments;

import org.dspace.app.xmlui.cocoon.AbstractDSpaceTransformer;
//import java.util.Date;

import org.xml.sax.SAXException;
import org.dspace.app.xmlui.wing.WingException;

import java.sql.SQLException;
import java.io.IOException;

import org.dspace.authorize.AuthorizeException;
import org.dspace.content.DSpaceObject;
import org.dspace.content.Item;

import ru.isuct.dspace.content.Comment;
import ru.isuct.dspace.content.CommentIterator;

import org.dspace.app.xmlui.utils.HandleUtil;

// Body et al.
import org.dspace.app.xmlui.wing.element.*;
// i18n
import org.dspace.app.xmlui.wing.Message;

public class Comments extends AbstractDSpaceTransformer
{
    private static final Message T_head = message("xmlui.Comments.head");
    private static final Message T_add_comment = message("xmlui.Comments.add_comment");	
    public void addBody (Body body)
            throws SAXException, WingException, SQLException, IOException, AuthorizeException
            {
        DSpaceObject dso = HandleUtil.obtainHandle(objectModel);
        if (!(dso instanceof Item))
        {
            return;
        }
        Item item = (Item) dso;

        CommentIterator it = Comment.findAllByItemID(context, item.getID());

        if ( it.hasNext() ) {
            Division divContainer = body.addDivision("item-comments-container");
            divContainer.setHead(T_head);

            try {
                while ( it.hasNext() ) {
                    Comment r = it.next();
                    Division commentWrapper = divContainer.addDivision("item-comments-message");
                    commentWrapper.addPara( "Комментирует: " + r.submitter().getFullName() + " ("  + r.submitted().toString() + ")");
                    commentWrapper.addPara( r.text() );
                }
            }
            finally {
                if ( it != null ) {
                    it.close();
                }
            }

        }

        // TODO add ref to comments?itemID=<id>
        String url = contextPath + "/comments?itemID=" + Integer.toString( item.getID() );
        Division div = body.addDivision("addComments");
        div.addList("comments-references").addItemXref(url, T_add_comment);
            }



    public void recycle () {
        super.recycle();
    }
}