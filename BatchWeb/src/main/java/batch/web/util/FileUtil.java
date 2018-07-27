/**
 * @Project :  스마트톨링정보시스템 구축
 * @Class : FileUtil.java
 * @Description : 
 *
 * @Author : LGCNS
 * @Since : 2017. 4. 20.
 *
 * @Copyright (c) 2018 EX All rights reserved.
 *-------------------------------------------------------------
 *              Modification Information
 *-------------------------------------------------------------
 * 날짜            수정자             변경사유 
 *-------------------------------------------------------------
 * 2018. 6. 12.        LGCNS             최초작성
 *-------------------------------------------------------------
 */

package batch.web.util;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.apache.commons.lang3.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

public class FileUtil {
	private static final Logger LOGGER = LoggerFactory.getLogger(FileUtil.class);
	
	
	public static Collection<File> getFiles(String dir, String wildcard) throws Exception {
		if (dir == null) {
			return null;
		}
		File f = new File(dir);
		if (!f.isDirectory()) {
			return null;
		}
		if (wildcard != null) {
			return FileUtils.listFiles(new File(dir), new WildcardFileFilter(wildcard) , TrueFileFilter.INSTANCE);			
		}
		else {
			return FileUtils.listFiles(new File(dir), null , TrueFileFilter.INSTANCE);						
		}
	}
	
	
	public static boolean makeDir(String dir) throws Exception {
		boolean isOk = true;
		File file = new File(dir);

		if (!file.exists()) {
			isOk = file.mkdirs();
		}
		return isOk;
	}

	public static String getFileExtension(String fileName) throws Exception {
		if (NullUtil.isNone(fileName)) {
			return null;
		}

		int idx = fileName.lastIndexOf(".");
		return fileName.substring(idx + 1);

	}
	
	public static Map<String, String> getFileNameWithoutExt(String fileName) throws Exception {
		if (NullUtil.isNone(fileName)) {
			return null;
		}

		int idx = fileName.lastIndexOf(".");
		Map<String, String> result = new HashMap<String, String>();
		result.put("filename", fileName.substring(0, idx));
		result.put("ext", fileName.substring(idx + 1));

		return result;
	}

	/**
	 * Copy File
	 * <pre>
	 *
	 * </pre>
	 * @param src String
	 * @param dest String
	 * @throws Exception
	 */
	public static boolean copyFile(String src, String dest) throws Exception {

		if (!makeDir(getDirectory(dest))) {
			return false;
		}

		InputStream in = new FileInputStream(src);
		OutputStream out = new FileOutputStream(dest);

		return copyFile(in, out);
	}


	/**
	 * Copy File
	 * <pre>
	 *
	 * </pre>
	 * @param in InputStream
	 * @param dest String
	 * @throws Exception
	 */
	public static boolean copyFile(InputStream in, String dest) throws Exception {

		if (!makeDir(getDirectory(dest))) {
			return false;
		}

		OutputStream out = new FileOutputStream(dest);

		return copyFile(in, out);

	}

	/**
	 * File Copy
	 * <pre>
	 *
	 * </pre>
	 * @param in InputStream
	 * @param out OutputStream
	 * @throws Exception
	 */
	public static boolean copyFile(InputStream in, OutputStream out) throws Exception {

		IOUtils.copy(in,out);
		in.close();
		out.close();

		return true;
	}

	/**
	 * Transfer file and delete source file
	 * <pre>
	 *
	 * </pre>
	 * @param src String
	 * @param dest String
	 * @throws Exception
	 */
	public static boolean transferTo(String src, String dest) throws Exception {
		if (!makeDir(getDirectory(dest))) {
			return false;
		}

		InputStream in = new FileInputStream(src);
		OutputStream out = new FileOutputStream(dest);


		return transferTo(src, in, out);
	}

