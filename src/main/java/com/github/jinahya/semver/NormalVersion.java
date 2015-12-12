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


import java.util.function.IntUnaryOperator;
import java.util.function.UnaryOperator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public class NormalVersion implements Comparable<NormalVersion> {


    private static final String IDENTIFIER_REGEX = "0|[1-9][0-9]*";


    public static final Pattern IDENTIFIER_PATTERN
        = Pattern.compile(IDENTIFIER_REGEX);


    private static final String REGEXP
        = "(\\d+)\\.(\\d+)\\.(\\d+)(-([^\\+]*))?(\\+(.+))?";


    private static final Pattern PATTERN = Pattern.compile(REGEXP);


    private static <T extends CharSequence> T requireValidIdentifier(
        final T identifier) {

        if (!IDENTIFIER_PATTERN.matcher(identifier).matches()) {
            throw new IllegalArgumentException(
                "invalid identifier: " + identifier);
        }

        return identifier;
    }


    public static class Builder {


        public static Builder valueOf(final String s) {

            final Matcher matcher = PATTERN.matcher(s);
            if (!matcher.matches()) {
                throw new IllegalArgumentException("invalid: " + s);
            }

            final Builder builder = new Builder()
                .majorVersion(matcher.group(1))
                .minorVersion(matcher.group(2))
                .patchVersion(matcher.group(3));

            final String preReleaseVersionString = matcher.group(5);
            if (preReleaseVersionString != null) {
                builder.preReeleaseVersion(
                    PreReleaseVersion.valueOf(preReleaseVersionString));
            }

            final String buildMetadataString = matcher.group(7);
            if (buildMetadataString != null) {
                builder.buildMetadata(
                    BuildMetadata.valueOf(buildMetadataString));
            }

            return builder;
        }


        public static Builder valueOf(final NormalVersion o) {

            return new Builder()
                .majorVersion(o.getMajorVersion())
                .minorVersion(o.getMinorVersion())
                .patchVersion(o.getPatchVersion())
                .preReeleaseVersion(o.getPreReleaseVersion())
                .buildMetadata(o.getBuildMetadata());
        }


        public NormalVersion build() {

            return new NormalVersion(majorVersion, minorVersion, patchVersion,
                                     preReleaseVersion, buildMetadata);
        }


        public Builder majorVersion(final int v) {

            if (v < 0) {
                throw new IllegalArgumentException("negative: " + v);
            }

            final int previous = this.majorVersion;
            this.majorVersion = v;
            if (previous < this.majorVersion) {
                minorVersion(0);
                patchVersion(0);
            }

            return this;
        }


        public Builder majorVersion(final String s) {

            return majorVersion(Integer.parseInt(requireValidIdentifier(s)));
        }


        public Builder minorVersion(final int v) {

            if (v < 0) {
                throw new IllegalArgumentException("negative: " + v);
            }

            final int previous = this.minorVersion;
            this.minorVersion = v;
            if (previous < this.minorVersion) {
                patchVersion(0);
            }

            return this;
        }


        public Builder minorVersion(final IntUnaryOperator operator) {

            return minorVersion(operator.applyAsInt(minorVersion));
        }


        public Builder increaseMinorVersion() {

            return minorVersion(minorVersion + 1);
        }


        public Builder minorVersion(final String s) {

            return minorVersion(Integer.parseInt(requireValidIdentifier(s)));
        }


        public Builder patchVersion(final int patchVersion) {

            if (patchVersion < 0) {
                throw new IllegalArgumentException("negative: " + patchVersion);
            }

            this.patchVersion = patchVersion;

            return this;
        }


        public Builder patchVersion(final IntUnaryOperator operator) {

            return patchVersion(operator.applyAsInt(patchVersion));
        }


        public Builder increasePatchVersion() {

            return patchVersion(patchVersion + 1);
        }


        public Builder patchVersion(final String s) {

            return patchVersion(Integer.parseInt(requireValidIdentifier(s)));
        }


        public Builder preReeleaseVersion(final PreReleaseVersion v) {

            this.preReleaseVersion = v;

            return this;
        }


        public Builder preReleaseVersion(
            final UnaryOperator<PreReleaseVersion> operator) {

            return preReeleaseVersion(operator.apply(preReleaseVersion));
        }


        public Builder preReleaseVersion(final PreReleaseVersion.Builder b) {

            return preReeleaseVersion(b.build());
        }


        public Builder preReleaseVersion(final String s) {

            return preReleaseVersion(PreReleaseVersion.Builder.valueOf(s));
        }


        public Builder buildMetadata(final BuildMetadata v) {

            this.buildMetadata = v;

            return this;
        }


        public Builder buildMetadata(
            final UnaryOperator<BuildMetadata> operator) {

            return buildMetadata(operator.apply(buildMetadata));
        }


        public Builder buildMetadata(final BuildMetadata.Builder b) {

            return buildMetadata(b.build());
        }


        public Builder buildMetadata(final String s) {

            return buildMetadata(BuildMetadata.Builder.valueOf(s));
        }


        private int majorVersion;


        private int minorVersion;


        private int patchVersion;


        private PreReleaseVersion preReleaseVersion;


        private BuildMetadata buildMetadata;

    }


    public static NormalVersion valueOf(final String s) {

        return Builder.valueOf(s).build();
    }


    private NormalVersion(final int major, final int minor, final int patch,
                          final PreReleaseVersion release,
                          final BuildMetadata metadata) {

        super();

        this.majorVersion = major;
        this.minorVersion = minor;
        this.patchVersion = patch;
        this.preReleaseVersion = release;
        this.buildMetadata = metadata;
    }


    @Override
    public int compareTo(final NormalVersion o) {

        if (o == null) {
            throw new NullPointerException("null o");
        }

        final int majorCompared = getMajorVersion() - o.getMajorVersion();
        if (majorCompared != 0) {
            return majorCompared;
        }

        final int minorCompared = getMajorVersion() - o.getMajorVersion();
        if (minorCompared != 0) {
            return minorCompared;
        }

        final int patchCompared = getPatchVersion() - o.getPatchVersion();
        if (patchCompared != 0) {
            return patchCompared;
        }

        // a pre-release version has lower precedence than a normal version
        if (getPreReleaseVersion() == null
            && o.getPreReleaseVersion() != null) {
            return 1;
        }
        if (getPreReleaseVersion() != null
            && o.getPreReleaseVersion() == null) {
            return -1;
        }
        if (getPreReleaseVersion() != null
            && o.getPreReleaseVersion() != null) {
            final int releaseCompared
                = getPreReleaseVersion().compareTo(o.getPreReleaseVersion());
            if (releaseCompared != 0) {
                return releaseCompared;
            }
        }

        return 0;
    }


    @Override
    public String toString() {

        final StringBuilder builder = new StringBuilder()
            .append(String.valueOf(getMajorVersion()))
            .append('.')
            .append(String.valueOf(getMinorVersion()))
            .append('.')
            .append(String.valueOf(getPatchVersion()));

        if (getPreReleaseVersion() != null) {
            builder.append('-').append(getPreReleaseVersion());
        }

        if (getBuildMetadata() != null) {
            builder.append('+').append(getBuildMetadata());
        }

        return builder.toString();
    }


    /**
     * Returns major version.
     *
     * @return major version.
     */
    public int getMajorVersion() {

        return majorVersion;
    }


    public NormalVersion getMajorVersionSet(final int value) {

        return Builder.valueOf(this).majorVersion(value).build();
    }


    /**
     * Returns a new instance with major version increased.
     *
     * @return a new instance with major version increased.
     */
    public NormalVersion getMajorVersionIncreased() {

        return getMajorVersionSet(majorVersion + 1);
    }


    public NormalVersion getMajorVersionSet(final IntUnaryOperator operator) {

        return getMajorVersionSet(operator.applyAsInt(majorVersion));
    }


    /**
     * Returns minor version.
     *
     * @return minor version.
     */
    public int getMinorVersion() {

        return minorVersion;
    }


    public NormalVersion getMinorVersionSet(final int value) {

        return Builder.valueOf(this).minorVersion(value).build();
    }


    /**
     * Returns a new instance with minor version increased.
     *
     * @return a new instance with minor version increased.
     */
    public NormalVersion getMinorVersionIncreased() {

        return getMinorVersionSet(minorVersion + 1);
    }


    public NormalVersion getMinorVersoinSet(final IntUnaryOperator operator) {

        return getMinorVersionSet(operator.applyAsInt(minorVersion));
    }


    /**
     * Returns patch version.
     *
     * @return patch version.
     */
    public int getPatchVersion() {

        return patchVersion;
    }


    public NormalVersion getPatchVersionSet(final int value) {

        return Builder.valueOf(this).patchVersion(value).build();
    }


    /**
     * Returns a new instance with patch version increased.
     *
     * @return a new instance with patch version increased.
     */
    public NormalVersion getPatchVersionIncreased() {

        return getPatchVersionSet(patchVersion + 1);
    }


    public NormalVersion getPatchVersoinSet(final IntUnaryOperator operator) {

        return getPatchVersionSet(operator.applyAsInt(patchVersion));
    }


    /**
     * Returns pre-release version.
     *
     * @return pre-release version.
     */
    public PreReleaseVersion getPreReleaseVersion() {

        return preReleaseVersion;
    }


    public NormalVersion getPreReleaseVersionSet(
        final PreReleaseVersion value) {

        return Builder.valueOf(this).preReeleaseVersion(value).build();
    }


    public NormalVersion getPreReleaseVersionSet(
        final UnaryOperator<PreReleaseVersion> operator) {

        return getPreReleaseVersionSet(operator.apply(preReleaseVersion));
    }


    /**
     * Returns build metadata.
     *
     * @return build metadata
     */
    public BuildMetadata getBuildMetadata() {

        return buildMetadata;
    }


    public NormalVersion getBuildMetadataSet(final BuildMetadata value) {

        return Builder.valueOf(this).buildMetadata(value).build();
    }


    public NormalVersion getBuildeMetadataSet(
        final UnaryOperator<BuildMetadata> operator) {

        return getBuildMetadataSet(operator.apply(buildMetadata));
    }


    private final int majorVersion;


    private final int minorVersion;


    private final int patchVersion;


    private final PreReleaseVersion preReleaseVersion;


    private final BuildMetadata buildMetadata;

}

