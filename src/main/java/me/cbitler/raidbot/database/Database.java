package me.cbitler.raidbot.database;

import java.sql.*;
import java.time.format.DateTimeFormatter;

/**
 * Class for managing the SQLite database for this bot
 * @author Christopher Bitler
 */
public class Database {
    String databaseName;
    Connection connection;

    //Thee are the queries for creating the tables

    String raidTableInit = "CREATE TABLE IF NOT EXISTS raids (\n"
            + " raidId text PRIMARY KEY, \n"
            + " serverId text NOT NULL, \n"
            + " channelId text NOT NULL, \n"
            + " leader text NOT NULL, \n"
            + " `name` text NOT NULL, \n"
            + " `description` text, \n"
            + " `dateTime` text NOT NULL, \n"
            + " `reminderTime` text, \n"
            + " hasWaitingList boolean NOT NULL, \n"
            + " roles text NOT NULL);";

    String raidUsersTableInit = "CREATE TABLE IF NOT EXISTS raidUsers (\n"
            + " userId text, \n"
            + " username text, \n"
            + " spec text, \n"
            + " ordre text, \n"
            + " raidId text)";

    String botServerSettingsInit = "CREATE TABLE IF NOT EXISTS serverSettings (\n"
            + " serverId text PRIMARY KEY, \n"
            + " raid_leader_role text)";
    /**
     * Time format to be used when interacting with the database
     */
    public static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("dd-MM-uuuu hh:mma z");

    /**
     * Create a new database with the specific filename. Doesn't physically create a database until you call the
     * connect method.
     *
     * @param databaseName The filename/location of the SQLite database
     */
    public Database(String databaseName) {
        this.databaseName = databaseName;
    }

    /**
     * Connect to the SQLite database and create the tables if they don't exist
     * Also creates the physical database file if it doesn't already exist
     */
    public void connect() {
        String url = "jdbc:sqlite:" + databaseName;
        try {
            connection = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println("Database connection error");
            System.exit(1);
        }

        try {
            tableInits();
        } catch (SQLException e) {
            System.out.println("Couldn't create tables");
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Run a query and return the results using the specified query and parameters
     *
     * @param query The query with ?s where the parameters need to be placed
     * @param data  The parameters to put in the query
     * @return QueryResult representing the statement used and the ResultSet
     * @throws SQLException If a database access error occurs
     */
    public QueryResult query(String query, Object[] data) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement(query);
        int i = 1;
        for (Object input : data) {
            stmt.setObject(i, input);
            i++;
        }

        ResultSet rs = stmt.executeQuery();

        return new QueryResult(stmt, rs);
    }

    /**
     * Run a query and return the results
     *
     * @param query The query
     * @return QueryResult representing the statement used and the ResultSet
     * @throws SQLException If a database access error occurs
     */
    public QueryResult query(String query) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement(query);
        ResultSet rs = stmt.executeQuery();

        return new QueryResult(stmt, rs);
    }

    /**
     * Run an update query with the specified parameters
     *
     * @param query The query with ?s where the parameters need to be placed
     * @param data  The parameters to put in the query
     * @throws SQLException If a database access error occurs
     */
    public void update(String query, Object[] data) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement(query);
        int i = 1;
        for (Object input : data) {
            stmt.setObject(i, input);
            i++;
        }

        stmt.execute();
        stmt.close();
    }

    /**
     * Create the database tables. Also alters the raid table to add the leader column if it doesn't exist.
     *
     * @throws SQLException If a database access error occurs
     */
    private void tableInits() throws SQLException {
        connection.createStatement().execute(raidTableInit);
        connection.createStatement().execute(raidUsersTableInit);
        connection.createStatement().execute(botServerSettingsInit);

    }
}
