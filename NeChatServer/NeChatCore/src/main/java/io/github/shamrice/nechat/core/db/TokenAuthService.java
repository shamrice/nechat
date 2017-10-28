package io.github.shamrice.nechat.core.db;

import io.github.shamrice.nechat.core.CoreContext;
import io.github.shamrice.nechat.core.db.dto.TokenDto;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Created by Erik on 10/20/2017.
 */
public class TokenAuthService extends DbService {

    public TokenAuthService(CoreContext coreContext) {
        super(coreContext);
    }

    public boolean authorizeToken(String token, String login) {

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
                        "    and t.create_dt < current_timestamp(); "
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
                sqlExc.printStackTrace();
            } finally {
                if (preparedStatement != null) {
                    try {
                        preparedStatement.close();
                    } catch (SQLException ex) {
                    }
                    preparedStatement = null;
                }
            }
        }

        if (!result) {
            System.out.println("Failed to validate token " + token + " for " + login);
        }

        return result;
    }

    public boolean createToken(int userId) {

        String newToken = UUID.randomUUID().toString().replace("-", "");
        System.out.println("Generated token: " + newToken);

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
                sqlExc.printStackTrace();
            } finally {
                if (preparedStatement != null) {
                    try {
                        preparedStatement.close();
                    } catch (SQLException ex) {
                    }
                }
            }
        }

        return false;
        /*
        String insertCommand = "INSERT INTO auth_tokens " +
                "(idusers, auth_token, expire_dt) " +
                "VALUES(" + userId + ", '" + newToken + "', '" + expireDate +"')";

        return executeCommand(insertCommand);
        */
    }

    public TokenDto getToken(int userId) {
        TokenDto tokenDao = null;
        PreparedStatement preparedStatement = null;

        if (null != conn) {
            try {
                preparedStatement = conn.prepareStatement(
                        "SELECT idauth_tokens, idusers, auth_token, create_dt, expire_dt " +
                                " FROM auth_tokens " +
                                " WHERE idusers = ? " +
                                "    and expire_dt > current_timestamp() " +
                                "    and create_dt <= current_timestamp(); "
                );

                preparedStatement.setInt(1, userId);

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
                sqlExc.printStackTrace();
            } finally {
                if (preparedStatement != null) {
                    try {
                        preparedStatement.close();
                    } catch (SQLException ex) {
                    }
                    preparedStatement = null;
                }
            }
        }
        return tokenDao;
    }
}
