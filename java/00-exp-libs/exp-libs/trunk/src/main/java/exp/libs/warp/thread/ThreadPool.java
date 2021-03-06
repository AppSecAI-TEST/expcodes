package exp.libs.warp.thread;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.TimeUnit;

/**
 * <pre>
 * 线程池.
 * </pre>	
 * <B>PROJECT：</B> exp-libs
 * <B>SUPPORT：</B> EXP
 * @version   1.0 2015-12-27
 * @author    EXP: 272629724@qq.com
 * @since     jdk版本：jdk1.6
 */
public class ThreadPool extends _ThreadPool<Object> {

	/**
	 * <pre>
	 * 构造函数
	 * 
	 * 根据八二定律初始化线程池（80%的任务可以由20%的核心线程处理）
	 * 作为基数的任务量 taskNum
	 * 则：
	 * 	核心线程数 corePoolSize = taskNum * 20%
	 * 	最大线程数 maxPoolSize = taskNum
	 * 	任务队列容量workQueueSize = taskNum * 80%
	 * 计算值不足1的，按1处理。
	 * 
	 * 另外：
	 * 	线程允许空闲时间keepAliveTime = 5s
	 * 	对拒绝任务的处理策略为CallerRunsPolicy（重试添加当前的任务）
	 * </pre>
	 * @param taskNum 任务数
	 */
	public ThreadPool(int taskNum) {
		super(taskNum);
	}
	
	/**
	 * <pre>
	 * 构造函数
	 * 
	 * 线程允许空闲时间keepAliveTime = 5s
	 * 对拒绝任务的处理策略为CallerRunsPolicy（重试添加当前的任务）
	 * </pre>
	 * 
	 * @param corePoolSize 核心线程数
	 * @param maxPoolSize 线程池维护线程的最大数量
	 * @param keepAliveTime 线程池维护线程所允许的空闲时间(单位：s)
	 * @param workQueueSize 线程池所使用的任务队列容量
	 */
	public ThreadPool(int corePoolSize, int maxPoolSize,
			long keepAliveTime, int workQueueSize) {
		super(corePoolSize, maxPoolSize, keepAliveTime, workQueueSize);
	}

	/**
	 * <pre>
	 * 构造函数
	 * </pre>
	 * 
	 * @param corePoolSize 核心线程数
	 * @param maxPoolSize 线程池维护线程的最大数量
	 * @param keepAliveTime 线程池维护线程所允许的空闲时间
	 * @param unit 线程池维护线程所允许的空闲时间的单位
	 * @param workQueueSize 线程池所使用的任务队列容量
	 * @param reHandler 线程池对拒绝任务的处理策略
	 */
	public ThreadPool(int corePoolSize, int maxPoolSize,
			long keepAliveTime, TimeUnit unit, int workQueueSize,
			RejectedExecutionHandler reHandler) {
		super(corePoolSize, maxPoolSize, keepAliveTime, unit, workQueueSize, reHandler);
	}

}
