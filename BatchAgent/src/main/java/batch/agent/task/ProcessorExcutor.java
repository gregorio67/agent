/**
 * @Project :  스마트톨링정보시스템 구축
 * @Class : ProcessorExcutor.java
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
 * 2018. 7. 9.        LGCNS             최초작성
 *-------------------------------------------------------------
 */

package batch.agent.task;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ProcessorExcutor {
	public static <T> Future<T> runProcess(String command, String shellName, List<String> arguments) throws Exception {
		ExecutorService executor = Executors.newSingleThreadExecutor();
		Future<T> result = executor.submit(new TaskProcess<T>(command, shellName, arguments));
		executor.shutdown();
		return result;
	}
}
