package org.appfuse.tool;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.types.FileSet;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * This class is almost a complete copy of replacepackages
 * by Ben Gill.  These utility methods can be used in the
 * plugin to change package names of appfuse source
 * contributions.  Liberties have been taken to make this
 * open source code just work here. Some methods were removed
 * and logging needs additional design consideration.
 *
 * @author <a href="mailto:david@capehenrytech.com">David L. Whitehurst</a>
 */
public class FileUtils {

    static final boolean SAVE_FILE = true;
    static final boolean DONT_SAVE_FILE = false;
    boolean debug = true;
    private String baseDir = "src"; // root
    private String workBaseDir = "null"; // actually called null
    private String existingPkgName = "org.appfuse"; // AppFuse
    private String newPkgName;
    private String existingPkgPath;
    private String newPkgPath;
    protected transient final Log log = LogFactory.getLog(getClass());

    StringBuffer logOutput = new StringBuffer();

    String[] invalidFileTypes = new String[] { "class", "jar", "jpg", "gif", "png", "ico" };

    private Vector filesets = new Vector();

    /**
     * Constructor
     */
    public FileUtils(String newPackage) {
        this.newPkgName = newPackage;

    }
    /**
     * simple method to add filesets
     * @param fileset
     */
    public void addFileset(FileSet fileset) {
        filesets.add(fileset);
    }

    /**
     * Override the default set of invalid file types that will be moved to the
     * new package structure.
     *
     * The invalidFileTypes must be a comma
     *  separated list of file extensions
     * for example "class,jar,exe"
     *
     * @param invalidFileTypes
     */
    public void setInvalidFileTypes(String invalidFileTypes)
    {
        this.invalidFileTypes = invalidFileTypes.split(",");
        log.debug("default invalidFileTypes overriden with [" + invalidFileTypes
                + "]");
    }

    /**
     * Set the base directory. The base directory will be where the Task starts
     * to recursively look for files with the given package name in it. This
     * method will be called by ANT when it invokes the Task.
     *
     * @param baseDir
     */
    public void setBaseDir(String baseDir)
    {
        if (!new File(baseDir.trim()).exists())
        {
            throw new ExceptionInInitializerError("Base dir [" + baseDir.trim()
                    + "] does not exist");
        }
        this.baseDir = correctFileSeparators(baseDir.trim());
        if (this.baseDir != null)
        {
            if (this.baseDir.endsWith(File.separator)) {
                this.workBaseDir = this.baseDir.substring(0, this.baseDir
                        .length())
                        + ".work";
            } else
            {
                this.workBaseDir = this.baseDir + ".work";
            }
           log.debug("Set workBaseDir to [" + this.workBaseDir + "]");
        }
    }

    /**
     * Validates the packagename
     *
     * @param packageName
     * @throws ExceptionInInitializerError
     *             if the package name is invalid
     */
    private void validatePackageName(String packageName)
            throws ExceptionInInitializerError
    {
        if (packageName.indexOf("/") != -1)
        {
            throw new ExceptionInInitializerError("packageName [" + packageName
                    + "] is invalid because it contains slashes");
        }
        if (packageName.indexOf("\\") != -1)
        {
            throw new ExceptionInInitializerError("packageName [" + packageName
                    + "] is invalid because it contains slashes");
        }
    }

    /**
     * Strips out start and end dots
     *
     * @param packageName
     * @return package name without starting and ending dots
     */
    private String stripUnwantedDots(String packageName)
    {

        while (packageName.endsWith("."))
        {
            packageName = packageName.substring(0, packageName.length() - 1);
        }
        while (packageName.startsWith("."))
        {
            packageName = packageName.substring(1);
        }
        log.debug("returning stipped package name of [" + packageName + "]");
        return packageName;
    }

    /**
     * Set the existing package name. This will become the string that the Task
     * looks to replace in the files look for files with the given package name
     * in it. This method will be called by ANT when it invokes the Task.
     *
     * @param existingPkgName
     */
    public void setExistingPkgName(String existingPkgName) throws Exception
    {

        log.info("existingPkgName came in as [" + existingPkgName + "]");
        this.existingPkgName = stripUnwantedDots(existingPkgName.trim());
        validatePackageName(this.existingPkgName);
        if (this.existingPkgName.length() == 0)
        {
            throw new Exception("Unsupported operation - cannot "
                    + " repackage from empty package name as would "
                    + " not know which imports to expand out");
        }
    }

