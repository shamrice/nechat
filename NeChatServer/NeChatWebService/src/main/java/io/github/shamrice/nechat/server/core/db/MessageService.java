package io.github.shamrice.nechat.server.core.db;

import io.github.shamrice.nechat.server.core.CoreContext;
import io.github.shamrice.nechat.server.core.db.dto.*;
import io.github.shamrice.nechat.logging.Log;
import io.github.shamrice.nechat.logging.LogLevel;

import java.sql.*;
import java.util.*;
import java.util.Date;

/**
 * Created by Erik on 10/20/2017.
 */
public class MessageService extends DbService {

    private String login;

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
            Log.get().logException(sqlExc);
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
            Log.get().logException(sqlExc);
        }

        return false;

    }

    public MessagesDto getUnreadMessages(String login) {

        this.login = login;

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
                "    u.login = ? " +
                "    and is_read_to = 0 ";

        Map<Integer, Object> queryParams = new HashMap<>();
        queryParams.put(1, login);

        return executePreparedStatement(query, queryParams).toType(MessagesDto.class);
    }

    public MessagesDto getChronologicalMessageHistory(String login, String withLogin, int startMessageId) {

        this.login = login;

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
                "    ((u.login = ? " +
                "     and uf.login = ? ) " +
                "    or " +
                "    (u.login = ? " +
                "     and uf.login = ? )) " +
                "    and m.idmessages > ? " +
                "order by idmessages asc; ";

        Map<Integer, Object> queryParams = new HashMap<>();
        queryParams.put(1, login);
        queryParams.put(2, withLogin);
        queryParams.put(3, withLogin);
        queryParams.put(4, login);
        queryParams.put(5, startMessageId);

        return executePreparedStatement(query, queryParams).toType(MessagesDto.class);
    }

    public MessagesDto getUnreadMessagesWithUser(String login, String withLogin) {

        this.login = login;

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
                "order by idmessages asc ";

        Map<Integer, Object> queryParams = new HashMap<>();
        queryParams.put(1, login);
        queryParams.put(2, withLogin);

        return executePreparedStatement(query, queryParams).toType(MessagesDto.class);
    }

    @Override
    protected DbDto executePreparedStatement(String query, Map<Integer, Object> queryParameters) {
        List<MessageDto> messageDtos = new ArrayList<>();

        if (null != conn) {
            PreparedStatement statement = null;

            try {

                statement = conn.prepareStatement(query);

                //set params if any
                for (int queryIndex : queryParameters.keySet()) {
                    statement.setObject(queryIndex, queryParameters.get(queryIndex));
                }

                ResultSet resultSet = statement.executeQuery();

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

        MessagesDto messagesDto = new MessagesDto(this.login);
        messagesDto.setMessageDtos(messageDtos);
        return messagesDto;
    }
}
