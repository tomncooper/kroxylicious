/*
 * Copyright Kroxylicious Authors.
 *
 * Licensed under the Apache Software License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */

package io.kroxylicious.identity;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

@SuppressWarnings("deprecation")
class IdentityTest {

    @Test
    void anonymousIdentityIsAnonymous() {
        // Given
        Identity anonymous = Identity.anonymous();

        // When
        // Then
        Assertions.assertThat(anonymous.isAnonymous()).isTrue();
        Assertions.assertThat(anonymous.principals()).isEmpty();
    }

    @Test
    void anonymousIdentityReturnsSameInstance() {
        // Given
        // When
        // Then
        Assertions.assertThat(Identity.anonymous()).isSameAs(Identity.anonymous());
    }

    @Test
    void anonymousIdentityEqualsAnonymousSubject() {
        // Given
        Identity anonymous = Identity.anonymous();
        Subject anonymousSubject = Subject.anonymous();

        // When
        // Then
        Assertions.assertThat(anonymous).isEqualTo(anonymousSubject);
    }

    @Test
    void anonymousIdentityToString() {
        // Given
        // When
        // Then
        Assertions.assertThat(Identity.anonymous()).hasToString("Identity[anonymous]");
    }
}
