package io.github.shamrice.nechat.core.db;

import io.github.shamrice.nechat.core.CoreContext;
import io.github.shamrice.nechat.core.db.dto.DbDto;
import io.github.shamrice.nechat.core.db.dto.UserDto;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

/**
 * Created by Erik on 10/20/2017.
 */
public class UserService extends DbService {

    public UserService(CoreContext coreContext) {
        super(coreContext);
    }

    public UserDto getUser(String login) {
        UserDto userDao = null;

        if (null != conn) {
            Statement statement = null;

            try {
                statement = conn.createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT idusers, login, password FROM users WHERE login = '" + login + "'");

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

    @Override
    protected DbDto executePreparedStatement(String query, Map<Integer, Object> queryParameters) {
        return null;
    }
}
