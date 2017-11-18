package io.github.shamrice.nechat.core.db;

import io.github.shamrice.nechat.core.CoreContext;

import java.sql.*;

/**
 * Created by Erik on 10/19/2017.
 */
public abstract class DbService {

    protected Connection conn = null;

    public DbService(CoreContext coreContext) {
        conn = coreContext.getDatabaseConnection();
        if (conn == null) {
            System.out.println("Core Connection is null! Database connection not possible!!!");
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

    }

}
