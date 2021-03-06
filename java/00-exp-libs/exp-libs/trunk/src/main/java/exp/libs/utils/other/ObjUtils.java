package exp.libs.utils.other;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import exp.libs.envm.DateFormat;
import exp.libs.utils.StrUtils;
import exp.libs.utils.time.TimeUtils;

/**
 * <PRE>
 * 对象工具包.
 * </PRE>
 * <B>PROJECT：</B> exp-libs
 * <B>SUPPORT：</B> EXP
 * @version   1.0 2016-02-02
 * @author    EXP: 272629724@qq.com
 * @since     jdk版本：jdk1.6
 */
public class ObjUtils {

	/** 日志器 */
	private final static Logger log = LoggerFactory.getLogger(ObjUtils.class);
	
	/** 私有化构造函数 */
	protected ObjUtils() {}
	
	public static Object toObj(String s, Class<?> clazz) {
		if(StrUtils.isEmpty(s)) {
			return (String.class == clazz ? "" : null);
		}
		
		Object o = s;
		String str = s.trim();
		if (Integer.class == clazz) {
			o = Integer.valueOf(str);
			
		} else if (Long.class == clazz) {
			o = Long.valueOf(str);
			
		} else if (BigInteger.class == clazz) {
			o = new BigInteger(str);
			
		} else if (Float.class == clazz) {
			o = Float.valueOf(str);
			
		} else if (Double.class == clazz) {
			o = Double.valueOf(str);
			
		} else if (Date.class == clazz) {
			o = TimeUtils.toDate(str);
			
		} else if (Timestamp.class == clazz) {
			o = TimeUtils.toTimestamp(TimeUtils.toDate(str));
		}
		return o;
	}
	
	public static String toStr(Object o, Class<?> clazz) {
		String s = "";
		if(o == null) {
			return s;
		}
		
		if(clazz == null) {
			return toStr(o);
		}
		
		if (isSubclass(clazz, Number.class)) {
			s = String.valueOf(o);
			
		} else if (Date.class == clazz) {
			s = TimeUtils.toStr((Date) o, DateFormat.YMDHMSS);
			
		} else if (Timestamp.class == clazz) {
			Date date = TimeUtils.toDate((Timestamp) o);
			s = TimeUtils.toStr(date, DateFormat.YMDHMSS);
			
		} else {
			s = o.toString();
		}
		return s;
	}
	
	public static String toStr(Object o) {
		String s = "";
		if(o == null) {
			return s;
		}
		
		if (o instanceof Number) {
			s = String.valueOf(o);
			
		} else if (o instanceof Date) {
			s = TimeUtils.toStr((Date) o, DateFormat.YMDHMSS);
			
		} else if (o instanceof Timestamp) {
			Date date = TimeUtils.toDate((Timestamp) o);
			s = TimeUtils.toStr(date, DateFormat.YMDHMSS);
			
		} else {
			s = o.toString();
		}
		return s;
	}
	
	public static boolean isSubclass(Class<?> cClazz, Class<?> fClazz) {
		boolean isChild = false;
		try {
			cClazz.asSubclass(fClazz);
			isChild = true;
			
		} catch (Exception e) {
			// 报错说明不是子类
		}
		return isChild;
	}
	
