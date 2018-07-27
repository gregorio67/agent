/**
 * @Project :  스마트톨링정보시스템 구축
 * @Class : FileList.java
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
 * 2018. 5. 16.        LGCNS             최초작성
 *-------------------------------------------------------------
 */

package dymn.sql.parser;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Iterator;

import dymn.sql.util.Utils;

public class FileList {
	
	public static void listFile(String dir) throws Exception {
		/** Get All SQL Files **/
		Collection<File> files = Utils.getFiles(dir);
		Iterator<File> itrFile = files.iterator();
		Path currentRelativePath = Paths.get("");
		String curDir = currentRelativePath.toAbsolutePath().toString();
		
		BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(curDir + File.separator + "sql_file.txt"));
		try {
			while(itrFile.hasNext()) {
				File f = itrFile.next();
				String path = f.getAbsolutePath();
				System.out.println(path);
				bos.write(path.getBytes());
				bos.write("\n".getBytes());
			}
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		finally {
			bos.close();
		}
	}

}
