package batch.agent.util;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;


public class PropertiesUtil extends PropertyPlaceholderConfigurer {
	/**LOGGER SET **/
	private static final Logger LOGGER = LoggerFactory.getLogger(PropertiesUtil.class);

	private static Map<String, String> propertiesMap;
    // Default as in PropertyPlaceholderConfigurer
    private int springSystemPropertiesMode = SYSTEM_PROPERTIES_MODE_FALLBACK;

    @Override
    public void setSystemPropertiesMode(int systemPropertiesMode) {
        super.setSystemPropertiesMode(systemPropertiesMode);
        springSystemPropertiesMode = systemPropertiesMode;
    }


    @Override
    protected void processProperties(ConfigurableListableBeanFactory beanFactory, Properties props) throws BeansException {
        super.processProperties(beanFactory, props);
        propertiesMap = new HashMap<String, String>();
        for (Object key : props.keySet()) {
            String keyStr = String.valueOf(key);
            String valueStr = resolvePlaceholder(keyStr, props, springSystemPropertiesMode);
            propertiesMap.put(keyStr, valueStr);
            if (LOGGER.isDebugEnabled()) {
            	LOGGER.debug("Load Properties :" +  keyStr + ":" + valueStr);
            }
        }
    }

    /**
     * This method return value with the name from properties map
     * @param name propertiy name
     * @return
     */
    public static String getString(String name)  {
        return propertiesMap.get(name) != null ? String.valueOf(propertiesMap.get(name)) : null;
    }

    public static int getInt(String name) {
    	if (propertiesMap.get(name) != null || propertiesMap.get(name) != "") {
            return Integer.parseInt(String.valueOf(propertiesMap.get(name)));
    	}
    	else {
    		return 0;
    	}
    }

    public static long getLong(String name) {
    	if (propertiesMap.get(name) != null || propertiesMap.get(name) != "") {
            return Long.parseLong(String.valueOf(propertiesMap.get(name)));
    	}
    	else {
    		return 0;
    	}
    }
    
    public static String currentDir() throws Exception {
    	Path curPath = Paths.get("");
    	return curPath.toAbsolutePath().toString();
    }
}
