package ru.lekveishvili.david.schedulebstu.network.mappers;

import java.util.ArrayList;
import java.util.List;

import ru.lekveishvili.david.schedulebstu.models.Group;
import ru.lekveishvili.david.schedulebstu.network.models.GroupResponse;
import ru.lekveishvili.david.schedulebstu.network.service.MainApiService;

public class GroupModelMapper {
    private MainApiService mainApiService;

    public GroupModelMapper(MainApiService mainApiService) {
        this.mainApiService = mainApiService;
    }

    public List<Group> transform(GroupResponse groupResponse) {
        List<Group> result = new ArrayList<>();

        if (groupResponse.data != null) {
            for (int i = 0; i < groupResponse.data.size(); i++) {
                GroupResponse.Group group = groupResponse.data.get(i);
                result.add(Group.newBuilder()
                        .withName(group.name)
                        .withId(group.id)
                        .build());
            }
        }
        return result;
    }
}
