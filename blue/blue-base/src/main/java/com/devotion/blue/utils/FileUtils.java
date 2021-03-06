package com.devotion.blue.utils;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import com.jfinal.core.JFinal;
import com.jfinal.kit.PathKit;
import com.jfinal.log.Log;


public class FileUtils {

    private static final Log log = Log.getLog(FileUtils.class);

    public static String getSuffix(String fileName) {
        if (fileName != null && fileName.contains(".")) {
            return fileName.substring(fileName.lastIndexOf("."));
        }
        return null;
    }


    public static String removePrefix(String src, String prefix) {
        if (src != null && src.startsWith(prefix)) {
            return src.substring(prefix.length());
        }
        return src;
    }


    public static String removeRootPath(String src) {
        return removePrefix(src, PathKit.getWebRootPath());
    }

    public static String readString(File file) {
        ByteArrayOutputStream baos = null;
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
            baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            for (int len; (len = fis.read(buffer)) > 0; ) {
                baos.write(buffer, 0, len);
            }
            return new String(baos.toByteArray(), JFinal.me().getConstants().getEncoding());
        } catch (Exception e) {
            log.error("read file error:" + e);
        } finally {
            close(fis, baos);
        }
        return null;
    }

    public static void writeString(File file, String string) {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file, false);
            fos.write(string.getBytes(JFinal.me().getConstants().getEncoding()));
        } catch (Exception e) {
            log.error("write file error:" + e);
        } finally {
            close(null, fos);
        }
    }

    private static void close(InputStream is, OutputStream os) {
        if (is != null)
            try {
                is.close();
            } catch (IOException e) {
                log.error("close InputStream error:" + e);
            }
        if (os != null)
            try {
                os.close();
            } catch (IOException e) {
                log.error("close OutputStream error:" + e);
            }
    }

    public static void unzip(String zipFilePath) throws IOException {
        String targetPath = zipFilePath.substring(0, zipFilePath.lastIndexOf("."));
        unzip(zipFilePath, targetPath);
    }

    public static void unzip(String zipFilePath, String targetPath) throws IOException {
        ZipFile zipFile = new ZipFile(zipFilePath);
        Enumeration<?> entryEnum = zipFile.entries();
//        try {
        if (null != entryEnum) {
            while (entryEnum.hasMoreElements()) {
                OutputStream os = null;
                InputStream is = null;
                try {
                    ZipEntry zipEntry = (ZipEntry) entryEnum.nextElement();
                    if (!zipEntry.isDirectory()) {
                        File targetFile = new File(targetPath + File.separator + zipEntry.getName());
                        File parentFile = targetFile.getParentFile();
                        if (!parentFile.exists()) {
                            parentFile.mkdirs();
                        }
                        os = new BufferedOutputStream(new FileOutputStream(targetFile));
                        is = zipFile.getInputStream(zipEntry);
                        byte[] buffer = new byte[4096];
                        int readLen;
                        while ((readLen = is.read(buffer, 0, 4096)) > 0) {
                            os.write(buffer, 0, readLen);
                        }
                    }
                } finally {
                    if (is != null)
                        is.close();
                    if (os != null)
                        os.close();
                }
            }
        }
//        } finally {
        zipFile.close();
//        }
    }

}