	/**
	 * Transfer file and delete source
	 * <pre>
	 *
	 * </pre>
	 * @param src String
	 * @param in InputStream
	 * @param dest String
	 * @throws Exception
	 */
	public static boolean transferTo(String src, InputStream in, String dest) throws Exception {

		if (!makeDir(getDirectory(dest))) {
			return false;
		}

		OutputStream out = new FileOutputStream(dest);

		return transferTo(src, in, out);
	}

	/**
	 * Transfer file and delete source file
	 * <pre>
	 *
	 * </pre>
	 * @param src String
	 * @param in InputStream
	 * @param out OutputStream
	 * @throws Exception
	 */
	public static boolean transferTo(String src, InputStream in, OutputStream out) throws Exception {

		IOUtils.copy(in,out);
		File file = new File(src);
		if (file.exists()) {
			file.delete();
		}
		in.close();
		out.close();
		return true;
	}


	/**
	 * Get File contents
	 * <pre>
	 *
	 * </pre>
	 * @param src String
	 * @return byte[]
	 * @throws Exception
	 */
	public static byte[] getContents(String src) throws Exception {
		InputStream in = new FileInputStream(src);

		return getContents(in);
	}

	/**
	 * Get File Contents
	 * <pre>
	 *
	 * </pre>
	 * @param in InputStream
	 * @return byte[]
	 * @throws Exception
	 */
	public static byte[] getContents(InputStream in) throws Exception {
		byte[] result =  IOUtils.toByteArray(in);
		in.close();

		return result;
	}

	/**
	 * Get file contents
	 * <pre>
	 *
	 * </pre>
	 * @param mFile
	 * @return
	 * @throws Exception
	 */

	public static byte[] getContents(MultipartFile mFile) throws Exception {
		return mFile.getBytes();
	}

	/**
	 * Get Multipart file information
	 * <pre>
	 *
	 * </pre>
	 * @param mFile MultipartFile
	 * @return Map<String, Object>
	 * @throws Exception
	 */
	public static Map<String, Object> getMultiPartInfo(MultipartFile mFile) throws Exception {
		Map<String, Object> fileInfo = new HashMap<String, Object>();

		fileInfo.put("fileName", mFile.getName());
		fileInfo.put("orgFileName", mFile.getOriginalFilename());
		fileInfo.put("fileSize", mFile.getSize());
//		fileInfo.put("stream", mFile.getInputStream());

		return fileInfo;
	}

	/**
	 * Get multipart file information
	 * <pre>
	 *
	 * </pre>
	 * @param mFiles MultipartFile[]
	 * @return List<Map<String, Object>>
	 * @throws Exception
	 */
	public static List<Map<String, Object>> getMultiPartInfo(MultipartFile[] mFiles) throws Exception {
		List<Map<String, Object>> fileInfos = new ArrayList<Map<String, Object>>();

		for (MultipartFile mFile : mFiles) {
			fileInfos.add(getMultiPartInfo(mFile));
		}
		return fileInfos;
	}

	/**
	 * Get Directory from file full path
	 * <pre>
	 *
	 * </pre>
	 * @param src String
	 * @return String
	 * @throws Exception
	 */
	public static String getDirectory(String src) throws Exception {
		String dir = null;

		int idx = src.lastIndexOf("/");
		dir = src.substring(0, idx - 1);

		return dir;
	}



    /**
     *  Get Multipart information
     * <pre>
     *
     * </pre>
     * @param request MultipartHttpServletRequest
     * @param uploadDir String
     * @return List<FileUploadVo>
     * @throws Exception
     */
	/**
	 * Create of append text to file.
	 * @author Yunho Jeong
	 * @since 2017.07.13
	 * @param strFileNameWithPath - File name with full path
	 * @param sbBody - Text string to write
	 * @param bAppend - Flag if create or append
	 * @return true if written successfully
	 */
	public static boolean write(String strFileNameWithPath, StringBuffer sbBody, boolean bAppend) {
		try (
			OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(strFileNameWithPath, bAppend), StandardCharsets.UTF_8);
			BufferedWriter bw = new BufferedWriter(writer);
			PrintWriter out = new PrintWriter(bw)) {
			out.print(sbBody);
		} catch (IOException e1) {
			e1.printStackTrace();
			return false;
		}

