package exp.libs.utils.other;

import java.io.FileReader;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import exp.libs.utils.io.IOUtils;

/**
 * javascript工具包
 * 
 * <B>PROJECT：</B> exp-libs
 * <B>SUPPORT：</B> EXP
 * @version   1.0 2015-12-27
 * @author    EXP: 272629724@qq.com
 * @since     jdk版本：jdk1.6
 */
public class JSUtils {

	/** 日志器 */
	private final static Logger log = LoggerFactory.getLogger(JSUtils.class);
	
	/** 私有化构造函数. */
	protected JSUtils() {}
	
	/**
	 * 执行JS脚本中的方法
	 * 
	 * @param jsFilePath JS文件路径
	 * @param jsMethod JS方法
	 * @param args 方法参数
	 * @return Object
	 */
	public static Object executeJS(String jsFilePath, String jsMethod, Object... args) {
		ScriptEngineManager manager = new ScriptEngineManager();
		ScriptEngine engine = manager.getEngineByName("javascript");
		
		Object result = null;
		FileReader reader = null;
		try {
			reader = new FileReader(jsFilePath);
			engine.eval(reader);
			Invocable inv = (Invocable) engine;
			result = inv.invokeFunction(jsMethod, args);
			
		} catch (Exception e) {
			log.error("执行JS方法 [{}] 失败, 所属文件: {}", jsMethod, jsFilePath, e);
		}
		IOUtils.close(reader);
		return result;
	}

}
