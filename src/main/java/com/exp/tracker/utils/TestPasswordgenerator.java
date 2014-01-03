/*
 * Copyright 2013 the original author or authors.
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

package com.exp.tracker.utils;

import org.springframework.security.authentication.encoding.MessageDigestPasswordEncoder;

public class TestPasswordgenerator
{

    /**
     * @param args
     */
    public static void main(String[] args)
    {

        printHash("Tom", "password");
    }

    private static void printHash(String id, String pass)
    {
        System.out.println("[User/Password = " + id + "/" + pass + "], [Hash="
                + getHash(id, pass) + "]");
    }

    private static String getHash(String id, String pass)
    {
        MessageDigestPasswordEncoder mdpe = new MessageDigestPasswordEncoder(
                "MD5");
        return mdpe.encodePassword(pass, id);
    }

}
