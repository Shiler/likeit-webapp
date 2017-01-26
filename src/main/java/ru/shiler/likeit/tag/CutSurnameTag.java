package ru.shiler.likeit.tag;

import org.apache.commons.lang3.StringUtils;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTagSupport;
import java.io.IOException;

/**
 * Used to cut long user surnames in tag body.
 */
public class CutSurnameTag extends BodyTagSupport {

    @Override
    public int doAfterBody() throws JspException {
        BodyContent bodyContent = getBodyContent();
        JspWriter out = bodyContent.getEnclosingWriter();
        String text = bodyContent.getString();
        if (text.length() > 12) {
            text = cutSurname(text);
        }
        bodyContent.clearBody();
        try {
            out.print(text);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return SKIP_BODY;
    }

    private String cutSurname(String fulLName) {
        String surname = StringUtils.substringAfterLast(fulLName, " ").substring(0, 1);
        int index = StringUtils.lastIndexOf(fulLName, " ");
        surname += ".";
        return fulLName.substring(0, index) + " " + surname;
    }

}
