/*
 * Copyright Kroxylicious Authors.
 *
 * Licensed under the Apache Software License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */

package io.kroxylicious.proxy.internal.subject;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

import io.kroxylicious.authentication.Principal;
import io.kroxylicious.proxy.authentication.SaslSubjectBuilder;
import io.kroxylicious.proxy.authentication.ProxySubject;
import io.kroxylicious.proxy.authentication.TransportSubjectBuilder;

public class DefaultSubjectBuilder implements TransportSubjectBuilder, SaslSubjectBuilder {
    private final List<PrincipalAdder> adders;

    public DefaultSubjectBuilder(List<PrincipalAdder> adders) {
        this.adders = adders;
    }

    @Override
    public CompletionStage<ProxySubject> buildTransportSubject(TransportSubjectBuilder.Context context) {
        try {
            if (adders.isEmpty()) {
                return CompletableFuture.completedFuture(ProxySubject.anonymous());
            }
            Set<Principal> collect = adders.stream()
                    .flatMap(adder -> adder.createPrincipals(context))
                    .collect(Collectors.toSet());
            return CompletableFuture.completedStage(new ProxySubject(collect));
        }
        catch (Exception e) {
            return CompletableFuture.failedStage(e);
        }
    }

    @Override
    public CompletionStage<ProxySubject> buildSaslSubject(SaslSubjectBuilder.Context context) {
        try {
            Set<Principal> collect = adders.stream()
                    .flatMap(lal -> lal.createPrincipals(context))
                    .collect(Collectors.toSet());
            return CompletableFuture.completedStage(new ProxySubject(collect));
        }
        catch (Exception e) {
            return CompletableFuture.failedStage(e);
        }
    }
}
