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
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public class Release {


    private static final String IDENTIFIER_REGEXP
        = "0|([1-9A-Za-z-][0-9A-Za-z-]*)";


    private static final Pattern IDENTIFIER_PATTERN
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


    public static String requireValidValue(final String value) {

        if (value == null) {
            throw new NullPointerException("null value");
        }

        for (final String identifier : value.split("\\.")) {
            requireValidIdentifier(identifier);
        }

        return value;
    }


    public static class Builder {


        public Builder identifiers(final String identifier,
                                   final String... otherIdentifiers) {

            if (identifier == null) {
                throw new NullPointerException("null identifier");
            }

            getIdentifiers().add(requireValidIdentifier(identifier));

            if (otherIdentifiers != null) {
                for (final String otherIdentifier : otherIdentifiers) {
                    getIdentifiers().add(
                        requireValidIdentifier(otherIdentifier));
                }
            }

            return this;
        }


        public Release build() {

            final StringBuilder builder = new StringBuilder();

            final Iterator<String> i = getIdentifiers().iterator();
            if (i.hasNext()) {
                builder.append(i.next());
            }
            while (i.hasNext()) {
                builder.append('.').append(i.next());
            }

            return new Release(builder.toString());
        }


        public List<String> getIdentifiers() {

            if (identifiers == null) {
                identifiers = new ArrayList<String>();
            }

            return identifiers;
        }


        private List<String> identifiers;

    }


    public static Builder valueOf(final String s) {

        final Builder builder = new Builder();

        for (final String identifier : s.split("\\.")) {
            builder.identifiers(identifier);
        }

        return builder;
    }


    private Release(final String value) {

        super();

        this.value = requireValidValue(value);
    }


    @Override
    public String toString() {

        return super.toString() + "{" + "value=" + value + "}";
    }


    public String getValue() {

        return value;
    }


    private final String value;

}

