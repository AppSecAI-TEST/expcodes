package exp.libs.algorithm.np.ispa;

import java.util.LinkedList;
import java.util.List;

import exp.libs.algorithm.struct.graph.Node;

public final class ISPARst {

	/** 是否为可行解 */
	private boolean isVaild;
	
	/** 无解的原因 */
	private String cause;
	
	/** 解的总开销 */
	private int cost;
	
	/** 移动轨迹路由 */
	private List<Node> routes;
	
	protected ISPARst() {
		this.isVaild = false;
		this.cause = "";
		this.cost = -1;
		this.routes = new LinkedList<Node>();
	}

	public boolean isVaild() {
		return isVaild;
	}

	protected void setVaild(boolean isVaild) {
		this.isVaild = isVaild;
	}

	public String getCause() {
		return cause;
	}

	protected void setCause(String cause) {
		this.cause = cause;
	}

	public int getCost() {
		return cost;
	}

	protected void setCost(int cost) {
		this.cost = cost;
	}

	public List<Node> getRoutes() {
		return new LinkedList<Node>(routes);
	}

	protected void setRoutes(List<Node> routes) {
		this.routes = routes;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("[vaild] : ").append(isVaild);
		sb.append("\r\n[cause] : ").append(cause);
		sb.append("\r\n[cost] : ").append(cost);
		sb.append("\r\n[route] : ");
		int size = routes.size();
		if(size > 0) {
			for(int i = 0; i < size - 1; i++) {
				sb.append(routes.get(i)).append(" -> ");
			}
			sb.append(routes.get(size - 1));
		} else {
			sb.append("null");
		}
		return sb.toString();
	}
	
}