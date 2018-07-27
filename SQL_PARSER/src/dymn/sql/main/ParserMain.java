/**
 * @Project :  스마트톨링정보시스템 구축
 * @Class : SQLParser.java
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
 * 2018. 4. 24.        LGCNS             최초작성
 *-------------------------------------------------------------
 */

package dymn.sql.main;

import dymn.sql.parser.FileList;
import dymn.sql.parser.IbatisParser;
import dymn.sql.parser.MybatisParser;

public class ParserMain {

	public static void main(String[] args) throws Exception {

		if (args.length < 4) {
			printUsage();
			System.exit(1);
		}

		String command = args[0];
		String type = args[1];
		String conf = args[2];
		String dir = args[3];

		/** SQL File List **/
		if ("file".equalsIgnoreCase(command)) {
			FileList.listFile(dir);	
		} 
		/** SQL Parsing **/
		else if ("parser".equalsIgnoreCase(command)) {
			/** MyBatis SQL Parsing **/
			if ("mybatis".equals(type)) {
				MybatisParser.myBatisParser(conf, dir);
			} 
			/** IBatis SQL Parsing **/
			else if ("ibatis".equals(type)) {
				IbatisParser.iBatisParser(conf, dir);
			} 
			else {
				System.out.println("Type should be [ibatis | mybatis]");
			}

		} 
		/** Print usage **/
		else if ("usage".equalsIgnoreCase(command)) {
			printUsage();
		} else {
			System.out.println("command should be [file : parser]");
		}
	}

	/**
	 * 
	 * <pre>
	 * 1.Description: print usage 
	 * 2.Biz Logic:
	 * 3.Author : LGCNS
	 * </pre>
	 * 
	 * @throws Exception
	 */
	public static void printUsage() throws Exception {
		System.out.printf("Usage java -jar sqlParser.jar [file | parser] [mybatis | ibatis]  configPath sqlPath");
	}
}
