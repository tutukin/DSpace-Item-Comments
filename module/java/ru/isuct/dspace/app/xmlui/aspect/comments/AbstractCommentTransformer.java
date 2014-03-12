/**
 * 
 */
package ru.isuct.dspace.app.xmlui.aspect.comments;

import java.util.ArrayList;

import org.dspace.app.xmlui.cocoon.AbstractDSpaceTransformer;

/**
 * @author tut
 *
 */
public class AbstractCommentTransformer extends AbstractDSpaceTransformer {
    public ArrayList<String> getErrorList(String sErrors) {
        ArrayList<String> errors = new ArrayList<String>();

        if ( ! "".equals(sErrors) ) {
            for (String e : sErrors.split(",") ) {
                errors.add(e);
            }
        }

        return errors;
    }

    public boolean isEmptyString( String s) {
        return (s == null || "".equals(s));
    }
}
