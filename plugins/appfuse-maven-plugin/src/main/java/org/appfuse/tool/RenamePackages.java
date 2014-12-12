package org.appfuse.tool;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugin.logging.SystemStreamLog;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.types.FileSet;


/**
 * This class is almost a complete copy of replacepackages
 * by Ben Gill.  These utility methods can be used in the
 * plugin to change package names of appfuse source
 * contributions.  Liberties have been taken to make this
 * open source code just work here. Some methods were removed
 * and logging now uses SystemStreamLog from Apache (Maven)
 *
 * @author <a href="mailto:david@capehenrytech.com">David L. Whitehurst</a>
 */
public class RenamePackages {

    static final boolean SAVE_FILE = true;
    static final boolean DONT_SAVE_FILE = false;
    boolean debug = false;
    private String baseDir = "src"; // root
    private String workBaseDir = "null"; // actually called null
    private String existingPkgName = "org.appfuse"; // AppFuse
    private String newPkgName;
    private String existingPkgPath;
    private String newPkgPath;
    protected transient final Log log = new SystemStreamLog();

    StringBuffer logOutput = new StringBuffer();

    String[] invalidFileTypes = new String[]{"class", "jar", "jpg", "gif", "png", "ico"};

    private Vector filesets = new Vector();

    /**
     * Constructor
     */
    public RenamePackages(String newPackage) {
        this.newPkgName = newPackage;
    }

    /**
     * simple method to add filesets
     *
     * @param fileset
     */
    public void addFileset(FileSet fileset) {
        filesets.add(fileset);
    }

    /**
     * Override the default set of invalid file types that will be moved to the
     * new package structure.
     * <p/>
     * The invalidFileTypes must be a comma
     * separated list of file extensions
     * for example "class,jar,exe"
     *
     * @param invalidFileTypes
     */
    public void setInvalidFileTypes(String invalidFileTypes) {
        this.invalidFileTypes = invalidFileTypes.split(",");

        if (debug) {
            log.debug("default invalidFileTypes overriden with [" + invalidFileTypes
                    + "]");
        }
    }

    /**
     * Set the base directory. The base directory will be where the Task starts
     * to recursively look for files with the given package name in it. This
     * method will be called by ANT when it invokes the Task.
     *
     * @param baseDir
     */
    public void setBaseDir(String baseDir) {
        if (!new File(baseDir.trim()).exists()) {
            throw new ExceptionInInitializerError("Base dir [" + baseDir.trim()
                    + "] does not exist");
        }
        this.baseDir = correctFileSeparators(baseDir.trim());
        if (this.baseDir != null) {
            if (this.baseDir.endsWith(File.separator)) {
                this.workBaseDir = this.baseDir.substring(0, this.baseDir
                        .length())
                        + ".work";
            } else {
                this.workBaseDir = this.baseDir + ".work";
            }

            if (debug) {
                log.debug("Set workBaseDir to [" + this.workBaseDir + "]");
            }
        }
    }

