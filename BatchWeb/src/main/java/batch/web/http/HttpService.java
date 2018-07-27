/**
 * @Project :  스마트톨링정보시스템 구축
 * @Class : HttpService.java
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
 * 2018. 6. 22.        LGCNS             최초작성
 *-------------------------------------------------------------
 */

package batch.web.http;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import batch.web.exception.BizException;

public class HttpService {
	private static final Logger LOGGER = LoggerFactory.getLogger(HttpService.class);
	
	private CloseableHttpClient httpClient;
	
	public void setHttpClient(CloseableHttpClient httpClient) {
		this.httpClient = httpClient;
	}

	public <T> T callPost(String URL, Map<String, String> param, final Class<?> responseType) throws Exception {
		
		List<NameValuePair> sendParam = new ArrayList<NameValuePair>();
		if (param != null) {
			Iterator<String> paramItr = param.keySet().iterator();
			while(paramItr.hasNext()) {
				String key = paramItr.next();
				sendParam.add(new BasicNameValuePair(key, param.get(key)));
			}			
		}
		
		UrlEncodedFormEntity entity = new UrlEncodedFormEntity(sendParam, Consts.UTF_8);
		HttpPost httpPost = new HttpPost(URL);
		
		httpPost.setEntity(entity);
				
		ResponseHandler<HttpEntity> responseHandler = new ResponseHandler<HttpEntity>() {

			@Override
			public HttpEntity handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
				int status = response.getStatusLine().getStatusCode();
                if (status >= 200 && status < 300) {
                    HttpEntity responseEntity = response.getEntity();
                    return responseEntity;
                } else {
                    throw new ClientProtocolException("Unexpected response status: " + status);
                }
			}
		};
		
		HttpEntity responseEntity = null;
		CloseableHttpResponse httpResponse = null;

		try {
			httpResponse = httpClient.execute(httpPost);
			responseEntity = responseHandler.handleResponse(httpResponse);
		}
		catch(Exception ex) {
			LOGGER.error("Http Post error :: {}", ex.getMessage());
			throw new BizException(ex.getMessage());
		}
		finally {
		}

		@SuppressWarnings("unchecked")
		T responseBody = (T) (responseEntity != null ? EntityUtils.toString(responseEntity) : null);;
		
		return responseBody;
	}

	public <T> T callPost(String URL, String param, String contentType, Class<?> responseType) throws Exception {

		HttpPost httpPost = new HttpPost(URL);
		httpPost.setHeader("Content-Type", contentType);
		StringEntity entity = null;
		if (param != null) {
			entity = new StringEntity(param);
		}
		if (entity != null) {
		    httpPost.setEntity(entity);			
		}
		
		
		ResponseHandler<HttpEntity> responseHandler = new ResponseHandler<HttpEntity>() {
			@Override
			public HttpEntity handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
				int status = response.getStatusLine().getStatusCode();
                if (status >= 200 && status < 300) {
                    HttpEntity responseEntity = response.getEntity();
                    return responseEntity;
                } else {
                    throw new ClientProtocolException("Unexpected response status: " + status);
                }
			}
		};
		
		HttpEntity responseEntity = null;
		CloseableHttpResponse httpResponse = null;
		try {
			httpResponse = httpClient.execute(httpPost);
			responseEntity = responseHandler.handleResponse(httpResponse);
		}
		catch(Exception ex) {
			LOGGER.error("Http Post error :: {}", ex.getMessage());
			throw new BizException(ex.getMessage());
		}
		finally {
			httpResponse.close();
		}

		@SuppressWarnings("unchecked")
		T responseBody = (T) (responseEntity != null ? EntityUtils.toString(responseEntity) : null);;
		
		return responseBody;		
	}	
	public <T> T callMultiPartPost(String URL, Map<String, String> param, String filePath, final Class<?> responseType) throws Exception {
				
		MultipartEntityBuilder builder = MultipartEntityBuilder.create();
		Iterator<String> paramItr = param.keySet().iterator();
		while(paramItr.hasNext()) {
			String key = paramItr.next();
			builder.addTextBody(key, param.get(key));
		}
		builder.addBinaryBody("file", new File(filePath), ContentType.APPLICATION_OCTET_STREAM, filePath);
		HttpPost httpPost = new HttpPost(URL);
		HttpEntity multipart = builder.build();
	    httpPost.setEntity(multipart);
		
		
		ResponseHandler<HttpEntity> responseHandler = new ResponseHandler<HttpEntity>() {
			@Override
			public HttpEntity handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
				int status = response.getStatusLine().getStatusCode();
                if (status >= 200 && status < 300) {
                    HttpEntity responseEntity = response.getEntity();
                    return responseEntity;
                } else {
                    throw new ClientProtocolException("Unexpected response status: " + status);
                }
			}
		};
		
		HttpEntity responseEntity = null;
		try {
			responseEntity = httpClient.execute(httpPost, responseHandler);
		}
		catch(Exception ex) {
			LOGGER.error("Http Post error :: {}", ex.getMessage());
			throw new BizException(ex.getMessage());
		}
		finally {
		}

		@SuppressWarnings("unchecked")
		T responseBody = (T) (responseEntity != null ? EntityUtils.toString(responseEntity) : null);;
		
		return responseBody;
	}


	
}
