package io.github.shamrice.nechat.server.core.db.dto;

import io.github.shamrice.nechat.logging.Log;

/**
 * Created by Erik on 11/22/2017.
 */
public abstract class BaseDto implements DbDto {

    public <T> T toType(Class<T> toType) {
        if (toType != null) {
            try {
                return toType.isInstance(this) ? toType.cast(this) : null;
            } catch (Exception e) {
                Log.get().logException(e);
            }
        }
        return null;
    }
}

