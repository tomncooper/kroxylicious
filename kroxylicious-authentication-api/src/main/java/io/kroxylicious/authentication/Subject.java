/*
 * Copyright Kroxylicious Authors.
 *
 * Licensed under the Apache Software License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */

package io.kroxylicious.authentication;

import java.util.Set;

/**
 * <p>Represents an actor in the system.
 * Subjects are composed of a possibly-empty set of identifiers represented as {@link Principal} instances.
 * An anonymous actor is represented by a Subject with an empty set of principals.</p>
 */
@FunctionalInterface
public interface Subject {
    /**
     * Returns the set of principals associated with this subject.
     * @return the set of principals associated with this subject.
     */
    Set<? extends Principal> principals();
}