    /**
     * Set the new package name. This will become the package name and replace
     * the existing package name in the source files. This method will be called
     * by ANT when it invokes the Task.
     *
     * @param newPkgName
     * @throws Exception
     */
    public void setNewPkgName(String newPkgName) throws Exception
    {
        log.info("newPkgName came in as [" + newPkgName + "]");
        this.newPkgName = stripUnwantedDots(newPkgName.trim());
        validatePackageName(this.newPkgName);
        if (this.newPkgName.length() == 0)
        {
            throw new Exception("Unimplemented operation");
        }
    }

    /**
     * Set the package paths. Replace the . delimiter with the relevant file
     * separator based on the o/s we are running on.
     */
    private void setPackagePaths()
    {
        this.existingPkgPath = getPackagePath(existingPkgName);
        this.newPkgPath = getPackagePath(newPkgName);
        log.debug("Set existing package path as [" + existingPkgPath + "]");
        log.debug("Set new package path as [" + newPkgPath + "]");
    }

    /**
     * Set the new package name. This will become the package name and replace
     * the existing package name in the source files. This method will be called
     * by ANT when it invokes the Task.
     *
     * @param pkgName
     * @return path of the pkg name
     */
    private String getPackagePath(String pkgName)
    {
        String[] pkgNames = pkgName.split("\\.");
        String aPath = "";

        for (int i = 0; i < pkgNames.length; i++)
        {
            if (aPath.length() != 0)
            {
                aPath += File.separator;
            }

            aPath += pkgNames[i];
        }
        return aPath;
    }

    /**
     * Creates a new directory, if it does not exist already
     *
     * @param directoryName
     */
    private void createDirectory(String directoryName)
    {
        File dir = new File(directoryName);

        if (!dir.exists())
        {
            if (dir.mkdirs())
            {
                String message = "Created directory [" + directoryName + "]";
                log.debug(message);
            }
            else
            {
                log.debug("Failed to create directory [" + directoryName + "]");
            }
        }
    }

