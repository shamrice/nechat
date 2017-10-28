package io.github.shamrice.nechat.core;

import io.github.shamrice.nechat.core.configuration.CoreConfiguration;
import io.github.shamrice.nechat.core.configuration.CoreConfigurationBuilder;

/**
 * Created by Erik on 10/19/2017.
 */
public class CoreContext {

    private static CoreContext instance = null;
    private CoreConfiguration coreConfiguration;

    private CoreContext() {
    }

    public static CoreContext getInstance() {
        if (instance == null) {
            instance = new CoreContext();
            instance.coreConfiguration = CoreConfigurationBuilder.build();
        }

        return instance;
    }

    public CoreConfiguration getCoreConfiguration() {
        return coreConfiguration;
    }
}
