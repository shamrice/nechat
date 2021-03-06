package io.github.shamrice.nechat.server.core.db;

import io.github.shamrice.nechat.server.core.CoreContext;
import io.github.shamrice.nechat.server.core.db.dto.DbDto;
import io.github.shamrice.nechat.server.core.db.dto.TokenDto;
import io.github.shamrice.nechat.logging.Log;
import io.github.shamrice.nechat.logging.LogLevel;
import org.springframework.security.access.AccessDeniedException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Erik on 10/20/2017.
 */
public class TokenAuthService extends DbService {

    public TokenAuthService(CoreContext coreContext) {
        super(coreContext);
    }

    public void authorizeToken(String token, String login) {

        boolean result = false;
        String tokenFound = "";

        PreparedStatement preparedStatement = null;

        if (conn != null) {
            try {
                preparedStatement = conn.prepareStatement("" +
                        "select t.auth_token " +
                        "from auth_tokens t " +
                        "join users u " +
                        "on u.idusers = t.idusers " +
                        "where " +
                        "    u.login = ? " +
                        "    and t.auth_token = ? " +
                        "    and t.expire_dt > current_timestamp() " +
                        "    and t.create_dt <= current_timestamp(); "
                );

                preparedStatement.setString(1, login);
                preparedStatement.setString(2, token);

                if (null != conn) {

                    ResultSet resultSet = preparedStatement.executeQuery();

                    while (resultSet.next()) {
                        tokenFound = resultSet.getString("auth_token");
                    }

                    result = token.equals(tokenFound);

                }
            } catch (SQLException sqlExc) {
                Log.get().logException(sqlExc);
            } finally {
                if (preparedStatement != null) {
                    try {
                        preparedStatement.close();
                    } catch (SQLException ex) {

                        Log.get().logMessage(
                                LogLevel.DEBUG,
                                this.getClass().getSimpleName() +
                                        " : Error closing statement. This can be ignored. " +
                                        ex.getMessage()
                        );
                    }
                    preparedStatement = null;
                }
            }
        }

        if (!result) {
            Log.get().logMessage(
                    LogLevel.DEBUG,
                    this.getClass().getSimpleName() +
                            " : Failed to validate token " + token + " for " + login
            );
            throw new AccessDeniedException("Unable to authenticate user "
                    + login + " with token " + token);
        }
    }

    public boolean createToken(int userId) {

        String newToken = UUID.randomUUID().toString().replace("-", "");
        Log.get().logMessage(LogLevel.INFORMATION, "Generated token: " + newToken);

        LocalDateTime expireDate = LocalDateTime.now().plusDays(1);

        PreparedStatement preparedStatement = null;

        if (null != conn) {
            try {
                preparedStatement = conn.prepareStatement(
                        "INSERT INTO auth_tokens " +
                                "(idusers, auth_token, expire_dt) " +
                                "VALUES(?, ?, ?); "
                );

                preparedStatement.setInt(1, userId);
                preparedStatement.setString(2, newToken);
                preparedStatement.setString(3, expireDate.toString());

                return executeCommand(preparedStatement);

            } catch (SQLException sqlExc) {
                Log.get().logException(sqlExc);
            } finally {
                if (preparedStatement != null) {
                    try {
                        preparedStatement.close();
                    } catch (SQLException ex) {
                        Log.get().logMessage(
                                LogLevel.DEBUG,
                                this.getClass().getSimpleName() +
                                        " : Error closing statement. This can be ignored. " +
                                        ex.getMessage()
                        );
                    }
                }
            }
        }

        return false;

    }

    public TokenDto getToken(int userId) {

        String query =  "SELECT idauth_tokens, idusers, auth_token, create_dt, expire_dt " +
                " FROM auth_tokens " +
                " WHERE idusers = ? " +
                "    and expire_dt > current_timestamp() " +
                "    and create_dt <= current_timestamp(); ";

        Map<Integer, Object> queryParams = new HashMap<>();
        queryParams.put(1, userId);

        return executePreparedStatement(query, queryParams).toType(TokenDto.class);
    }

    @Override
    protected DbDto executePreparedStatement(String query, Map<Integer, Object> queryParameters) {

        TokenDto tokenDao = new TokenDto(-1, -1, null, null, null);
        PreparedStatement preparedStatement = null;

        if (null != conn) {
            try {
                preparedStatement = conn.prepareStatement(query);

                for (int paramIndex : queryParameters.keySet()) {
                    preparedStatement.setObject(paramIndex, queryParameters.get(paramIndex));
                }

                ResultSet resultSet = preparedStatement.executeQuery();

                //get last token
                while (resultSet.next()) {
                    tokenDao = new TokenDto(
                            resultSet.getInt("idauth_tokens"),
                            resultSet.getInt("idusers"),
                            resultSet.getString("auth_token"),
                            resultSet.getDate("create_dt"),
                            resultSet.getDate("expire_dt")
                    );
                }
            } catch (SQLException sqlExc) {
                Log.get().logException(sqlExc);
            } finally {
                if (preparedStatement != null) {
                    try {
                        preparedStatement.close();
                    } catch (SQLException ex) {
                        Log.get().logMessage(
                                LogLevel.DEBUG,
                                this.getClass().getSimpleName() +
                                        " : Error closing statement. This can be ignored. " +
                                        ex.getMessage()
                        );
                    }
                    preparedStatement = null;
                }
            }
        }
        return tokenDao;
    }
}
