

/**
 *
 * AVLTree
 *
 * An implementation of aמ AVL Tree with
 * distinct integer keys and info.
 *
 */

public class AVLTree {
	
	private AVLNode root;
	
	public AVLTree() {
		root = new AVLNode(); //fake node key=-1
	}
	public AVLTree(AVLNode a) {
		root = a;
		root.setLeft(new AVLNode()); //sets fake sons for new root key=-1
		root.setRight(new AVLNode());
	}
	
  /**
   * public boolean empty()
   *
   * Returns true if and only if the tree is empty.
   * Complexity: O(1)
   * Analysis: The empty method checks if the tree is empty by verifying whether the root node is a real node or not. 
   * 		   As it only involves a simple check, the time complexity is O(1).
   *
   */
  public boolean empty() {	
	  return !root.isRealNode(); 
  }

 /**
   * public String search(int k)
   *
   * Returns the info of an item with key k if it exists in the tree.
   * otherwise, returns null.
   * 
   * Complexity: O(log n)
   * Analysis: The search method finds the node with the given key k in the AVL tree. In the worst case, 
   * 		   it may have to traverse the entire height of the tree, resulting in a time complexity of O(log n), h=log n in an AVLTree.
   */
  public String search(int k) {
	  AVLNode x = root;
	  while(x.isRealNode() && k!=x.getKey()) {
			if(k>x.getKey())
				x=(AVLNode)x.getRight();
			else
				x=(AVLNode)x.getLeft();
		}
		if(!x.isRealNode())
			return null;
		return x.getValue();
  }

  /**
   * public int insert(int k, String i)
   *
   * Inserts an item with key k and info i to the AVL tree.
   * The tree must remain valid, i.e. keep its invariants.
   * Returns the number of re-balancing operations, or 0 if no re-balancing operations were necessary.
   * A promotion/rotation counts as one re-balance operation, double-rotation is counted as 2.
   * Returns -1 if an item with key k already exists in the tree.
   * 
   * 
   * Complexity: O(log n)
   * Analysis: in this method we first insert regularly into the tree, going through the height of the tree, which is O(log n) in
   * 		   an AVLTree. then we update parent and sons in O(1). then we call the updateHeight function which is O(log n), analysis for it is 
   * 		   available downwards. then, we balance the tree if needed with the balance method which is O(1), see below.
   * 		   O(log n) + O(1) + O(log n) + O(1) = O(log n).
   */
  public int insert(int k, String i) {
	    IAVLNode y = null;
	    IAVLNode x = root;
	    int rotations = 0;

	    // Find the position for insertion
	    while (x.isRealNode()) {
	        y = x;
	        if (x.getKey() == k)
	            return -1; // Item with key k already exists
	        if (k < x.getKey())
	            x = x.getLeft();
	        else
	            x = x.getRight();
	    }

	    IAVLNode z = new AVLNode(k, i);
	    z.setParent(y);
	    z.setLeft(new AVLNode()); // Set fake sons for new AVLNode with key=-1 height=-1
	    z.setRight(new AVLNode());
	    z.setHeight(0);

	    if (y == null) {
	        root = (AVLNode) z;
	    } else if (k < y.getKey()) {
	        y.setLeft(z);
	    } else {
	        y.setRight(z);
	    }

	    // Update heights before rotations
	    updateHeight((AVLNode) z);

	    // Perform rotations if necessary
	    IAVLNode current = z;
	    while (current != null) {
	        IAVLNode parent = current.getParent(); // Store the parent node before moving up in the tree
	        int balanceFactor = current.getLeft().getHeight() - current.getRight().getHeight();

	        if (balanceFactor == 2 || balanceFactor == -2) {
	            // Perform rotations if the balance factor is not within [-1, 1]
	            int numRotations = balance((AVLNode) current);
	            if (current == root) {
	                root = numRotations == 1 ? (AVLNode) parent : root; // Update root if necessary
	            }
	            rotations += numRotations;
	            break; // No need to update heights further, as balance has been restored
	        }
	        current = parent; // Move up in the tree
	    }

	    return rotations;
	}



