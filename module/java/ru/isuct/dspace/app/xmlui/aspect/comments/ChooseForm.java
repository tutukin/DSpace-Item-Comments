/**
 * 
 */
package ru.isuct.dspace.app.xmlui.aspect.comments;

import org.dspace.app.xmlui.cocoon.AbstractDSpaceTransformer;
import org.dspace.app.xmlui.wing.Message;
import org.dspace.app.xmlui.wing.WingException;
import org.dspace.app.xmlui.wing.element.Body;
import org.dspace.app.xmlui.wing.element.Division;
import org.dspace.app.xmlui.wing.element.Item;
import org.dspace.app.xmlui.wing.element.List;
import org.dspace.app.xmlui.wing.element.PageMeta;

/**
 * @author tut
 *
 */
public class ChooseForm extends AbstractDSpaceTransformer
{
    private static final Message T_head =
            message("xmlui.Comment.choose.head");

    private static final Message T_para1 =
            message("xmlui.Comment.choose.explain"); // "choose form explanations para1";

    private static final Message T_have_account =
            message("xmlui.Comment.choose.have_account"); // "have account";

    private static final Message T_no_account =
            message("xmlui.Comment.choose.no_account"); // "no account";

    private static final Message T_forgot_password =
            message("xmlui.Comment.choose.forgot_password"); //"forgot password";



    public void addPageMeta(PageMeta pageMeta) throws WingException
    {
        pageMeta.addMetadata("title").addContent(T_head);
    }



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
