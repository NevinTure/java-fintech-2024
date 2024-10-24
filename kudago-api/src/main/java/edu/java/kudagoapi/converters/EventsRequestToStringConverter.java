package edu.java.kudagoapi.converters;

import edu.java.kudagoapi.dtos.events.EventsRequest;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class EventsRequestToStringConverter implements Converter<EventsRequest, String> {
    @Override
    public String convert(EventsRequest source) {
        return String.format("expand=%s&fields=%s&actual_since=%s&actual_until=%s&page_size=%d",
                String.join(",", source.getExpand()),
                String.join(",", source.getFields()),
                source.getFromDate().toString(),
                source.getToDate().toString(),
                source.getPageSize()
        );
    }
}
