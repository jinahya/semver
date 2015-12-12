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
public class Version implements Comparable<Version> {


    private static final String IDENTIFIER_REGEX = "0|[1-9][0-9]*";


    public static final Pattern IDENTIFIER_PATTERN
        = Pattern.compile(IDENTIFIER_REGEX);


    private static final String REGEXP
        = "(\\d+)\\.(\\d+)\\.(\\d+)(-([^\\+]*))?(\\+(.+))?";


    private static final Pattern PATTERN = Pattern.compile(REGEXP);


    public static String requireValidIdentifier(final String identifier) {

        if (identifier == null) {
            throw new NullPointerException("null identifier");
        }

        if (!IDENTIFIER_PATTERN.matcher(identifier).matches()) {
            throw new IllegalArgumentException(
                "invalid identifier: " + identifier);
        }

        return identifier;
    }


//    private static String requireValidValue(final String value) {
//
//        if (value == null) {
//            throw new NullPointerException("null value");
//        }
//
//        if (!PATTERN.matcher(value).matches()) {
//            throw new IllegalArgumentException("invalid verison: " + value);
//        }
//
//        return value;
//    }
    public static class Builder implements Comparable<Builder> {


        public static Builder valueOf(final String s) {

            if (s == null) {
                throw new NullPointerException("null string");
            }

            final Matcher matcher = PATTERN.matcher(s);
            if (!matcher.matches()) {
                throw new IllegalArgumentException("invalid value: " + s);
            }

            final Builder builder = new Builder()
                .major(matcher.group(1))
                .minor(matcher.group(2))
                .patch(matcher.group(3));

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


        @Override
        public int compareTo(final Builder o) {

            if (o == null) {
                throw new NullPointerException("null o");
            }

            final int majorCompared = major - o.major;
            if (majorCompared != 0) {
                return majorCompared;
            }

            final int minorCompared = minor - o.minor;
            if (minorCompared != 0) {
                return minorCompared;
            }

            final int patchCompared = patch - o.patch;
            if (patchCompared != 0) {
                return patchCompared;
            }

            // a pre-release version has lower precedence than a normal version
            if (release == null && o.release != null) {
                return 1;
            }
            if (release != null && o.release == null) {
                return -1;
            }
            if (release != null && o.release != null) {
                final int releaseCompared = release.compareTo(o.release);
                if (releaseCompared != 0) {
                    return releaseCompared;
                }
            }

            return 0;
        }


        public Version build() {

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

            return new Version(builder.toString());
        }


        public int getMajor() {

            return major;
        }


        public void setMajor(final int major) {

            if (major < 0) {
                throw new IllegalArgumentException("negative major: " + major);
            }

            final int previous = this.major;
            this.major = major;
            if (previous < this.major) {
                setMinor(0);
                setPatch(0);
            }
        }


        public Builder major(final int major) {

            setMajor(major);

            return this;
        }


        public Builder major(final String major) {

            if (major == null) {
                throw new NullPointerException("null major");
            }

            if (!IDENTIFIER_PATTERN.matcher(major).matches()) {
                throw new IllegalArgumentException("invalid major: " + major);
            }

            return major(Integer.parseInt(requireValidIdentifier(major)));
        }


        public int getMinor() {

            return minor;
        }


        public void setMinor(final int minor) {

            if (minor < 0) {
                throw new IllegalArgumentException("negative minor: " + minor);
            }

            final int previous = this.minor;
            this.minor = minor;
            if (previous < this.minor) {
                setPatch(0);
            }
        }


        public Builder minor(final int minor) {

            setMinor(minor);

            return this;
        }


        public Builder minor(final String minor) {

            if (minor == null) {
                throw new NullPointerException("null minor");
            }

            if (!IDENTIFIER_PATTERN.matcher(minor).matches()) {
                throw new IllegalArgumentException("invalid minor: " + minor);
            }

            return minor(Integer.parseInt(requireValidIdentifier(minor)));
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


        public Builder patch(final String patch) {

            if (patch == null) {
                throw new NullPointerException("null patch");
            }

            if (!IDENTIFIER_PATTERN.matcher(patch).matches()) {
                throw new IllegalArgumentException("invalid patch: " + patch);
            }

            return patch(Integer.parseInt(requireValidIdentifier(patch)));
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


    public static Version valueOf(final String s) {

        return Builder.valueOf(s).build();
    }


    private Version(final String value) {

        super();

//        this.value = requireValidValue(value);
        this.value = value;
    }


    @Override
    public int compareTo(final Version o) {

        if (o == null) {
            throw new NullPointerException("null o");
        }

        return Builder.valueOf(value).compareTo(Builder.valueOf(o.value));
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

