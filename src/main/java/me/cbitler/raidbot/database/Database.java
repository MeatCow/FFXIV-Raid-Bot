package me.cbitler.raidbot.database;

import java.sql.*;
import java.util.List;

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
            + " `date` text NOT NULL, \n"
            + " `time` text NOT NULL, \n"
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
     * Create a new database with the specific filename
     * @param databaseName The filename/location of the SQLite database
     */
    public Database(String databaseName) {
        this.databaseName = databaseName;
    }

    /**
     * Connect to the SQLite database and create the tables if they don't exist
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
     * @throws SQLException
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
     * @throws SQLException
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
     * @throws SQLException
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
     * @throws SQLException
     */
    private void tableInits() throws SQLException {
        connection.createStatement().execute(raidTableInit);
        connection.createStatement().execute(raidUsersTableInit);
        connection.createStatement().execute(botServerSettingsInit);

        // Database updates
        try {
            //connection.createStatement().execute("DROP TABLE IF EXISTS raidUsersQueue");
            //connection.createStatement().execute("ALTER TABLE raidUsers ADD COLUMN ordre text");
            //connection.createStatement().execute("ALTER TABLE raidUsers DROP COLUMN role");
            //connection.createStatement().execute("ALTER TABLE raids ADD COLUMN queue text");
            //connection.createStatement().execute("ALTER TABLE raids DROP COLUMN ordre text");
        } catch (Exception e) {
            System.out.println("Issue modifying raidUsers or raids table");
            System.out.println(e.getMessage());
        }
    }
}
