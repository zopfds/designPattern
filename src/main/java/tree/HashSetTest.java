package tree;

/**
 * ${DESCRIPTION}
 *
 * @author jianbo.pan@mljr.com
 * @version ${VERSION}
 * @create 2018/8/30
 */
public class HashSetTest {

    public static void rotateLeft(TreeNode root){
        if(root != null){
            TreeNode right = root.getRight();
            root.setRight(right.getLeft());
            if(right.getLeft() != null){
                right.getLeft().setParent(root);
            }
            right.setParent(root.getParent());
            right.setLeft(root);
            if(right.getParent() == null){
                root = right;
            }else if(right.getParent().getLeft() == root){
                right.getParent().setLeft(right);
            }else{
                right.getParent().setRight(right);
            }
            root.setParent(right);
        }
    }

    public static TreeNode findSucc(TreeNode root){
        TreeNode min = root.getRight();

        while(min != null && min.getLeft() != null){
            min = min.getLeft();
        }

        if(min != null){
            return min;
        }

        min = root.getParent();
        while(min != null && min.getParent() != null){
            if(min == min.getParent().getLeft()){
                return min;
            }
            min = min.getParent();
        }

        return null;
    }
}


class TreeNode<E>{
    private TreeNode<E> parent;
    private TreeNode<E> left;
    private TreeNode<E> right;
    private TreeNode<E> prev;
    boolean red;

    public TreeNode(TreeNode<E> parent, TreeNode<E> left, TreeNode<E> right, TreeNode<E> prev, boolean red) {
        this.parent = parent;
        this.left = left;
        this.right = right;
        this.prev = prev;
        this.red = red;
    }

    public TreeNode<E> getParent() {
        return parent;
    }

    public void setParent(TreeNode<E> parent) {
        this.parent = parent;
    }

    public TreeNode<E> getLeft() {
        return left;
    }

    public void setLeft(TreeNode<E> left) {
        this.left = left;
    }

    public TreeNode<E> getRight() {
        return right;
    }

    public void setRight(TreeNode<E> right) {
        this.right = right;
    }

    public TreeNode<E> getPrev() {
        return prev;
    }

    public void setPrev(TreeNode<E> prev) {
        this.prev = prev;
    }

    public boolean isRed() {
        return red;
    }

    public void setRed(boolean red) {
        this.red = red;
    }
}