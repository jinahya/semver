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
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;


/**
 * Represents a {@code build metadata} of the {@code Semantic Versioning}.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public class Metadata {


    private static final String IDENTIFIER_REGEXP = "[0-9A-Za-z-]+";


    public static final Pattern IDENTIFIER_PATTERN
        = Pattern.compile(IDENTIFIER_REGEXP);


    public static <T extends CharSequence> T requireValidIdentifier(
        final T indentifier) {

        if (indentifier == null) {
            throw new NullPointerException("null identifier");
        }

        if (!IDENTIFIER_PATTERN.matcher(indentifier).matches()) {
            throw new IllegalArgumentException(
                "invalid metadata identifier: " + indentifier);
        }

        return indentifier;
    }


//    public static String requireValidValue(final String value) {
//
//        if (value == null) {
//            throw new NullPointerException("null value");
//        }
//
//        for (final String identifier : value.split("\\.")) {
//            requireValidIdentifier(identifier);
//        }
//
//        return value;
//    }
    public static class Builder {


        public static Builder valueOf(final String s) {

            final Builder builder = new Builder();

            for (final String identifier : s.split("\\.")) {
                builder.identifiers(identifier);
            }

            return builder;
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

            if (identifier == null) {
                throw new NullPointerException("null identifier");
            }

            identifiers.add(requireValidIdentifier(identifier));

            if (otherIdentifiers != null) {
                for (final String otherIdentifier : otherIdentifiers) {
                    identifiers.add(requireValidIdentifier(otherIdentifier));
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

            if (identifiers == null) {
                throw new NullPointerException("null identifiers");
            }

            while (identifiers.hasNext()) {
                identifiers(identifiers.next());
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

            if (identifiers == null) {
                throw new NullPointerException("null identifiers");
            }

            return identifiers(identifiers.iterator());
        }


        /**
         * Builds to an instance of {@code Metadata}.
         *
         * @return an instance of {@code Metadata}
         */
        public Metadata build() {

            if (identifiers.isEmpty()) {
                throw new IllegalStateException("no identifiers");
            }

            final StringBuilder builder = new StringBuilder();

            final Iterator<String> i = identifiers.iterator();
            if (i.hasNext()) {
                builder.append(i.next());
            }
            while (i.hasNext()) {
                builder.append('.').append(i.next());
            }

            return new Metadata(builder.toString());
        }


        /**
         * Returns a copied list of identifiers.
         *
         * @return a copied list of identifiers.
         */
        public List<String> getIdentifiers() {

            return new ArrayList<String>(identifiers);
        }


        private final List<String> identifiers = new ArrayList<String>();

    }


    public static Metadata valueOf(final String s) {

        return Builder.valueOf(s).build();
    }


    private Metadata(final String value) {

        super();

//        this.value = requireValidValue(value);
        this.value = value;
    }


    @Override
    public String toString() {

        return super.toString() + "{"
               + "value=" + value
               + "}";
    }


    /**
     * Returns the value of this instance.
     *
     * @return the value.
     */
    public String getValue() {

        return value;
    }


    private final String value;

}

