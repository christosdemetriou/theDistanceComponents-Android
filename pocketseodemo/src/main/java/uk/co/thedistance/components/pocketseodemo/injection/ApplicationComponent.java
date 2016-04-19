/*
 * Copyright (c) The Distance Agency Ltd 2016.
 */

package uk.co.thedistance.components.pocketseodemo.injection;

import javax.inject.Singleton;

import dagger.Component;
import uk.co.thedistance.components.pocketseodemo.model.DataRepository;

@Singleton
@Component(modules = {ApplicationModule.class, MozscapeModule.class})
public interface ApplicationComponent {

    DataRepository repository();

}
