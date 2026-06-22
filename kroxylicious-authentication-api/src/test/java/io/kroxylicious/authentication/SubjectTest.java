/*
 * Copyright Kroxylicious Authors.
 *
 * Licensed under the Apache Software License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */

package io.kroxylicious.authentication;

import java.util.Set;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SubjectTest {

    @Test
    void shouldWorkAsFunctionalInterface() {
        Subject subject = () -> Set.of(new TestPrincipal("alice"));
        assertThat(subject.principals()).hasSize(1);
    }

    @Test
    void shouldSupportNonEmptyPrincipalSet() {
        TestPrincipal alice = new TestPrincipal("alice");
        TestPrincipal bob = new TestPrincipal("bob");
        Set<TestPrincipal> expected = Set.of(alice, bob);
        Subject subject = () -> expected;
        assertThat(subject.principals()).hasSize(2);
        assertThat(subject.principals()).isEqualTo(expected);
    }

    @Test
    void shouldSupportEmptyPrincipalSet() {
        Subject subject = Set::of;
        assertThat(subject.principals()).isEmpty();
    }

    record TestPrincipal(String name) implements Principal {
    }
}
