package com.tcmj.shampug;

import org.junit.jupiter.api.Test;

/**
 * Version class of shampug randoms.
 * @author tcmj
 */
public class VersionTest {

    /**
     * Not a real test but some real outputs of all available methods.
     * <pre>
     *     Version.getVendor        = tcmj
     *     Version.getTitle         = ShamPug Randoms
     *     Version.getVersion       = 1.01
     *     Version.getFullString    = Running tcmj's ShamPug Randoms Version 1.01 on Java 1.8.0_301
     *     Version.getMajorVersion  = 1
     *     Version.getMinorVersion  = 1
     *     Version.getJavaUserInfos = FileEncoding: UTF-8, Country: DE, Language: de, Timezone: E
     * </pre>
     */
    @Test
    void whenWeLogAllMethods() {
        System.out.println("Version.getVendor        = " + Version.getVendor());
        System.out.println("Version.getTitle         = " + Version.getTitle());
        System.out.println("Version.getVersion       = " + Version.getVersion());
        System.out.println("Version.getFullString    = " + Version.getFullString());
        System.out.println("Version.getJavaUserInfos = " + Version.getJavaUserInfos());
    }

}