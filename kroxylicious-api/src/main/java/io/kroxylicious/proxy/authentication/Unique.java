/*
 * Copyright Kroxylicious Authors.
 *
 * Licensed under the Apache Software License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */

package io.kroxylicious.proxy.authentication;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import io.kroxylicious.identity.SingularPrincipal;

/**
 * <p>Annotates implementations {@link Principal} which are only intended to
 * have a single instance present in a {@link Subject}'s {@code principals}.</p>
 *
 * <p>Annotated classes may then be used with {@link Subject#uniquePrincipalOfType(Class)}.</p>
 *
 * @deprecated Use {@link io.kroxylicious.identity.SingularPrincipal} instead. Will be removed at 1.0.
 */
@Deprecated(since = "0.24.0", forRemoval = true)
@SingularPrincipal
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Unique {
}
