package org.appfuse.webapp.client.application;

import org.appfuse.webapp.client.application.base.place.EntityListPlace;
import org.appfuse.webapp.client.application.base.place.EntityProxyPlace;
import org.appfuse.webapp.requests.ApplicationRequestFactory;

import com.google.inject.Inject;

public class ApplicationPlaceHistoryFactory {
	private final EntityProxyPlace.Tokenizer entityPlaceTokenizer;
	private final EntityListPlace.Tokenizer entityListPlaceTokenizer;

	@Inject
	public ApplicationPlaceHistoryFactory(ApplicationRequestFactory requestFactory, ApplicationProxyFactory applicationProxyFactory) {
		this.entityListPlaceTokenizer = new EntityListPlace.Tokenizer(applicationProxyFactory, requestFactory);
		this.entityPlaceTokenizer = new EntityProxyPlace.Tokenizer(requestFactory);
	}

	public EntityProxyPlace.Tokenizer getEntityPlaceTokenizer() {
		return entityPlaceTokenizer;
	}

	public EntityListPlace.Tokenizer getEntityListPlaceTokenizer() {
		return entityListPlaceTokenizer;
	}
}
