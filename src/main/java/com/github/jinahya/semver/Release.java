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
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;


/**
 * Represents {@code Pre-release version} of {@code Semantic Versioning}.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public class Release implements Comparable<Release> {


    private static final String IDENTIFIER_REGEXP
        = "0|([1-9A-Za-z-][0-9A-Za-z-]*)";


    public static final Pattern IDENTIFIER_PATTERN
        = Pattern.compile(IDENTIFIER_REGEXP);


    public static String requireValidIdentifier(final String identifier) {

        if (identifier == null) {
            throw new NullPointerException("null identifier");
        }

        if (!IDENTIFIER_PATTERN.matcher(identifier).matches()) {
            throw new IllegalArgumentException(
                "invalid release identifier: " + identifier);
        }

        return identifier;
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
    public static class Builder implements Comparable<Builder> {


        public static Builder valueOf(final String s) {

            final Builder builder = new Builder();

            for (final String identifier : s.split("\\.")) {
                builder.identifiers(identifier);
            }

            return builder;
        }


        @Override
        public int compareTo(final Builder o) {

            if (o == null) {
                throw new NullPointerException("null o");
            }

            final List<String> identifiers1 = getIdentifiers();
            final List<String> identifiers2 = o.getIdentifiers();

            while (!identifiers1.isEmpty() && !identifiers2.isEmpty()) {
                final String identifier1 = identifiers1.remove(0);
                final String identifier2 = identifiers2.remove(0);
                System.out.println("i1: " + identifier1 + ", i2: " + identifier2);
                final boolean numeric1 = Version.IDENTIFIER_PATTERN
                    .matcher(identifier1).matches();
                final boolean numeric2 = Version.IDENTIFIER_PATTERN
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
                        System.out.println("numerically: " + numerically);
                        return numerically;
                    }
                }
                // identifiers with letters or hyphens are compared lexically in
                // ASCII sort order
                final int lexically = identifier1.compareTo(identifier2);
                if (lexically != 0) {
                    System.out.println("lexically: " + lexically);
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


        public Builder identifiers(final Iterator<String> identifiers) {

            if (identifiers == null) {
                throw new NullPointerException("null identifiers");
            }

            while (identifiers.hasNext()) {
                identifiers(identifiers.next());
            }

            return this;
        }


        public Builder identifiers(final Iterable<String> identifiers) {

            if (identifiers == null) {
                throw new NullPointerException("null identifiers");
            }

            return identifiers(identifiers.iterator());
        }


        /**
         * Builds an instance of {@code Release}.
         *
         * @return an instance of {@code Release}
         */
        public Release build() {

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

            return new Release(builder.toString());
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


    public static Release valueOf(final String s) {

        return Builder.valueOf(s).build();
    }


    private Release(final String value) {

        super();

        //this.value = requireValidValue(value);
        this.value = value;
    }


    @Override
    public int compareTo(final Release o) {

        if (o == null) {
            throw new NullPointerException("null o");
        }

        return Builder.valueOf(value).compareTo(Builder.valueOf(o.value));
    }


    @Override
    public String toString() {

        return super.toString() + "{"
               + "value=" + value
               + "}";
    }


    public String getValue() {

        return value;
    }


    private final String value;

}

