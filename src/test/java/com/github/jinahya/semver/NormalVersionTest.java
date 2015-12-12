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


import java.util.Arrays;
import java.util.List;
import static java.util.concurrent.ThreadLocalRandom.current;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
import org.testng.annotations.Test;


/**
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public class NormalVersionTest {


    private static final Logger logger
        = LoggerFactory.getLogger(NormalVersionTest.class);


    private static List<String> VALIDS = Arrays.asList(
        "1.9.0", "1.10.0",
        "1.0.0-alpha", "1.0.0-alpha.1", "1.0.0-0.3.7", "1.0.0-x.7.z.92",
        "1.0.0-alpha+001", "1.0.0+20130313144700", "1.0.0-beta+exp.sha.5114f85",
        "1.0.0", "2.0.0", "2.1.1",
        "1.0.0-alpha",
        "1.0.0-alpha", "1.0.0-alpha.1", "1.0.0-alpha.beta",
        "1.0.0-beta", "1.0.0-beta.2", "1.0.0-beta.11", "1.0.0-rc.1");


    @Test
    public static void valueOf() {

        for (final String expected : VALIDS) {
            final String actual = NormalVersion.valueOf(expected).toString();
            assertEquals(actual, expected);
        }
    }


    @Test
    public void precedence() {

        final String[] values = {
            "1.0.0-alpha",
            "1.0.0-alpha.1",
            "1.0.0-alpha.beta",
            "1.0.0-beta",
            "1.0.0-beta.2",
            "1.0.0-beta.11",
            "1.0.0-rc.1",
            "1.0.0"
        };

        for (int i = 0; i < values.length; i++) {
            final NormalVersion version = NormalVersion.valueOf(values[i]);
            for (int j = 0; j < i; j++) {
                assertTrue(version.compareTo(NormalVersion.valueOf(values[j])) > 0);
            }
            assertTrue(version.compareTo(version) == 0);
            for (int j = i + 1; j < values.length; j++) {
                assertTrue(version.compareTo(NormalVersion.valueOf(values[j])) < 0);
            }
        }
    }


    @Test(expectedExceptions = IllegalArgumentException.class)
    public void leadingZerosInMajorNumber() {

        NormalVersion.valueOf("00.0.0");
    }


    @Test(expectedExceptions = IllegalArgumentException.class)
    public void nonNumericInMajorNumber() {

        NormalVersion.valueOf("a.0.0");
    }

//    @Test
//    public void increaseMajor() {
//
//        final int major = current().nextInt(1024);
//        final NormalVersion version = new NormalVersion.Builder().majorVersion(major).build();
//        assertEquals(version.getMajorVersionIncreased().getMajorVersion(), major + 1);
//    }
//
//
//    @Test
//    public void increaseMinor() {
//
//        final int minor = current().nextInt(1024);
//        final NormalVersion version = new NormalVersion.Builder().minorVersion(minor).build();
//        assertEquals(version.getMinorVersionIncreased().getMinorVersion(), minor + 1);
//    }
//
//
//    @Test
//    public void increasePatch() {
//
//        final int patch = current().nextInt(1024);
//        final NormalVersion version = new NormalVersion.Builder().patchVersion(patch).build();
//        assertEquals(version.getPatchVersionIncreased().getPatchVersion(), patch + 1);
//    }
}

