package com.easysoft.ecommerce.model.json;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by vutran on 1/7/2019.
 */
public class DateSerializer extends JsonSerializer<Date> {

    public void serialize(Date dt, JsonGenerator jsonGen, SerializerProvider serProv)
            throws IOException {
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = sdf.format(dt);
        jsonGen.writeString(formattedDate);
    }
}
