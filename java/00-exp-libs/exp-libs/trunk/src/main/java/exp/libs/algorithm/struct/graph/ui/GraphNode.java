package exp.libs.algorithm.struct.graph.ui;

import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.graph.Node;
import org.jgraph.graph.DefaultGraphCell;

public class GraphNode {

	private final static int HEIGHT = 32;
	
	private final static int MIN_DENSITY = 32;
	
	private final static int MAX_DENSITY = 320;
	
	private boolean isGraphSrc;
	
	private boolean isGraphSnk;
	
	private String name;
	
	private DefaultGraphCell cellNode;
	
	private Node gefNode;
	
	public GraphNode(String name) {
		this.isGraphSrc = false;
		this.isGraphSnk = false;
		this.name = (name == null ? "" : name);
		this.cellNode = new DefaultGraphCell(this.name);
		this.cellNode.addPort();
		this.gefNode = new Node();
		this.gefNode.width = calculateDensity(this.name);	// GEF计算节点X坐标的参考值，并非节点宽度值, 影响节点间密度
		this.gefNode.height = this.gefNode.width;	// GEF计算节点Y坐标的参考值， 一般与width取值相同， 否则节点间距会过密
		this.gefNode.setPadding(new Insets(	// GEF计算节点间间距的偏移量(不能为0， 否则节点会胶合)
				GraphParams.NODE_TOP_PAD, 
				GraphParams.NODE_LEFT_PAD, 
				GraphParams.NODE_BOTTOM_PAD, 
				GraphParams.NODE_RIGHT_PAD));
	}
	
	/**
	 * 通过节点名称计算节点间的密度参考值
	 * @param name
	 * @return
	 */
	private int calculateDensity(String name) {
		int len = (name == null ? 0 : name.length());
		int density = len * 10;
		density = (density < MIN_DENSITY ? MIN_DENSITY : density);
		density = (density > MAX_DENSITY ? MAX_DENSITY : density);
		return density;
	}
	
	public String getName() {
		return name;
	}
	
	public DefaultGraphCell getCellNode() {
		return cellNode;
	}

	public Node getGefNode() {
		return gefNode;
	}
	
	public int getX() {
		return gefNode.x;
	}
	
	public int getY() {
		return gefNode.y;
	}
	
	public int getWidth() {
		return gefNode.width;
	}
	
	public int getHeight() {
		return HEIGHT;	// 节点高度为固定值即可（文字描述的节点名称只能有一行）
	}

	public boolean isGraphSrc() {
		return isGraphSrc;
	}

	public boolean isGraphSnk() {
		return isGraphSnk;
	}
	
	protected void markGraphSrc() {
		this.isGraphSrc = true;
	}
	
	protected void markGraphSnk() {
		this.isGraphSnk = true;
	}
	
}