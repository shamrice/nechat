package io.github.shamrice.nechat.core.db.dto;

import java.lang.reflect.Type;

/**
 * Created by Erik on 11/22/2017.
 */
public abstract class BaseDto implements DbDto {

    public <T> T toType(Class<T> toType) {
        if (toType != null) {
            try {
                return toType.isInstance(this) ? toType.cast(this) : null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}

