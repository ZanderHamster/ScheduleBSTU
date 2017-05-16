package ru.lekveishvili.david.schedulebstu.network.mappers;

import java.util.ArrayList;
import java.util.List;

import ru.lekveishvili.david.schedulebstu.models.EventType;
import ru.lekveishvili.david.schedulebstu.models.Teacher;
import ru.lekveishvili.david.schedulebstu.network.models.EventTypesResponse;
import ru.lekveishvili.david.schedulebstu.network.models.TeacherResponse;
import ru.lekveishvili.david.schedulebstu.network.service.MainApiService;

public class EventTypeModelMapper {
    private MainApiService mainApiService;

    public EventTypeModelMapper(MainApiService mainApiService) {
        this.mainApiService = mainApiService;
    }

    public List<EventType> transform(EventTypesResponse eventTypeResponse) {
        List<EventType> result = new ArrayList<>();

        for (int i = 0; i < eventTypeResponse.eventTypes.size(); i++) {
            EventTypesResponse.EventType eventType = eventTypeResponse.eventTypes.get(i);
            result.add(EventType.newBuilder()
                    .withName(eventType.name)
                    .withId(eventType.id)
                    .build());
        }
        return result;
    }
}
