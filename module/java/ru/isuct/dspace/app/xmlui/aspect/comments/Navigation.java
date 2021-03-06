package ru.isuct.dspace.app.xmlui.aspect.comments;

import java.sql.SQLException;

import org.dspace.app.xmlui.cocoon.AbstractDSpaceTransformer;
import org.dspace.app.xmlui.wing.Message;
import org.dspace.app.xmlui.wing.WingException;
import org.dspace.app.xmlui.wing.element.List;
import org.dspace.app.xmlui.wing.element.Options;
import org.dspace.authorize.AuthorizeManager;

public class Navigation extends AbstractDSpaceTransformer {
    private static final Message T_comments_menu =
            message("xmlui.Comments.Navigation.head");

    private static final Message T_comments_manage =
            message("xmlui.Comments.Navigation.requests");

    public void addOptions(Options options) throws WingException, SQLException {
        boolean isSystemAdmin = AuthorizeManager.isAdmin(this.context);
        
        if ( isSystemAdmin ) {
            List comments = options.addList("comments");
            comments.setHead(T_comments_menu);
            comments.addItem().addXref(contextPath+"/comments", T_comments_manage);
        }
    }
}
