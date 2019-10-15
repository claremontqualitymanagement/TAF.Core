package se.claremont.taf.div;

import org.junit.Assume;
import se.claremont.taf.core.testset.TestSet;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ResourceManager {

    public static File getFileFromResources(String fileName){
        ClassLoader classLoader = ResourceManager.class.getClassLoader();
        File file = new File(classLoader.getResource(fileName).getFile());
        return file;
    }

    public static Class extractFileFromResourcesAndCompileAndLoadIt(String resourceFile){
        return extractFileFromResourcesAndCompileAndLoadIt(getFileFromResources(resourceFile));
    }

    public static TestSet getTestSetObjectFromResources(String path){
        try {
            return (TestSet) extractFileFromResourcesAndCompileAndLoadIt(path).newInstance();
        }catch (Exception e){
            Assume.assumeTrue("Could not extract TestSet object from path '" + path + "'. Error: " + e.toString(), false);
            return null;
        }
    }

    public static Class extractFileFromResourcesAndCompileAndLoadIt(File resourceFile) {
        String source = null;
        try {
            source = String.join(System.lineSeparator(), Files.readAllLines(resourceFile.toPath()));
        } catch (IOException e) {
            Assume.assumeTrue("Could not read resource file '" + resourceFile.getName() + "'.", false);
            return null;
        }
        String[] sourceFileLines = source.split(System.lineSeparator());
        String packagePath = "";
        for(String line : sourceFileLines){
            if(line.trim().startsWith("package")){
                packagePath = line.trim().substring(8).replace(";", "" ).replace(".", "/") + "/" + resourceFile.getName();
                break;
            }
        }
        File root = new File("/java"); // On Windows running on C:\, this is C:\java.
        File sourceFile = new File(root, packagePath);
        sourceFile.getParentFile().mkdirs();
        try {
            System.out.println("Attempting to write '" + sourceFile.toPath().toAbsolutePath() + "'.");
            Files.write(sourceFile.toPath(), source.getBytes(StandardCharsets.UTF_8));
            System.out.println("Wrote resource file '" + resourceFile + "' to '" + sourceFile.getAbsolutePath() + "'.");
        } catch (IOException e) {
            Assume.assumeTrue("Could not write compiled source file for '" + resourceFile.getName() + "' to disk.", false);
            return null;
        }

        // Compile source file.
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        if(compiler.run(System.in, System.out, System.out, sourceFile.getPath()) != 0){
            System.out.println("WARNING: Compilation error. Could not compile source code successfully.");
            Assume.assumeTrue("Could not compile source code from '" + resourceFile.getName() + "' successfully.", false);
            tryCleanOutFile(sourceFile);
            return null;
        }
        tryCleanOutFile(sourceFile);
        String classFilePath = new File(root, packagePath.substring(0, packagePath.length() - ".java".length()) + ".class").getPath();
        System.out.println("Compiled file '" + classFilePath + "'.");

        // Load and instantiate compiled class.
        URLClassLoader classLoader = null;
        try {
            classLoader = URLClassLoader.newInstance(new URL[]{root.toURI().toURL()}, ClassLoader.getSystemClassLoader());
        } catch (MalformedURLException e) {
            Assume.assumeTrue("Could not create an URL from '" + root.getName() + "'.", false);
            tryCleanOutFile(new File(classFilePath));
            return null;
        }
        String className = packagePath.replace("/", ".").replace(".java", "");
        Assume.assumeTrue("Could not read compiled class file for class '" + className + "' at '" + classFilePath + "'.", Files.isReadable(Paths.get(classFilePath)));
        Class<?> cls = null; // Should print "hello".
        try {
            cls = Class.forName(className, false, classLoader);
        } catch (ClassNotFoundException e) {
            Assume.assumeTrue("Could not find the class '" + className + "' after compilation.", false);
            tryCleanOutFile(new File(classFilePath));
            return null;
        }
        //Object instance = cls.newInstance(); // Should print "world".    }
        tryCleanOutFile(new File(classFilePath));
        return cls;
    }

    public static void tryCleanOutFile(File file){
        try {
            Files.delete(file.toPath());
        } catch (IOException e) {
            System.out.println("Could not remove file '" + file.getPath() + "'. Error: " + e.toString());
        }
    }

}
