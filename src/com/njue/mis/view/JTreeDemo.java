package com.njue.mis.view;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.event.*;
import javax.swing.tree.*;
class JTreeDemo 
{
    JFrame mainFrame;
    JScrollPane scrollPane;
    JTree simpleTree;
    JButton addButton;
    JButton removeButton;
    JTextField insertField;
    JPanel panel;
    public JTreeDemo() {
        mainFrame = new JFrame ( "JTreeDemo" );
        DefaultMutableTreeNode swing = new DefaultMutableTreeNode("Swing");
        buildTree(swing);
        simpleTree = new JTree(swing);
        simpleTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        simpleTree.getSelectionModel().addTreeSelectionListener(new TreeSelectionListener(){
            public void valueChanged( TreeSelectionEvent e ){
                System.out.println( "selection changed" );
            }
        });
        simpleTree.setRootVisible(false);
        simpleTree.setShowsRootHandles(true);
        simpleTree.putClientProperty("JTree.lineStyle","Horizontal");
        simpleTree.putClientProperty("JTree.lineStyle", "Angled");
//        simpleTree.putClientProperty("JTree.lineStyle","None");
//        simpleTree.setCellRenderer( new CustomTreeCellRenderer() );
        simpleTree.setEditable( false );
        simpleTree.getModel().addTreeModelListener(new TreeModelListener(){
                public void treeNodesChanged(TreeModelEvent e) {
                    System.out.println("node changed");
                }
                public void treeNodesInserted(TreeModelEvent e) {
                    System.out.println( "node inserted" );
                }
                public void treeNodesRemoved(TreeModelEvent e) {
                    System.out.println("node removed");
                }
                public void treeStructureChanged(TreeModelEvent e) {
                    System.out.println( "strutrued changed" );
                }
            });
        scrollPane = new JScrollPane( simpleTree );

        addButton = new JButton ("add");
        addButton.addActionListener( new ActionListener(){
            public void actionPerformed( ActionEvent e){
                TreePath parentPath = simpleTree.getSelectionPath();
                if( parentPath != null ){
                    DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode)parentPath.getLastPathComponent();
                    DefaultTreeModel model = (DefaultTreeModel)simpleTree.getModel();
                    DefaultMutableTreeNode child = new DefaultMutableTreeNode( insertField.getText() );
                    model.insertNodeInto( child , parentNode,0 );
                    simpleTree.scrollPathToVisible( new TreePath( child.getPath() ) ); 
                }
            }
        });
        removeButton = new JButton ("remove");
        removeButton.addActionListener( new ActionListener(){
            public void actionPerformed( ActionEvent e){
                TreePath path = simpleTree.getSelectionPath();
                if( path != null ){
                    DefaultMutableTreeNode removeNode = (DefaultMutableTreeNode)path.getLastPathComponent();
                    DefaultTreeModel model = (DefaultTreeModel)simpleTree.getModel();
                    model.removeNodeFromParent( removeNode );
                }
            }
        });
        insertField = new JTextField(20);
        panel = new JPanel ( new GridLayout(15,1) );
        panel.add(insertField);
        panel.add( addButton );
        panel.add( removeButton );

        mainFrame.getContentPane().add( panel,BorderLayout.LINE_END );
        mainFrame.getContentPane().add( scrollPane,BorderLayout.LINE_START  );
        mainFrame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        mainFrame.pack();
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setVisible( true );
    }
    private void buildTree(DefaultMutableTreeNode root){
        DefaultMutableTreeNode parent;
        DefaultMutableTreeNode child;
        parent = new DefaultMutableTreeNode("Containers");
        child = new DefaultMutableTreeNode("JFrame");
        parent.add(child);
        child = new DefaultMutableTreeNode("JPanel");
        parent.add(child);
        child = new DefaultMutableTreeNode("JDialog");
        parent.add(child);
        root.add(parent);
        parent = new DefaultMutableTreeNode("Components");
        child = new DefaultMutableTreeNode("JButton");
        parent.add(child);
        child = new DefaultMutableTreeNode("JLabel");
        parent.add(child);
        child = new DefaultMutableTreeNode("JList");
        parent.add(child);
        root.add(parent);
    }
    private class CustomTreeCellRenderer extends DefaultTreeCellRenderer{
        public CustomTreeCellRenderer(){
            /*setLeafIcon( new ImageIcon("images/leaf.gif") );
            setOpenIcon( new ImageIcon("images/expand.gif") );
            setClosedIcon( new ImageIcon("images/unexpand.gif") );*/
        }
        public Component getTreeCellRendererComponent(
            JTree tree, Object value,
            boolean sel, boolean expanded,
            boolean leaf, int row, boolean hasFocus){
            JButton button = new JButton ( value.toString() );
            if( leaf )
                button.setIcon(new ImageIcon("images/leaf.png") );
            else{
                if( expanded )
                    button.setIcon(new ImageIcon("images/expand.png") );
                else
                    button.setIcon(new ImageIcon("images/unexpand.png") );
            }
            return button;
        }
    }
    public static void main(String[] args) 
    {
        new JTreeDemo();
    }
}
   
