
public class BinarySearchTree 
{
    private class Node
    {    
    	public int key;
        public String value;    
        public Node left;  
        public Node right;  
            
        public Node(int key, String value) 
        {    
    		this.key = key;
    		this.value = value;
    		this.left = null;    
            this.right = null;    
        }    
    }    
     
    public Node root = null;       

    
    //-------------------------------------------------------------
    public void add(int key, String value) {
        root = addRecur(root, key, value);
    }
    
    private Node addRecur(Node curr, int key, String value) {
        if (curr == null) 
            return new Node(key, value);
        else
        	if (key < curr.key)
        		curr.left = addRecur(curr.left, key, value);
        	else if (key > curr.key) 
        		curr.right = addRecur(curr.right, key, value);
        	else
        		curr.value = value;
        return curr;
    }    

    //-------------------------------------------------------------
    public String get(int key) {
        return getRecur(root, key);
    }

    private String getRecur(Node curr, int key) {
        if (curr == null)
            return null;
        if (key == curr.key)
            return curr.value;
        return key < curr.key ? getRecur(curr.left, key) : getRecur(curr.right, key);
    } 
    
    //-------------------------------------------------------------
    public boolean contains(int key) {
        return containsRecur(root, key);
    }

    private boolean containsRecur(Node curr, int key) {
        if (curr == null)
            return false;
        if (key == curr.key)
            return true;
        return key < curr.key ? containsRecur(curr.left, key) : containsRecur(curr.right, key);
    } 
       

    //-------------------------------------------------------------
    public void delete(int key) {
        root = deleteRecur(root, key);
    }   
    
    private Node deleteRecur(Node curr, int key) {
        if (curr == null)  // key not in tree
            return null;

        if (key == curr.key)  // remove this node
        {
        	if (curr.left == null && curr.right == null)
        	    return null;
        	else if (curr.right == null) // replace this node with left child
        	    return curr.left;
        	else if (curr.left == null) // replace this node with right child
        	    return curr.right;
        	else
        	{
        		Node n = maxNode(curr.left);
        		curr.key = n.key;
        		curr.value = n.value;
        		curr.left = deleteRecur(curr.left, n.key);
        		return curr;
        	}
        }
        else
        {
        	if (key < curr.key) 
        		curr.left = deleteRecur(curr.left, key);
        	else
        		curr.right = deleteRecur(curr.right, key);

        	return curr;
        }
    }    

    
    //-------------------------------------------------------------  
    private Node maxNode(Node root) {
        return root.right == null ? root : maxNode(root.right);
    } 
    
    //-------------------------------------------------------------   
    public void traverseInOrder(Node node) {
        if (node != null) {
            traverseInOrder(node.left);
            System.out.print(" " + node.key+'/'+node.value);
            traverseInOrder(node.right);
        }
    }    
       
    //-------------------------------------------------------------
    public void print() 
    {    
    	traverseInOrder(root);
    }    
}

