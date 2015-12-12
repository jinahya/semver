/*
 * Copyright 2015 Jin Kwon &lt;jinahya_at_gmail.com&gt;.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.jinahya.semver;


import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;


/**
 * Represents {@code Pre-release version} part of {@code Semantic Versioning}.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public class PreReleaseVersion implements Comparable<PreReleaseVersion> {


    private static final String IDENTIFIER_REGEXP
        = "0|([1-9A-Za-z-][0-9A-Za-z-]*)";


    /**
     * A pre-compiled pattern for identifiers.
     */
    public static final Pattern IDENTIFIER_PATTERN
        = Pattern.compile(IDENTIFIER_REGEXP);


    /**
     * Checks if given identifier is valid.
     *
     * @param <T> identifier parameter type
     * @param identifier the identifier to check
     *
     * @return the identifier if valid
     *
     * @throws NullPointerException if {@code identifier} is {@code null}
     * @throws IllegalArgumentException if {@code identifier} is not valid.
     *
     * @see #IDENTIFIER_PATTERN
     */
    public static <T extends CharSequence> T requireValidIdentifier(
        final T identifier) {

        if (!IDENTIFIER_PATTERN.matcher(identifier).matches()) {
            throw new IllegalArgumentException(
                "invalid identifier: " + identifier);
        }

        return identifier;
    }


    /**
     * Class for building {@link PreReleaseVersion}s.
     */
    public static class Builder {


        /**
         * Creates a new instance by parsing specified string.
         *
         * @param s the string to parse
         *
         * @return a new instance of {@code Builder}
         *
         * @see PreReleaseVersion#requireValidIdentifier(java.lang.CharSequence)
         */
        public static Builder valueOf(final String s) {

            final Builder builder = new Builder();

            for (final String identifier : s.split("\\.")) {
                builder.identifiers(identifier);
            }

            return builder;
        }


        public static Builder valueOf(final PreReleaseVersion built) {

            return new Builder().identifiers(built.getIdentifiers());
        }


        private void identifier(final String identifier) {

            for (final String token : identifier.split("\\.")) {
                identifiers.add(requireValidIdentifier(token));
            }
        }


        /**
         * Adds identifiers.
         *
         * @param identifier an identifier.
         * @param otherIdentifiers more identifers.
         *
         * @return this.
         *
         * @see PreReleaseVersion#requireValidIdentifier(java.lang.CharSequence)
         */
        public Builder identifiers(final String identifier,
                                   final String... otherIdentifiers) {

            identifier(identifier);

            if (otherIdentifiers != null) {
                for (final String otherIdentifier : otherIdentifiers) {
                    identifier(otherIdentifier);
                }
            }

            return this;
        }


        public Builder identifiers(final Iterator<String> identifiers) {

            while (identifiers.hasNext()) {
                identifier(identifiers.next());
            }

            return this;
        }


        public Builder identifiers(final Iterable<String> identifiers) {

            return identifiers(identifiers.iterator());
        }


        /**
         * Builds an instance of {@code Release}.
         *
         * @return an instance of {@code Release}
         */
        public PreReleaseVersion build() {

            if (identifiers.isEmpty()) {
                throw new IllegalStateException("no identifiers");
            }

            return new PreReleaseVersion(identifiers);
        }


        private final List<String> identifiers = new ArrayList<String>();

    }


    public static PreReleaseVersion valueOf(final String s) {

        return Builder.valueOf(s).build();
    }


    private PreReleaseVersion(final List<String> identifiers) {

        super();

//        if (identifiers == null) {
//            throw new NullPointerException("null identifiers");
//        }
//
//        if (identifiers.isEmpty()) {
//            throw new IllegalArgumentException("empty identifiers");
//        }
//
//        for (final String identifier : identifiers) {
//            if (!IDENTIFIER_PATTERN.matcher(identifier).matches()) {
//                throw new IllegalArgumentException(
//                    "invalid identifier: " + identifier);
//            }
//        }
        this.identifiers = Collections.unmodifiableList(identifiers);
    }


    @Override
    public int compareTo(final PreReleaseVersion o) {

        final List<String> identifiers1
            = new ArrayList<String>(getIdentifiers());
        final List<String> identifiers2
            = new ArrayList<String>(o.getIdentifiers());

        while (!identifiers1.isEmpty() && !identifiers2.isEmpty()) {
            final String identifier1 = identifiers1.remove(0);
            final String identifier2 = identifiers2.remove(0);
            final boolean numeric1 = NormalVersion.IDENTIFIER_PATTERN
                .matcher(identifier1).matches();
            final boolean numeric2 = NormalVersion.IDENTIFIER_PATTERN
                .matcher(identifier2).matches();
            // Numeric identifiers always have lower precedence than
            // non-numeric identifiers.
            if (numeric1 && !numeric2) {
                return -1;
            }
            if (!numeric1 && numeric2) {
                return 1;
            }
            // identifiers consisting of only digits are compared
            // numerically
            if (numeric1 && numeric2) {
                final int numerically = new BigInteger(identifier1)
                    .compareTo(new BigInteger(identifier2));
                if (numerically != 0) {
                    return numerically;
                }
            }
            // identifiers with letters or hyphens are compared lexically in
            // ASCII sort order
            final int lexically = identifier1.compareTo(identifier2);
            if (lexically != 0) {
                return lexically;
            }
        }
        // A larger set of pre-release fields has a higher precedence
        // than a smaller set, if all of the preceding identifiers are
        // equal.
        if (identifiers1.isEmpty() && !identifiers2.isEmpty()) {
            return -1;
        }
        if (!identifiers1.isEmpty() && identifiers2.isEmpty()) {
            return 1;
        }

        return 0;
    }


    /**
     * Returns a string representation of this release.
     *
     * @return a string representation of this release
     */
    @Override
    public String toString() {

        final StringBuilder builder = new StringBuilder();

        final Iterator<String> i = identifiers.iterator();
        if (i.hasNext()) {
            builder.append(i.next());
        }
        while (i.hasNext()) {
            builder.append('.').append(i.next());
        }

        return builder.toString();
    }


    /**
     * Returns an unmodifiable list of identifiers.
     *
     * @return an unmodifiable list of identifiers.
     */
    public List<String> getIdentifiers() {

        return identifiers;
    }


    private final List<String> identifiers;

}

