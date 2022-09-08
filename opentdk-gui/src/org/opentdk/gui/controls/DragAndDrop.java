package org.opentdk.gui.controls;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;

/**
 * JavaFX class that provides drag and drop functionality between list views.
 * The specified list views are committed to the constructor as parameters.<br>
 * <br>
 * Usage to activate drag and drop between two lists:
 * 
 * <pre>
 * lstv_left.setCellFactory(drag {@literal ->} new DragAndDrop(lstv_left, lstv_right));
 * lstv_right.setCellFactory(drag {@literal ->} new DragAndDrop(lstv_left, lstv_right));
 * </pre>
 * 
 * @author FME (LK Test Solutions)
 *
 */
public class DragAndDrop extends ListCell<String> {
	/**
	 * Object that stores the dragged object to drop it in the other list view.
	 * ObjectProperty usage is necessary to display the content correctly.
	 */
	private final ObjectProperty<ListCell<String>> drag = new SimpleObjectProperty<>();
	/**
	 * Enable drag and drop interaction between two list views.
	 * @param left first {@link ListView}
	 * @param right second {@link ListView}
	 */
	public DragAndDrop(ListView<String> left, ListView<String> right) {
		ListView<String> first = left;
		ListView<String> second = right;

		first.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		second.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

		// #######################################################
		// #################### First list view ##################
		// #######################################################

		first.setCellFactory(lv -> {
			ListCell<String> cell = new ListCell<String>() {

				@Override
				public void updateItem(String item, boolean empty) {
					super.updateItem(item, empty);
					setText(item);
				}
			};

			// If a cell in the list has content it can be dragged.
			cell.setOnDragDetected(event -> {
				if (!cell.isEmpty()) {
					Dragboard db = cell.startDragAndDrop(TransferMode.MOVE);
					ClipboardContent cbc = new ClipboardContent();
					cbc.putString(cell.getItem());
					db.setContent(cbc);
					drag.set(cell);

				}
				if (second.getItems().size() == 0) {
					second.getItems().add("");

				}
			});

			// If a dragged cell is hovering over another cell - transfer mode move
			// activates
			cell.setOnDragOver(event -> {
				Dragboard db = event.getDragboard();
				if (event.getGestureSource() != second && db.hasString()) {
					event.acceptTransferModes(TransferMode.MOVE);
				}
			});

			// Complete dropping only if the drag board has content
			cell.setOnDragDropped(event -> {
				Dragboard db = event.getDragboard();
				if (db.hasString() && drag.get() != null) {
					ListCell<String> sourceCell = drag.get();
					if (second.getItems().contains(sourceCell.getItem())) {
						first.getItems().add(sourceCell.getItem());
						event.setDropCompleted(true);
						drag.set(null);
					}
				} else {
					event.setDropCompleted(false);
				}
			});

			// A cell can't be in both lists at the same time.
			cell.setOnDragDone(event -> {
				if (second.getItems().contains(cell.getItem()) && first.getItems().contains(cell.getItem())) {
					first.getItems().remove(cell.getItem());
					second.getItems().remove("");
				}
			});

			return cell;
		});

		// #######################################################
		// ################# Second list view ###################
		// #######################################################

		second.setCellFactory(lv -> {
			ListCell<String> cell = new ListCell<String>() {
				@Override
				public void updateItem(String item, boolean empty) {
					super.updateItem(item, empty);
					setText(item);
				}
			};

			cell.setOnDragDetected(event -> {
				if (!cell.isEmpty()) {
					Dragboard db = cell.startDragAndDrop(TransferMode.MOVE);
					ClipboardContent cbc = new ClipboardContent();
					cbc.putString(cell.getItem());
					db.setContent(cbc);
					drag.set(cell);
				}
				if (first.getItems().size() == 0) {
					first.getItems().add("");

				}
			});

			cell.setOnDragOver(event -> {
				Dragboard db = event.getDragboard();
				if (event.getGestureSource() != first && db.hasString()) {
					event.acceptTransferModes(TransferMode.MOVE);
				}
			});

			cell.setOnDragDropped(event -> {
				Dragboard db = event.getDragboard();
				if (db.hasString() && drag.get() != null) {
					ListCell<String> sourceCell = drag.get();
					if (first.getItems().contains(sourceCell.getItem())) {
						second.getItems().add(sourceCell.getItem());
						event.setDropCompleted(true);
						drag.set(null);
					}
				} else {
					event.setDropCompleted(false);
				}
			});

			cell.setOnDragDone(event -> {
				if (first.getItems().contains(cell.getItem()) && second.getItems().contains(cell.getItem())) {
					second.getItems().remove(cell.getItem());
					first.getItems().remove("");
				}
			});

			return cell;
		});
	}

}