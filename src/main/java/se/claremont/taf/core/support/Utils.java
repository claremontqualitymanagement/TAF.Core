package se.claremont.taf.core.support;

import se.claremont.taf.core.support.api.Taf;

import java.io.File;

/**
 * Created by magnusolsson on 2016-09-21.
 *
 * Singelton class for Utils stuff.
 *
 */
public class Utils {

    private static Utils instance = null;
    public final int SUPPORTED_TAF_JVM_VERSION = 8;

    private Utils() {

    }

    public static Utils getInstance() {
        if( instance == null)
            instance = new Utils();
        return instance;
    }

    /**
     *
     * @return os for running jvm
     */
    public String getOS() {
        String os = System.getProperty("os.name");
        return os;
    }

    /**
     *
     * @return gets the separator for the default filesystem
     */
    public String FileSeparator() {
        return System.getProperty("file.separator");
    }

    /**
     * @return User home directory path
     */
    public String getUserHomeDirectory() {
        return System.getProperty("user.home");
    }

    /**
     * @return User working directory path
     */
    public String getUserWorkingDirectory() {
        return System.getProperty("user.dir");
    }

    /**
     *
     * @return true if jvm is running on Windows, otherwise false
     */
    public boolean amIWindowsOS() {
        return getOS().toLowerCase().contains("win");
    }

    /**
     *
     * @return true if jvm is running on Linux distribution otherwise false
     */
    public boolean amILinuxOS() {
        return getOS().toLowerCase().contains("linux");
    }

    /**
     *
     * @return true if jvm is running on Mac OS X, otherwise false
     */
    public boolean amIMacOS() {
        return getOS().toLowerCase().contains("mac");
    }


    /**
     *
     * @return root directory
     */
    @SuppressWarnings("WeakerAccess")
    public String getRootDirectory() {
        return File.listRoots()[0].getAbsolutePath();
    }

    /**
     * Checks if pathToFile exists and is a file
     * @param pathToFile The path to the file to check existance of.
     * @return true if file path exists and is file
     */
    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public boolean doesFileExists(String pathToFile) {

        try {
            File f = new File( pathToFile );
            if( f.exists() && f.isFile() ) {
                return true;
            }
        }
        catch (Exception fe) {
            System.err.println("You got problem: " + fe.getStackTrace());

        }
        return false;
    }

}
