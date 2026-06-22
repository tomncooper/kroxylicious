/*
 * Copyright Kroxylicious Authors.
 *
 * Licensed under the Apache Software License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */

package io.kroxylicious.proxy.internal.subject;

import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import io.kroxylicious.proxy.authentication.ClientSaslContext;
import io.kroxylicious.proxy.authentication.ProxySubject;
import io.kroxylicious.proxy.authentication.SaslSubjectBuilder;
import io.kroxylicious.proxy.authentication.User;

public class SaslSubjectBuilderImpl implements SaslSubjectBuilder {

    @Override
    public CompletionStage<ProxySubject> buildSaslSubject(Context context) {
        ClientSaslContext clientSaslContext = context.clientSaslContext();
        return CompletableFuture.completedStage(
                new ProxySubject(Set.of(new User(clientSaslContext.authorizationId()))));
    }
}
