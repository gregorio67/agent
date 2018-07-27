package cmn.deploy.base;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import cmn.deploy.util.PropertiesUtil;

public class BaseController {
	
	protected final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
	
	/** Message Source **/
    @Resource(name ="messageSource")
    protected MessageSource messageSource;
    
    @Resource(name = "passwordEncoder")
    protected BCryptPasswordEncoder passwordEncoder;
    
    @Resource(name = "localeResolver")
    protected SessionLocaleResolver localeResolver;
    
    
    
    /**
     * 
     *<pre>
     * Return Request Header
     *</pre>
     * @param param Map<String, Object>
     * @return T
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
	protected <T> T getRequestHeader(Map<String, Object> param) throws Exception {
       	return (T)param.get(BaseConstants.REQUEST_PARAM_HEADER);    		
    }
    
    /**
     * 
     *<pre>
     * Return Json Request Data
     *</pre>
     * @param param Map<String, Object>
     * @return T
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
	protected <T> T getRequestData(Map<String, Object> param) throws Exception {
       	return (T)param.get(BaseConstants.REQUEST_PARAM_BODY);    		
    }
    
    
    protected Map<String, Object> getPageInfo(int curPage, int totalCount) throws Exception {
    	
    	Map<String, Object> pageMap = new HashMap<String, Object>();
    	
		pageMap.put("currentPage", curPage);			
		pageMap.put("pageRowCount", PropertiesUtil.getInt("screen.page.rowcount"));
		pageMap.put("pageGroupCount", PropertiesUtil.getInt("screen.page.groupcount"));
		pageMap.put("totalRowCount", totalCount);
		
		return pageMap;
    }
    
    protected String encPassword(String password) throws Exception {
    	if (password == null) {
    		return null;
    	}
    	
    	return passwordEncoder.encode(password);
    }
    
}
