package io.github.shamrice.nechat.core.db;

import io.github.shamrice.nechat.core.CoreContext;
import io.github.shamrice.nechat.core.db.dto.MessageDto;
import io.github.shamrice.nechat.core.db.dto.MessagesDto;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Erik on 10/20/2017.
 */
public class MessageService extends DbService {

    public MessageService(CoreContext coreContext) {
        super(coreContext);
    }

    public boolean markMessagesAsRead(String login, int[] messageIds) {

        try {

            StringBuilder querySb = new StringBuilder();
            querySb.append("" +
                    "UPDATE messages " +
                    "SET is_read = 1 " +
                    "WHERE " +
                    "  idusers = (SELECT idusers FROM users WHERE login = ?) " +
                    "  and idmessages in ( ");

            for (int i = 0; i < messageIds.length; i++) {
                querySb.append(" ? ");
                if (i != (messageIds.length - 1)) {
                    querySb.append(", ");
                } else {
                    querySb.append(" )");
                }
            }

            PreparedStatement preparedStatement = conn.prepareStatement(querySb.toString());

            preparedStatement.setString(1, login);

            for (int i = 0; i < messageIds.length; i++) {
                preparedStatement.setInt(i + 2, messageIds[i]);
            }

            return executeCommand(preparedStatement);

        } catch (SQLException sqlExc) {
            sqlExc.printStackTrace();
        }

        return false;

    }

    public boolean sendMessageToUser(String login, String toLogin, String message) {

        try {
            PreparedStatement preparedStatement = conn.prepareStatement("" +
                    "insert into messages " +
                    "(idusers, from_idusers, message) " +
                    "values( " +
                    " ( " +
                    "    select idusers " +
                    "    from users " +
                    "    where login = ? " +
                    "    ), " +
                    "    ( " +
                    "    select idusers  " +
                    "        from users " +
                    "    where login = ? " +
                    "    ), " +
                    "    ? " +
                    ")"
            );
            preparedStatement.setString(1, toLogin);
            preparedStatement.setString(2, login);
            preparedStatement.setString(3, message);

            return executeCommand(preparedStatement);

        } catch (SQLException sqlExc) {
            sqlExc.printStackTrace();
        }

        return false;

    }

    public MessagesDto getUnreadMessages(String login) {
        List<MessageDto> messageDtos = new ArrayList<>();

        String query =  "" +
                "select " +
                "    m.idmessages, " +
                "    m.idusers, " +
                "    u.login, " +
                "    m.from_idusers, " +
                "    uf.login as from_login, " +
                "    m.message, " +
                "    m.is_read, " +
                "    m.create_dt " +
                "from messages m " +
                "join users u " +
                "  on u.idusers = m.idusers " +
                "join users uf " +
                "  on uf.idusers = m.from_idusers " +
                "where " +
                "    u.login = '" + login + "' " +
                "    and is_read_to = 0 ";

        if (null != conn) {
            Statement statement = null;

            try {
                statement = conn.createStatement();
                ResultSet resultSet = statement.executeQuery(query);

                //get last token
                while (resultSet.next()) {

                    Timestamp createTimestamp = resultSet.getTimestamp("create_dt");
                    Date createDate = new Date(createTimestamp.getTime());

                    MessageDto messageDao = new MessageDto(
                            resultSet.getInt("idmessages"),
                            resultSet.getInt("idusers"),
                            resultSet.getString("login"),
                            resultSet.getInt("from_idusers"),
                            resultSet.getString("from_login"),
                            resultSet.getString("message"),
                            resultSet.getBoolean("is_read"),
                            createDate
                    );

                    messageDtos.add(messageDao);
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

        MessagesDto messagesDto = new MessagesDto(login);
        messagesDto.setMessageDtos(messageDtos);
        return messagesDto;
    }

    public MessagesDto getChronologicalMessageHistory(String login, String withLogin) {
        List<MessageDto> messageDtos = new ArrayList<>();

        String query =  "" +
                "select " +
                "    m.idmessages, " +
                "    m.idusers, " +
                "    u.login, " +
                "    m.from_idusers, " +
                "    uf.login as from_login, " +
                "    m.message, " +
                "    m.is_read, " +
                "    m.create_dt " +
                "from messages m " +
                "join users u " +
                "  on u.idusers = m.idusers " +
                "join users uf " +
                "  on uf.idusers = m.from_idusers " +
                "where " +
                "    (u.login = '" + login + "' " +
                "     and uf.login = '" + withLogin + "') " +
                "    or " +
                "    (u.login = '" + withLogin + "' " +
                "     and uf.login = '" + login + "') " +
                "order by idmessages asc ";

        if (null != conn) {
            Statement statement = null;

            try {
                statement = conn.createStatement();
                ResultSet resultSet = statement.executeQuery(query);

                //get last token
                while (resultSet.next()) {

                    Timestamp createTimestamp = resultSet.getTimestamp("create_dt");
                    Date createDate = new Date(createTimestamp.getTime());

                    MessageDto messageDao = new MessageDto(
                            resultSet.getInt("idmessages"),
                            resultSet.getInt("idusers"),
                            resultSet.getString("login"),
                            resultSet.getInt("from_idusers"),
                            resultSet.getString("from_login"),
                            resultSet.getString("message"),
                            resultSet.getBoolean("is_read"),
                            createDate
                    );

                    messageDtos.add(messageDao);
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

        MessagesDto messagesDto = new MessagesDto(login);
        messagesDto.setMessageDtos(messageDtos);
        return messagesDto;

    }

    public MessagesDto getUnreadMessagesWithUser(String login, String withLogin) {
        List<MessageDto> messageDtos = new ArrayList<>();

        String query =  "" +
                "select " +
                "    m.idmessages, " +
                "    m.idusers, " +
                "    u.login, " +
                "    m.from_idusers, " +
                "    uf.login as from_login, " +
                "    m.message, " +
                "    m.is_read, " +
                "    m.create_dt " +
                "from messages m " +
                "join users u " +
                "  on u.idusers = m.idusers " +
                "join users uf " +
                "  on uf.idusers = m.from_idusers " +
                "where " +
                "    (u.login = ? " +
                "     and uf.login = ? " +
                "     and m.is_read = 0 ) " +
               /* "    or " +
                "    (u.login = ? " +
                "     and uf.login = ? " +
                "     )" + */
                "order by idmessages asc ";

        if (null != conn) {
            PreparedStatement statement = null;

            try {
                statement = conn.prepareStatement(query);
                statement.setString(1, login);
                statement.setString(2, withLogin);
                /*statement.setString(3, withLogin);
                statement.setString(4, login);*/
                ResultSet resultSet = statement.executeQuery();

                //get last token
                while (resultSet.next()) {

                    Timestamp createTimestamp = resultSet.getTimestamp("create_dt");
                    Date createDate = new Date(createTimestamp.getTime());

                    MessageDto messageDao = new MessageDto(
                            resultSet.getInt("idmessages"),
                            resultSet.getInt("idusers"),
                            resultSet.getString("login"),
                            resultSet.getInt("from_idusers"),
                            resultSet.getString("from_login"),
                            resultSet.getString("message"),
                            resultSet.getBoolean("is_read"),
                            createDate
                    );

                    messageDtos.add(messageDao);
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

        MessagesDto messagesDto = new MessagesDto(login);
        messagesDto.setMessageDtos(messageDtos);
        return messagesDto;

    }
}