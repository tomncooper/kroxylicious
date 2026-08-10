/*
 * Copyright Kroxylicious Authors.
 *
 * Licensed under the Apache Software License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */

package io.kroxylicious.proxy.authentication;

import java.util.Set;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

@SuppressWarnings("deprecation")
class SubjectTest {

    User user1 = new User("name");
    User user2 = new User("name2");

    FakeUniquePrincipal unique = new FakeUniquePrincipal("name");
    FakeUniquePrincipal unique2 = new FakeUniquePrincipal("name2");
    FakeSingularOnlyPrincipal singularOnly = new FakeSingularOnlyPrincipal("singular");
    FakeMultiplePrincipal foo = new FakeMultiplePrincipal("foo");
    FakeMultiplePrincipal bar = new FakeMultiplePrincipal("bar");

    @Test
    void userIsRequiredRightNow() { // but not eventually
        Assertions.assertThatThrownBy(() -> new Subject(unique))
                .hasMessage("A subject with non-empty principals must have exactly one io.kroxylicious.proxy.authentication.User principal.");
    }

    @Test
    void uniquenessIsEnforced() {
        // Given
        // When
        // Then
        Assertions.assertThatThrownBy(() -> new Subject(user1, user2))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("2 principals of class io.kroxylicious.proxy.authentication.User were found")
                .hasMessageContaining("is a singular principal type");

        Assertions.assertThatThrownBy(() -> new Subject(user1, unique, unique2))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("2 principals of class io.kroxylicious.proxy.authentication.FakeUniquePrincipal were found")
                .hasMessageContaining("is a singular principal type");
    }

    @Test
    void canExtractUniquePrincipals() {
        Subject subject = new Subject(user1, unique);
        Assertions.assertThat(subject.uniquePrincipalOfType(User.class)).hasValue(user1);
        Assertions.assertThat(subject.uniquePrincipalOfType(FakeUniquePrincipal.class)).hasValue(unique);
        Subject subject2 = new Subject(user1);
        Assertions.assertThat(subject2.uniquePrincipalOfType(FakeUniquePrincipal.class)).isEmpty();
        Assertions.assertThat(Subject.anonymous().uniquePrincipalOfType(FakeUniquePrincipal.class)).isEmpty();
    }

    @Test
    void throwIaeWhenUsingNonSingularClassWithUniqueExtractor() {
        // Given
        Subject subject = new Subject(user1, unique);

        // When
        // Then
        Assertions.assertThatThrownBy(() -> subject.uniquePrincipalOfType(FakeMultiplePrincipal.class))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("is not a singular principal type");
    }

    @Test
    void canExtractSingularOnlyPrincipals() {
        // Given
        Subject subject = new Subject(user1, singularOnly);

        // When
        // Then
        Assertions.assertThat(subject.uniquePrincipalOfType(FakeSingularOnlyPrincipal.class)).hasValue(singularOnly);
    }

    @Test
    void canExtractPrincipals() {
        Subject subject = new Subject(user1, unique, foo, bar);
        Assertions.assertThat(subject.allPrincipalsOfType(User.class)).isEqualTo(Set.of(user1));
        Assertions.assertThat(subject.allPrincipalsOfType(FakeUniquePrincipal.class)).isEqualTo(Set.of(unique));
        Assertions.assertThat(subject.allPrincipalsOfType(FakeMultiplePrincipal.class)).isEqualTo(Set.of(foo, bar));
        Subject subject2 = new Subject(user1);
        Assertions.assertThat(subject2.allPrincipalsOfType(FakeUniquePrincipal.class)).isEmpty();
        Assertions.assertThat(Subject.anonymous().allPrincipalsOfType(FakeUniquePrincipal.class)).isEmpty();
    }

    @Test
    void shouldConsiderEmptySetOfPrinciplesAnonymous() {
        // Given
        Subject emptySubject = new Subject(Set.of());

        // When
        // Then
        Assertions.assertThat(emptySubject.isAnonymous()).isTrue();
    }

    @Test
    void shouldNotConsiderSetOfPrinciplesAnonymous() {
        // Given
        Subject emptySubject = new Subject(user1, foo);

        // When
        // Then
        Assertions.assertThat(emptySubject.isAnonymous()).isFalse();
    }

    @Test
    void shouldConsiderEmptySetOfPrinciplesEqual() {
        // Given
        Subject emptySubject = new Subject(Set.of());

        // When
        // Then
        Assertions.assertThat(emptySubject).isEqualTo(Subject.anonymous());
    }
}