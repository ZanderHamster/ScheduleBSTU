package ru.lekveishvili.david.schedulebstu.screens.home.di;

import dagger.Module;
import dagger.Provides;
import dagger.Subcomponent;
import ru.lekveishvili.david.schedulebstu.network.service.MainApiService;
import ru.lekveishvili.david.schedulebstu.network.usecase.GetGroupUseCase;
import ru.lekveishvili.david.schedulebstu.screens.home.HomeController;

@Subcomponent(modules = HomeComponent.GroupControllerModule.class)
public interface HomeComponent {
    void inject(HomeController controller);

    @Module
    class GroupControllerModule {
        @Provides
        GetGroupUseCase provideGetGroupUseCase(MainApiService mainApiService) {
            return new GetGroupUseCase(mainApiService);
        }
    }
}
