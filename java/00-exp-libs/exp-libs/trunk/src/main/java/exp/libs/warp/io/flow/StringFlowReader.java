package exp.libs.warp.io.flow;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import exp.libs.envm.Charset;
import exp.libs.envm.Endline;

/**
 * <PRE>
 * 字符串流读取器.
 * 	该读取器只会对指定字符串逐字符读入一次，无法重新读取.
 * </PRE>
 * <B>PROJECT：</B> exp-libs
 * <B>SUPPORT：</B> EXP
 * @version   1.0 2015-12-27
 * @author    EXP: 272629724@qq.com
 * @since     jdk版本：jdk1.6
 */
public class StringFlowReader {

	/** 日志器 */
	private final static Logger log = LoggerFactory.getLogger(StringFlowReader.class);
	
	/** 所读入字符串的默认编码 */
	public final static String DEFAULT_ENCODE = Charset.ISO;
	
	/**
	 * 所读入字符串的默认[行终止符].
	 * (默认为换行符, 但存在无换行符的流式文件需要另外指定)
	 */
	public final static char DEFAULT_LINE_END = Endline.CR;
	
	/** 所读入的字符串编码 */
	private String encode;
	
	/** 流读取器 */
	private InputStreamReader strReader;
	
	/** 标记是否存在可读的下一行 */
	private boolean hasNextLine;
	
	/**
	 * 构造函数
	 * @param str 待读入字符串
	 */
	public StringFlowReader(String str) {
		init(str, DEFAULT_ENCODE);
	}
	
	/**
	 * 构造函数
	 * @param str 待读入字符串
	 * @param encode 待读入字符串的编码
	 */
	public StringFlowReader(String str, String encode) {
		init(str, encode);
	}
	
	/**
	 * 初始化
	 * @param str 待读入字符串
	 * @param encode 待读入字符串的编码
	 */
	public void init(String str, String encode) {
		this.encode = testEncode(encode) ? encode : DEFAULT_ENCODE;
		this.hasNextLine = false;
		
		if(str != null) {
			try {
				this.strReader = new InputStreamReader(
						new ByteArrayInputStream(str.getBytes(this.encode)), 
						this.encode);
				this.hasNextLine = true;
			} catch (Exception e) {
				log.error("读取字符串 [{}] 失败.", str, e);
				close();
			}
		}
		
		if(!hasNextLine) {
			log.error("构造字符串 [{}] 的流式读取器失败.", str);
		}
	}
	
	/**
	 * 测试编码是否合法.
	 * @param encode 被测试编码
	 * @return true:编码合法; false:编码非法
	 */
	private boolean testEncode(String encode) {
		boolean isVaild = true;
		try {
			"test".getBytes(encode);
		} catch (UnsupportedEncodingException e) {
			isVaild = false;
		}
		return isVaild;
	}
	
	/**
	 * 当前文件流是否存在下一行（以实际的[行终止符]标记[行]）
	 * @return true:存在; false:不存在
	 */
	public boolean hasNextLine() {
		return hasNextLine;
	}
	
	/**
	 * 读取当前行（使用[换行符]作为[行终止符]）.
	 * 	此方法需配合 hasNextLine 方法使用（类似迭代器的使用方式）.
	 * @return 当前行数据
	 */
	public String readLine() {
		return readLine(DEFAULT_LINE_END);
	}
	
	/**
	 * 读取当前行（使用[自定义符号]作为[行终止符]）.
	 * 	此方法需配合 hasNextLine 方法使用（类似迭代器的使用方式）.
	 * @param lineEnd 自定义行终止符
	 * @return 当前行数据
	 */
	public String readLine(char lineEnd) {
		if(!hasNextLine) {
			return "";
		}
		
		StringBuilder line = new StringBuilder();
		try {
			while(true) {
				int n = strReader.read();
				if(n == -1) {
					hasNextLine = false;	//已到文件末尾
					break;
				}
				char c = (char) n;
				line.append(c);
				
				if(c == lineEnd) {	// 已到行尾
					break;
				}
			}
		} catch (IOException e) {
			log.error("读取字符串流过程中发生异常.", e);
			close();
		}
		return line.toString();
	}
	
	/**
	 * 关闭文件流读取器
	 */
	public void close() {
		if(strReader != null) {
			try {
				strReader.close();
			} catch (IOException e) {
				System.err.println("关闭字符串流式读取器失败.");
			}
		}
		strReader = null;
		hasNextLine = false;
	}
	
}
