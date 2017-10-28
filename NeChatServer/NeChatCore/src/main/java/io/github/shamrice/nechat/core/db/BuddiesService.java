package io.github.shamrice.nechat.core.db;

import io.github.shamrice.nechat.core.CoreContext;
import io.github.shamrice.nechat.core.db.dto.BuddiesDto;
import io.github.shamrice.nechat.core.db.dto.UserDto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Erik on 10/28/2017.
 */
public class BuddiesService extends DbService {

    public BuddiesService(CoreContext coreContext) {
        super(coreContext);
    }

    public BuddiesDto getBuddyList(String login) {
        BuddiesDto results = new BuddiesDto(login);
        PreparedStatement preparedStatement = null;

        String query =  "" +
                "SELECT  " +
                "    ub.idusers, " +
                "    ub.login     " +
                "FROM buddies b " +
                "JOIN users u " +
                "  on u.idusers = b.idusers " +
                "JOIN users ub " +
                "  on ub.idusers = b.idusers_buddy " +
                "WHERE " +
                "    u.login = ? " +
                "    and ub.deleted = 0;";

        if (null != conn) {
            try {
                preparedStatement = conn.prepareStatement(query);
                preparedStatement.setString(1, login);

                ResultSet resultSet = preparedStatement.executeQuery();

                List<UserDto> buddies = new ArrayList<>();

                //get last token
                while (resultSet.next()) {

                    UserDto buddy = new UserDto(
                            resultSet.getInt("idusers"),
                            resultSet.getString("login")
                    );

                    buddies.add(buddy);
                }

                results.setIdBuddies(buddies);

            } catch (SQLException sqlExc) {
                sqlExc.printStackTrace();
            } finally {
                if (preparedStatement != null) {
                    try {
                        preparedStatement.close();
                    } catch (SQLException ex) {}
                    preparedStatement = null;
                }
            }
        }

        return results;
    }

    public boolean addBuddy(String login, String buddyLogin) {
        boolean result = false;
        PreparedStatement preparedStatement = null;

        if (null != conn) {
            try {
                preparedStatement = conn.prepareStatement(
                        "INSERT INTO buddies " +
                                "(idusers, idusers_buddy) " +
                                "VALUES ( " +
                                "  ( " +
                                "    SELECT idusers " +
                                "    FROM users " +
                                "    WHERE login = ? " +
                                "  ), " +
                                "    ( " +
                                "    SELECT idusers " +
                                "    FROM users " +
                                "    WHERE login = ? " +
                                "  )  " +
                                ")"
                );

                preparedStatement.setString(1, login);
                preparedStatement.setString(2, buddyLogin);
                result = executeCommand(preparedStatement);

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

        return result;
    }

    public boolean deleteBuddy(String login) {
        boolean result = false;
        PreparedStatement preparedStatement = null;

        if (null != conn) {
            try {
                preparedStatement = conn.prepareStatement(
                        "DELETE FROM buddies " +
                                "WHERE idusers_buddy in ( " +
                                "  SELECT idusers " +
                                "    FROM users " +
                                "    WHERE login = ? " +
                                ");"
                );

                preparedStatement.setString(1, login);
                result = executeCommand(preparedStatement);

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

        return result;
    }
}
