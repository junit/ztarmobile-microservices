/* 
 * Copyright (C) Ztar Mobile, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Armando Rivas <arivas@ztarmobile.com>, March 2017.
 */
package com.ztarmobile.invoicing.common;

import static com.ztarmobile.invoicing.common.CommonUtils.invalidInput;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.zip.GZIPInputStream;

import org.apache.log4j.Logger;

/**
 * Utility to handle the file operations.
 *
 * @author armandorivas
 * @since 03/03/17
 */
public class FileUtils {
    /**
     * Logger for this class
     */
    private static final Logger log = Logger.getLogger(FileUtils.class);

    /**
     * Private constructor.
     */
    private FileUtils() {
        // no objects are allowed to be created.
    }

    /**
     * Copies a file from one directory into another directory
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
     * Gunzip a file.
     * 
     * @param file
     *            The file to be gunzip it.
     */
    public static void gunzipIt(File file) {
        byte[] buffer = new byte[1024];
        FileOutputStream out = null;
        GZIPInputStream gzis = null;
        try {
            gzis = new GZIPInputStream(new FileInputStream(file));
            out = new FileOutputStream(file.toString().substring(0, file.toString().length() - 3));

            int len;
            while ((len = gzis.read(buffer)) > 0) {
                out.write(buffer, 0, len);
            }
        } catch (IOException ex) {
            log.error(ex);
            invalidInput("Can't extract file due to: " + ex);
        } finally {
            close(gzis);
            close(out);
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
                log.warn("Error while trying to close the input stream due to: " + e);
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
                log.warn("Error while trying to close the output stream due to: " + e);
            }
        }
    }
}
