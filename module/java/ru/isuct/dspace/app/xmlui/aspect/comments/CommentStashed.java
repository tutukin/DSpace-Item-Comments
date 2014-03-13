package ru.isuct.dspace.app.xmlui.aspect.comments;

import org.dspace.app.xmlui.wing.Message;
import org.dspace.app.xmlui.wing.WingException;
import org.dspace.app.xmlui.wing.element.Body;
import org.dspace.app.xmlui.wing.element.Division;
import org.dspace.app.xmlui.wing.element.List;
import org.dspace.app.xmlui.wing.element.PageMeta;

public class CommentStashed extends AbstractCommentTransformer
{
    private static final Message T_head = message("xmlui.Comments.CommentStashed.head");

    private static final Message T_body = message("xmlui.Comments.CommentStashed.body");
    
    private static final Message T_submit = message("xmlui.Comments.CommentStashed.submit");

    public void addPageMeta(PageMeta pageMeta) throws WingException
    {
        pageMeta.addMetadata("title").addContent(T_head);
    }

    public void addBody(Body body) throws WingException
    {
        
        Division commentDiv = body.addInteractiveDivision(
                "add-comment-form",
                contextPath + "/comments",
                Division.METHOD_POST,
                "primary"
                );

        commentDiv.setHead( T_head );
        commentDiv.addPara( T_body );
        List form = commentDiv.addList("form", List.TYPE_FORM);
        form.addItem().addButton("submit").setValue( T_submit );
        commentDiv.addHidden("comments-continue").setValue( knot.getId() );
    }
}