    /*
     * This method gets called recursively (mainly by itself) and walks through
     * the directory structure looking for files with existingPkgPath in their
     * fully qualified (absolute) names.
     *
     * If a file is found, then it is parsed for the existingPkgName and any
     * instance of this string will be replaced by the newPkgName. The file is
     * then saved back out in its new package directory.
     *
     * @param inDirName the directory to search
     */
    private void repackage(String inDirName, String inWorkDirName)
            throws IOException {
        log.debug("Inside repackage inDirName is [" + inDirName + "]");
        log.debug("Inside repackage inWorkDirName is [" + inWorkDirName + "]");

        String[] files = new File(inDirName).list();

        if (files == null)
        {
            return;
        }

        log.debug("There are [" + files.length + "] files in dir [" + inDirName
                + "]");

        for (int i = 0; i < files.length; i++)
        {
            log.debug("file is [" + files[i] + "]");

            String fileName = inDirName + File.separator + files[i];
            File aFile = new File(fileName);

            if (files[i].equals("CVS"))
            {
                log.debug("ignoring CVS dir");
            }
            else if (aFile.isDirectory())
            {
                log.debug("Got a dir [" + fileName + "]");

                String newDirName = inDirName + File.separator + files[i];
                String newWorkDirName = inWorkDirName + File.separator
                        + files[i];

                if (isOldPackageDir(fileName))
                {
                    String newPath = convertOldPackageDirName(fileName);

                    log.debug("found old package dir [" + fileName + "] "
                            + "newPath is [" + newPath.toString() + "]");
                    createDirectory(newPath.toString());

                }
                log.debug("Recursing with [" + newDirName + "]");

                repackage(newDirName, newWorkDirName);
                /**
                 * TODO: Create this empty directory but we need
                 * to convert the name to the correct name within the new
                 * package structure based on the depth and differences
                 * in depth between old pkg path and new...
                 */

            }
            else
            {
                // Normal file
                log.debug("Processing file [" + fileName + "] existingPkgPath is ["
                        + existingPkgPath + "]");

                // Should we process this file at all or leave it?
                int existingPathIndexPos = fileName.indexOf(existingPkgPath);

                if (existingPathIndexPos != -1)
                {
                    // Normal file in old package structure
                    log.debug("found file with existing package name ["
                           + fileName + "]");
                    String newPath = convertOldPackageDirName(fileName
                            .substring(0, fileName.lastIndexOf(File.separator)));
                    String newFileName = newPath
                            + fileName.substring(fileName
                                    .lastIndexOf(File.separator));

                    log.debug("creating directory [" + newPath + "]");
                    createDirectory(newPath);

                    if (isValidFileType(fileName))
                    {
                        String output = changePackageNamesInFile(fileName,
                                FileUtils.DONT_SAVE_FILE);

                        log.debug("Saving file [" + fileName
                                + "] to new package directory ["
                                + newFileName + "]");
                        toFile(newFileName, output);
                    }
                    else
                    {
                        log.debug("Renaming file non valid file type ["
                                + fileName + "]");
                        if (!aFile.renameTo(new File(newFileName)))
                        {
                            log.debug("Failed to rename file [" + fileName + "] to ["
                                    + newFileName + "]");
                        }
                    }
                }
                else
                {
                    // Normal file - not in old package structure

                    // Stip off existing baseDir
                    String newFileName = this.workBaseDir
                            + fileName.substring(this.baseDir.length());
                    String newPath = newFileName.substring(0, newFileName
                            .lastIndexOf(File.separator));
                    log.debug("Creating dir [" + newPath + "]");
                    createDirectory(newPath);

                    if (aFile.renameTo(new File(newFileName)))
                    {
                        log.debug("Saved file [" + newFileName
                                + "] to new directory structure");
                    } else
                    {
                        log.debug("Failed to rename file [" + fileName + "] to ["
                                + newFileName + "] to new directory structure");
                    }
                }
            }
        }
    }

    /*
     * This method gets called recursively (mainly by itself) and walks through
     * the directory structure looking for ANY valid files with existingPkgPath.
     *
     * If a file is found, then it is parsed then it is output for the caller to
     * check.
     *
     * @param inDirName the directory to search
     */
    private void checkSummary(String inDirName)
            throws IOException
    {
        log.debug("Inside checkSummary inDirName is [" + inDirName + "]");

        String[] files = new File(inDirName).list();

        if (files == null)
        {
            return;
        }

        log.debug("There are [" + files.length + "] files in dir [" + inDirName + "]");

        for (String file : files) {
            log.debug("file is [" + file + "]");

            String fileName = inDirName + File.separator + file;
            File aFile = new File(fileName);

            if (aFile.isDirectory()) {
                log.debug("Got a dir [" + fileName + "]");

                String newDirName = inDirName + File.separator + file;

                log.debug("Recursing with [" + newDirName + "]");

                checkSummary(newDirName);
            } else {
                // Normal file
                log.debug("Checking file [" + fileName + "] existingPkgPath is ["
                        + existingPkgPath + "]");

                if (isValidFileType(fileName)) {
                    if (hasFileOldPathOrPkg(fileName)) {

                        log.debug("File [" + fileName + "] still has old pkg [" + existingPkgPath + "]");
                    }
                }
            }
        }
    }


    private boolean matches(String patternStr, String fileContents) {

    	Pattern pattern = Pattern.compile(patternStr);
        Matcher matcher = pattern.matcher(fileContents);
        return matcher.matches();
    }


    private boolean hasFileOldPathOrPkg(String fileName) {

    	try {
            String fileContents = fromFile(fileName);

            String patternStr = escape(existingPkgPath);

            if (matches(patternStr, fileContents)) {
                return true;
            }

            patternStr = getUnixPath(existingPkgPath);
            patternStr = escape(patternStr);

            if (matches(patternStr, fileContents)) {
                return true;
            }

            patternStr = getWindowsPath(existingPkgPath);
            patternStr = escape(patternStr);

            return matches(patternStr, fileContents);

        } catch (IOException e) {
            log.error("Error loading fileContents in hasFileOldPathOrPkg [" + e.getMessage() + "]");
            return false;
        }

    }

