package ru.isuct.dspace.app.xmlui.aspect.comments;

import java.io.IOException;
import java.sql.SQLException;

import org.apache.cocoon.environment.ObjectModelHelper;
import org.apache.cocoon.environment.Request;
import org.dspace.app.xmlui.cocoon.AbstractDSpaceTransformer;
import org.dspace.app.xmlui.wing.WingException;
import org.dspace.app.xmlui.wing.element.Body;
import org.dspace.app.xmlui.wing.element.CheckBox;
import org.dspace.app.xmlui.wing.element.Division;
import org.dspace.app.xmlui.wing.element.Item;
import org.dspace.app.xmlui.wing.element.List;
import org.dspace.app.xmlui.wing.element.Row;
import org.dspace.app.xmlui.wing.element.Table;
import org.dspace.authorize.AuthorizeException;
import org.dspace.content.DCValue;
import org.xml.sax.SAXException;

import ru.isuct.dspace.content.CommentStash;
import ru.isuct.dspace.content.CommentStashIterator;
import ru.isuct.dspace.content.RegistrationRequest;
import ru.isuct.dspace.content.RegistrationRequestIterator;

public class AdminRegistrationRequestsForm extends AbstractDSpaceTransformer {
	private static final String T_head = "башка";
	private static final String T_para1 = "пузо";
	private static final String T_button_invite = "Invite";
	private static final String T_button_decline = "Decline";
	private static final String T_button_drop_comment = "Drop";
	private static final String T_checkbox_drop = "Drop";
	private static final String T_st_col1 = "[X]";
	private static final String T_st_col2 = "Comment";
	private static final String T_st_col3 = "Item";
	private static final String T_st_col4 = "Submitted";

	public void addBody(Body body)
			throws SAXException, WingException, SQLException, IOException, AuthorizeException
	{
		String sRequest = parameters.getParameter("request", null);
		
		if ( sRequest != null ) {
			body.addDivision("xxx").addPara(sRequest);
		}
		else {
			body.addDivision("xxx").addPara("request = null %-(");
		}
		
		Division main = body.addDivision("rra");
		Division reqDiv = null;
		
		main.setHead(T_head);
		main.addPara(T_para1);
		
		RegistrationRequestIterator reqs = RegistrationRequest.findAll(context);
		
		while ( reqs.hasNext() ) {
			RegistrationRequest req = reqs.next();
			reqDiv  = main.addInteractiveDivision(
				"admin-comments",
				contextPath + "/comments",
				Division.METHOD_POST,
				"primary"
			);
			this.addRequestAdminForm(reqDiv, req);			
			this.addStashedCommentsAdminForm(reqDiv, req);
			reqDiv.addHidden("comments-continue").setValue( knot.getId() );
		}
	}
	
	
	private void addStashedCommentsAdminForm(Division main, RegistrationRequest req) throws SQLException, WingException
	{
		CommentStashIterator stash_it = req.getStash();
		int rows = 3, cols = 4;
		
		Table stashTable = main.addTable("stash", rows, cols);
		Row header = stashTable.addRow(Row.ROLE_HEADER);
		header.addCellContent( T_st_col1);
		header.addCellContent( T_st_col2);
		header.addCellContent( T_st_col3);
		header.addCellContent( T_st_col4);
		
		List stashList = main.addList("stash", List.TYPE_FORM, "horizontal");
		
		while( stash_it.hasNext() ) {
			CommentStash stash = stash_it.next();
			String stash_id = Integer.toString( stash.getId() );
			int item_id = stash.getItemId();
			String description = getItemDescription(item_id);
			
			Row row = stashTable.addRow(Row.ROLE_DATA);
			CheckBox box = row.addCell().addCheckBox("drop_stash");
			box.setLabel(T_checkbox_drop);
			box.addOption(stash_id);
			
			row.addCell().addContent( stash.text() );
			row.addCell().addContent( description );
			row.addCell().addContent( stash.submitted().toString() );
		}
		
		stashList.addItem().addButton("drop").setValue(T_button_drop_comment);
	}
	
	
	private void addRequestAdminForm(Division main, RegistrationRequest req) throws WingException
	{
		String req_id = Integer.toString(req.getId());
		String label = req.fullName() + "<" + req.email() + ">";
		
		List reqAdminList = main.addList("request-admin");
		reqAdminList.addLabel( label );
		Item actions = reqAdminList.addItem();
		actions.addButton("invite_person").setValue(T_button_invite);
		actions.addButton("decline_person").setValue(T_button_decline);
		actions.addHidden("req_id").setValue(req_id);		
	}
	
	@SuppressWarnings("deprecation")
	public String getItemDescription(int item_id) throws SQLException {
		org.dspace.content.Item item = org.dspace.content.Item.find(context, item_id);
		String description = "No item description available";
		if ( item != null ) {
			@SuppressWarnings("deprecation")
			DCValue[] authors = item.getMetadata("dc.contributor.author");
			@SuppressWarnings("deprecation")
			DCValue[] titles = item.getMetadata("dc.title");
			description = authors[0].value + ". " + titles[0].value; 
		}
		return description;
	}
}
