package org.appfuse.webapp.client.application;

import org.appfuse.webapp.client.application.base.place.EntityProxyPlace;
import org.appfuse.webapp.client.application.base.place.EntitySearchPlace;
import org.appfuse.webapp.client.requests.ApplicationRequestFactory;

import com.google.inject.Inject;

public class ApplicationPlaceHistoryFactory {
    private final EntityProxyPlace.Tokenizer entityPlaceTokenizer;
    private final EntitySearchPlace.Tokenizer entityListPlaceTokenizer;

    @Inject
    public ApplicationPlaceHistoryFactory(ApplicationRequestFactory requestFactory, ApplicationProxyFactory applicationProxyFactory) {
        this.entityListPlaceTokenizer = new EntitySearchPlace.Tokenizer(applicationProxyFactory, requestFactory);
        this.entityPlaceTokenizer = new EntityProxyPlace.Tokenizer(requestFactory);
    }

    public EntityProxyPlace.Tokenizer getEntityPlaceTokenizer() {
        return entityPlaceTokenizer;
    }

    public EntitySearchPlace.Tokenizer getEntityListPlaceTokenizer() {
        return entityListPlaceTokenizer;
    }
}
