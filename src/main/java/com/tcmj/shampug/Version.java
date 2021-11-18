package com.tcmj.shampug;

import java.util.TimeZone;

import static com.tcmj.shampug.util.FileUtils.getFilePath;
import static com.tcmj.shampug.util.FileUtils.getUTF8FileContent;

/**
 * <b>Version of the tcmj - shampug randoms - api.</b>
 * <p>
 * General rule is that a overall compatibility of same major versions will be guaranteed.
 * Major versions will be used to implement really great stuff or changes
 * Minor versions will be used to implement new features and also bug fixes
 * </p>
 * <p> This library will ever be available as open source. Non-Profit usage will always be
 * granted - for absolutely nothing (free)! </p>
 */
public final class Version {

    private static final String TITLE = "ShamPug Randoms";
    private static final String VENDOR = "tcmj";
    private static String POM_VERSION = getUTF8FileContent(getFilePath("version.txt", Version.class));

    private Version() { // Non-Instantiable
    }

    /**
     * @return Tries to read the application title provided from the manifests implementation/specification entries.
     */
    public static String getTitle() {
        String implementationTitle = Version.class.getPackage().getImplementationTitle();
        if (implementationTitle != null) {
            return implementationTitle;
        }
        String specificationTitle = Version.class.getPackage().getSpecificationTitle();
        if (specificationTitle != null) {
            return specificationTitle;
        }
        return TITLE;
    }

    /**
     * @return Tries to read the application version provided from the manifests implementation/specification entries.
     */
    public static String getVersion() {
        String implementationVersion = Version.class.getPackage().getImplementationVersion();
        if (implementationVersion != null) {
            return implementationVersion;
        }
        String specificationVersion = Version.class.getPackage().getSpecificationVersion();
        if (specificationVersion != null) {
            return specificationVersion;
        }
        return POM_VERSION;
    }

    /**
     * @return Tries to read the application vendor provided from the manifests implementation/specification entries.
     */
    public static String getVendor() {
        String implementationVendor = Version.class.getPackage().getImplementationVendor();
        if (implementationVendor != null) {
            return implementationVendor;
        }
        String specificationVendor = Version.class.getPackage().getSpecificationVendor();
        if (specificationVendor != null) {
            return specificationVendor;
        }
        return VENDOR;
    }

    /**
     * @return 'Running tcmj's ShamPug Randoms Version 1.01 on Java 1.8.0_301'
     */
    public static String getFullString() {
        return String.format("Running %s's %s Version %s on Java %s", getVendor(), getTitle(), getVersion(), System.getProperty("java.version"));
    }

    /**
     * @return 'FileEncoding: UTF-8, Country: DE, Language: de, Timezone: Europe/Berlin'
     */
    public static String getJavaUserInfos() {
        return "FileEncoding: " + System.getProperty("file.encoding") + // UTF-8
            ", Country: " + System.getProperty("user.country") +        // DE
            ", Language: " + System.getProperty("user.language") +      // de
            ", Timezone: " + TimeZone.getDefault().getID();       // Europe/Berlin
    }

    /**
     * Main method which simply writes some infos to console.
     */
    public static void main(String[] args) {
        System.out.println(getFullString());
        System.out.println(getJavaUserInfos());
        String usage = "ShamPug shamPug = ShamPug.setup()\n" +
            "                         .withRegistryStrategy(Strategy.GLOBAL)\n" +
            "                         .usingSeed(1234567869L)\n" +
            "                         .create();";

        System.out.println("Usage Example:");
        System.out.println(usage);
    }
}
