package com.tcmj.shampug.modules.custom.json;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.tcmj.shampug.ShamPug;
import com.tcmj.shampug.modules.custom.mem.CustomMem;
import com.tcmj.shampug.pub.Record;
import org.slf4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * <h2>Jackson Json Custom Deserializer for Lists of data records</h2>
 * <pre>
 * ObjectMapper mapper = new ObjectMapper();
 * SimpleModule module = new SimpleModule("CustomRecordDeserializer", new Version(1, 0, 0, null, null, null));
 * module
 *    .addDeserializer(Record.class, new CustomRecordDeserializer(SHAMPUG))
 *    .addDeserializer(List.class, new CustomRecordListDeserializer(SHAMPUG));
 * mapper.registerModule(module);
 * List<Record<CustomMem>> customMem = mapper.readValue(stream, List.class);
 * </pre>
 */
public class CustomRecordListDeserializer extends StdDeserializer<List<Record<CustomMem>>> {
    /** slf4j Logging framework. */
    private static final Logger LOG = getLogger(CustomRecordListDeserializer.class);

    private final ShamPug shamPug;

    public CustomRecordListDeserializer(ShamPug shamPug) {
        super((Class<?>) null);
        this.shamPug = Objects.requireNonNull(shamPug, "ShamPug may not be null in CustomRecordListDeserializer!");
    }

    @Override
    public List<Record<CustomMem>> deserialize(JsonParser parser, DeserializationContext ctxt) throws IOException, JacksonException {
        LOG.trace("Deserialize using CustomRecordList-Deserializer = {}", this);
        List<Record<CustomMem>> list = new ArrayList<>();
        JsonNode node = parser.getCodec().readTree(parser);

        //Loop through our category names:
        node.fields().forEachRemaining(stringJsonNodeEntry -> {
            System.out.println("stringJsonNodeEntry = " + stringJsonNodeEntry);
            final String categoryName = stringJsonNodeEntry.getKey();
            LOG.debug("Records for category: {}", categoryName);  //single record
            JsonNode value = stringJsonNodeEntry.getValue(); //all records
            Iterator<JsonNode> iterator = value.iterator();
            while (iterator.hasNext()) {
                JsonNode data = iterator.next();
                LOG.trace("DataRecord: ", data);  //single record
                CustomMem record = new CustomMem(categoryName, this.shamPug.getRandomUnit());
                data.fields().forEachRemaining(single -> {
                    JsonNode singleValue = single.getValue();
                    LOG.trace("Key='{}' Value='{}' ({})", single.getKey(), singleValue, singleValue.getNodeType());

                    switch(singleValue.getNodeType()) {
                        case NUMBER:
                            if (singleValue.isInt() || singleValue.isShort()) {
                                record.add(single.getKey(), singleValue.asInt());
                            } else if (singleValue.isDouble() || singleValue.isFloat()) {
                                record.add(single.getKey(), singleValue.asDouble());
                            } else if (singleValue.isLong()) {
                                record.add(single.getKey(), singleValue.longValue());
                            }
                            break;
                        case BOOLEAN:
                            record.add(single.getKey(), singleValue.asBoolean());
                            break;
                        case STRING:
                            record.add(single.getKey(), singleValue.asText());
                            break;
                        default:
                            record.add(single.getKey(), String.valueOf(singleValue.asText()));
                    }
                });
                list.add(record);
                LOG.debug("Adding Record {} ", record);
            }
        });
        return list;
    }
}
