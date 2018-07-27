package cmn.deploy.exception;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.ibatis.exceptions.PersistenceException;
import org.mybatis.spring.MyBatisSystemException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

import cmn.deploy.base.BaseConstants;
import cmn.deploy.util.MessageUtil;
import cmn.deploy.util.NullUtil;

public class JsonExceptionResolver extends SimpleMappingExceptionResolver {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(JsonExceptionResolver.class);
	
	
	private int maxLength;
		
    private final  String exceptionCodeAttribute = "exception";

    
    public void setMaxLength(int maxLength) {
		this.maxLength = maxLength;
	}

	@Override
    protected ModelAndView getModelAndView(String viewName, Exception ex) {
		ModelAndView mv = super.getModelAndView(viewName,ex);
		Map<String, Object> model = mv.getModel();
		
		String code = model.get(this.exceptionCodeAttribute)!=null ?
        		(String)model.get(this.exceptionCodeAttribute):  BaseConstants.DEFAULT_ERROR_CODE;
        		
        String message = ex.getMessage();
        try {
            message = message == null ? MessageUtil.getMessage(code) : message;        	
        }
        catch(Exception exception) {
        	message = "";
        }

        model.put("message", message);
        model.put("error", "true");
        model.put("errortype", ex.getClass().getSimpleName());
        model.put("code",code);

		return mv;
    }
 
    @Override
    protected ModelAndView doResolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
    	String msgCode = null;
    	String msgValue = null;
    	
    	ex.printStackTrace();
 
    	if (ex instanceof BizException) {
    		BaseException be = (BaseException)ex; 
    		msgCode = be.getCode();
    		msgValue = be.getMessage();
    		if (msgValue == null) {
    	        try {
    	         	msgValue = MessageUtil.getMessage(msgCode);
    	         	msgValue = msgCode + "<BR>" + msgValue; 
    	        }
    	        catch(Exception exception) {
    	          	msgValue = "";
    	        }    			
    		}
    	}
    	/** Mybatis Or Database Exception **/
    	else if (ex instanceof MyBatisSystemException || ex instanceof PersistenceException || ex instanceof DataAccessException ) {
    		msgCode = BaseConstants.DEFAULT_DB_ERROR_CODE;
    		
    		if (ex.getCause() instanceof java.sql.SQLException) {
    	    	/*-------------------------------------------------------------
    	    	 * 오라클의 경우 모든 오류를 SQLException으로 날립니다.
    	    	 * 이에 해당 오류메세지를 구분하여 메세지를 설정해야합니다.
    	    	 * 민자영업소의 요청에 따라 Oracle Exception ID 정보를 출력합니다.
    	    	 *-------------------------------------------------------------*/
    	    	msgCode = BaseConstants.DEFAULT_DB_ERROR_CODE;
    	    	String oraErrMsg = ex.getCause().getMessage();
    	    	
    	    	// 오라클 오류메세지 여부를 판단합니다.
    	    	if (oraErrMsg != null && oraErrMsg.length() > 0) {
    	    		String oraCode = null;
    	    		Pattern pattern = Pattern.compile("ORA-\\d+");
    	    		
    	    		Matcher matcher = pattern.matcher(oraErrMsg);
    	    		
    	    		if (matcher.find()) {
    	    			oraCode = matcher.group();
    	    		}
    	    		try {
    	    			msgValue = MessageUtil.getMessage( msgCode, new Object[] {oraCode});
    	    			msgValue = msgCode + "<BR>" + msgValue;
    	    		}
    	    		catch(Exception exception) {
    	    			msgValue = "";
    	    		}
    	    	}
    		}
    		else {
	    		try {
	    			msgValue = MessageUtil.getMessage( msgCode);
	    			msgValue = msgCode + "<BR>" + msgValue;
	    		}
	    		catch(Exception exception) {
	    			msgValue = "";
	    		}    	    		
	    	}
    	    	
    	}
    	else {
    		/** Set Default Message Code **/
    		msgCode =  BaseConstants.DEFAULT_ERROR_CODE;
            try {
            	msgValue = MessageUtil.getMessage( msgCode);
            	msgValue = msgCode + "<BR>" + msgValue; 
            }
            catch(Exception exception) {
            	msgValue = "";
            }  		
    	}
    	

    	/** Set error code and message for client **/
        Map<String, Object> retMsg = new HashMap<String, Object>();
        
      	retMsg.put("message", msgValue);
      	msgCode = !NullUtil.isNone(msgCode) ? msgCode : BaseConstants.DEFAULT_ERROR_CODE;
       	retMsg.put("status", BaseConstants.STATUS_ERROR);	

       	if (LOGGER.isDebugEnabled()) {
           	LOGGER.debug("Error Message to clinet :: {}", retMsg.toString());       		
       	}
       	
       ModelAndView mav = new ModelAndView();
       mav.setViewName("jsonView");

       mav.addObject("message", retMsg);
       return mav;
    }
}
