package ru.shiler.likeit.regex;

import org.junit.Assert;
import org.junit.Test;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Evgeny Yushkevich on 16.01.2017.
 */
public class RegexTest {

    @Test
    public void testRegex() {
        Pattern pattern = Pattern.compile("([a-zA-Z0-9]{1,20}@[a-zA-Z0-9]{1,20}.[a-zA-Z]{2,10})");
        Matcher matcher = pattern.matcher("kek123@ya.ru");
        Assert.assertTrue(matcher.matches());
    }

    @Test
    public void testRegex2() {
        Pattern pattern = Pattern.compile("([a-zA-Z0-9]{1,20}@[a-zA-Z0-9]{1,20}.[a-zA-Z]{2,10})");
        Matcher matcher = pattern.matcher("kek12ssss3@.ru");
        Assert.assertFalse(matcher.matches());
    }

    @Test
    public void testRegex3() {
        Pattern pattern = Pattern.compile("([0-9]{1,3})");
        Matcher matcher = pattern.matcher("19");
        Assert.assertTrue(matcher.matches());
    }

    @Test
    public void testRegex4() {
        Pattern pattern = Pattern.compile("([0-9]{1,3})");
        Matcher matcher = pattern.matcher("user123");
        Assert.assertFalse(matcher.matches());
    }

    @Test
    public void testRegex5() {
        Pattern pattern = Pattern.compile("([а-яА-Яa-zA-Z\\s]{6,50})");
        Matcher matcher = pattern.matcher("Евгений Юшкевич");
        Assert.assertTrue(matcher.matches());
    }

    @Test
    public void testRegex6() {
        Pattern pattern = Pattern.compile("([а-яА-Яa-zA-Z\\s]{6,50})");
        Matcher matcher = pattern.matcher("А вот это ! не пойдет");
        Assert.assertFalse(matcher.matches());
    }

}
