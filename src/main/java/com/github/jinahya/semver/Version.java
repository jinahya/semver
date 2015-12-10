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
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.validation.Valid;
import javax.validation.constraints.Min;


/**
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public class Version {


    private static final String NORMAL_IDENTIFIER_REGEXP = "0|[1-9][0-9]*?";


////    public static final String REGEXP
////        = "(" + NORMAL_IDENTIFIER_REGEXP + ")" + "\\."
////          + "(" + NORMAL_IDENTIFIER_REGEXP + ")" + "\\."
////          + "(" + NORMAL_IDENTIFIER_REGEXP + ")"
////          + "(-" + "(" + Release.REGEXP + "))??"
////          + "(\\+" + "(" + Metadata.REGEXP + "))??";
//    public static final String REGEXP
//        = "(" + NORMAL_IDENTIFIER_REGEXP + ")" + "\\."
//          + "(" + NORMAL_IDENTIFIER_REGEXP + ")" + "\\."
//          + "(" + NORMAL_IDENTIFIER_REGEXP + ")"
//          + "(-" + "(" + Release.REGEXP + "))??"
//          + "(\\+" + "(" + "(" + Metadata.IDENTIFIER_REGEXP + ")(\\.(" + Metadata.IDENTIFIER_REGEXP + "))*" + "))??";
//    public static final Pattern PATTERN = Pattern.compile(REGEXP);
    private static final String REGEXP
        = "(\\d+)\\.(\\d+)\\.(\\d+)(-([^\\+]*))?(\\+(.+))?";


    private static final Pattern PATTERN = Pattern.compile(REGEXP);


    public static String requireValidValue(final String value) {

        if (value == null) {
            throw new NullPointerException("null input");
        }

        if (!PATTERN.matcher(value).matches()) {
            throw new IllegalArgumentException("version not matches: " + value);
        }

        return value;
    }


    public static class Builder {


        public Version build() {

            return new Version(value());
        }


        private String value() {

            final StringBuilder builder
                = new StringBuilder(String.valueOf(major))
                .append('.')
                .append(String.valueOf(minor))
                .append('.')
                .append(String.valueOf(patch));

            if (release != null) {
                release.append(builder);
            }

            if (metadata != null) {
                metadata.append(builder);
            }

            System.out.println("toString: " + builder.toString());

            return builder.toString();
        }


        public Builder major(final int major) {

            if (major < 0) {
                throw new IllegalArgumentException("major(" + major + ") < 0");
            }

            this.major = major;

            return this;
        }


        public Builder minor(final int minor) {

            if (minor < 0) {
                throw new IllegalArgumentException("minor(" + minor + ") < 0");
            }

            this.minor = minor;

            return this;
        }


        public Builder patch(final int patch) {

            if (patch < 0) {
                throw new IllegalArgumentException("patch(" + patch + ") < 0");
            }

            this.patch = patch;

            return this;
        }


        public Builder release(final Release release) {

            this.release = release;

            return this;
        }


        public Builder release(final Release.Builder builder) {

            return release(builder.build());
        }


        public Builder release(final String value) {

            return release(Release.valueOf(value));
        }


        public Builder metadata(final Metadata metadata) {

            this.metadata = metadata;

            return this;
        }


        public Builder metadata(final Metadata.Builder builder) {

            return metadata(builder.build());
        }


        public Builder metadata(final String value) {

            return metadata(Metadata.valueOf(value));
        }


        @Min(0)
        private int major;


        @Min(0)
        private int minor;


        @Min(0)
        private int patch;


        @Valid
        private Release release;


        @Valid
        private Metadata metadata;

    }


    public static Builder valueOf(final String s) {

        if (s == null) {
            throw new NullPointerException("null string");
        }

        final Matcher matcher = PATTERN.matcher(s);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("invalid version string: " + s);
        }

        final Builder builder = new Builder()
            .major(Integer.parseInt(matcher.group(1)))
            .minor(Integer.parseInt(matcher.group(2)))
            .patch(Integer.parseInt(matcher.group(3)));

        final String releaseValue = matcher.group(5);
        System.out.println("rv: " + releaseValue);
        if (releaseValue != null) {
            builder.release(Release.valueOf(releaseValue));
        }

        final String metadataValue = matcher.group(7);
        System.out.println("mv: " + metadataValue);
        if (metadataValue != null) {
            builder.metadata(Metadata.valueOf(metadataValue));
        }

        return builder;
    }


    private Version(final String value) {

        super();

        this.value = requireValidValue(value);
    }


    public <T extends Appendable> T append(final T appendable)
        throws IOException {

        if (appendable == null) {
            throw new NullPointerException("null appendable");
        }

        return (T) appendable.append(value);
    }


    public StringBuilder append(final StringBuilder builder) {

        try {
            return (StringBuilder) append((Appendable) builder);
        } catch (final IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }


    @Override
    public String toString() {

        return append(new StringBuilder()).toString();
    }


    public String getValue() {

        return value;
    }


    private final String value;

}

