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


import static org.testng.Assert.assertEquals;
import org.testng.annotations.Test;
import static org.testng.Assert.assertEquals;


/**
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public class ReadMeTest {


    @Test
    public void buildMetadata() {
        final BuildMetadata.Builder builder
            = new BuildMetadata.Builder().identifiers("exp", "sha", "5114f85");
        final BuildMetadata built = builder.build();
        assertEquals(built.toString(), "exp.sha.5114f85");
    }


    @Test
    public void preReleaseVersion() {
        final PreReleaseVersion.Builder builder
            = new PreReleaseVersion.Builder().identifiers("x", "7", "z", "92");
        final PreReleaseVersion built = builder.build();
        assertEquals(built.toString(), "x.7.z.92");
    }


    @Test
    public void normalVersion() {

        final NormalVersion.Builder builder = new NormalVersion.Builder()
            .majorVersion(0)
            .minorVersion(1)
            .patchVersion(0)
            .preReleaseVersion(
                new PreReleaseVersion.Builder()
                .identifiers("x", "7", "z", "92"))
            .buildMetadata(
                new BuildMetadata.Builder()
                .identifiers("exp", "sha", "5114f85"));
        final NormalVersion built = builder.build();
        assertEquals(built.toString(), "0.1.0-x.7.z.92+exp.sha.5114f85");
    }


    @Test
    public void assertPatchMinorResetTo0WhenMajorIncreased() {

        final NormalVersion.Builder builder = new NormalVersion.Builder()
            .majorVersion(0)
            .minorVersion(1)
            .patchVersion(2);
        final NormalVersion built = builder.build();
        assertEquals(built.toString(), "0.1.2");
        assertEquals(built.getMajorVersionIncreased().toString(), "1.0.0");
    }


    @Test
    public void assertPatchResetTo0WhenMinorIncreased() {

        final NormalVersion.Builder builder = new NormalVersion.Builder()
            .majorVersion(1)
            .minorVersion(0)
            .patchVersion(1);
        final NormalVersion built = builder.build();
        assertEquals(built.toString(), "1.0.1");
        assertEquals(built.getMinorVersionIncreased().toString(), "1.1.0");
    }

}

