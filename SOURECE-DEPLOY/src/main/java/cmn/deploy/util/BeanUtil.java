package cmn.deploy.util;

import java.io.FileInputStream;
import java.io.InputStream;

import org.springframework.util.ResourceUtils;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

public class BeanUtil{

	/**
	 * 
	 *<pre>
	 * Spring Bean return with bean name
	 *</pre>
	 * @param beanName String 
	 * @return
	 * @throws Exception
	 */
	public static <T> T getBean(String beanName) throws Exception {
		WebApplicationContext context = ContextLoader.getCurrentWebApplicationContext();

		@SuppressWarnings("unchecked")
		T bean = (T) context.getBean(beanName);

		return bean;
	}
	
	/**
	 * 
	 *<pre>
	 * 1.Description: Return WebApplicationContext
	 * 2.Biz Logic:
	 * 3.Author : LGCNS
	 *</pre>
	 * @return WebApplicationContext
	 * @throws Exception
	 */
	
	public static WebApplicationContext getContext() throws Exception {
		return ContextLoader.getCurrentWebApplicationContext();
	}
	
	/**
	 * 
	 *<pre>
	 * 1.Description: Return Current Active profiles
	 * 2.Biz Logic:
	 * 3.Author : LGCNS
	 *</pre>
	 * @return String[]
	 * @throws Exception
	 */
	public static String[] getActiveProfile() throws Exception {
		return getContext().getEnvironment().getActiveProfiles();
	}
	
	/**
	 * 
	 *<pre>
	 * 1.Description: Return FileInputStream form resource name
	 * 2.Biz Logic:
	 * 3.Author : LGCNS
	 *</pre>
	 * @param resourceName resource name 
	 * @return FileFinputStream from resource name
	 * @throws Exception
	 */
	public static InputStream getResource(String resourceName) throws Exception {
		if (resourceName == null) {
			return null;
		}
		
		if (!resourceName.startsWith(ResourceUtils.CLASSPATH_URL_PREFIX)) {
			resourceName = ResourceUtils.CLASSPATH_URL_PREFIX + resourceName;
		}
		return new FileInputStream(ResourceUtils.getFile(resourceName));
	}
}
