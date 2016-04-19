/*
 * Copyright (c) The Distance Agency Ltd 2016.
 */

package uk.co.thedistance.components.pocketseodemo.injection;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import uk.co.thedistance.components.pocketseodemo.model.DataRepository;
import uk.co.thedistance.components.pocketseodemo.model.DataRepositoryImpl;
import uk.co.thedistance.components.pocketseodemo.mozscape.MSHelper;
import uk.co.thedistance.components.pocketseodemo.mozscape.MSWebService;

@Module
public class ApplicationModule {

    public ApplicationModule() {
    }

    @Provides
    @Singleton
    DataRepository provideDataRepository(MSWebService mozWebService, MSHelper.Authenticator mMozAuthenticator) {
        return new DataRepositoryImpl(mozWebService, mMozAuthenticator);
    }

}