	/**
	 * <pre>
	 * 通过Serializable序列化方式深度克隆对象，
	 * 要求所克隆的对象及其下所有成员都要实现Serializable接口。
	 * 
	 * 因为java的[基本数据类型]是值传递，可以直接复制，
	 * 而其[包装类]（如String, Integer等）也都已经实现了Serializable接口，
	 * 因此对于一般的待克隆对象，实现Serializable接口后，直接使用即可。
	 * 
	 * 若待克隆对象下存在[引用数据类型]（如自定义的class），则要求它必须实现Serializable接口。
	 * </pre>
	 * @param serialObject 被克隆的对象，必须实现Serializable接口
	 * @return 克隆的对象
	 */
	public static Object clone(Object serialObject) {
		Object newObj = null;
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream out = new ObjectOutputStream(baos);
			out.writeObject(serialObject);
			out.close();
			
			ByteArrayInputStream bin = new ByteArrayInputStream(baos.toByteArray());
			ObjectInputStream in = new ObjectInputStream(bin);
			newObj = in.readObject();
			in.close();
			
		} catch (Exception e) {
			log.error("克隆对象 [{}] 失败.", serialObject, e);
		}
		return newObj;
	}
	
	/**
	 * 实例化对象
	 *
	 * @param _class 类路径
	 * @return 实例化对象
	 * @throws ClassNotFoundException 找不到类
	 * @throws InstantiationException	实例化异常
	 * @throws IllegalAccessException  反射调用异常
	 */
	public static Object instanceClass(String clazz) 
			throws ClassNotFoundException, InstantiationException, 
			IllegalAccessException {
		Class<?> inst = Class.forName(clazz);
		return inst.newInstance();
	}
	
	/**
	 * 获取指定基类的所有子类.
	 * 
	 * 由于java父类不清楚其下的子孙是什么,
	 * 此方式通过递归检索编译目录判断所有类之间的关联性,以确认父子关系.
	 * 
	 * @param baseClass 基类
	 * @return 子类列表
	 */
	public static List<String> getAllChildClass(Class<?> baseClass) {
		String compilePath = PathUtils.getRootCompilePath();	//根编译目录
		File rootDir = new File(compilePath);
		
		//路径分隔符转换为包分隔符
		compilePath = compilePath.replaceAll("[\\\\|/]", ".");
		if(compilePath.endsWith(".") == false) {
			compilePath += ".";
		}
		
		List<String> childClassList = new LinkedList<String>();
		searchChildClass(rootDir, compilePath, baseClass, childClassList);
		return childClassList;
	}
	
	/**
	 * 递归检索所有类，并通过父转子异常测试以获取指定基类的所有子类。
	 * 
	 * @param curFile 当前处理的文件类
	 * @param pathPrefix 路径前缀（包路径格式）
	 * @param baseClass 需查找子类的基类
	 * @param childClassList 存储检索的子类列表（包路径格式）
	 */
	private static void searchChildClass(File curFile, String pathPrefix, 
			Class<?> baseClass, List<String> childClassList) {
		if(childClassList == null || pathPrefix == null) {
			return;
		}
		
		//若是目录,向下递归
		if(curFile.isDirectory()) {
			File[] files = curFile.listFiles();
			for (File file : files) {
				searchChildClass(file, pathPrefix, baseClass, childClassList);
			}
			
		//若是文件,判定处理
		} else {
			if (curFile.getPath().endsWith(".class") == true) {
				String childClassName = null;
				try {
					childClassName = curFile.getPath().
							replaceAll("[\\\\|/]", ".").	//路径分隔符转换为包分隔符
							replace(pathPrefix, "").	//去前缀
							replace(".class", "");		//去后缀
					
					//基类不会是自身的子类
					if(childClassName.equals(baseClass.getName())) {
						return;
					}
					
					//实例化当前类,并尝试将指定基类做转换测试,只要不抛出异常则说明为父子关系
					Class<?> childClass = Class.forName(childClassName);
					childClass.asSubclass(baseClass);
					childClassList.add(childClassName);
					
				} catch (ClassNotFoundException e) {
					//forName 类不存在
					
				} catch (ClassCastException e) {
					//asSubclass 非父子关系都会抛出此异常
				}
			}
		}
	}
	
	public static Object toBean(Map<String, Object> map, 
			Class<? extends Object> clazz) {
		if(map == null || clazz == null) {
			return null;
		}
		
		Field[] fields = clazz.getDeclaredFields();
		Method[] methods = clazz.getMethods();
		Object obj = null;
		try {
			obj = clazz.newInstance();
			
		} catch (Exception e) {
			log.error("构造 [{}] 实例失败.", clazz.getName(), e);
			return null;
		}
		
		for (Method method : methods) {
			String methodName = method.getName();
			if (methodName.startsWith("set")) {
				String propertyName = methodName.substring(3);
				
				String fieldName = null;
				for (int i = 0; i < fields.length; i++) {
					Field field = fields[i];
					if(propertyName.equalsIgnoreCase(field.getName())) {
						fieldName = field.getName();
						break;
					}
				}
				
				// 利用setter方法对对应的成员域置值
				if (fieldName != null) {
					Object value = map.get(fieldName);
					try {
						method.invoke(obj, value);
					} catch (Exception e) {
						log.error("[{}]: 为成员域 [{}] 置值失败.", clazz.getName(), fieldName);
					}
				} else {
					log.warn("[{}]: 不存在属性值 [{}] 对应的成员域.", clazz.getName(), propertyName);
				}
			}
		}
		return obj;
	}
	
	/**
	 * 通过反射调用私有方法.
	 * 	可用于单元测试私有方法.
	 * 
	 * @param instnOrClazz
	 *            如果是调用实例方法，该参数为实例对象，
	 *            如果调用静态方法，该参数为实例对象或对应类***.class
	 * @param methodName 调用的方法名
	 * @param paramVals 调用方法的参数
	 * @param valClazzs 调用方法的参数对应的类型类
	 * @return 调用结果
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Object invokeMethod(Object instnOrClazz, String methodName,
			Object[] paramVals, Class[] valClazzs) {
		if(instnOrClazz == null || StrUtils.isEmpty(methodName)) {
			return null;
		}
		Class clazz = (instnOrClazz instanceof Class ? 
				(Class) instnOrClazz : instnOrClazz.getClass());
		
		Class[] valTypes = null;
		if(paramVals != null && valClazzs != null) {
			if(paramVals.length != valClazzs.length) {
				log.error("反射调用方法失败: [{}.{}()], 入参与类型个数不一致.", clazz, methodName);
				return null;
			} else {
				valTypes = valClazzs;
			}
			
		} else if(paramVals != null) {
			valTypes = new Class[paramVals.length];
			for (int i = 0; i < paramVals.length; i++) {
				valTypes[i] = (paramVals[i] != null ? 
						paramVals[i].getClass() : Object.class);
			}
			
		} else if(valClazzs != null) {
			valTypes = new Class[valClazzs.length];
			for (int i = 0; i < valClazzs.length; i++) {
				valTypes[i] = (valClazzs[i] != null ? 
						valClazzs[i] : Object.class);
			}
		}
		
		Object result = null;
		try {
			Method method = clazz.getDeclaredMethod(methodName, valTypes);
			method.setAccessible(true);	// 临时开放调用权限

			if (paramVals == null && valTypes != null) {
				paramVals = new Object[valTypes.length];
			}
			result = method.invoke(instnOrClazz, paramVals);
			
		} catch (Exception e) {
			log.error("反射调用方法失败: [{}.{}()]", clazz, methodName, e);
		}
		return result;
	}
	
	/**
	 * 打印Bean中的所有成员域（使用MULTI_LINE_STYLE风格）
	 * @param bean bean对象
	 * @return
	 */
	public static String printBean(Object bean) {
		return new ReflectionToStringBuilder(bean, 
				ToStringStyle.MULTI_LINE_STYLE).toString();
	}
	
	/**
	 * 打印Bean中的所有成员域
	 * @param bean bean对象
	 * @param toStringStyle 打印风格, 建议值 ToStringStyle.MULTI_LINE_STYLE
	 * @return
	 */
	public static String printBean(Object bean, ToStringStyle style) {
		if(bean == null) {
			return "";
		}
		return new ReflectionToStringBuilder(bean, style).toString();
	}
	
	public static boolean toSerializable(Serializable o, String outFile) {
		boolean isOk = false;
		try {
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(outFile));
			oos.writeObject(o);
			oos.flush();
			oos.close();
			
		} catch (Exception e) {
			log.error("序列化对象到外存文件失败: [{}]", outFile, e);
		}
		return isOk;
	}
	
	public static Object unSerializable(String inFile) {
		Object o = null;
		try {
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(inFile));
			o = ois.readObject();
			ois.close();
			
		} catch (Exception e) {
			log.error("从外存文件恢复序列化对象失败: [{}]", inFile, e);
		}
		return o;
	}
	
}