    /**
     * Validates the packagename
     *
     * @param packageName
     * @throws ExceptionInInitializerError if the package name is invalid
     */
    private void validatePackageName(String packageName)
            throws ExceptionInInitializerError {
        if (packageName.indexOf("/") != -1) {
            throw new ExceptionInInitializerError("packageName [" + packageName
                    + "] is invalid because it contains slashes");
        }
        if (packageName.indexOf("\\") != -1) {
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
    private String stripUnwantedDots(String packageName) {

        while (packageName.endsWith(".")) {
            packageName = packageName.substring(0, packageName.length() - 1);
        }
        while (packageName.startsWith(".")) {
            packageName = packageName.substring(1);
        }

        if (debug) {
            log.debug("returning stripped package name of [" + packageName + "]");
        }

        return packageName;
    }

    /**
     * Set the existing package name. This will become the string that the Task
     * looks to replace in the files look for files with the given package name
     * in it. This method will be called by ANT when it invokes the Task.
     *
     * @param existingPkgName
     */
    public void setExistingPkgName(String existingPkgName) throws Exception {

        log.info("existingPkgName came in as [" + existingPkgName + "]");

        this.existingPkgName = stripUnwantedDots(existingPkgName.trim());
        validatePackageName(this.existingPkgName);
        if (this.existingPkgName.length() == 0) {
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
    public void setNewPkgName(String newPkgName) throws Exception {
        log.info("newPkgName came in as [" + newPkgName + "]");

        this.newPkgName = stripUnwantedDots(newPkgName.trim());
        validatePackageName(this.newPkgName);
        if (this.newPkgName.length() == 0) {
            throw new Exception("Unimplemented operation");
        }
    }

    /**
     * Set the package paths. Replace the . delimiter with the relevant file
     * separator based on the o/s we are running on.
     */
    private void setPackagePaths() {
        this.existingPkgPath = getPackagePath(existingPkgName);
        this.newPkgPath = getPackagePath(newPkgName);

        if (debug) {
            log.debug("Set existing package path as [" + existingPkgPath + "]");
            log.debug("Set new package path as [" + newPkgPath + "]");
        }
    }

    /**
     * Set the new package name. This will become the package name and replace
     * the existing package name in the source files. This method will be called
     * by ANT when it invokes the Task.
     *
     * @param pkgName
     * @return path of the pkg name
     */
    private String getPackagePath(String pkgName) {
        String[] pkgNames = pkgName.split("\\.");
        String aPath = "";

        for (int i = 0; i < pkgNames.length; i++) {
            if (aPath.length() != 0) {
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
    private void createDirectory(String directoryName) {
        File dir = new File(directoryName);

        if (!dir.exists()) {
            if (dir.mkdirs()) {
                String message = "Created directory [" + directoryName + "]";

                if (debug) {
                    log.debug(message);
                }
            } else {
                log.error("Failed to create directory [" + directoryName + "]");
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

        if (debug) {
            log.debug("Inside repackage inDirName is [" + inDirName + "]");
            log.debug("Inside repackage inWorkDirName is [" + inWorkDirName + "]");
        }

        String[] files = new File(inDirName).list();

        if (files == null) {
            return;
        }

        if (debug) {
            log.debug("There are [" + files.length + "] files in dir [" + inDirName
                    + "]");
        }

        for (int i = 0; i < files.length; i++) {
            if (debug) {
                log.debug("file is [" + files[i] + "]");
            }

            String fileName = inDirName + File.separator + files[i];
            File aFile = new File(fileName);

            if (files[i].equals("CVS")) {
                if (debug) {
                    log.debug("ignoring CVS dir");
                }
            } else if (aFile.isDirectory()) {
                if (debug) {
                    log.debug("Got a dir [" + fileName + "]");
                }

                String newDirName = inDirName + File.separator + files[i];
                String newWorkDirName = inWorkDirName + File.separator
                        + files[i];

                if (isOldPackageDir(fileName)) {
                    String newPath = convertOldPackageDirName(fileName);

                    if (debug) {
                        log.debug("found old package dir [" + fileName + "] "
                                + "newPath is [" + newPath.toString() + "]");
                    }
                    createDirectory(newPath.toString());

                } else {

                    // Replace existing baseDir
                    String newPath = this.workBaseDir
                            + fileName.substring(this.baseDir.length());

                    if (debug) {
                        log.debug("found dir outside old package [" + fileName + "] "
                                + "newPath is [" + newPath + "]");
                    }
                    createDirectory(newPath);
                }

                if (debug) {
                    log.debug("Recursing with [" + newDirName + "]");
                }

                repackage(newDirName, newWorkDirName);

            } else {
                // Normal file
                if (debug) {
                    log.debug("Processing file [" + fileName + "] existingPkgPath is ["
                            + existingPkgPath + "]");
                }

                // Should we process this file at all or leave it?
                int existingPathIndexPos = fileName.indexOf(existingPkgPath);

                if (existingPathIndexPos != -1) {
                    // Normal file in old package structure
                    if (debug) {
                        log.debug("found file with existing package name ["
                                + fileName + "]");
                    }

                    String newPath = convertOldPackageDirName(fileName
                            .substring(0, fileName.lastIndexOf(File.separator)));
                    String newFileName = newPath
                            + fileName.substring(fileName
                            .lastIndexOf(File.separator));

                    if (debug) {
                        log.debug("creating directory [" + newPath + "]");
                    }

                    createDirectory(newPath);

                    if (isValidFileType(fileName)) {
                        String output = changePackageNamesInFile(fileName,
                                RenamePackages.DONT_SAVE_FILE);

                        if (debug) {
                            log.debug("Saving file [" + fileName
                                    + "] to new package directory ["
                                    + newFileName + "]");
                        }

                        toFile(newFileName, output);
                    } else {
                        if (debug) {
                            log.debug("Renaming file non valid file type ["
                                    + fileName + "]");
                        }

                        if (!aFile.renameTo(new File(newFileName))) {
                            log.error("Failed to rename file [" + fileName + "] to ["
                                    + newFileName + "]");
                        }
                    }
                } else {
                    // Normal file - not in old package structure

                    // Stip off existing baseDir
                    String newFileName = this.workBaseDir
                            + fileName.substring(this.baseDir.length());

                    if (aFile.renameTo(new File(newFileName))) {
                        if (debug) {
                            log.debug("Saved file [" + newFileName
                                    + "] to new directory structure");
                        }
                    } else {
                        log.error("Failed to rename file [" + fileName + "] to ["
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
            throws IOException {
        if (debug) {
            log.debug("Inside checkSummary inDirName is [" + inDirName + "]");
        }

        String[] files = new File(inDirName).list();

        if (files == null) {
            return;
        }

        if (debug) {
            log.debug("There are [" + files.length + "] files in dir [" + inDirName + "]");
        }

        for (String file : files) {

            if (debug) {
                log.debug("file is [" + file + "]");
            }

            String fileName = inDirName + File.separator + file;
            File aFile = new File(fileName);

            if (aFile.isDirectory()) {

                if (debug) {
                    log.debug("Got a dir [" + fileName + "]");
                }

                String newDirName = inDirName + File.separator + file;

                if (debug) {
                    log.debug("Recursing with [" + newDirName + "]");
                }

                checkSummary(newDirName);
            } else {
                // Normal file
                if (debug) {
                    log.debug("Checking file [" + fileName + "] existingPkgPath is ["
                            + existingPkgPath + "]");
                }

                if (isValidFileType(fileName)) {
                    if (hasFileOldPathOrPkg(fileName)) {
                        if (debug) {
                            log.debug("File [" + fileName + "] still has old pkg [" + existingPkgPath + "]");
                        }
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

    private String convertOldPackageDirName(String dirName) {
        // Need to create new package directory
        int startExistingPathIndexPos = dirName.indexOf(existingPkgPath);
        int endExistingPathIndexPos = startExistingPathIndexPos
                + existingPkgPath.length();

        StringBuffer newPath = new StringBuffer();
        newPath.append(this.workBaseDir);

        // Get the front bit
        if (debug) {
            log.debug("startExistingPathIndexPos is [" + startExistingPathIndexPos + "]");
            log.debug("about to do substring on [" + dirName + "] positions ["
                    + this.baseDir.length() + "] and [" + startExistingPathIndexPos
                    + "]");
        }

        String firstPartFileName = dirName.substring(this.baseDir.length(),
                startExistingPathIndexPos);

        if (debug) {
            log.debug("firstPartFileName is [" + firstPartFileName + "]");
        }

        newPath.append(firstPartFileName);
        newPath.append(newPkgPath);

        String lastPartFileName = dirName.substring(endExistingPathIndexPos);

        if (debug) {
            log.debug("appending lastPartFileName [" + lastPartFileName + "]");
        }

        newPath.append(lastPartFileName);

        return newPath.toString();
    }


    private boolean isOldPackageDir(String dirName) {
        if (debug) {
            log.debug("inside isOldPackageDir with [" + dirName + "]");
        }

        // Should we process this file at all or leave it?
        int existingPathIndexPos = dirName.indexOf(existingPkgPath);

        if (existingPathIndexPos != -1) {
            if (debug) {
                log.debug("found dir with existing package name [" + dirName + "]");
            }
            return true;
        }

        if (debug) {
            log.debug("dir [" + dirName + "] is not old package dir");
        }

        return false;
    }

    /*
     * This method reads a file and returns a string suitable for some regex
     * operation
     *
     * @param fileName the file name to open and read @return the file contents
     */
    public String fromFile(String fileName) throws IOException {
        /*BufferedReader in = new BufferedReader(new FileReader(fileName));
        StringBuffer fileContents = new StringBuffer();
        String str;

        while ((str = in.readLine()) != null)
        {
            fileContents.append(str);
            fileContents.append("\n");
        }
        in.close();
        return fileContents.toString();*/
        return FileUtils.readFileToString(new File(fileName), "UTF-8");
    }

    /*
     * This method saves a file to disk
     *
     * @param fileName the name of the file @param contents the files contents
     * @throws IOException if there is an error writing the file
     */
    public void toFile(String fileName, String contents) throws IOException {
        if (debug) {
            log.debug("Saving file to fileName [" + fileName + "]");
        }

        FileUtils.writeStringToFile(new File(fileName), contents, "UTF-8");
    }

    /**
     * Checks to see if the file is a valid type
     *
     * @return true if the file is a valid type, else false
     */
    private boolean isValidFileType(String fileName) {
        for (int i = 0; i < invalidFileTypes.length; i++) {
            if (fileName.endsWith(invalidFileTypes[i])) {
                if (debug) {
                    log.debug("File [" + fileName
                            + "] will just be moved as it is not a valid type");
                }
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
    private String escape(String str) {
        String newStr = "";
        char[] strArr = str.toCharArray();

        for (int i = 0; i < strArr.length; i++) {
            if (strArr[i] == '.') {
                newStr += "\\";
            }
            if (strArr[i] == '\\') {
                newStr += "\\";
            }
            newStr += strArr[i];
        }

        if (debug) {
            log.debug("escaped str is [" + newStr + "]");
        }

        return newStr;
    }

    /**
     * This method changes the name of any strings in the file and saves the
     * file back to disk
     * <p/>
     * With paths, there may be both \ path delimiters or / so we need to run
     * the regex twice to ensure we have replaced both types
     *
     * @return the contents of the file after the package names have been
     *         changed
     */
    private String changePackagePaths(String fileContents) {
        String output = changeWindowsPaths(fileContents);
        output = changeUnixPaths(output);
        return output;
    }

    /**
     * This method changes the name of any strings in the file and saves the
     * file back to disk
     * <p/>
     * With paths, there may be both \ path delimiters or / so we need to run
     * the regex twice to ensure we have replaced both types
     *
     * @return the contents of the file after the package names have been
     *         changed
     */
    private String changeUnixPaths(String fileContents) {
        if (debug) {
            log.debug("inside changeUnixPaths");
        }

        String patternStr;

        if (newPkgPath.length() == 0) {
            patternStr = getUnixPath(existingPkgPath) + "/";
        } else {
            if (debug) {
                log.debug("before calling getUnixPath existingPkgPath is ["
                        + existingPkgPath + "]");
            }

            patternStr = getUnixPath(existingPkgPath);
        }

        if (debug) {
            log.debug("patternStr before escaping is [" + patternStr + "]");
        }

        patternStr = escape(patternStr);

        if (debug) {
            log.debug("after escaping the search/match string is [" + patternStr
                    + "]");
            log.debug("newPkgPath is [" + newPkgPath + "] about to escape it");
        }

        String replacementStr = escape(getUnixPath(newPkgPath));

        if (debug) {
            log.debug("replacementStr after escaping is [" + replacementStr + "]");
        }

        return performReplacement(fileContents, patternStr, replacementStr);
    }

    /**
     * This method replaces any UNIX style path separators for Windows style
     * separators
     *
     * @return the path
     */
    private String getWindowsPath(String path) {
        String newStr = "";
        char[] strArr = path.toCharArray();

        for (int i = 0; i < strArr.length; i++) {
            if (strArr[i] == '/') {
                newStr += "\\";
            } else {
                newStr += strArr[i];
            }
        }

        if (debug) {
            log.debug("escaped str is [" + newStr + "]");
        }

        return newStr;
    }

    /**
     * This method replaces any Windows style path separators for UNIX style
     * separators
     *
     * @return the path
     */
    private String getUnixPath(String path) {
        if (debug) {
            log.debug("inside getUnixPath with path [" + path + "]");
        }

        String newStr = "";
        char[] strArr = path.toCharArray();

        for (int i = 0; i < strArr.length; i++) {
            if (strArr[i] == '\\') {
                newStr += "/";
            } else {
                newStr += strArr[i];
            }
        }

        if (debug) {
            log.debug("escaped str is [" + newStr + "]");
        }

        return newStr;
    }

    /**
     * This method changes the name of any strings in the file and saves the
     * file back to disk
     * <p/>
     * With paths, there may be both \ path delimiters or / so we need to run
     * the regex twice to ensure we have replaced both types
     *
     * @return the contents of the file after the package names have been
     *         changed
     */
    private String changeWindowsPaths(String fileContents) {
        if (debug) {
            log.debug("inside changeWindowsPaths");
        }

        String patternStr;

        if (newPkgPath.length() == 0) {
            patternStr = getWindowsPath(existingPkgPath) + "\\";
        } else {
            if (debug) {
                log.debug("existingPkgPath is currently [" + existingPkgPath
                        + "] before calling getWindowsPath");
            }

            patternStr = getWindowsPath(existingPkgPath);

        }

        if (debug) {
            log.debug("patternStr is [" + patternStr
                    + "] after calling getWindowsPath");
        }

        patternStr = escape(patternStr);

        if (debug) {
            log.debug("After escaping the pattern/search str it is [" + patternStr
                    + "]");
            log.debug("Before escaping and calling getWindowsPath the newPkgPath it is ["
                    + newPkgPath + "]");
        }

        String replacementStr = escape(getWindowsPath(newPkgPath));

        if (debug) {
            log.debug("After escaping it, it is now [" + replacementStr + "]");
        }

        return performReplacement(fileContents, patternStr, replacementStr);
    }

    /**
     * This method changes the name of any strings in the file and saves the
     * file back to disk
     * <p/>
     * With paths, there may be both \ path delimiters or / so we need to run
     * the regex twice to ensure we have replaced both types
     *
     * @return the contents of the file after the package names have been
     *         changed
     */
    private String performReplacement(String fileContents, String patternStr,
                                      String replacementStr) {
        if (debug) {
            log.debug("replacing [" + patternStr + "] with [" + replacementStr + "]");
        }

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
     * @return the contents of the file after the package names have been
     *         changed
     */
    private String changePackageNames(String fileContents) {
        String patternStr;

        if (newPkgName.length() == 0) {
            patternStr = existingPkgName + ".";
        } else {
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
                                            boolean saveToSameFile) throws IOException {
        if (debug) {
            log.debug("calling fromFile with fileName [" + fileName + "]");
        }

        String inputStr = fromFile(fileName);

        String output = changePackageNames(inputStr);
        output = changePackagePaths(output);

        if (saveToSameFile) {
            if (debug) {
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
    private String correctFileSeparators(String fileName) {
        String localSeparator = File.separator;

        if (localSeparator.equals("\\")) {
            return fileName.replace('/', '\\');
        } else {
            return fileName.replace('\\', '/');
        }
    }

    /**
     * This method changes the name of any strings in the file and saves the
     * file back to disk, the difference is, it uses Spring Ant-style paths
     * to load the files
     */
    private void renameOtherFiles() {
        if (debug) {
            log.debug("Inside renameOtherFiles");
        }

        try {
            for (Iterator itFSets = filesets.iterator(); itFSets.hasNext();) {
                FileSet fs = (FileSet) itFSets.next();
                fs.setDir(new File(workBaseDir));

                if (debug) {
                    log.debug("got fileset fs [" + fs + "]");
                }

                DirectoryScanner ds = fs.getDirectoryScanner(new org.apache.tools.ant.Project());

                if (debug) {
                    log.debug("ds baseDir is [" + ds.getBasedir().getAbsolutePath() + "]");
                }

                ds.scan();
                String[] includedFiles = ds.getIncludedFiles();

                if (debug) {
                    log.debug("Got includedFiles [" + includedFiles + "]");
                }

                if (includedFiles != null) {
                    for (int i = 0; i < includedFiles.length; i++) {
                        processOtherFile(includedFiles[i]);
                    }
                } else {
                    if (debug) {
                        log.debug("Did not find any matching files for one of the filesets");
                    }

                }
            }
        }
        catch (Exception e) {
            log.error("Exception at end of renaming other files [" + e.getMessage()
                    + "]");
        }
    }

    /**
     * This method changes paths and package names in the given file
     */

    private void processOtherFile(String fileName) {
        try {
            if (debug) {
                log.debug("Processing file [" + fileName + "]");
            }

            if (isValidFileType(fileName)) {
                fileName = correctFileSeparators(fileName);

                if (debug) {
                    log.debug("After correcting file separators fileName is ["
                            + fileName + "]");
                    log.debug("file is valid so changing package names");
                }

                fileName = workBaseDir + File.separator + fileName;
                changePackageNamesInFile(fileName,
                        RenamePackages.SAVE_FILE);

                if (debug) {
                    log.debug("processing change package names on other file ["
                            + fileName + "]");
                }
            } else {
                log.error("Not processing file [" + fileName + "] as it is not a valid type");
            }
        }
        catch (FileNotFoundException f) {
            // continue and process next
            log.error("could not find resource from path ["
                    + fileName
                    + "]");
        }
        catch (IOException e) {
            log.error("IOException when renaming other files [" + e.getMessage() + "]");
        }
    }

    /**
     * This method removes directory structures
     */
    public void deleteAll(String fileName) {

        File aFile = new File(fileName);

        if (debug) {
            log.debug("inside deleteAll with fileName [" + fileName + "]");
        }

        if (aFile.exists()) {
            boolean isDir = aFile.isDirectory();
            if (isDir) {
                String[] inFiles = aFile.list();

                for (int fileNum = 0; fileNum < inFiles.length; fileNum++) {
                    String subFileName = fileName + File.separator
                            + inFiles[fileNum];
                    deleteAll(subFileName);
                }
            }

            if (debug) {
                log.debug("About to delete file inside deleteAll [" + fileName + "]");
            }

            if (aFile.delete()) {
                if (debug) {
                    if (isDir) {
                        log.debug("Deleted dir [" + fileName + "]");
                    } else {
                        log.debug("Deleted file [" + fileName + "]");
                    }
                }
            } else {
                log.error("Failed to delete file/dir [" + fileName + "]");
            }
        }
    }

    private void refactorNonPackageFiles() {

        try {
            String[] extensions = {"java", "page", "application", "properties", "tld", "xml"};

            Iterator filesInMain = FileUtils.iterateFiles(new File(this.workBaseDir), extensions, true);

            while (filesInMain.hasNext()) {
                File f = (File) filesInMain.next();
                changePackageNamesInFile(f.getAbsolutePath(), RenamePackages.SAVE_FILE);
            }

        } catch (IOException ioex) {
            log.error("IOException: " + ioex.getMessage());
        }
    }

    /**
     * This is the main method that gets invoked when ANT calls this task
     */
    public void execute() {
        try {

            if (newPkgName == null) {
                throw new BuildException(
                        "The new package path needs to be set using <renamepackages "
                                + "newPkgName=\"${new.pkg.name}\"");
            }

            if (baseDir == null) {
                throw new BuildException(
                        "The base directory needs to be set using <renamepackages "
                                + "baseDir=\"${src.base.dir}\"/>\n");
            }

            if (debug) {
                log.info("existingPkgName is [" + this.existingPkgName + "]");
                log.info("newPkgName is [" + this.newPkgName + "]");
            }

            setPackagePaths();

            if (debug) {
                log.info("Package paths set");
            }

            repackage(this.baseDir, this.workBaseDir);

            if (debug) {
                log.info("RePackage directories");
            }

            renameOtherFiles();

            if (debug) {
                log.info("Rename other files");
            }

            // fix files with qualified names other than old package
            refactorNonPackageFiles();

            // Check the new dir structures for any files left over with the old pkg name in
            checkSummary(this.workBaseDir);

            if (debug) {
                log.info("CheckSummary");
            }

            deleteAll(this.baseDir);

            if (debug) {
                log.info("Delete all");
            }

            File workBaseDir = new File(this.workBaseDir);
            if (workBaseDir.renameTo(new File(this.baseDir))) {
                if (debug) {
                    log.info("Successfully renamed work dir back to base dir");
                }
            } else {
                log.error("Error could not rename work dir");
            }

            // delete src/**/java/org if it exists
            deleteOrgPackage("main");
            deleteOrgPackage("test");
        }
        catch (IOException ioe) {
            log.error("Caught an IO:" + ioe.getMessage());
        }
        catch (Exception e) {
            log.error("Uncaught exception caught [" + e.getMessage() + "]");
        }

        // success!
        log.info("[AppFuse] Refactored all 'org.appfuse' packages and paths to '" + newPkgName + "'.");
    }

    private void deleteOrgPackage(String path) {
        File orgDir = new File(baseDir + "/" + path + "/java/org");
        if (orgDir.isDirectory() && orgDir.list().length == 0) {
            if (!orgDir.delete()) {
                log.warn("Failed to delete '" + orgDir.getAbsolutePath() + "', please delete manually.");
            }
        }
    }

}

