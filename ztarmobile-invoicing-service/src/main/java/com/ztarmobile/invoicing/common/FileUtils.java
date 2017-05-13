/* 
 * Copyright (C) Ztar Mobile, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Armando Rivas <arivas@ztarmobile.com>, March 2017.
 */
package com.ztarmobile.invoicing.common;

import static com.ztarmobile.invoicing.common.CommonUtils.invalidInput;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.apache.log4j.Logger;

/**
 * Utility to handle the file operations.
 *
 * @author armandorivas
 * @since 03/03/17
 */
public class FileUtils {
    /**
     * The file extension for compressed files.
     */
    public static final String GZIP_EXT = ".gz";
    /**
     * Logger for this class.
     */
    private static final Logger LOG = Logger.getLogger(FileUtils.class);

    /**
     * Private constructor.
     */
    private FileUtils() {
        // no objects are allowed to be created.
    }

    /**
     * Copies a file from one directory into another directory.
     * 
     * @param source
     *            The source file.
     * @param target
     *            The target file.
     * 
     */
    public static void copy(File source, File target) {
        try {
            CopyOption[] options = new CopyOption[] { StandardCopyOption.REPLACE_EXISTING,
                    StandardCopyOption.COPY_ATTRIBUTES };

            Files.copy(source.toPath(), target.toPath(), options);
        } catch (IOException e) {
            invalidInput("There was a problem while copying the source file: " + source.getName() + " to "
                    + target.getPath() + ", due to: " + e);
        }
    }

    /**
     * Compresses a file and the existing file is deleted by default.
     * 
     * @param file
     *            The file to be compressed.
     */
    public static void zipIt(File file) {
        byte[] buffer = new byte[1024];
        FileInputStream fis = null;
        FileOutputStream fos = null;
        GZIPOutputStream gzipOS = null;

        File resultingFile = new File(file.getAbsolutePath() + GZIP_EXT);

        try {
            fis = new FileInputStream(file);
            fos = new FileOutputStream(resultingFile);
            gzipOS = new GZIPOutputStream(fos);

            int len;
            while ((len = fis.read(buffer)) != -1) {
                gzipOS.write(buffer, 0, len);
            }
            // we delete the original file after the process
            if (!file.delete()) {
                LOG.warn("This file: " + file + " could not be deleted");
            }
        } catch (IOException ex) {
            LOG.error(ex);
            invalidInput("Can't compress file due to: " + ex);
        } finally {
            close(gzipOS);
            close(fos);
            close(fis);
        }
    }

    /**
     * Gunzip a file.
     * 
     * @param file
     *            The file to be gunzip it.
     * @return The file after it has been uncompressed.
     */
    public static File gunzipIt(File file) {
        byte[] buffer = new byte[1024];
        FileOutputStream out = null;
        GZIPInputStream gzis = null;
        // file without .gz extension...
        File resultingFile = new File(file.toString().substring(0, file.toString().length() - GZIP_EXT.length()));
        try {
            gzis = new GZIPInputStream(new FileInputStream(file));
            out = new FileOutputStream(resultingFile);

            int len;
            while ((len = gzis.read(buffer)) > 0) {
                out.write(buffer, 0, len);
            }
        } catch (IOException ex) {
            LOG.error(ex);
            invalidInput("Can't extract file due to: " + ex);
        } finally {
            // the resources are closed.
            close(gzis);
            close(out);
        }
        return resultingFile;
    }

    /**
     * Executes a shell command.
     * 
     * @param commandExpression
     *            The command expression to be executed.
     */
    public static void executeShellCommand(String commandExpression) {
        try {
            Runtime rt = Runtime.getRuntime();
            String[] cmd = { "/bin/sh", "-c", commandExpression };
            Process proc = rt.exec(cmd);
            printStream(proc.getInputStream(), false);
            printStream(proc.getErrorStream(), true);
        } catch (Exception ex) {
            invalidInput("Can't execute the shell command due to: " + ex);
        }
    }

    /**
     * Prints the input stream.
     * 
     * @param inputStream
     *            The input stream.
     * @param isError
     *            true, prints the log error, otherwise the info level.
     */
    private static void printStream(InputStream inputStream, boolean isError) {
        try (BufferedReader is = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = is.readLine()) != null) {
                if (isError) {
                    LOG.error(line);
                } else {
                    LOG.info(line);
                }
            }
        } catch (IOException ex) {
            invalidInput("Can't stream due to: " + ex);
        }
    }

    /**
     * The input stream.
     * 
     * @param stream
     *            The stream.
     */
    public static void close(InputStream stream) {
        if (stream != null) {
            try {
                stream.close();
            } catch (IOException e) {
                LOG.warn("Error while trying to close the input stream due to: " + e);
            }
        }
    }

    /**
     * The output stream.
     * 
     * @param stream
     *            The stream.
     */
    public static void close(OutputStream stream) {
        if (stream != null) {
            try {
                stream.close();
            } catch (IOException e) {
                LOG.warn("Error while trying to close the output stream due to: " + e);
            }
        }
    }
}
