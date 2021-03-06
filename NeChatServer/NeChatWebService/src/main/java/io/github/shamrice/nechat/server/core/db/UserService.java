package io.github.shamrice.nechat.server.core.db;

import io.github.shamrice.nechat.server.core.CoreContext;
import io.github.shamrice.nechat.server.core.db.dto.DbDto;
import io.github.shamrice.nechat.server.core.db.dto.UserDto;
import io.github.shamrice.nechat.logging.Log;
import io.github.shamrice.nechat.logging.LogLevel;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Erik on 10/20/2017.
 */
public class UserService extends DbService {

    public UserService(CoreContext coreContext) {
        super(coreContext);
    }

    public UserDto getUser(String login) {

        String query = "SELECT idusers, login, password FROM users WHERE login = ?";
        Map<Integer, Object> queryParams = new HashMap<>();
        queryParams.put(1, login);

        return executePreparedStatement(query, queryParams).toType(UserDto.class);
    }

    @Override
    protected DbDto executePreparedStatement(String query, Map<Integer, Object> queryParameters) {

        UserDto userDao = new UserDto(-1, null, null);

        if (null != conn) {
            PreparedStatement statement = null;

            try {
                statement = conn.prepareStatement(query);
                for (int queryIndex : queryParameters.keySet()) {
                    statement.setObject(queryIndex, queryParameters.get(queryIndex));
                }
                ResultSet resultSet = statement.executeQuery();

                while (resultSet.next()) {
                    userDao = new UserDto(
                            resultSet.getInt("idusers"),
                            resultSet.getString("login"),
                            resultSet.getString("password")
                    );
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
        return userDao;
    }
}
