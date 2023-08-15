

/** 
 * public class AVLNode
 *
 * If you wish to implement classes other than AVLTree
 * (for example other AVLNode), do it in this file, not in another file. 
 */
public class AVLNode implements IAVLNode{

	private IAVLNode left;
	private IAVLNode right;
	private IAVLNode parent;
	
	private int height;
	private int key;
	private String value;
	
	public AVLNode() { //constructor for fake son
		key=-1;
		height=-1;
	}
	
	public AVLNode(int k, String v) {
		key = k;
		value = v;
		height =0;
	}
	
	public int getKey()	{
		return key; 
	}
	public String getValue() {
		return value; 
	}
	public void setLeft(IAVLNode node) {
		this.left = node;
	}
	public IAVLNode getLeft() {
		return left;
	}
	public void setRight(IAVLNode node)
	{
		this.right = node;
	}
	public IAVLNode getRight() {
		return right; 
	}
	public void setParent(IAVLNode node) {
		this.parent = node; 
	}
	public IAVLNode getParent()	{
		return parent; 
	}
	public boolean isRealNode()	{
		return key != -1; 
	}
	public void setHeight(int height){
		this.height = height; 
	}
	public int getHeight(){
		return height; 
	}

}
