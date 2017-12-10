package io.github.shamrice.nechat.server.core.db.dto;

/**
 * Created by Erik on 11/22/2017.
 */
public interface DbDto {
    <T> T toType(Class<T> toType);
}
