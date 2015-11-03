package com.theironyard;

import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Created by holdenhughes on 11/3/15.
 */
public class MainTest {
    public Connection startConnection() throws SQLException {
        Connection conn = DriverManager.getConnection("jdbc:h2:./test");
        Main.createTables(conn);
        return conn;
    }

    public void endConnection (Connection conn) throws SQLException {
        Statement stmt = conn.createStatement();
        stmt.execute("DROP TABLE users");
        stmt.execute("DROP TABLE messages");
        conn.close();
    }

    @Test
    public void testUser() throws SQLException {
        Connection conn = startConnection();
        Main.insertUser(conn, "Alice", "1234");
        User user = Main.selectUser(conn,"Alice");
        endConnection(conn);

        assertTrue(user !=null);
    }

    @Test
    public void testMessage() throws SQLException {
        Connection conn = startConnection();
        Main.insertUser(conn, "Alice", "1234");
        Main.insertMessage(conn, 1, -1, "Hello, world");
        Message message = Main.selectMessage(conn, 1);
        endConnection(conn);

        assertTrue(message != null);
    }

    @Test
    public void testReplies() throws SQLException{
        Connection conn = startConnection();
        Main.insertUser(conn, "Alice", "");
        Main.insertUser(conn, "Bob", "");
        Main.insertMessage(conn, 1, -1, "hello");
        Main.insertMessage(conn, 2, 1, "This is a reply");
        Main.insertMessage(conn, 2, 1, "This is another reply");
        ArrayList<Message> replies = Main.selectReplies(conn, 1);
        endConnection(conn);

        assertTrue(replies.size() == 2);
    }

}