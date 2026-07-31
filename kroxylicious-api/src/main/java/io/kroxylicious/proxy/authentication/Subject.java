/*
 * Copyright Kroxylicious Authors.
 *
 * Licensed under the Apache Software License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */

package io.kroxylicious.proxy.authentication;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import io.kroxylicious.identity.Identity;
import io.kroxylicious.identity.SingularPrincipal;

/**
 * <p>Represents an actor in the system.
 * Subjects are composed of a possibly-empty set of identifiers represented as {@link Principal} instances.
 * An anonymous actor is represented by a Subject with an empty set of principals.
 * As a convenience, {@link Subject#anonymous()} returns such a subject.
 * </p>
 *
 * <p>The principals included in a subject might comprise the following:</p>
 * <ul>
 * <li>information proven by a client, such as a SASL authorized id,</li>
 * <li>information known about the client, such as the remote peer's IP address,</li>
 * <li>information obtained about the client from a trusted source, such as lookup up role or group information from a directory.</li>
 * </ul>
 *
 * @param principals the set of identifiers associated with this subject.
 */
@Deprecated
@SuppressWarnings("deprecation")
public record Subject(Set<Principal> principals) implements Identity {

    private static final Subject ANONYMOUS = new Subject(Set.of());

    /**
     * Returns the anonymous subject (no principals).
     * @return the anonymous subject
     */
    public static Subject anonymous() {
        return ANONYMOUS;
    }

    /**
     * Creates a subject from the given principals.
     * @param principals the principals
     */
    public Subject(Principal... principals) {
        this(Set.of(principals));
    }

    /**
     * Creates a subject from the given principal set. Validates that non-empty subjects have exactly one {@link User} principal.
     * @param principals the principals
     */
    public Subject(Set<Principal> principals) {
        principals.stream()
                .collect(Collectors.groupingBy(Object::getClass))
                .forEach((principalClass, instances) -> {
                    if (isSingularPrincipal(principalClass) && instances.size() > 1) {
                        throw new IllegalArgumentException(
                                instances.size() + " principals of " + principalClass + " were found, but " + principalClass + " is annotated with "
                                        + singularAnnotationOn(principalClass));
                    }
                });
        Optional<User> user = uniquePrincipalOfType(principals, User.class);
        if (!principals.isEmpty() && user.isEmpty()) {
            throw new IllegalArgumentException("A subject with non-empty principals must have exactly one " + User.class.getName() + " principal.");
        }
        this.principals = Set.copyOf(principals);
    }

    /**
     * Returns the unique principal of the given type, if present.
     * @param uniquePrincipalType the principal type, which must be annotated with {@link Unique}
     * @param <P> the principal type
     * @return the principal, or empty
     */
    @Override
    public <P extends io.kroxylicious.identity.Principal> Optional<P> uniquePrincipalOfType(Class<P> uniquePrincipalType) {
        return uniquePrincipalOfType(this.principals, uniquePrincipalType);
    }

    /**
     * Returns whether this is the anonymous subject.
     * @return true if this subject has no principals
     */
    @Override
    public boolean isAnonymous() {
        return this.principals.isEmpty();
    }

    private static <P extends io.kroxylicious.identity.Principal> Optional<P> uniquePrincipalOfType(Set<? extends io.kroxylicious.identity.Principal> principals,
                                                                                                    Class<P> uniquePrincipalType) {
        if (isSingularPrincipal(uniquePrincipalType)) {
            return principals.stream()
                    .filter(uniquePrincipalType::isInstance)
                    .map(uniquePrincipalType::cast)
                    .findFirst();
        }
        else {
            throw new IllegalArgumentException(uniquePrincipalType + " is not annotated with " + singularAnnotationExpected());
        }
    }

    private static boolean isSingularPrincipal(Class<?> principalClass) {
        return principalClass.isAnnotationPresent(SingularPrincipal.class) || principalClass.isAnnotationPresent(Unique.class);
    }

    private static Class<?> singularAnnotationOn(Class<?> principalClass) {
        if (principalClass.isAnnotationPresent(SingularPrincipal.class)) {
            return SingularPrincipal.class;
        }
        return Unique.class;
    }

    private static String singularAnnotationExpected() {
        return SingularPrincipal.class + " or " + Unique.class;
    }

    /**
     * Returns all principals of the given type.
     * @param principalType the principal type
     * @param <P> the principal type
     * @return the matching principals
     */
    @Override
    public <P extends io.kroxylicious.identity.Principal> Set<P> allPrincipalsOfType(Class<P> principalType) {
        return this.principals.stream()
                .filter(principalType::isInstance)
                .map(principalType::cast)
                .collect(Collectors.toSet());
    }

}
