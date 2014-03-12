package ru.isuct.dspace.app.xmlui.aspect.comments;

import java.util.ArrayList;

import org.apache.cocoon.environment.ObjectModelHelper;
import org.apache.cocoon.environment.Request;
import org.dspace.app.xmlui.wing.Message;
import org.dspace.app.xmlui.wing.WingException;
import org.dspace.app.xmlui.wing.element.Body;
import org.dspace.app.xmlui.wing.element.Division;
import org.dspace.app.xmlui.wing.element.Item;
import org.dspace.app.xmlui.wing.element.List;
import org.dspace.app.xmlui.wing.element.PageMeta;
import org.dspace.app.xmlui.wing.element.Text;
import org.dspace.app.xmlui.wing.element.TextArea;

public class AddCommentFormRegister extends AbstractCommentTransformer {
    private static final Message T_head =
            message("xmlui.Comments.addComment.head");

    private static final Message T_para1 =
            message("xmlui.Comments.addComment.explain_register");

    private static final Message T_comment =
            message("xmlui.Comments.addComment.label");

    private static final Message T_email =
            message("xmlui.Comments.addComment.email");

    private static final Message T_submit =
            message("xmlui.Comments.addComment.submit");

    private static final Message T_empty_comment =
            message("xmlui.Comments.addComment.comment_empty");

    private static final Message T_empty_email =
            message("xmlui.Comments.addComment.email_empty");

    private static final Message T_email_exists =
            message("xmlui.Comments.addComment.email_exists");

    private static final Message T_firstname =
            message("xmlui.Comments.addComment.first_name");

    private static final Message T_empty_firstname =
            message("xmlui.Comments.addComment.first_name_empty");

    private static final Message T_lastname =
            message("xmlui.Comments.addComment.last_name");

    private static final Message T_empty_lastname =
            message("xmlui.Comments.addComment.last_name_empty");



    public void addPageMeta(PageMeta pageMeta) throws WingException
    {
        pageMeta.addMetadata("title").addContent(T_head);
    }


    public void addBody(Body body)
            throws WingException
            {
        Request request = ObjectModelHelper.getRequest(objectModel);
        ArrayList<String> errors = getErrorList( parameters.getParameter("errors","") );

        String email = request.getParameter("email");
        String comment = request.getParameter("comment");
        String firstname = request.getParameter("firstname");
        String lastname = request.getParameter("lastname");

        Division commentDiv = body.addInteractiveDivision(
                "comment-form",
                contextPath + "/comments",
                Division.METHOD_POST,
                "primary"
                );

        commentDiv.setHead( T_head );
        commentDiv.addPara( T_para1 );

        if ( errors.contains("email_exists") ) {
            commentDiv.addPara().addHighlight("bold").addContent(T_email_exists + " ("+email+")");
            email = null;
        }

        List form = commentDiv.addList("form", List.TYPE_FORM);

        TextArea commentItem = form.addItem().addTextArea("comment");
        commentItem.setLabel( T_comment );
        if ( ! isEmptyString(comment) ) {
            commentItem.setValue(comment);
        }
        if ( errors.contains("comment_empty") ) {
            commentItem.addError(T_empty_comment);
        }

        Item nameItem = form.addItem();
        Text firstNameItem = nameItem.addText("firstname");
        firstNameItem.setLabel( T_firstname );
        if ( ! isEmptyString(firstname) ) {
            firstNameItem.setValue(firstname);
        }
        if ( errors.contains("firstname_empty") ) {
            firstNameItem.addError( T_empty_firstname );
        }

        Text lastNameItem = nameItem.addText("lastname");
        lastNameItem.setLabel( T_lastname );
        if ( ! isEmptyString(lastname) ) {
            lastNameItem.setValue(lastname);
        }
        if ( errors.contains("lastname_empty") ) {
            firstNameItem.addError( T_empty_lastname );
        }

        Text emailItem = form.addItem().addText("email");
        emailItem.setLabel( T_email );
        if ( ! isEmptyString(email) ) {
            emailItem.setValue(email);
        }
        if ( errors.contains("email_empty") ) {
            emailItem.addError(T_empty_email);
        }

        form.addItem().addButton("submit").setValue( T_submit );

        commentDiv.addHidden("comments-continue").setValue( knot.getId() );
            }
}