    private String convertOldPackageDirName(String dirName)
    {
        // Need to create new package directory
        int startExistingPathIndexPos = dirName.indexOf(existingPkgPath);
        int endExistingPathIndexPos = startExistingPathIndexPos
                + existingPkgPath.length();

        StringBuffer newPath = new StringBuffer();
        newPath.append(this.workBaseDir);
        // Get the front bit
        log.debug("startExistingPathIndexPos is [" + startExistingPathIndexPos + "]");
        log.debug("about to do substring on [" + dirName + "] positions ["
                + this.baseDir.length() + "] and [" + startExistingPathIndexPos
                + "]");

        String firstPartFileName = dirName.substring(this.baseDir.length(),
                startExistingPathIndexPos);
        log.debug("firstPartFileName is [" + firstPartFileName + "]");
        newPath.append(firstPartFileName);
        newPath.append(newPkgPath);

        String lastPartFileName = dirName.substring(endExistingPathIndexPos);
        log.debug("appending lastPartFileName [" + lastPartFileName + "]");
        newPath.append(lastPartFileName);
        return newPath.toString();
    }


    private boolean isOldPackageDir(String dirName)
    {
        log.debug("inside isOldPackageDir with [" + dirName + "]");

        // Should we process this file at all or leave it?
        int existingPathIndexPos = dirName.indexOf(existingPkgPath);

        if (existingPathIndexPos != -1)
        {
            log.debug("found dir with existing package name [" + dirName + "]");
            return true;
        }
        log.debug("dir [" + dirName + "] is not old package dir");
        return false;
    }

    /*
     * This method reads a file and returns a string suitable for some regex
     * operation
     *
     * @param fileName the file name to open and read @return the file contents
     */
    public String fromFile(String fileName) throws IOException
    {
        BufferedReader in = new BufferedReader(new FileReader(fileName));
        StringBuffer fileContents = new StringBuffer();
        String str;

        while ((str = in.readLine()) != null)
        {
            fileContents.append(str);
            fileContents.append("\n");
        }
        in.close();
        return fileContents.toString();
    }

    /*
     * This method saves a file to disk
     *
     * @param fileName the name of the file @param contents the files contents
     * @throws IOException if there is an error writing the file
     */
    public void toFile(String fileName, String contents) throws IOException
    {
        log.debug("Saving file to fileName [" + fileName + "]");
        BufferedOutputStream bout = new BufferedOutputStream(
                new DataOutputStream(new FileOutputStream(fileName)));

        bout.write(contents.getBytes());
        bout.flush();
        bout.close();
    }

    /**
     * Checks to see if the file is a valid type
     *
     * @return true if the file is a valid type, else false
     */
    private boolean isValidFileType(String fileName)
    {
        for (int i = 0; i < invalidFileTypes.length; i++)
        {
            if (fileName.endsWith(invalidFileTypes[i]))
            {
                log.debug("File [" + fileName
                        + "] will just be moved as it is not a valid type");
                return false;
            }
        }
        return true;
    }

    /**
     * Escapes the search token so it is fit for a regex replacement
     *
     * @return true if the file is a valid type, else false
     */
    private String escape(String str)
    {
        String newStr = "";
        char[] strArr = str.toCharArray();

        for (int i = 0; i < strArr.length; i++)
        {
            if (strArr[i] == '.')
            {
                newStr += "\\";
            }
            if (strArr[i] == '\\')
            {
                newStr += "\\";
            }
            newStr += strArr[i];
        }
        log.debug("escaped str is [" + newStr + "]");
        return newStr;
    }

    /**
     * This method changes the name of any strings in the file and saves the
     * file back to disk
     *
     * With paths, there may be both \ path delimiters or / so we need to run
     * the regex twice to ensure we have replaced both types
     *
     * @return the contents of the file after the package names have been
     *         changed
     */
    private String changePackagePaths(String fileContents)
    {
        String output = changeWindowsPaths(fileContents);
        output = changeUnixPaths(output);
        return output;
    }

