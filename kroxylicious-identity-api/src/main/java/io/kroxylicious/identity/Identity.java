/*
 * Copyright Kroxylicious Authors.
 *
 * Licensed under the Apache Software License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */

package io.kroxylicious.identity;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>A bridge interface representing an actor in the system, composed of a set of {@link Principal} instances.
 * This interface exists to allow both the legacy {@code io.kroxylicious.proxy.authentication.Subject} record
 * and the new {@link Subject} record to be passed to the Authorizer API during the migration period.</p>
 *
 * <p>External consumers should target {@link Subject} directly. This interface will be removed at 1.0.</p>
 *
 * @deprecated This bridge interface will be removed at 1.0. Use {@link Subject} directly.
 */
@Deprecated(since = "0.24.0", forRemoval = true)
public interface Identity {

    /**
     * Returns the set of principals associated with this identity.
     * @return the principals
     */
    Set<? extends Principal> principals();

    /**
     * Returns the unique principal of the given type, if present.
     * @param uniquePrincipalType the principal type, which must be annotated with {@link SingularPrincipal}
     * @param <P> the principal type
     * @return the principal, or empty
     * @throws IllegalArgumentException if the type is not annotated with {@link SingularPrincipal}
     */
    default <P extends Principal> Optional<P> uniquePrincipalOfType(Class<P> uniquePrincipalType) {
        if (!uniquePrincipalType.isAnnotationPresent(SingularPrincipal.class)) {
            throw new IllegalArgumentException(uniquePrincipalType + " is not annotated with " + SingularPrincipal.class);
        }
        return principals().stream()
                .filter(uniquePrincipalType::isInstance)
                .map(uniquePrincipalType::cast)
                .findFirst();
    }

    /**
     * Returns all principals of the given type.
     * @param principalType the principal type
     * @param <P> the principal type
     * @return the matching principals
     */
    default <P extends Principal> Set<P> allPrincipalsOfType(Class<P> principalType) {
        return principals().stream()
                .filter(principalType::isInstance)
                .map(principalType::cast)
                .collect(Collectors.toSet());
    }

    /**
     * Returns whether this identity is anonymous (has no principals).
     * @return true if this identity has no principals
     */
    default boolean isAnonymous() {
        return principals().isEmpty();
    }

    /**
     * Returns an anonymous identity with no principals.
     * @return an anonymous identity
     */
    static Identity anonymous() {
        return AnonymousIdentity.INSTANCE;
    }
}
