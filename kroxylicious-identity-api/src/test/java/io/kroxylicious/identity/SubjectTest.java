/*
 * Copyright Kroxylicious Authors.
 *
 * Licensed under the Apache Software License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */

package io.kroxylicious.identity;

import java.util.Set;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

@SuppressWarnings("deprecation")
class SubjectTest {

    FakeSingularPrincipal singular = new FakeSingularPrincipal("name");
    FakeSingularPrincipal singular2 = new FakeSingularPrincipal("name2");
    FakeMultiplePrincipal foo = new FakeMultiplePrincipal("foo");
    FakeMultiplePrincipal bar = new FakeMultiplePrincipal("bar");

    @Test
    void singularUniquenessIsEnforced() {
        // Given
        // When
        // Then
        Assertions.assertThatThrownBy(() -> new Subject(singular, singular2))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("2 principals of class io.kroxylicious.identity.FakeSingularPrincipal were found")
                .hasMessageContaining("is annotated with interface io.kroxylicious.identity.SingularPrincipal");
    }

    @Test
    void multipleNonSingularPrincipalsAllowed() {
        // Given
        // When
        Subject subject = new Subject(singular, foo, bar);

        // Then
        Assertions.assertThat(subject.principals()).isEqualTo(Set.of(singular, foo, bar));
    }

    @Test
    void canExtractUniquePrincipals() {
        // Given
        Subject subject = new Subject(singular, foo);

        // When
        // Then
        Assertions.assertThat(subject.uniquePrincipalOfType(FakeSingularPrincipal.class)).hasValue(singular);
    }

    @Test
    void uniquePrincipalOfTypeReturnsEmptyWhenAbsent() {
        // Given
        Subject subject = new Subject(foo);

        // When
        // Then
        Assertions.assertThat(subject.uniquePrincipalOfType(FakeSingularPrincipal.class)).isEmpty();
        Assertions.assertThat(Subject.anonymous().uniquePrincipalOfType(FakeSingularPrincipal.class)).isEmpty();
    }

    @Test
    void throwIaeWhenUsingNonSingularClassWithUniqueExtractor() {
        // Given
        Subject subject = new Subject(singular, foo);

        // When
        // Then
        Assertions.assertThatThrownBy(() -> subject.uniquePrincipalOfType(FakeMultiplePrincipal.class))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("is not annotated with interface io.kroxylicious.identity.SingularPrincipal");
    }

    @Test
    void canExtractPrincipals() {
        // Given
        Subject subject = new Subject(singular, foo, bar);

        // When
        // Then
        Assertions.assertThat(subject.allPrincipalsOfType(FakeSingularPrincipal.class)).isEqualTo(Set.of(singular));
        Assertions.assertThat(subject.allPrincipalsOfType(FakeMultiplePrincipal.class)).isEqualTo(Set.of(foo, bar));
    }

    @Test
    void allPrincipalsOfTypeReturnsEmptyWhenAbsent() {
        // Given
        Subject subject = new Subject(foo);

        // When
        // Then
        Assertions.assertThat(subject.allPrincipalsOfType(FakeSingularPrincipal.class)).isEmpty();
        Assertions.assertThat(Subject.anonymous().allPrincipalsOfType(FakeSingularPrincipal.class)).isEmpty();
    }

    @Test
    void emptySetOfPrincipalsIsAnonymous() {
        // Given
        Subject emptySubject = new Subject(Set.of());

        // When
        // Then
        Assertions.assertThat(emptySubject.isAnonymous()).isTrue();
    }

    @Test
    void nonEmptySetOfPrincipalsIsNotAnonymous() {
        // Given
        Subject subject = new Subject(foo);

        // When
        // Then
        Assertions.assertThat(subject.isAnonymous()).isFalse();
    }

    @Test
    void emptySetOfPrincipalsEqualsAnonymous() {
        // Given
        Subject emptySubject = new Subject(Set.of());

        // When
        // Then
        Assertions.assertThat(emptySubject).isEqualTo(Subject.anonymous());
    }

    @Test
    void anonymousFactoryReturnsSameInstance() {
        // Given
        // When
        // Then
        Assertions.assertThat(Subject.anonymous()).isSameAs(Subject.anonymous());
    }

    @Test
    void principalsSetIsDefensivelyCopied() {
        // Given
        java.util.HashSet<Principal> mutable = new java.util.HashSet<>();
        mutable.add(foo);
        Subject subject = new Subject(mutable);

        // When
        mutable.add(bar);

        // Then
        Assertions.assertThat(subject.principals()).isEqualTo(Set.of(foo));
    }
}