    /**
     * This method changes the name of any strings in the file and saves the
     * file back to disk
     *
     * With paths, there may be both \ path delimiters or / so we need to run
     * the regex twice to ensure we have replaced both types
     *
     * @return the contents of the file after the package names have been
     *         changed
     */
    private String changeUnixPaths(String fileContents)
    {
        log.debug("inside changeUnixPaths");

        String patternStr;

        if (newPkgPath.length() == 0)
        {
            patternStr = getUnixPath(existingPkgPath) + "/";
        }
        else
        {
            log.debug("before calling getUnixPath existingPkgPath is ["
                    + existingPkgPath + "]");
            patternStr = getUnixPath(existingPkgPath);
        }
        log.debug("patternStr before escaping is [" + patternStr + "]");
        patternStr = escape(patternStr);
        log.debug("after escaping the search/match string is [" + patternStr
                + "]");
        log.debug("newPkgPath is [" + newPkgPath + "] about to escape it");
        String replacementStr = escape(getUnixPath(newPkgPath));
        log.debug("replacementStr after escaping is [" + replacementStr + "]");

        return performReplacement(fileContents, patternStr, replacementStr);
    }

    /**
     * This method replaces any UNIX style path separators for Windows style
     * separators
     *
     * @return the path
     */
    private String getWindowsPath(String path)
    {
        String newStr = "";
        char[] strArr = path.toCharArray();

        for (int i = 0; i < strArr.length; i++)
        {
            if (strArr[i] == '/')
            {
                newStr += "\\";
            } else
            {
                newStr += strArr[i];
            }
        }
        log.debug("escaped str is [" + newStr + "]");
        return newStr;
    }

    /**
     * This method replaces any Windows style path separators for UNIX style
     * separators
     *
     * @return the path
     */
    private String getUnixPath(String path)
    {
        log.debug("inside getUnixPath with path [" + path + "]");
        String newStr = "";
        char[] strArr = path.toCharArray();

        for (int i = 0; i < strArr.length; i++)
        {
            if (strArr[i] == '\\')
            {
                newStr += "/";
            } else
            {
                newStr += strArr[i];
            }
        }
        log.debug("escaped str is [" + newStr + "]");
        return newStr;
    }

    /**
     * This method changes the name of any strings in the file and saves the
     * file back to disk
     *
     * With paths, there may be both \ path delimiters or / so we need to run
     * the regex twice to ensure we have replaced both types
     *
     * @return the contents of the file after the package names have been
     *         changed
     */
    private String changeWindowsPaths(String fileContents)
    {
        log.debug("inside changeWindowsPaths");

        String patternStr;

        if (newPkgPath.length() == 0)
        {
            patternStr = getWindowsPath(existingPkgPath) + "\\";
        }
        else
        {
            log.debug("existingPkgPath is currently [" + existingPkgPath
                    + "] before calling getWindowsPath");
            patternStr = getWindowsPath(existingPkgPath);

        }
        log.debug("patternStr is [" + patternStr
                + "] after calling getWindowsPath");
        patternStr = escape(patternStr);
        log.debug("After escaping the pattern/search str it is [" + patternStr
                + "]");
        log.debug("Before escaping and calling getWindowsPath the newPkgPath it is ["
                + newPkgPath + "]");
        String replacementStr = escape(getWindowsPath(newPkgPath));
        log.debug("After escaping it, it is now [" + replacementStr + "]");
        return performReplacement(fileContents, patternStr, replacementStr);
    }

    /**
     * This method changes the name of any strings in the file and saves the
     * file back to disk
     *
     * With paths, there may be both \ path delimiters or / so we need to run
     * the regex twice to ensure we have replaced both types
     *
     * @return the contents of the file after the package names have been
     *         changed
     */
    private String performReplacement(String fileContents, String patternStr,
            String replacementStr)
    {
        log.debug("replacing [" + patternStr + "] with [" + replacementStr + "]");

        // Compile regular expression
        Pattern pattern = Pattern.compile(patternStr);

        // Replace all occurrences of pattern in input
        Matcher matcher = pattern.matcher(fileContents);
        String output = matcher.replaceAll(replacementStr);

        /*
         * if (debug) { debug("output is [" + output + "]"); }
         */
        return output;
    }

