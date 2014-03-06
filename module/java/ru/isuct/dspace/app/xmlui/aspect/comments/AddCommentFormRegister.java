package ru.isuct.dspace.app.xmlui.aspect.comments;

import java.util.ArrayList;

import org.apache.cocoon.environment.ObjectModelHelper;
import org.apache.cocoon.environment.Request;
import org.dspace.app.xmlui.cocoon.AbstractDSpaceTransformer;
import org.dspace.app.xmlui.wing.WingException;
import org.dspace.app.xmlui.wing.element.Body;
import org.dspace.app.xmlui.wing.element.Division;
import org.dspace.app.xmlui.wing.element.Item;
import org.dspace.app.xmlui.wing.element.List;
import org.dspace.app.xmlui.wing.element.Password;
import org.dspace.app.xmlui.wing.element.Text;
import org.dspace.app.xmlui.wing.element.TextArea;

public class AddCommentFormRegister extends AbstractCommentTransformer {
	private static final String T_head = "Stash comment and request registration";
	private static final String T_para1 = "Provide us with registration info and add a comment";
	private static final String T_comment = "Comment";
	private static final String T_email = "email";
	private static final String T_submit = "Submit";
	private static final String T_empty_comment = "Enter a comment";
	private static final String T_empty_email = "Email is required";
	private static final String T_email_exists = "Email existsx";
	private static final String T_firstname = "First name";
	private static final String T_empty_firstname = "Please, specify your first name";
	private static final String T_lastname = "last name";
	private static final String T_empty_lastname = "please, specify your last name";

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
