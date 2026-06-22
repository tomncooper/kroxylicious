/*
 * Copyright Kroxylicious Authors.
 *
 * Licensed under the Apache Software License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */

package io.kroxylicious.proxy.authentication;

import io.kroxylicious.authentication.Principal;

public interface PrincipalFactory<P extends Principal> {
    P newPrincipal(String name);
}
