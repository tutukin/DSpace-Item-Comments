/**
 * 
 */
package ru.isuct.dspace.app.xmlui.aspect.comments;

import org.dspace.app.xmlui.cocoon.AbstractDSpaceTransformer;
import org.dspace.app.xmlui.wing.WingException;
import org.dspace.app.xmlui.wing.element.Body;
import org.dspace.app.xmlui.wing.element.Division;
import org.dspace.app.xmlui.wing.element.List;
import org.dspace.app.xmlui.wing.element.TextArea;

/**
 * @author tut
 *
 */
public class AddCommentForm extends AbstractDSpaceTransformer {
	
	private static final String T_head = "add comment form header";
	private static final String T_para1 = "add comment form explanations para1";
	private static final String T_comment = "your comment";
	private static final String T_submit = "submit";
	private static final String T_comment_empty = "Comment should not be empty";

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
		
		form.addItem().addButton("submit").setValue( T_submit );
		
		commentDiv.addHidden("comments-continue").setValue( knot.getId() );
	}
}
