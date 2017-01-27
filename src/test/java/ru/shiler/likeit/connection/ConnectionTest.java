package ru.shiler.likeit.connection;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Evgeny Yushkevich on 27.01.2017.
 */
public class ConnectionTest {

    @Test
    public void testConnection() throws IOException {
        URL serverUrl = new URL("http://localhost:8080");
        HttpURLConnection connection = (HttpURLConnection) serverUrl.openConnection();
        connection.setRequestMethod("GET");
        connection.connect();
        int code = connection.getResponseCode();
        Assert.assertTrue(code == 200);
    }

}
