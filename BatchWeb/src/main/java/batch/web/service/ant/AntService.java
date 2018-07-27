/**
 * @Project :  스마트톨링정보시스템 구축
 * @Class : AntService.java
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
 * 2018. 6. 25.        LGCNS             최초작성
 *-------------------------------------------------------------
 */

package batch.web.service.ant;

import java.io.File;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.ProjectHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import batch.web.exception.BizException;
import batch.web.util.FileUtil;

@Service("antService")
public class AntService implements AntIService{

	private static final Logger LOGGER = LoggerFactory.getLogger(AntService.class);
	
	private static final String DEFAULT_BUILD_FILE = "classpath:build.xml";
	@Override
	public boolean buildSource(String sourceDir, String targetDir, String libPath) throws Exception {
		boolean isSuccess = false;
		
		if (sourceDir == null || targetDir == null) {
			throw new BizException("Source dir and target dir should be needed");
		}
		
		FileUtil.makeDir(targetDir);
		
//		String buildxml = PropertiesUtil.getString("ant.build.xml.file");
//		File buildFile = null;
//		if (buildxml == null) {
//			buildFile = ResourceUtils.getFile(DEFAULT_BUILD_FILE);
//		}
//		else {
//			buildFile = new File(buildxml);
//		}
		System.setProperty("JAVA_HOME", "C:/STIS/bin/jdk1.7.0_80");
		File buildFile = null;
		buildFile = ResourceUtils.getFile(DEFAULT_BUILD_FILE);
		
		Project project = new Project();
		project.setUserProperty("ant.file", buildFile.getAbsolutePath());
		project.setUserProperty("lib.dir", libPath);
		project.setUserProperty("src.dir", sourceDir);
		project.setUserProperty("build.dir", targetDir);
		project.setUserProperty("java.home", "C:/STIS/bin/jdk1.7.0_80");
		
		project.init();
		
		ProjectHelper helper = ProjectHelper.getProjectHelper();
		project.addReference("ant.projectHelper", helper);
		try {
			helper.parse(project, buildFile);
			project.executeTarget(project.getDefaultTarget());			
		}
		catch(Exception ex) {
			LOGGER.error("Ant Build Error :: {}", ex.getMessage());
			isSuccess = false;
			return isSuccess;
		}
		
		LOGGER.info("Ant build is successfully eneded");
		return isSuccess;
		
	}
	
	public static void main(String args[]) throws Exception {
		AntService antService = new AntService();
		antService.buildSource("C:/STIS/workspace/SMS/src", "C:/STIS/workspace/SMS/build", "C:/STIS/bin/aws");
	}
}
