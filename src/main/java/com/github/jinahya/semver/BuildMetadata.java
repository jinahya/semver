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


import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;


/**
 * Represents a {@code build metadata} part of the {@code Semantic Versioning}.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public class BuildMetadata {


    private static final String IDENTIFIER_REGEXP = "[0-9A-Za-z-]+";


    /**
     * A pre-compiled pattern for identifiers.
     */
    public static final Pattern IDENTIFIER_PATTERN
        = Pattern.compile(IDENTIFIER_REGEXP);


    /**
     * Checks whether given identifier is valid.
     *
     * @param <T> identifier type parameter
     * @param indentifier the identifier to be checked.
     *
     * @return given {@code identifier}
     *
     * @throws NullPointerException if {@code identifier} is {@code null}.
     * @throws IllegalArgumentException if {@code identifier} is not valid.
     *
     * @see #IDENTIFIER_PATTERN
     */
    public static <T extends CharSequence> T requireValidIdentifier(
        final T indentifier) {

        if (!IDENTIFIER_PATTERN.matcher(indentifier).matches()) {
            throw new IllegalArgumentException("invalid: " + indentifier);
        }

        return indentifier;
    }


    /**
     * Class for building {@link BuildMetadata}s.
     */
    public static class Builder {


        public static Builder valueOf(final String s) {

            final Builder builder = new Builder();

            for (final String identifier : s.split("\\.")) {
                builder.identifiers(identifier);
            }

            return builder;
        }


        public static Builder valueOf(final BuildMetadata built) {

            return new Builder().identifiers(built.getIdentifiers());
        }


        private void identifier(final String identifier) {

            for (final String token : identifier.split("\\.")) {
                identifiers.add(requireValidIdentifier(token));
            }
        }


        /**
         * Adds identifies.
         *
         * @param identifier an identifier
         * @param otherIdentifiers more identifiers
         *
         * @return this
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


        /**
         * Adds identifiers.
         *
         * @param identifiers identifiers
         *
         * @return this
         */
        public Builder identifiers(final Iterator<String> identifiers) {

            while (identifiers.hasNext()) {
                identifier(identifiers.next());
            }

            return this;
        }


        /**
         * Adds identifiers.
         *
         * @param identifiers identifiers
         *
         * @return this
         */
        public Builder identifiers(final Iterable<String> identifiers) {

            return identifiers(identifiers.iterator());
        }


        /**
         * Builds to an instance of {@code Metadata}.
         *
         * @return an instance of {@code Metadata}
         */
        public BuildMetadata build() {

            if (identifiers.isEmpty()) {
                throw new IllegalStateException("no identifiers");
            }

            return new BuildMetadata(identifiers);
        }


        private final List<String> identifiers = new ArrayList<String>();

    }


    /**
     * Returns a {@code Metadata} built from specified string.
     *
     * @param s the string
     *
     * @return a {@code Metadata}
     *
     * @see Builder#valueOf(java.lang.String)
     */
    public static BuildMetadata valueOf(final String s) {

        return Builder.valueOf(s).build();
    }


    private BuildMetadata(final List<String> identifiers) {

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


    /**
     * Returns a string representation of this metadata.
     *
     * @return a string representation of this metadata
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

