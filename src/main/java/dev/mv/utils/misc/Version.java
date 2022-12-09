package dev.mv.utils.misc;

import lombok.Getter;
import lombok.Setter;

//version class
public class Version {
    @Getter @Setter
    private int major, minor, patch;

    public Version() {
        this(1, 0, 0);
    }

    public Version(int major) {
        this(major, 0, 0);
    }

    public Version(int major, int minor) {
        this(major, minor, 0);
    }

    public Version(int major, int minor, int patch) {
        this.major = major;
        this.minor = minor;
        this.patch = patch;
    }

    public static Version parse(String version) {
        if (version == null) {
            return null;
        }
        if (version.startsWith("#version")) {
            version = version.replaceAll("#version", "").replaceAll(" ", "");
            char[] versionChars = version.toCharArray();
                if (versionChars.length == 2) {
                    int major = versionChars[0] - 48;
                    int minor = versionChars[1] - 48;
                    return new Version(major, minor, 0);
                }
                if (versionChars.length == 1) {
                    int major = versionChars[0] - 48;
                    return new Version(major, 0, 0);
                }
            if (versionChars.length == 3) {
                int major = versionChars[0] - 48;
                int minor = versionChars[1] - 48;
                int patch = versionChars[2] - 48;
                return new Version(major, minor, patch);
            }
            return null;
        }

        version = version.replaceAll("v", "").replaceAll(" ", "");
        String[] parts = version.split("\\.");
        try {
            if (parts.length == 2) {
                int major = Integer.parseInt(parts[0]);
                int minor = Integer.parseInt(parts[1]);
                return new Version(major, minor, 0);
            }
            if (parts.length == 1) {
                int major = Integer.parseInt(parts[0]);
                return new Version(major, 0, 0);
            }
            if (parts.length == 3) {
                int major = Integer.parseInt(parts[0]);
                int minor = Integer.parseInt(parts[1]);
                int patch = Integer.parseInt(parts[2]);
                return new Version(major, minor, patch);
            }
            return null;
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public String toString() {
        return String.format("%d.%d.%d", major, minor, patch);
    }

    public String toString(String separator) {
        return String.format("%d%s%d%s%d", major, separator, minor, separator, patch);
    }

    public String toGlslVersion() {
        return String.format("#version %d%d%d", major, minor, patch);
    }

    public int toVulkanVersion() {
        return major << 22 | minor << 12 | patch;
    }
}