  /**
   * private int balance(AVLNode node)
   *
   * The balance method performs rotations on the AVL tree to restore the AVL properties. 
   * 
   * Complexity: O(1)
   * Analysis: The rotations are performed in constant time for each node, so the time complexity is O(1). 
   * 		   it uses the function leftRotate and rightRotate which are also O(1).
   */
   private int balance(AVLNode node) {
	    int rotations = 0;
	    int balanceFactor = node.getLeft().getHeight() - node.getRight().getHeight();

	    if (balanceFactor == 2) {
	        int leftBalanceFactor = node.getLeft().getLeft().getHeight() - node.getLeft().getRight().getHeight();
	        if (leftBalanceFactor == -1) {
	            // Left-Right rotation
	            node.setLeft(leftRotate((AVLNode) node.getLeft()));
	            rotations++;
	        }
	        // Right rotation
	        AVLNode newRoot = rightRotate(node);
	        if (node == root) {
	            root = newRoot; // Update root if necessary
	        }
	        rotations++;
	    } else if (balanceFactor == -2) {
	        int rightBalanceFactor = node.getRight().getLeft().getHeight() - node.getRight().getRight().getHeight();
	        if (rightBalanceFactor == 1) {
	            // Right-Left rotation
	            node.setRight(rightRotate((AVLNode) node.getRight()));
	            rotations++;
	        }
	        // Left rotation
	        AVLNode newRoot = leftRotate(node);
	        if (node == root) {
	            root = newRoot; // Update root if necessary
	        }
	        rotations++;
	    }

	    return rotations;
	}
   
	/**
	 * private AVLNode rightRotate(IAVLNode y)
	 * 
	 * this function makes a right rotation, used to balance the tree
	 * we use this function in the balance method in order to make the needed rolls
	 * in this function we also update the height of the nodes after rotating them.
	 * 
	 * Complexity: O(1)
	 * Analysis: The rotation is performed in constant time, O(1), since it involves a fixed number of pointer updates
	 * 			 and attribute (height) updates.
	 * 
	 */
   private AVLNode rightRotate(IAVLNode y) {
	   AVLNode x = (AVLNode) y.getLeft();
	    AVLNode T2 = (AVLNode) x.getRight();

	    x.setParent(y.getParent());
	    if (y.getParent() != null) {
	        if (y == y.getParent().getLeft()) {
	            y.getParent().setLeft(x);
	        } else {
	            y.getParent().setRight(x);
	        }
	    }

	    x.setRight(y);
	    y.setParent(x);

	    y.setLeft(T2);
	    T2.setParent(y);

	    // Update height for nodes y and x
	    y.setHeight(1 + Math.max(y.getLeft().getHeight(), y.getRight().getHeight()));
	    x.setHeight(1 + Math.max(x.getLeft().getHeight(), x.getRight().getHeight()));
	    
	    return x;
	}
   
   
   /**
	 * private AVLNode leftRotate(IAVLNode x)
	 * 
	 * this function makes a left rotation, used to balance the tree
	 * we use this function in the balance method in order to make the needed rolls.
	 * in this function we also update the height of the nodes after rotating them.
	 * 
	 * Complexity: O(1)
	 * Analysis: The rotation is performed in constant time, O(1), since it involves a fixed number of pointer updates
	 * 			 and attribute (height) updates.
	 * 
	 */
	private AVLNode leftRotate(IAVLNode x) {
		 AVLNode y = (AVLNode) x.getRight();
		    AVLNode T2 = (AVLNode) y.getLeft();

		    y.setParent(x.getParent());
		    if (x.getParent() != null) {
		        if (x == x.getParent().getLeft()) {
		            x.getParent().setLeft(y);
		        } else {
		            x.getParent().setRight(y);
		        }
		    }

		    y.setLeft(x);
		    x.setParent(y);

		    x.setRight(T2);
		    T2.setParent(x);

		    // Update height for nodes x and y
		    x.setHeight(1 + Math.max(x.getLeft().getHeight(), x.getRight().getHeight()));
		    y.setHeight(1 + Math.max(y.getLeft().getHeight(), y.getRight().getHeight()));
		    return y;
	}

   
   /**
	 * public void updateHeight(AVLNode node)
	 * 
	 * This function updates the height of the parameter node and above, traverses upwards to the root.
	 * We use this in the insert function in order to maintain correct heights after simple insertion and before rotation if needed.
	 * 
	 * 
	 * Complexity: O(log n)
	 * Analysis: This function traverses through the height of the tree, node parameter is a leaf, and it runs until it reaches
	 * 			 root.  In AVLTree h=log n, so this function runs at O(log n).
	 * 		     
	 * 
	 */
   public void updateHeight(AVLNode node) {
	   if(!node.isRealNode())
		   return;
	   int counter=0;
	   while(node.getParent()!=null) {
		   if(node.getHeight()<counter)
			   node.setHeight(counter);
		   node=(AVLNode) node.getParent();
		   counter++;
	   }
   }

   /**
    * public String min()
    *
    * Returns the info of the item with the smallest key in the tree,
    * or null if the tree is empty.
    * 
    * Complexity: O(log n)
    * Analysis: The min method finds the node with the smallest key in the AVL tree. It only needs to traverse the leftmost branch 
    * 			of the tree, and the time complexity is O(log n) in a balanced AVL tree.
    * 
    * 
    */
   public String min()
   {
	   if(this.empty())
		   return null;
	   AVLNode x = root;
	   while(x.getLeft().isRealNode())
		   x=(AVLNode)x.getLeft();
	   return x.getValue();
   }

