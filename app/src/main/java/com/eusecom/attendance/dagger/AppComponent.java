package com.eusecom.attendance.dagger;

import com.eusecom.attendance.DaggerMainActivity;
import com.eusecom.attendance.DaggerProductListener;

import javax.inject.Singleton;
import dagger.Component;

/**
 * Created by Valentine on 4/20/2016.
 */
@Singleton
@Component(
        modules = {
                AppModule.class,
                ShoppingCartModule.class
        }
)
public interface AppComponent {
    void inject(DaggerProductListener presenter);
    void inject(DaggerMainActivity activity);

}
