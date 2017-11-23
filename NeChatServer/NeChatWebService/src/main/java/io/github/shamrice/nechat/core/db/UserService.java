package io.github.shamrice.nechat.core.db;

import io.github.shamrice.nechat.core.CoreContext;
import io.github.shamrice.nechat.core.db.dto.DbDto;
import io.github.shamrice.nechat.core.db.dto.UserDto;

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
        return userDao;
    }
}