    /**
     * This method changes the name of any strings in the file and saves the
     * file back to disk
     *
     * TODO: This needs to support newPkgPath of "" and therefore strip out the
     * package string completely and remove and imports statements that will
     * collapse into (ie. import org.appfuse.Constants; to import Constants;
     * which will cause a compilation error
     *
     * @return the contents of the file after the package names have been
     *         changed
     */
    private String changePackageNames(String fileContents)
    {
        String patternStr;

        if (newPkgName.length() == 0)
        {
            patternStr = existingPkgName + ".";
        }
        else
        {
            patternStr = existingPkgName;
        }
        patternStr = escape(patternStr);

        String replacementStr = escape(newPkgName);

        return performReplacement(fileContents, patternStr, replacementStr);
    }

    /**
     * This method changes the name of any strings in the file and saves the
     * file back to disk
     *
     * @return the contents of the file after the package names have been
     *         changed
     */
    private String changePackageNamesInFile(String fileName,
            boolean saveToSameFile) throws IOException
    {
        log.debug("calling fromFile with fileName [" + fileName + "]");
        String inputStr = fromFile(fileName);

        String output = changePackageNames(inputStr);
        output = changePackagePaths(output);

        if (saveToSameFile)
        {
            if (debug)
            {
                log.debug("replaced package names in file and now saving it to ["
                        + fileName + "]");
            }
            toFile(fileName, output);
        }

        return output;
    }

    /**
     * This method corrects the File separators on the file name
     *
     * @return the correct path to the file
     */
    private String correctFileSeparators(String fileName)
    {
        String localSeparator = File.separator;

        if (localSeparator.equals("\\"))
        {
            return fileName.replace('/', '\\');
        }
        else
        {
            return fileName.replace('\\', '/');
        }
    }

    /**
     * This method changes the name of any strings in the file and saves the
     * file back to disk, the difference is, it uses Spring Ant-style paths
     * to load the files
     */
    private void renameOtherFiles()
    {
        log.info("Inside renameOtherFiles");
        try
        {
            for (Iterator itFSets = filesets.iterator(); itFSets.hasNext(); )
            {
                FileSet fs = (FileSet)itFSets.next();
                fs.setDir(new File(workBaseDir));
                log.debug("got fileset fs [" + fs + "]");
                // TODO - figure out why this won't compile (getProject())
                DirectoryScanner ds = fs.getDirectoryScanner(new org.apache.tools.ant.Project());
                log.debug("ds baseDir is [" + ds.getBasedir().getAbsolutePath() + "]");
                ds.scan();
                String[] includedFiles = ds.getIncludedFiles();
                log.debug("Got includedFiles [" + includedFiles + "]");
                if (includedFiles != null)
                {
                    for(int i=0; i<includedFiles.length; i++) {
                        processOtherFile(includedFiles[i]);
                    }
                }
                else
                {
                    log.info("Did not find any matching files for one of the filesets");

                }
            }
        }
        catch (Exception e)
        {
            log.error("Exception at end of renaming other files [" + e.getMessage()
                    + "]");
        }
    }

    /**
     * This method changes paths and package names in the given file
     */

    private void processOtherFile(String fileName)
    {
        try
        {
            log.error("processOtherFile DLW");
            log.error("Processing file [" + fileName + "]");

            if (isValidFileType(fileName))
            {
                fileName = correctFileSeparators(fileName);
                log.error("After correcting file separators fileName is ["
                    + fileName + "]");
                log.error("file is valid so changing package names");
                fileName = workBaseDir + File.separator + fileName;
                changePackageNamesInFile(fileName,
                    FileUtils.SAVE_FILE);
                log.error("processing change package names on other file ["
                    + fileName + "]");
            }
            else
            {
                log.error("Not processing file [" + fileName + "] as it is not a valid type");
            }
        }
        catch (FileNotFoundException f)
        {
            // continue and process next
            log.error("could not find resource from path ["
                + fileName
                + "]");
        }
        catch (IOException e)
        {
            log.error("IOException when renaming other files [" + e.getMessage() + "]");
        }
    }

