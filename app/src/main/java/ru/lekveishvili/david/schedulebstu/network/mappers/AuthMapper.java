package ru.lekveishvili.david.schedulebstu.network.mappers;

import io.realm.RealmList;
import ru.lekveishvili.david.schedulebstu.models.Authorization;
import ru.lekveishvili.david.schedulebstu.models.Group;
import ru.lekveishvili.david.schedulebstu.network.models.AuthResponse;
import ru.lekveishvili.david.schedulebstu.network.service.MainApiService;

public class AuthMapper {

    private MainApiService mainApiService;

    public AuthMapper(MainApiService mainApiService) {
        this.mainApiService = mainApiService;
    }

    public Authorization transform(AuthResponse authResponse) {
        String fullName = authResponse.data.name;
        String[] parts = fullName.split(" ");
        RealmList<Group> groupList = new RealmList<>();
        for (int i = 0; i < authResponse.data.groups.size(); i++) {
            groupList.add(Group.newBuilder()
                    .withName(authResponse.data.groups.get(i))
                    .build());
        }
        //TODO сделать нормальный парсер, независящий от колличества пробелов в имени
        return Authorization.newBuilder()
                .withFullName(fullName)
                .withFirstName(parts[0])
                .withSecondName(parts[1])
                .withThirdName(parts[2])
                .withToken(authResponse.data.token)
                .withTypeUser(authResponse.data.typeUser)
                .withGroups(groupList)
                .build();

    }
}
