package se.claremont.taf.parallelism;

import org.junit.After;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.Test;
import se.claremont.taf.core.logging.LogFolder;
import se.claremont.taf.core.testrun.CliTestRunner;
import se.claremont.taf.core.testrun.Settings;
import se.claremont.taf.core.testrun.TestRun;
import se.claremont.taf.core.testset.UnitTestClass;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class TestExecutionTest extends UnitTestClass {
    String runName;

    @After
    public void teardown() throws IOException {
        for(File directory : new File(TestRun.getSettingsValue(Settings.SettingParameters.BASE_LOG_FOLDER)).listFiles(File::isDirectory)){
            if(directory.getName().contains(runName)){
                directory.delete();
                return;
            }
        }
    }

     private boolean summaryFileFound(){
         for(File directory : new File(TestRun.getSettingsValue(Settings.SettingParameters.BASE_LOG_FOLDER)).listFiles(File::isDirectory)){
             if(directory.getName().contains(runName)){
                 for(File file : directory.listFiles()){
                     if(file.getName().equals("_summary.html")){
                         return true;
                     }
                 }
             }
         }
         return false;
     }

     private void checkWriteAccess(){
         File f = new File(TestRun.getSettingsValue(Settings.SettingParameters.BASE_LOG_FOLDER));
         Assume.assumeTrue("Cannot write to base folder. Ignoring test.", f.canWrite());
     }
}
