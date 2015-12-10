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


import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public class Version {


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
                builder.append('-').append(release.getValue());
            }

            if (metadata != null) {
                builder.append('+').append(metadata.getValue());
            }

            return builder.toString();
        }


        public int getMajor() {

            return major;
        }


        public void setMajor(final int major) {

            if (major < 0) {
                throw new IllegalArgumentException("major(" + major + ") < 0");
            }

            this.major = major;
        }


        public Builder major(final int major) {

            setMajor(major);

            return this;
        }


        public int getMinor() {

            return minor;
        }


        public void setMinor(final int minor) {

            this.minor = minor;
        }


        public Builder minor(final int minor) {

            setMinor(minor);

            return this;
        }


        public int getPatch() {

            return patch;
        }


        public void setPatch(final int patch) {

            this.patch = patch;
        }


        public Builder patch(final int patch) {

            setPatch(patch);

            return this;
        }


        public Release getRelease() {

            return release;
        }


        public void setRelease(final Release release) {

            this.release = release;
        }


        public Builder release(final Release release) {

            setRelease(release);

            return this;
        }


        public Builder release(final Release.Builder builder) {

            return release(builder.build());
        }


        public Builder release(final String value) {

            return release(Release.valueOf(value));
        }


        public Metadata getMetadata() {

            return metadata;
        }


        public void setMetadata(final Metadata metadata) {

            this.metadata = metadata;
        }


        public Builder metadata(final Metadata metadata) {

            setMetadata(metadata);

            return this;
        }


        public Builder metadata(final Metadata.Builder builder) {

            return metadata(builder.build());
        }


        public Builder metadata(final String value) {

            return metadata(Metadata.valueOf(value));
        }


        private int major;


        private int minor;


        private int patch;


        private Release release;


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
        if (releaseValue != null) {
            builder.release(Release.valueOf(releaseValue));
        }

        final String metadataValue = matcher.group(7);
        if (metadataValue != null) {
            builder.metadata(Metadata.valueOf(metadataValue));
        }

        return builder;
    }


    private Version(final String value) {

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