   /**
    * public String max()
    *
    * Returns the info of the item with the largest key in the tree,
    * or null if the tree is empty.
    * 
    * Complexity: O(log n)
    * Analysis: The max method finds the node with the largest key in the AVL tree. It only needs to traverse the rightmost branch 
    * 			of the tree, and the time complexity is O(log n) in a balanced AVL tree.
    */
   public String max()
   {
	   if(this.empty())
		   return null;
	   AVLNode x = root;
	   while(x.getRight().isRealNode())
		   x=(AVLNode)x.getRight();
	   return x.getValue();
   }

  /**
   * public int[] keysToArray()
   *
   * Returns a sorted array which contains all keys in the tree,
   * or an empty array if the tree is empty.
   * 
   * Complexity: O(n)
   * Analysis: The keysToArray method returns a sorted array containing all keys in the AVL tree. 
   * 		   It performs an in-order traversal of the tree,visiting each node once, so the time complexity is O(n).
   * 		   it is doing so by calling the "keysToArray(int[] arr, AVLNode x,int index)" helper function, which is O(n).
   */
  public int[] keysToArray()
  {
        int[] arr = new int[this.size()];
        AVLNode x = root;
        keysToArray(arr,x,0);
        return arr;
        
  }
  
  
  /**
   * public int keysToArray(int[] arr, AVLNode x,int index)
   *
   * gets an array and fills it sorted like an in-order array, which contains all keys in the tree.
   * 
   * Complexity: O(n)
   * Analysis: The keysToArray method gets an array and fills it sorted like an in-order array. 
   * 		   It performs an in-order traversal of the tree,visiting each node once, so the time complexity is O(n).
   */
  public int keysToArray(int[] arr, AVLNode x,int index) {
	  if(x.isRealNode()) {
		  index = keysToArray(arr,(AVLNode)x.getLeft(),index);
		  arr[index++]=x.getKey();
		  index = keysToArray(arr,(AVLNode)x.getRight(),index);
	  }
	  return index;
  }
  /**
   * public String[] infoToArray()
   *
   * Returns an array which contains all info in the tree,
   * sorted by their respective keys,
   * or an empty array if the tree is empty.
   * 
   * 
   * Complexity: O(n)
   * Analysis: The infoToArray method returns an array containing all values in the AVL tree, sorted by their respective keys. 
   * 		   Similar to keysToArray, it performs an in-order traversal of the tree, visiting each node once, so the time complexity is O(n).
   * 		   it is doing so by calling the "infoToArray(String[] arr, AVLNode x,int index)" helper function, which is O(n).
   */
  public String[] infoToArray()
  {
        String[] arr = new String[this.size()];
        AVLNode x = root;
        infoToArray(arr,x,0);
        return arr;
  }
  
  /**
   * public int infoToArray(String[] arr, AVLNode x,int index)
   *
   * gets an array and fills it sorted like an in-order array considering key placements, which contains all the 
   * suited values with placement in the tree.
   * 
   * Complexity: O(n)
   * Analysis: The infoToArray method gets an array and fills it sorted like an in-order array, but with values 
   * 		   instead of keys in the suited placements. 
   * 		   It performs an in-order traversal of the tree,visiting each node once, so the time complexity is O(n).
   */
  public int infoToArray(String[] arr, AVLNode x,int index) {
	  if(x.isRealNode()) {
		  index = infoToArray(arr,(AVLNode)x.getLeft(),index);
		  arr[index++]=x.getValue();
		  index = infoToArray(arr,(AVLNode)x.getRight(),index);
	  }
	  return index;
  }

   /**
    * public int size()
    *
    * Returns the number of nodes in the tree.
    * 
    * Complexity: O(n)
    * Analysis: this function calls the countNodes function, which is O(n), the complexity is O(n) because of that.
    */
   public int size()
   {
	   AVLNode x = root;
	   return countNodes(x);
   }
   
   /**
    * public int countNodes(AVLNode x)
    *
    * Returns the number of nodes in the tree.
    * 
    * Complexity: O(n)
    * Analysis: The countNodes method calculates the number of nodes in the AVL tree by performing a post-order traversal of the tree and counting the nodes. 
    * 		    It visits each node once, so the time complexity is O(n).
    */
   public int countNodes(AVLNode x) {
	   if(!x.isRealNode())
		   return 0;
	   return 1+ countNodes((AVLNode)x.getLeft()) + countNodes((AVLNode)x.getRight());
   }
   
   /**
    * public int getRoot()
    *
    * Returns the root AVL node, or null if the tree is empty
    * 
    * Complexity: O(1)
    * Analysis: The getRoot method returns the root node of the AVL tree. 
    * 			It involves a simple pointer access, so the time complexity is O(1).
    */
   public IAVLNode getRoot()
   {
	   if(this.empty())
		   return null;
	   return root;
   }
   

}
  
