package io.github.shamrice.nechat.server.core.db.dto;

import java.util.List;

/**
 * Created by Erik on 11/12/2017.
 */
public class MessagesDto extends BaseDto implements DbDto {
    private String login;
    private List<MessageDto> messageDtos;

    public MessagesDto(String login) {
        this.login = login;
    }

    public void setMessageDtos(List<MessageDto> messageDtos) {
        this.messageDtos = messageDtos;
    }

    public List<MessageDto> getMessageDtos() {
        return messageDtos;
    }

    public String getLogin() {
        return login;
    }
}
