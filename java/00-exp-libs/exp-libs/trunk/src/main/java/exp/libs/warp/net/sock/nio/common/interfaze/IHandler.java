package exp.libs.warp.net.sock.nio.common.interfaze;

/**
 * <pre>
 * 业务处理接口
 * 
 * 由用户实现
 * </pre>	
 * <B>PROJECT：</B> exp-libs
 * <B>SUPPORT：</B> EXP
 * @version   1.0 2015-12-27
 * @author    EXP: 272629724@qq.com
 * @since     jdk版本：jdk1.6
 */
public interface IHandler {

	/**
	 * <pre>
	 * 会话创建事件处理逻辑
	 * </pre>
	 * 
	 * @param session 会话
	 * @throws Exception 异常
	 */
	public void onSessionCreated(ISession session) throws Exception;

	/**
	 * <pre>
	 * 消息接收事件处理逻辑
	 * </pre>
	 * 
	 * @param session 会话
	 * @param msg 消息
	 * @throws Exception 异常
	 */
	public void onMessageReceived(ISession session, Object msg) throws Exception;

	/**
	 * <pre>
	 * 异常事件处理逻辑。
	 * 在实现过滤器方法时，若不捕获异常，则所有异常都会被抛到此方法中。
	 * </pre>
	 * @param session 会话
	 * @param exception 异常
	 */
	public void onExceptionCaught(ISession session, Throwable exception);
}
