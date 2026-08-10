/*
 * Copyright Kroxylicious Authors.
 *
 * Licensed under the Apache Software License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */

package io.kroxylicious.identity;

import java.lang.annotation.Annotation;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Checks whether a type is a singular principal type, either by direct annotation
 * or via a one-level meta-annotation scan, and validates uniqueness constraints.
 *
 * <p>This class exists to share validation logic between the legacy
 * {@code io.kroxylicious.proxy.authentication.Subject} and the new {@link Subject} during the
 * migration period. At 1.0, the meta-annotation scanning will no longer be needed and
 * this validation should be inlined into {@link Subject} directly.</p>
 *
 * @deprecated Transitional utility. Will be removed at 1.0 when the legacy Subject and
 *             {@link io.kroxylicious.identity.SingularPrincipal @SingularPrincipal}-based
 *             meta-annotation scanning are no longer required.
 */
@Deprecated(since = "0.24.0", forRemoval = true)
public final class SingularPrincipals {

    private SingularPrincipals() {
    }

    /**
     * Validates that at most one principal of each singular principal type is present.
     *
     * @param principals the principals to validate
     * @throws IllegalArgumentException if any singular principal type has more than one instance
     */
    public static void validateUniqueness(Set<? extends Principal> principals) {
        principals.stream()
                .collect(Collectors.groupingBy(Object::getClass))
                .forEach((principalClass, instances) -> {
                    if (isSingular(principalClass) && instances.size() > 1) {
                        throw new IllegalArgumentException(
                                instances.size() + " principals of " + principalClass + " were found, but " + principalClass
                                        + " is a singular principal type");
                    }
                });
    }

    /**
     * Returns whether the given type is a singular principal type.
     * A type is singular if it is annotated with {@link SingularPrincipal} directly,
     * or if any of its annotations are themselves annotated with {@link SingularPrincipal}.
     *
     * @param type the type to check
     * @return true if the type is a singular principal type
     */
    public static boolean isSingular(Class<?> type) {
        if (type.isAnnotationPresent(SingularPrincipal.class)) {
            return true;
        }
        for (Annotation annotation : type.getAnnotations()) {
            if (annotation.annotationType().isAnnotationPresent(SingularPrincipal.class)) {
                return true;
            }
        }
        return false;
    }
}
