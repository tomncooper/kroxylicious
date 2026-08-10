/*
 * Copyright Kroxylicious Authors.
 *
 * Licensed under the Apache Software License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */

package io.kroxylicious.identity;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@SingularPrincipal
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@interface FakeMetaSingularAnnotation {
}
