package ru.isuct.dspace.app.xmlui.aspect.comments;

import org.dspace.app.xmlui.wing.WingException;

import java.io.Serializable;

import org.apache.cocoon.util.HashUtil;
import org.apache.cocoon.caching.CacheableProcessingComponent;
import org.apache.excalibur.source.SourceValidity;
import org.apache.excalibur.source.impl.validity.NOPValidity;
import org.dspace.app.xmlui.cocoon.AbstractDSpaceTransformer;

import org.dspace.app.xmlui.wing.element.Body;
import org.dspace.app.xmlui.wing.element.Division;
import org.dspace.app.xmlui.wing.element.List;
import org.dspace.app.xmlui.wing.element.Text;
import org.dspace.app.xmlui.wing.element.TextArea;

import org.dspace.app.xmlui.wing.Message;

public class CommentForm extends AbstractDSpaceTransformer implements
		CacheableProcessingComponent {
	
	static private final Message T_head = message("xmlui.comments.commentform.head");
	static private final Message T_para1 = message("xmlui.comments.commentform.para1");
	static private final Message T_email = message("xmlui.comments.commentform.email");
	static private final Message T_email_help = message("xmlui.comments.commentform.email_help");
	static private final Message T_comment = message("xmlui.comments.commentform.comment");
	static private final Message T_submit = message("xmlui.comments.commentform.submit");
	
	@Override
	public Serializable getKey() {
		String email = parameters.getParameter("email","");
        String comment = parameters.getParameter("comment","");
        String page = parameters.getParameter("page","unknown");
        
       return HashUtil.hash(email + "-" + comment + "-" + page);
	}

	@Override
	public SourceValidity getValidity() {
		return NOPValidity.SHARED_INSTANCE;
	}
	
	
	public void addBody(Body body)
		throws WingException
	{
		String email = parameters.getParameter("email","");
		String eperson_id = parameters.getParameter("eperson_id", "");
		String item_id = parameters.getParameter("item_id","");
        String comment = parameters.getParameter("comment","");
        String error = parameters.getParameter("error", "");
        String info = parameters.getParameter("info", "");
        
		Division commentDiv = body.addInteractiveDivision(
			"comment-form",
			contextPath + '/' + sitemapURI,
			Division.METHOD_POST,
			"primary"
		);
		
		commentDiv.setHead( T_head );
		if ( ! error.equals("") ) {
			commentDiv.addPara( error );
		}
		if ( ! info.equals("") ) {
			commentDiv.addPara(info);
		}
		
		commentDiv.addPara( T_para1 );
		
		List form = commentDiv.addList("form", List.TYPE_FORM);
		
		if ( ! parameters.getParameter("ask_password", "").equals("") ) {
			form.addItem().addPassword("password").setLabel("Enter your password");
		}
		
		if ( ! parameters.getParameter("ask_registration", "").equals("") ) {
			form.addItem().addText("firstname").setLabel("First name");
			form.addItem().addText("lastname").setLabel("Last name");
			form.addItem().addPassword("password1").setLabel("specify password");
			form.addItem().addPassword("password2").setLabel("repeat password");
		}
		
		if ( eperson_id.equals("") ) {
			Text emailItem = form.addItem().addText("email");
			emailItem.setAutofocus("autofocus");
			emailItem.setLabel( T_email );
			emailItem.setHelp( T_email_help );
			
			emailItem.setValue( email );
		}
		else {
			// logged in
			commentDiv.addHidden("email").setValue(email);
			commentDiv.addHidden("eperson_id").setValue(eperson_id);
		}
		
		commentDiv.addHidden("item_id").setValue(item_id);
		commentDiv.addHidden("page").setValue( parameters.getParameter("page","") );
		
		TextArea commentItem = form.addItem().addTextArea("comment");
		commentItem.setLabel( T_comment );
		
		commentItem.setValue( comment );
		
		form.addItem().addButton("submit").setValue( T_submit );
	}

}
