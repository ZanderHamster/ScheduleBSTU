package ru.lekveishvili.david.schedulebstu.network.mappers;

import java.util.ArrayList;
import java.util.List;

import ru.lekveishvili.david.schedulebstu.models.ClassTime;
import ru.lekveishvili.david.schedulebstu.network.models.ClassTimeResponse;
import ru.lekveishvili.david.schedulebstu.network.service.MainApiService;

public class ClassTimeMapper {
    private MainApiService mainApiService;

    public ClassTimeMapper(MainApiService mainApiService) {
        this.mainApiService = mainApiService;
    }

    public List<ClassTime> transform(ClassTimeResponse classTimeResponse) {
        List<ClassTime> result = new ArrayList<>();

        if (classTimeResponse.data != null) {
            for (int i = 0; i < classTimeResponse.data.size(); i++) {
                ClassTimeResponse.Datum datum = classTimeResponse.data.get(i);

                result.add(ClassTime.newBuilder()
                        .withId(datum.id)
                        .withStart(datum.time.start)
                        .withEnd(datum.time.end)
                        .build());
            }
        }
        return result;
    }
}
