package service.pool;

import org.apache.thrift.transport.TSocket;

/** 
 * @author ���
 * @version 1.0
 * @datetime 2015-12-30 ����09:33:14 
 * ���ӳؽӿ�
 */
public interface ConnectionProvider {

	/**
     * ȡ���ӳ��е�һ������
     * @return
     */
    public TSocket getConnection();
    
    /**
     * ��������
     * @param socket
     */
    public void closeConnection(TSocket socket);
    
    /**
     * �ͷ����ӳ�
     */
    public void destroyPool();

}
