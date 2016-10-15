/*
 * Created on 2006-3-18
 */
package com.wugy.java.utils;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

/**
 * Zip相关函数
 *
 * @author zhangym
 * @version 0.9
 * @since 0.9
 */
public final class ZipUtil {

	public static File[] toFiles(File[] srcFiles) {
		List<File> files = new ArrayList<>();
		for (int i = 0; i < srcFiles.length; i++) {
			File file = srcFiles[i];
			if (!file.exists()) {
				System.out.println("warnning:the file you want to backup is missing=" + file.getAbsolutePath());
				continue;
			}
			if (file.isDirectory()) {
				// 递归调用
				File[] dir = toFiles(file.listFiles());
				for (int j = 0; j < dir.length; j++) {
					File dirfile = dir[j];
					files.add(dirfile);
				}
			} else {
				files.add(file);
			}
		}
		File[] a = new File[files.size()];
		return (File[]) files.toArray(a);
	}

	public static File[] toFiles(String[] srcFiles) {
		File[] a = new File[srcFiles.length];
		for (int i = 0; i < srcFiles.length; i++) {
			a[i] = new File(srcFiles[i]);
		}
		return toFiles(a);
	}

	/**
	 * @param file
	 * @param preffixLen
	 *            根目录长度，为0时使用绝对路径保存
	 * @return
	 */
	public static String entryName(File file, int preffixLen) {
		String entryName = file.getPath();
		if (file.isDirectory())
			entryName = entryName.endsWith(File.separator) ? entryName : entryName + File.separator;
		entryName = entryName.replace(File.separatorChar, '/').substring(preffixLen);
		return entryName;
	}

	/**
	 * 压缩文件
	 *
	 * @param srcFiles
	 *            要被压缩的文件名
	 * @param dstDir
	 *            压缩后的文件名
	 * @param comment
	 *            文件注释
	 * @param preffixLen
	 *            根目录长度，为0时使用绝对路径保存，大于0时，会对文件的绝对路径前减去该长度
	 */

	public static void zip(String[] strFiles, String dstFile, String comment, int preffixLen) throws IOException {
		if (strFiles.length == 0)
			return;
		File fileDst = new File(dstFile);
		File parentDir = fileDst.getParentFile();
		if (!parentDir.exists()) {
			parentDir.mkdirs();
		}
		if (fileDst.exists())
			fileDst.delete();
		fileDst.createNewFile();

		ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(fileDst));
		zos.setMethod(ZipOutputStream.DEFLATED);

		if (comment != null) {
			zos.setComment(comment);
		}

		DataOutputStream dos = new DataOutputStream(zos);

		File[] srcFiles = toFiles(strFiles);

		for (int i = 0; i < srcFiles.length; i++) {

			String entryPath = entryName(srcFiles[i], preffixLen);
			zos.putNextEntry(new ZipEntry(entryPath));

			FileInputStream fis = new FileInputStream(srcFiles[i]);

			byte[] buff = new byte[8192];
			int len = 0;

			while (true) {
				len = fis.read(buff);
				if (len == -1 || len == 0)
					break;

				dos.write(buff, 0, len);
			}

			zos.closeEntry();
			fis.close();
		}
		dos.close();
		zos.close();
	}

	/**
	 * 解压缩文件
	 *
	 * @param srcFile
	 *            压缩文件名
	 * @param dstDir
	 *            要解压到的目录
	 * @param entryName
	 *            要解压哪个文件，如果为null则解压所有文件。
	 * @param retainPath
	 *            是否保留文件路径信息，true-保留，false-不保留。
	 */
	public static void unzip(String srcFile, String dstDir, String entryName, boolean retainPath) throws Exception {

		ZipFile zipFile = null;

		try {
			zipFile = new ZipFile(srcFile);
			Enumeration<? extends ZipEntry> entryEnu = zipFile.entries();
			while (entryEnu.hasMoreElements()) {
				File fileItem = null;
				ZipEntry entry = (ZipEntry) entryEnu.nextElement();

				String name = entry.getName();
				if (entryName != null && !entryName.equalsIgnoreCase(name))
					continue;
				if (dstDir != null) {
					if (!retainPath) {
						File f = new File(name);
						name = f.getName();
					} else {
						if (System.getProperty("os.name").indexOf("Windows") != -1) {

							if (name.matches("^[a-zA-Z]:.*")) {
								// 如果是WINDOWS系统，将文件名中的驱动器符号去掉
								name = name.substring(name.indexOf('\\') + 1);
							}
						}
					}
					fileItem = new File(dstDir + File.separator + name);
				} else {
					fileItem = new File(name);
				}

				File parentDir = fileItem.getParentFile();
				if (!parentDir.exists()) {
					parentDir.mkdirs();
				}
				if (fileItem.exists())
					fileItem.delete();
				fileItem.createNewFile();
				FileOutputStream fos = null;
				InputStream is = null;

				try {
					fos = new FileOutputStream(fileItem);
					is = zipFile.getInputStream(entry);

					byte[] buff = new byte[8192];
					int len = 0;
					while ((len = is.read(buff)) != -1) {
						fos.write(buff, 0, len);
					}
				} finally {
					try {
						fos.flush();
						fos.close();
						is.close();
					} catch (Exception e) {
					}
				}
			}
		} catch (Exception e) {
			throw new Exception("解压缩失败：" + e.getMessage());

		} finally {
			try {
				zipFile.close();
			} catch (Exception e) {
			}
		}

	}

	/**
	 * 将文件解压缩到指定的目录，保持被压缩文件的路径信息。
	 */
	public static void unzip(String zipFile, String dstDir) throws Exception {
		unzip(zipFile, dstDir, null, true);
	}

	/**
	 * 解压缩文件，覆盖被压缩的文件。
	 */
	public static void unzip(String zipFile) throws Exception {
		unzip(zipFile, null, null, true);
	}

}
