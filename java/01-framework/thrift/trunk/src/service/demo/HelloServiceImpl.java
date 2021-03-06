package service.demo;

import java.util.ArrayList;
import java.util.List;

import org.apache.thrift.TException;

/** 
 * @author 李军
 * @version 1.0
 * @datetime 2015-12-28 下午02:39:42 
 * 类说明 
 */
public class HelloServiceImpl implements Hello.Iface {

	public Course getCourse(int id) throws TException {
		Course c = new Course();
		c.setId(id);
		
		List<Person> list = new ArrayList<Person>();
		Person p = new Person();
		p.setId(1);
		p.setFirstName("张三");
		p.setEmail("zhangsan@163.com");
		list.add(p);
		
		p = new Person();
		p.setId(2);
		p.setFirstName("李四");
		p.setEmail("lisi@163.com");
		list.add(p);
		
		c.setStudent(p);
		c.setStudents(list);
		
		return c;
	}
 
	int getPersonCount = 0;
	public Person getPerson(int id, String name) throws TException {
		Person p = new Person();
		p.setId(id);
		p.setFirstName(name);
		p.setEmail("test@163.com");
		
		getPersonCount++;
		System.out.println("getPerson count：" + getPersonCount);
		return p;
	}

	public boolean helloBoolean(boolean para) throws TException {
		return para;
	}

	public int helloInt(int para) throws TException {
		return para;
	}

	public String helloNull() throws TException {
		return null;
	}

	int helloStringCount = 0;
	public String helloString(String para) throws TException {
		helloStringCount++;
		System.out.println("helloString count：" + helloStringCount);
		return "hello-" + para;
	}

	public void helloVoid() throws TException {
		System.out.println("hello word...");
	}

}
