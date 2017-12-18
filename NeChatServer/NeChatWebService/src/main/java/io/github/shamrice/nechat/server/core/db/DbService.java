package io.github.shamrice.nechat.server.core.db;

import io.github.shamrice.nechat.server.core.CoreContext;
import io.github.shamrice.nechat.server.core.db.dto.DbDto;
import io.github.shamrice.nechat.logging.Log;
import io.github.shamrice.nechat.logging.LogLevel;

import java.sql.*;
import java.util.Map;

/**
 * Created by Erik on 10/19/2017.
 */
public abstract class DbService {

    protected Connection conn = null;

    public DbService(CoreContext coreContext) {
        conn = coreContext.getDatabaseConnection();
        if (conn == null) {
            Log.get().logMessage(LogLevel.ERROR, "Core Connection is null! Database connection not possible!!!");
        }
    }

    protected abstract DbDto executePreparedStatement(String query, Map<Integer, Object> queryParameters);

    protected boolean executeCommand(PreparedStatement statement) {
        boolean result = false;
        if (null != conn) {

            try {
                int rowsUpdated = statement.executeUpdate();
                if (rowsUpdated > 0) {
                    result = true;
                }
            } catch (SQLException sqlExc) {
                Log.get().logException(sqlExc);
            } finally {
                if (statement != null) {
                    try {
                        statement.close();
                    } catch (SQLException ex) {
                        Log.get().logMessage(
                                LogLevel.DEBUG,
                                this.getClass().getSimpleName() +
                                        " : Error closing statement. This can be ignored. " +
                                        ex.getMessage()
                        );
                    }
                    statement = null;
                }
            }
        }

        return result;

    }

}
