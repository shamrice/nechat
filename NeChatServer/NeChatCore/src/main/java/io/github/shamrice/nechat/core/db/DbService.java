package io.github.shamrice.nechat.core.db;

import io.github.shamrice.nechat.core.configuration.Db.DbCredentials;
import io.github.shamrice.nechat.core.CoreContext;

import java.sql.*;

/**
 * Created by Erik on 10/19/2017.
 */
public abstract class DbService {

    private CoreContext coreContext;
    protected Connection conn = null;

    public DbService(CoreContext coreContext) {
        this.coreContext = coreContext;

        try {

            DbCredentials dbCredentials = this.coreContext.getCoreConfiguration().getDbConfiguration().getDbCredentials();

            System.out.println("Getting mysql jdbc driver");
            Class.forName("com.mysql.jdbc.Driver").newInstance();

            System.out.println("create sql connection to " + dbCredentials.getInstance());

            conn = DriverManager.getConnection(
                    "jdbc:mysql://" +
                            dbCredentials.getInstance() +
                            "/" +
                            dbCredentials.getSchema() +
                            "?user=" +
                            dbCredentials.getUsername() +
                            "&password=" +
                            dbCredentials.getPassword()
            );

        } catch (SQLException sqlException) {
            System.out.println("Unable to connect to SQL database!");
            sqlException.printStackTrace();
        } catch (ClassNotFoundException classNotFoundExc) {
            System.out.println("Unable to load JDBC Driver!");
            classNotFoundExc.printStackTrace();
        } catch (Exception exc) {
            exc.printStackTrace();
        }
    }

    protected boolean executeCommand(PreparedStatement statement) {
        boolean result = false;
        if (null != conn) {

            try {
                statement.execute();
                result = true;

            } catch (SQLException sqlExc) {
                sqlExc.printStackTrace();
            } finally {
                if (statement != null) {
                    try {
                        statement.close();
                    } catch (SQLException ex) {}
                    statement = null;
                }
            }
        }

        return result;


/*
        if (null != conn && !insertCommand.equals("")) {
            Statement statement = null;

            System.out.println("Insert command:\n" + insertCommand);

            try {
                statement = conn.createStatement();
                statement.execute(insertCommand);
                result = true;

            } catch (SQLException sqlExc) {
                sqlExc.printStackTrace();
            } finally {
                if (statement != null) {
                    try {
                        statement.close();
                    } catch (SQLException ex) {}
                    statement = null;
                }
            }
        }
*/

    }

}
