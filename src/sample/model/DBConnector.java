package sample.model;

import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class DBConnector {

    private Connection conn;

    private final String TABLE_MOVIE = "movies";

    private final String QUERY_MOVIES = "SELECT * FROM " + TABLE_MOVIE;
    private ObservableList<Game> games;


    public static Connection getConnection() throws SQLException {

        String dbURL = "jdbc:oracle:thin:@localhost:1521:xe";
        String username = "c##videogame";
        String password = "tiger";

        Connection conn = DriverManager.getConnection(dbURL, username, password);
        return conn;


    }



}

