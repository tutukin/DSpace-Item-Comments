package ru.isuct.dspace.app.xmlui.aspect.comments;

import org.dspace.app.xmlui.cocoon.AbstractDSpaceTransformer;
import org.dspace.app.xmlui.wing.WingException;
import org.dspace.app.xmlui.wing.element.List;
import org.dspace.app.xmlui.wing.element.Options;

public class Navigation extends AbstractDSpaceTransformer {
	private static final String T_comments_menu = "Комментарии";
	private static final String T_comments_manage = "запросы";

	public void addOptions(Options options) throws WingException {
		List comments = options.addList("comments");
		comments.setHead(T_comments_menu);
		comments.addItem().addXref(contextPath+"/comments", T_comments_manage);
	}
}
