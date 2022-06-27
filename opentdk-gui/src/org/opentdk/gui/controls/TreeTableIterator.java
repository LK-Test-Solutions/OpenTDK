package org.opentdk.gui.controls;

import java.util.Iterator;
import java.util.Stack;

import javafx.scene.control.TreeItem;

/**
 * Iterates over a TreeTable and adds values to stack.
 * @author FME
 */
public class TreeTableIterator<T> implements Iterator<TreeItem<T>> {

	private Stack<TreeItem<T>> stack = new Stack<>();
	
	public TreeTableIterator(TreeItem<T> root) {
		stack.push(root);
	}
	
	@Override
	public boolean hasNext() {
		return !stack.isEmpty(); 
	}

	@Override
	public TreeItem<T> next() {
		TreeItem<T> nextItem = stack.pop();
		nextItem.getChildren().forEach(stack::push);
		
		return nextItem;
	}

}
