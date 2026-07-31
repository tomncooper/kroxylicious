/*
 * Copyright Kroxylicious Authors.
 *
 * Licensed under the Apache Software License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */

package io.kroxylicious.identity;

import java.util.Set;

/**
 * Lightweight anonymous identity implementation used by {@link Identity#anonymous()}.
 */
@SuppressWarnings("deprecation")
final class AnonymousIdentity implements Identity {

    static final AnonymousIdentity INSTANCE = new AnonymousIdentity();

    private AnonymousIdentity() {
    }

    @Override
    public Set<? extends Principal> principals() {
        return Set.of();
    }

    @Override
    public boolean isAnonymous() {
        return true;
    }

    @Override
    public String toString() {
        return "Identity[anonymous]";
    }

    @Override
    public int hashCode() {
        return 0;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Identity id && id.isAnonymous();
    }
}
