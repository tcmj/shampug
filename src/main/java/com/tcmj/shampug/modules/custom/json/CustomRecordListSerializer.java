package com.tcmj.shampug.modules.custom.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.tcmj.shampug.modules.custom.mem.CustomMem;
import com.tcmj.shampug.pub.Record;

import java.io.IOException;
import java.util.List;

public class CustomRecordListSerializer extends StdSerializer<List<Record<CustomMem>>> {

    public CustomRecordListSerializer() {
        this(null);
    }

    public CustomRecordListSerializer(Class<List<Record<CustomMem>>> t) {
        super(t);
    }

    @Override
    public void serialize(List<Record<CustomMem>> value, JsonGenerator gen, SerializerProvider provider) throws IOException {

        gen.writeStartObject();    // {


        gen.writeFieldName("catty"); // todo from constructor
        gen.writeStartArray();   // [

        for (Record<CustomMem> customMemRecord : value) {

            gen.writeStartObject();  // {

            for (String token : customMemRecord.getTokens()) {  //todo total falsch nur um compilable zu bekommen

                Comparable comparable = customMemRecord.get(token);   //all single key:value pairs


                if (comparable instanceof Boolean) {
                    gen.writeBooleanField(token, (boolean) comparable);
                } else if (comparable instanceof Character) {
                    gen.writeStringField(token, String.valueOf(comparable));
                } else if (comparable instanceof Double) {
                    gen.writeNumberField(token, (Double) comparable);
                } else {
                    gen.writeStringField(token, String.valueOf(comparable));

                }

            }
            gen.writeEndObject();   // }
        }

        gen.writeEndArray();        // ]
        gen.writeEndObject();    // }

    }

}
