/**
 * @Project :  스마트톨링정보시스템 구축
 * @Class : LogViewerCtr.java
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
 * 2018. 5. 30.        LGCNS             최초작성
 *-------------------------------------------------------------
 */

package batch.web.controller.system;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.databind.ObjectMapper;

import batch.web.util.BeanUtil;
import batch.web.util.NullUtil;
import batch.web.util.PropertiesUtil;
import batch.web.util.ReverseReader;

@RestController
public class LogViewerCtr {

	private static final Logger LOGGER = LoggerFactory.getLogger(LogViewerCtr.class);

	/** ObjectMapper **/
	private static ObjectMapper objectMapper = new ObjectMapper();

	@RequestMapping(value = "/log/viewer.do", produces = MediaType.APPLICATION_JSON_VALUE)
	public ModelAndView logViewer(String logType, String logSize, String callback) throws Exception {

		Map<String, Object> resultMap = new HashMap<String, Object>();
		/** Cross domain **/
		// response.addHeader("Access-Control-Allow-Origin", "*");
		// response.setHeader("Access-Control-Allow-Headers", "origin,
		// x-requested-with, content-type, accept");
		// String logType = params.get("logType") != null ?
		// String.valueOf(params.get("logType")) : "";

		/** Log file location **/
		String logLocation = null;

		if (NullUtil.isNull(logType)) {
			logLocation = "log.app.location";
		} else {
			logLocation = "log." + logType + ".location";
		}

		String logFile = PropertiesUtil.getString(logLocation);
		int logLength = 0;
		if (!NullUtil.isNull(logSize)) {
			logLength = Integer.parseInt(logSize);
		} else {
			logLength = PropertiesUtil.getInt("log.read.line");
		}

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Read log file :: {}", logFile);
		}

		ModelAndView mav = new ModelAndView("ajaxMultiDataView");
		String result = null;
		/** Check log file exists **/
		File file = new File(logFile);
		if (!file.exists()) {
			resultMap.put("log", "Log file not found :: " + logFile);
			StringBuilder sb = new StringBuilder();
			result = sb.append(callback).append("(").append(map2Json(resultMap)).append(")").toString();
			return mav.addObject(result);
		}

		if (!file.canRead()) {
			resultMap.put("log", "Log file can't read :: " + logFile);
			StringBuilder sb = new StringBuilder();
			result = sb.append(callback).append("(").append(map2Json(resultMap)).append(")").toString();
			return mav.addObject(result);

		}

		BufferedReader in = null;
		StringBuilder logContent = new StringBuilder();
		int idx = 0;

		try {
			in = new BufferedReader(new InputStreamReader(new ReverseReader(file)));

			boolean isLoop = true;
			while (isLoop) {
				String contents = in.readLine();
				logContent.append(contents).append("<BR>");
				if (idx++ >= logLength) {
					isLoop = false;
				}
			}
		} catch (Exception logex) {
			logex.printStackTrace();
		} finally {
			if (in != null) {
				in.close();
			}
		}

		if (logContent.length() <= 0) {
			resultMap.put("log", "No Contents");
			StringBuilder sb = new StringBuilder();
			result = sb.append(callback).append("(").append(map2Json(resultMap)).append(")").toString();
			return mav.addObject(result);

		} else {
			resultMap.put("log", logContent.toString());
			StringBuilder sb = new StringBuilder();
			result = sb.append(callback).append("(").append(map2Json(resultMap)).append(")").toString();

		}
		mav.addObject(result);
		return mav;
	}

	@RequestMapping(value = "/log/systemInfo.do")
	public @ResponseBody Map<String, Object> getSystemInfo(HttpServletRequest request) throws Exception {

		Map<String, Object> resultMap = new HashMap<String, Object>();
		@SuppressWarnings("unchecked")
		Map<String, Object> map = (Map<String, Object>) BeanUtil.getBean("systemInfo");

		Iterator<String> itr = map.keySet().iterator();
		while (itr.hasNext()) {
			String key = itr.next();
			String value = (String) map.get(key);

			resultMap.put(key, value);
		}

		return resultMap;
	}

	private <K, V> String map2Json(Map<K, V> dataMap) throws Exception {
		String jsonData = null;

		try {
			jsonData = objectMapper.writeValueAsString(dataMap);
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			throw new Exception(e.getMessage(), e);
		}

		return jsonData;
	}

}
