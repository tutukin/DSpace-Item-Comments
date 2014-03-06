/**
 * 
 */
package ru.isuct.dspace.app.xmlui.aspect.comments;

import org.dspace.app.xmlui.cocoon.AbstractDSpaceTransformer;
import org.dspace.app.xmlui.wing.WingException;
import org.dspace.app.xmlui.wing.element.Body;
import org.dspace.app.xmlui.wing.element.Division;
import org.dspace.app.xmlui.wing.element.Item;
import org.dspace.app.xmlui.wing.element.List;

/**
 * @author tut
 *
 */
public class ChooseForm extends AbstractDSpaceTransformer
{
	private static final String T_head = "choose form head";
	private static final String T_para1 = "choose form explanations para1";
	private static final String T_have_account = "have account";
	private static final String T_no_account = "no account";
	private static final String T_forgot_password = "forgot password";

	public void addBody(Body body)
			throws WingException
	{
		Division div = body.addInteractiveDivision(
			"choose-form",
			contextPath + "/comments",
			Division.METHOD_POST,
			"primary"
		);
		
		div.setHead(T_head);
		div.addPara(T_para1);
		
		List form = div.addList("choose-form", List.TYPE_FORM);
		Item submit = form.addItem();
		
		submit.addButton("have_account").setValue(T_have_account);
		submit.addButton("no_account").setValue(T_no_account);
		submit.addButton("forgot_password").setValue(T_forgot_password);
		
		div.addHidden("comments-continue").setValue( knot.getId() );
	}
	
}
