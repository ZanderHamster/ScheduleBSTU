package ru.lekveishvili.david.schedulebstu.service;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;

public class BottomNavigationService {
    private BehaviorSubject<Boolean> bottomNavigationVisibilityBehaviouSubject = BehaviorSubject.create();

    public BottomNavigationService() {
        bottomNavigationVisibilityBehaviouSubject.onNext(true);
    }

    public void hide() {
        bottomNavigationVisibilityBehaviouSubject.onNext(false);
    }

    public void show() {
        bottomNavigationVisibilityBehaviouSubject.onNext(true);
    }

    public Observable<Boolean> getBottomNavigationVisibilityObservable() {
        return bottomNavigationVisibilityBehaviouSubject;
    }
}