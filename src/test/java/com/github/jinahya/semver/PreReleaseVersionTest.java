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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static org.testng.Assert.assertEquals;
import org.testng.annotations.Test;


/**
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public class PreReleaseVersionTest {


    private static final Logger logger
        = LoggerFactory.getLogger(PreReleaseVersionTest.class);


    private static final List<String> VALIDS
        = Arrays.asList("alpha", "alpha.1", "0.3.7", "x.7.z.92");


    @Test
    public void valueOf() {

        VALIDS.forEach(expected -> {
            final String actual = PreReleaseVersion.valueOf(expected).toString();
            assertEquals(actual, expected);
        });
    }

}

