package io.github.shamrice.nechat.core.db.dto.util;

import io.github.shamrice.nechat.core.db.dto.DbDto;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Created by Erik on 11/22/2017.
 */
public class DtoConverterUtil<T> {



    public T something(DbDto dtoToConvert) {

        System.out.println("BLAH? " +  this.getClass().getGenericSuperclass().toString());
        System.out.println("BLAH? " +  this.getClass().getName());
        Type tType = ((ParameterizedType)this).getActualTypeArguments()[0];

        System.out.println("BLAH2? " + tType.getTypeName());

        T result = null;

        if (dtoToConvert != null) {
            try {
                System.out.println("Converting to messsagesDto: " + dtoToConvert.getClass().getSimpleName());
                result = (T) dtoToConvert;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return result;
    }
}
