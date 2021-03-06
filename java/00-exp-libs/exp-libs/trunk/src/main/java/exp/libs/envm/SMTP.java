package exp.libs.envm;

public enum SMTP {

	QQ("smtp.qq.com", 25), 
	
	QQ_COMPANY("smtp.exmail.qq.com", 465), 
	
	GMAIL("smtp.gmail.com", 587),
	
	_163("smtp.163.com", 25),
	
	_126("smtp.126.com", 25),
	
	_139("SMTP.139.com", 25),
	
	_263("smtp.263.net", 25),
	
	_263CN("263.net.cn", 25),
	
	X263("smtp.x263.net", 25),
	
	_21CN("smtp.21cn.com", 25),
	
	SINA("smtp.sina.com", 25),
	
	SINA_VIP("smtp.vip.sina.com", 25),
	
	SOHU("smtp.sohu.com", 25),
	
	FOXMAIL("SMTP.foxmail.com", 25), 
	
	CHINA("smtp.china.com", 25),
	
	TOM("smtp.tom.com", 25),
	
	ETANG("smtp.etang.com", 25),
	
	;
	
	public String SERVER;
	
	public int PORT;
	
	private SMTP(String server, int port) {
		this.SERVER = server;
		this.PORT = port;
	}
	
}