		return true;
	}

	/**
	 * Create of append text to file.
	 * @author Yunho Jeong
	 * @since 2017.07.13
	 * @param strFileNameWithPath - File name with full path
	 * @param hmSrc - HashMap to write
	 * @param bAppend - Flag if create or append
	 * @return true if written successfully
	 */
	public static boolean write(String strFileNameWithPath, LinkedHashMap<String, Object> hmSrc, boolean bAppend) {
//		System.out.println(javax.servlet.ServletContext.getRealPath("/"));
		return write(strFileNameWithPath, convHashMapToSB(hmSrc), bAppend);
	}

	/**
	 * Convert HashMap to StringBuffer. Value of HashMap encoded to unicode.
	 * @author Yunho Jeong
	 * @since 2017.07.13
	 * @param hmSrc - HashMap instance to convert
	 * @return Converted string as a StringBuffer.
	 */
	public static StringBuffer convHashMapToSB(LinkedHashMap<String, Object> hmSrc) {
		String strNewLine = System.lineSeparator();
		StringBuffer sb = new StringBuffer();

		for (String strKey : hmSrc.keySet()) {
			// bilingual properties should be created as a unicode.
			sb.append(strKey + "=" + StringEscapeUtils.escapeJava((String)hmSrc.get(strKey)) + strNewLine);
		}

		return sb;
	}

	/**
	 * Move file from strSrcFile to strTgtFile. StrSrcFile and strTgtFile should have full path of file system.
	 * @author Yunho Jeong
	 * @since 2017.07.13
	 * @param strSrcFile
	 * @param strTgtFile
	 * @return true if successful
	 */
	public static boolean moveFile(String strSrcFile, String strTgtFile) {
		File fSrcFile = new File(strSrcFile);
		File fTgtFile = new File(strTgtFile);

		try {
			FileUtils.deleteQuietly(fTgtFile);
			FileUtils.moveFile(fSrcFile, fTgtFile);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}


	/**
	 * Create Zip Contents
	 *<pre>
	 *
	 *</pre>
	 * @param directory File
	 * @param files String
	 * @return
	 * @throws Exception
	 */
	public static byte[] getZipConentes(String[] files) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ZipOutputStream zos = new ZipOutputStream(baos);

        byte bytes[] = new byte[2048];

        for (String fileName : files) {
            FileInputStream fis = new FileInputStream(fileName);
            BufferedInputStream bis = new BufferedInputStream(fis);

            zos.putNextEntry(new ZipEntry(fileName));

            int bytesRead;
            while ((bytesRead = bis.read(bytes)) != -1) {
                zos.write(bytes, 0, bytesRead);
            }
            zos.closeEntry();
            bis.close();
            fis.close();
        }
        zos.flush();
        baos.flush();
        zos.close();
        baos.close();

        return baos.toByteArray();
	}

	/**
	 * Create ZIP file
	 *<pre>
	 *
	 *</pre>
	 * @param srcDir File
	 * @param files String[]
	 * @param targetFile String
	 * @throws Exception
	 */
	public void creteZipFile(File srcDir, String[] files, String targetFile) throws Exception {

        FileOutputStream fos = new FileOutputStream(new File(targetFile));
        ZipOutputStream zos = new ZipOutputStream(fos);

        byte bytes[] = new byte[2048];
        FileInputStream fis = null;
        BufferedInputStream bis = null;
        try {
            for (String fileName : files) {
                fis = new FileInputStream(srcDir.getPath() + File.separator + fileName);
                bis = new BufferedInputStream(fis);

                zos.putNextEntry(new ZipEntry(fileName));

                int bytesRead;
                while ((bytesRead = bis.read(bytes)) != -1) {
                    zos.write(bytes, 0, bytesRead);
                }
            }        	
        }
        catch(Exception ex) {
        	
        }
        finally {
            zos.closeEntry();
            bis.close();
            fis.close();
        	
        }
	}  
}