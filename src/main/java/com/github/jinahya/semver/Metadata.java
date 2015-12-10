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


import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;
import javax.validation.constraints.NotNull;


/**
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public class Metadata {


    protected static final String IDENTIFIER_REGEXP = "[0-9A-Za-z-]+";


    private static final Pattern IDENTIFIER_PATTERN
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


        public Builder value(final String value) {

            this.value = requireValidValue(value);

            return this;
        }


        public Builder identifiers(final String identifier,
                                   final String... otherIdentifiers) {

            if (identifier == null) {
                throw new NullPointerException("null identifiers");
            }

            identifiers.add(requireValidIdentifier(identifier));

            if (otherIdentifiers != null) {
                for (final String otherIdentifier : otherIdentifiers) {
                    identifiers.add(requireValidIdentifier(otherIdentifier));
                }
            }

            value = null;

            return this;
        }


        public Metadata build() {

            return new Metadata(value());
        }


        private String value() {

            if (value != null) {
                return null;
            }

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


        private String value;


        private final List<String> identifiers = new ArrayList<String>();

    }


    public static Builder valueOf(final String s) {

        final Builder builder = new Builder();

        for (final String identifier : s.split("\\.")) {
            builder.identifiers(identifier);
        }

        return builder;
    }


    private Metadata(final String value) {

        super();

        this.value = requireValidValue(value);
    }


    @Override
    public String toString() {

        return super.toString() + "{"
               + "value=" + value
               + "}";
    }


    public <T extends Appendable> T append(final T appendable)
        throws IOException {

        if (appendable == null) {
            throw new NullPointerException("null appendable");
        }

        if (!value.isEmpty()) {
            appendable.append('+').append(value);
        }

        return appendable;
    }


    public StringBuilder append(final StringBuilder builder) {

        try {
            return (StringBuilder) append((Appendable) builder);
        } catch (final IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }


    public String getValue() {

        return value;
    }


    @NotNull
    private final String value;

}

