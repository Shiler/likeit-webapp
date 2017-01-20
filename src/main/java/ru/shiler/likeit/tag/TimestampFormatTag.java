package ru.shiler.likeit.tag;

import ru.shiler.likeit.util.TimestampUtils;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by Evgeny Yushkevich on 14.01.2017.
 */
public class TimestampFormatTag extends SimpleTagSupport {

    private Timestamp timestamp;
    private String locale;

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    @Override
    public void doTag() throws JspException, IOException {
        JspWriter out = getJspContext().getOut();
        out.println(TimestampUtils.formatTimestamp(timestamp, locale));
    }

}