    /**
     * This method removes directory structures
     */
    public void deleteAll(String fileName)
    {

        File aFile = new File(fileName);
        log.debug("inside deleteAll with fileName [" + fileName + "]");

        if (aFile.exists())
        {
            boolean isDir = aFile.isDirectory();
            if (isDir)
            {
                String[] inFiles = aFile.list();

                for (int fileNum = 0; fileNum < inFiles.length; fileNum++)
                {
                    String subFileName = fileName + File.separator
                            + inFiles[fileNum];
                    deleteAll(subFileName);
                }
            }
            log.debug("About to delete file inside deleteAll [" + fileName + "]");

            if (aFile.delete())
            {
                if (isDir)
                {
                    log.debug("Deleted dir [" + fileName + "]");
                } else
                {
                    log.debug("Deleted file [" + fileName + "]");
                }
            }
            else
            {
                log.error("Failed to delete file/dir [" + fileName + "]");
            }
        }
    }

    private void refactorNonPackageFiles() {

        try
        {
            String[] extensions = {"properties","tld","xml"};
            
            Iterator filesInMain = org.apache.commons.io.FileUtils.iterateFiles(
                    new File(this.workBaseDir), extensions, true);

            while (filesInMain.hasNext()) {
                File f = (File) filesInMain.next();
                changePackageNamesInFile(f.getAbsolutePath(), FileUtils.SAVE_FILE);
            }

            if (this.baseDir.equals("core")) {
                // core
                Iterator filesInCore = org.apache.commons.io.FileUtils.iterateFiles(
                        new File("core"), extensions, true);

                while (filesInCore.hasNext()) {
                    File f = (File) filesInCore.next();
                    changePackageNamesInFile(f.getAbsolutePath(), FileUtils.SAVE_FILE);
                }
            }

            if (this.baseDir.equals("web")) {
                // web
                Iterator filesInWeb = org.apache.commons.io.FileUtils.iterateFiles(
                        new File("web"), extensions, true);

                while (filesInWeb.hasNext()) {
                    File f = (File) filesInWeb.next();
                    changePackageNamesInFile(f.getAbsolutePath(), FileUtils.SAVE_FILE);
                }
            }

        } catch (IOException ioex) {
            log.error("IOException: " + ioex.getMessage());
        }
    }

    /**
     * This is the main method that gets invoked when ANT calls this task
     */
    public void execute()
    {
        try
        {

            if (newPkgName == null)
            {
                throw new BuildException(
                        "The new package path needs to be set using <renamepackages "
                                + "newPkgName=\"${new.pkg.name}\"");
            }

            if (baseDir == null)
            {
                throw new BuildException(
                        "The base directory needs to be set using <renamepackages "
                                + "baseDir=\"${src.base.dir}\"/>\n");
            }

            log.debug("existingPkgName is [" + this.existingPkgName + "]");
            log.debug("newPkgName is [" + this.newPkgName + "]");

            setPackagePaths();
            log.error("Package paths set");

            repackage(this.baseDir, this.workBaseDir);
            log.error("RePackage directories");

            renameOtherFiles();
            log.error("Rename other files");

            refactorNonPackageFiles();

            // Check the new dir structures for any files left over with the old pkg name in
            checkSummary(this.workBaseDir);
            log.error("CheckSummary");

            deleteAll(this.baseDir);
            log.error("Delete all");

            File workBaseDir = new File(this.workBaseDir);
            if (workBaseDir.renameTo(new File(this.baseDir)))
            {
                log.error("Successfully renamed work dir back to base dir");
            }
            else
            {
                log.error("Error could not rename work dir");
            }
        }
        catch (IOException ioe)
        {
            log.error("Caught an IO:" + ioe.getMessage());
        }
        catch (Exception e)
        {
            log.error("Uncaught exception caught [" + e.getMessage() + "]");
        }
        finally
        {
            //writeLog(); removed method (overkill)
        }
    }

}
