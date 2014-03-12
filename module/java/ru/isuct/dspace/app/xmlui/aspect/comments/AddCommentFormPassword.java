package ru.isuct.dspace.app.xmlui.aspect.comments;

import java.util.ArrayList;

import org.apache.cocoon.environment.ObjectModelHelper;
import org.apache.cocoon.environment.Request;
import org.dspace.app.xmlui.wing.Message;
import org.dspace.app.xmlui.wing.WingException;
import org.dspace.app.xmlui.wing.element.Body;
import org.dspace.app.xmlui.wing.element.Division;
import org.dspace.app.xmlui.wing.element.List;
import org.dspace.app.xmlui.wing.element.PageMeta;
import org.dspace.app.xmlui.wing.element.Password;
import org.dspace.app.xmlui.wing.element.Text;
import org.dspace.app.xmlui.wing.element.TextArea;

public class AddCommentFormPassword extends AbstractCommentTransformer {

    private static final Message T_head =
            message("xmlui.Comments.addComment.head");

    private static final Message T_para1 =
            message("xmlui.Comments.addComment.addComment.explain_password");

    private static final Message T_comment =
            message("xmlui.Comments.addComment.label");

    private static final Message T_email =
            message("xmlui.Comments.addCommentPassword.email");

    private static final Message T_submit =
            message("xmlui.Comments.addComment.submit");

    private static final Message T_comment_empty =
            message("xmlui.Comments.addComment.comment_empty");

    private static final Message T_email_empty =
            message("xmlui.Comments.addComment.email_empty");

    private static final Message T_password_empty =
            message("xmlui.Comments.addComment.password_empty");

    private static final Message T_email_not_found =
            message("xmlui.Comments.addComment.email_not_found");

    private static final Message T_password =
            message("xmlui.Comments.addComment.password");;




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

                Division commentDiv = body.addInteractiveDivision(
                        "comment-password-form",
                        contextPath + "/comments",
                        Division.METHOD_POST,
                        "primary"
                        );

                commentDiv.setHead( T_head );
                commentDiv.addPara( T_para1 );

                if ( errors.contains("email_not_found") ) {
                    commentDiv.addPara().addHighlight("bold").addContent(T_email_not_found + " ("+email+")");
                    email = null;
                }


                List form = commentDiv.addList("form", List.TYPE_FORM);

                TextArea commentItem = form.addItem().addTextArea("comment");
                commentItem.setLabel( T_comment );
                if ( ! isEmptyString(comment) ) {
                    commentItem.setValue(comment);
                }
                if ( errors.contains("comment_empty") ) {
                    commentItem.addError(T_comment_empty);
                }

                Text emailItem = form.addItem().addText("email");
                emailItem.setLabel( T_email );
                if ( ! isEmptyString(email) ) {
                    emailItem.setValue(email);
                }
                if ( errors.contains("email_empty") ) {
                    emailItem.addError(T_email_empty);
                }


                Password passwordItem = form.addItem().addPassword("password");
                passwordItem.setLabel(T_password);
                if ( errors.contains("password_empty") ) {
                    passwordItem.addError(T_password_empty);
                }

                form.addItem().addButton("submit").setValue( T_submit );
                commentDiv.addHidden("comments-continue").setValue( knot.getId() );
                    }

}
