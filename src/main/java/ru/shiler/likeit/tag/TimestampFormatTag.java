package ru.shiler.likeit.tag;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

/**
 * Created by Evgeny Yushkevich on 14.01.2017.
 */
public class TimestampFormatTag extends SimpleTagSupport {

    private Timestamp timestamp;

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public void doTag() throws JspException, IOException {
        JspWriter out = getJspContext().getOut();
        out.println(formatTimestamp(timestamp));
    }

    private String formatTimestamp(Timestamp timestamp) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm dd.MM");
        return dateFormat.format(timestamp);
    }
}
