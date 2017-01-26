package ru.shiler.likeit.tag;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTagSupport;
import java.io.IOException;

/**
 * Cuts the <code>body</code> of the tag for <code>symbols</code>
 * amount and adds '...' to the end.
 */
public class CutTag extends BodyTagSupport {

    private int symbols;

    public void setSymbols(int symbols) {
        this.symbols = symbols;
    }

    @Override
    public int doAfterBody() throws JspException {
        BodyContent bodyContent = getBodyContent();
        JspWriter out = bodyContent.getEnclosingWriter();
        String text = bodyContent.getString();
        if (text.length() > symbols) {
            text = text.substring(0, symbols - 1) + "...";
        }
        bodyContent.clearBody();
        try {
            out.print(text);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return SKIP_BODY;
    }
}
