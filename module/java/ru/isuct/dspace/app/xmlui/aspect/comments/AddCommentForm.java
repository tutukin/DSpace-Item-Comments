/**
 * 
 */
package ru.isuct.dspace.app.xmlui.aspect.comments;

import org.dspace.app.xmlui.wing.WingException;
import org.dspace.app.xmlui.wing.element.Body;
import org.dspace.app.xmlui.wing.element.Division;
import org.dspace.app.xmlui.wing.element.Item;
import org.dspace.app.xmlui.wing.element.List;
import org.dspace.app.xmlui.wing.element.PageMeta;
import org.dspace.app.xmlui.wing.element.TextArea;
import org.dspace.app.xmlui.wing.Message;

/**
 * @author tut
 *
 */
public class AddCommentForm extends AbstractCommentTransformer {

    private static final Message T_head =
            message("xmlui.Comments.addComment.head");

    private static final Message T_para1 = 
            message("xmlui.Comments.addComment.explain_simple");

    private static final Message T_comment = 
            message("xmlui.Comments.addComment.label");

    private static final Message T_submit = 
            message("xmlui.Comments.addComment.submit");

    private static final Message T_comment_empty = 
            message("xmlui.Comments.addComment.comment_empty");
    
    private static final Message T_cancel =
            message("xmlui.Comments.addComment.cancel");




    public void addPageMeta(PageMeta pageMeta) throws WingException
    {
        pageMeta.addMetadata("title").addContent(T_head);
    }


    public void addBody(Body body)
            throws WingException
            {
        String sErrors = parameters.getParameter("errors","");

        Division commentDiv = body.addInteractiveDivision(
                "add-comment-form",
                contextPath + "/comments",
                Division.METHOD_POST,
                "primary"
                );

        commentDiv.setHead( T_head );
        commentDiv.addPara( T_para1 );
        List form = commentDiv.addList("form", List.TYPE_FORM);
        TextArea commentItem = form.addItem().addTextArea("comment");
        commentItem.setLabel( T_comment );
        if ( "comment_empty".equals(sErrors) ) {
            commentItem.addError(T_comment_empty);
        }

        Item submit = form.addItem(); 
        submit.addButton("submit").setValue( T_submit );
        submit.addButton("cancel").setValue( T_cancel );

        commentDiv.addHidden("comments-continue").setValue( knot.getId() );
            }
}
