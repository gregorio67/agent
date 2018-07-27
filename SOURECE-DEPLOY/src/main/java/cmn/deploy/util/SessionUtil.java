package cmn.deploy.util;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import cmn.deploy.base.BaseConstants;
import cmn.deploy.exception.BizException;


public abstract class SessionUtil {

	/**LOGGER SET **/
	private static final Logger LOGGER = LoggerFactory.getLogger(SessionUtil.class);
	
	private static Object syncObject = new Object();
	
	/**
     * 세션정보에서 attribute set
     * @param String attribute key name
     * @return void
     */
    public static void setSession(String name, Object object) throws Exception, Exception{
    	if (NullUtil.isNull(name) || NullUtil.isNull(object)) {
    		LOGGER.error("Session name and value is null");
    		throw new BizException("Session name and value is null");
    	}
   
    	RequestContextHolder.getRequestAttributes().setAttribute(name, object, RequestAttributes.SCOPE_SESSION);
    }    
    
    /**
     * 세션정보에서 attribute get
     * @param Stringattribute key name
     * @return Object attribute Obj
     */
	@SuppressWarnings("unchecked")
	public static <T> T getSession(String name) throws Exception, Exception{
    	if (NullUtil.isNull(name)) {
    		LOGGER.error("Session name is null");
    		throw new BizException("Session name is null");
    	}
    	return (T) RequestContextHolder.getRequestAttributes().getAttribute(name, RequestAttributes.SCOPE_SESSION);
    }
    
    /**
     * 세션정보에서 attribute delete
     * @param Stringattribute key name
     * @return void
     */
    public static void removeSession(String name) throws Exception, Exception{
    	if (NullUtil.isNull(name)) {
    		LOGGER.error("Session name is null");
    		throw new BizException("Session name is null");
    	}
    	RequestContextHolder.getRequestAttributes().removeAttribute(name, RequestAttributes.SCOPE_SESSION);
    }
    
    /**
     * 세션삭제
     * @param request HttpServletRequest
     * @return void
     */
    public static void invalidSession() throws Exception, Exception{
    	ServletRequestAttributes attributes = (ServletRequestAttributes)RequestContextHolder.currentRequestAttributes();
    	attributes.getRequest().getSession().invalidate();
    }
    
    /**
     * 
     *<pre>
     * 1.Description: Return User Id
     * 2.Biz Logic:
     * 3.Author : LGCNS
     *</pre>
     * @return
     * @throws Exception
     */
    public static String getUserId() throws Exception {
		/** Get User id from session **/
		Map<String, Object> session = SessionUtil.getSession(BaseConstants.DEFAULT_SESSION_NAME);
		if (session == null) {
			throw new BizException("Session is null");
		}
		
		String userId = session.get("userId") != null ? String.valueOf(session.get("userId")) : "";

		return userId;
    }
    
    /**
     * 
     *<pre>
     * 1.Description: Return userRole
     * 2.Biz Logic:
     * 3.Author : LGCNS
     *</pre>
     * @return
     * @throws Exception
     */
    public static int getUserRole() throws Exception {
		/** Get User id from session **/
		Map<String, Object> session = SessionUtil.getSession(BaseConstants.DEFAULT_SESSION_NAME);
		if (session == null) {
			throw new BizException("Session is null");
		}
		
		int userRole = session.get("userRole") != null ? Integer.parseInt(String.valueOf(session.get("userRole"))) : 0;

		return userRole;
    }    
    
    public abstract String getCurrentLocale() throws Exception;
}
