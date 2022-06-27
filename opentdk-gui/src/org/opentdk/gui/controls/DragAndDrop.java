package org.opentdk.gui.controls;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;

/**
 * JavaFX Class for drag and drop functionality. 
 * The specified list views are committed to the constructor as parameters.
 * @author LK Test Solutions GmbH
 *
 */

public class DragAndDrop extends ListCell<String> {
	
	private final ObjectProperty<ListCell<String>> drag = new SimpleObjectProperty<>();
	private ListView<String> first;
	private ListView<String> second;
	
	/**
	 * Drag and drop types:
	 * MOVE: Drag and drop between two list views
	 * SORT: Additionally change list cell order of second list view
	 * 
	 * @author FME
	 *
	 */
	public enum DragType {
		MOVE,
		SORT
	}
	
	/**
	 * Single list view sorting
	 * @param list
	 */
	public DragAndDrop(ListView<String> list)
	{
		second = list;
		
		second.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
	    	
	    	second.setCellFactory(lv -> 
	    	{
           ListCell<String> cell = new ListCell<String>()
           {  
               @Override
               public void updateItem(String item , boolean empty) 
               {
                   super.updateItem(item, empty);
                   setText(item);
               }
           };
           
           cell.setOnDragDetected(event -> 
           {
               if (! cell.isEmpty()) 
               {
                   Dragboard db = cell.startDragAndDrop(TransferMode.MOVE);
                   ClipboardContent cbc = new ClipboardContent();
                   cbc.putString(cell.getItem());
                   db.setContent(cbc);
                   drag.set(cell);
               }
               if(first.getItems().size() == 0)
               {
               	   first.getItems().add("");
               	   
               }
           });

           cell.setOnDragOver(event -> {
               Dragboard db = event.getDragboard();
               if (event.getGestureSource() != first && db.hasString()) 
               {
                   event.acceptTransferModes(TransferMode.MOVE);
               }
           });
           	           
           cell.setOnDragEntered(event -> 
           {       	   
	            if (event.getGestureSource() != cell &&
	                    event.getDragboard().hasString()) 
	            {			                
	                Dragboard db = event.getDragboard();           
	                ObservableList<String> items = second.getItems();
	                ListCell<String> sourceCell = drag.get();
	                int draggedIdx = items.indexOf(db.getString()), thisIdx = items.indexOf(cell.getItem());
	                
	                if(thisIdx > -1 && thisIdx < items.size() && second.getItems().contains(sourceCell.getItem()))
	                {
	                
		                items.set(draggedIdx, cell.getItem());
		                items.set(thisIdx, db.getString());
		                
		                second.getSelectionModel().select(-1);
		                second.getSelectionModel().select(thisIdx);               
	                }
	                event.consume();
	            }
	        });           
           
//           cell.setOnDragDropped(event -> 
//           {	        	  
//               Dragboard db = event.getDragboard();
//               if (db.hasString() && drag.get() != null) 
//               {
//                   ListCell<String> sourceCell = drag.get();	                  	                  
//                   if(first.getItems().contains(sourceCell.getItem()))
//                   {	                	   
//	                	   second.getItems().add(sourceCell.getItem());		                	   	                	   	                   
//	                	   event.setDropCompleted(true);
//	                	   drag.set(null);
//                   }
//               } else {
//                   event.setDropCompleted(false);
//               }
//           });
           
           cell.setOnDragDone(event -> 
           {
	        	   if (first.getItems().contains(cell.getItem()) && second.getItems().contains(cell.getItem())) 
	        	   {
	        		   second.getItems().remove(cell.getItem());
	        		   first.getItems().remove("");
	        	   }	        	   
           });
           
           return cell ;
        });
	}

	/**
	 * Interaction between two list views
	 * @param left
	 * @param right
	 * @param type
	 */
	public DragAndDrop(ListView<String> left, ListView<String> right, DragType type)
	{
		first = left;
		second = right;		
		
		first.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    		second.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		
	    	//#######################################################
	    //#################### First list view ##################
	    //#######################################################
	    	
	    	first.setCellFactory(lv -> 
	    	{
           ListCell<String> cell = new ListCell<String>()
           {
	        		   
	           @Override
	           public void updateItem(String item , boolean empty) 
	           {
	                   super.updateItem(item, empty);		                   
		               		setText(item);  		                   
	           }
	        };
	           
		   // If a cell in the list has content it can be dragged. 
           cell.setOnDragDetected(event -> 
           {
               if (! cell.isEmpty()) 
               {
                   Dragboard db = cell.startDragAndDrop(TransferMode.MOVE);
                   ClipboardContent cbc = new ClipboardContent();
                   cbc.putString(cell.getItem());
                   db.setContent(cbc);
                   drag.set(cell);
                   
               }
               if(second.getItems().size() == 0)
               {
               	   second.getItems().add("");
               	   
               }
           });

//	           If a dragged cell is hovering over another cell - trasnfer mode move acitvates
           cell.setOnDragOver(event -> 
           {
               Dragboard db = event.getDragboard();
               if (event.getGestureSource() != second && db.hasString()) 
               {
                   event.acceptTransferModes(TransferMode.MOVE);	               
               }
           });
           
//	           cell.setOnDragEntered(event -> {
//	                if (event.getGestureSource() != cell &&
//	                        event.getDragboard().hasString()) {
//	                    cell.setOpacity(0.5);
//	                }
//	            });
//
//	            cell.setOnDragExited(event -> {
//	                if (event.getGestureSource() != cell &&
//	                        event.getDragboard().hasString()) {
//	                    cell.setOpacity(1);
//	                }
//	            });
           
           //Complete dropping only if the dragbord has content 
           cell.setOnDragDropped(event -> 
           {
               Dragboard db = event.getDragboard();
               if (db.hasString() && drag.get() != null) 
               {
                   ListCell<String> sourceCell = drag.get();	                  	                                                        
                   if(second.getItems().contains(sourceCell.getItem())){
                	   first.getItems().add(sourceCell.getItem());		                	  	                       	                	   	                   
                	   event.setDropCompleted(true);
                	   drag.set(null);
                }
//	               if (db.hasString()) {	                   
//		                   ObservableList<String> items = lstv_AvailableRowTypes.getItems();
//		                   int draggedIdx = items.indexOf(db.getString());
//	                       int thisIdx = items.indexOf(cell.getItem());
//	                       items.set(draggedIdx, cell.getItem());
//	                       items.set(thisIdx, db.getString());
//	                       List<String> itemscopy = new ArrayList<>(lstv_AvailableRowTypes.getItems());
//	                       lstv_AvailableRowTypes.getItems().setAll(itemscopy);                      
//		                   event.setDropCompleted(true);
//		                   drag.set(null);
//	               }
               } 
               else 
               {
                   event.setDropCompleted(false);
               }
           });	          

           //A cell can't be in both lists at the same time.
           cell.setOnDragDone(event -> 
           {
	        	   if (second.getItems().contains(cell.getItem()) && first.getItems().contains(cell.getItem())) 
	        	   {
	        		    first.getItems().remove(cell.getItem());
	        	   		second.getItems().remove("");
	        	   	}	        	  
           });	          
           
           return cell ;	          
        });
	   	
	    	//#######################################################
	    //################# Second list view  ###################
	    //#######################################################
	    	
	    	second.setCellFactory(lv -> 
	    	{
           ListCell<String> cell = new ListCell<String>()
           {  
               @Override
               public void updateItem(String item , boolean empty) 
               {
                   super.updateItem(item, empty);
                   setText(item);
               }
           };
           
           cell.setOnDragDetected(event -> 
           {
               if (! cell.isEmpty()) 
               {
                   Dragboard db = cell.startDragAndDrop(TransferMode.MOVE);
                   ClipboardContent cbc = new ClipboardContent();
                   cbc.putString(cell.getItem());
                   db.setContent(cbc);
                   drag.set(cell);
               }
               if(first.getItems().size() == 0)
               {
               	   first.getItems().add("");
               	   
               }
           });

           cell.setOnDragOver(event -> {
               Dragboard db = event.getDragboard();
               if (event.getGestureSource() != first && db.hasString()) 
               {
                   event.acceptTransferModes(TransferMode.MOVE);
               }
           });
           	           
           cell.setOnDragEntered(event -> 
           {
        	   if(type == DragType.SORT)
	            if (event.getGestureSource() != cell &&
	                    event.getDragboard().hasString()) 
	            {			                
	                Dragboard db = event.getDragboard();           
	                ObservableList<String> items = second.getItems();
	                ListCell<String> sourceCell = drag.get();
	                int draggedIdx = items.indexOf(db.getString()), thisIdx = items.indexOf(cell.getItem());
	                
	                if(thisIdx > -1 && thisIdx < items.size() && second.getItems().contains(sourceCell.getItem()))
	                {
	                
		                items.set(draggedIdx, cell.getItem());
		                items.set(thisIdx, db.getString());
		                
		                second.getSelectionModel().select(-1);
		                second.getSelectionModel().select(thisIdx);               
	                }
	                event.consume();
	            }
	        });
           
//
//	            cell.setOnDragExited(event -> {
//	                if (event.getGestureSource() != cell &&
//	                        event.getDragboard().hasString()) {
//	                    cell.setOpacity(1);
//	                }
//	            });
           
           cell.setOnDragDropped(event -> 
           {	        	  
               Dragboard db = event.getDragboard();
               if (db.hasString() && drag.get() != null) 
               {
                   ListCell<String> sourceCell = drag.get();	                  	                  
                   if(first.getItems().contains(sourceCell.getItem()))
                   {	                	   
                	   second.getItems().add(sourceCell.getItem());		                	   	                	   	                   
                	   event.setDropCompleted(true);
                	   drag.set(null);
                   }
//	                   if (db.hasString()) {	                   
//		                   ObservableList<String> items = lstv_SelectedRowTypes.getItems();
//		                   int draggedIdx = items.indexOf(db.getString());
//	                       int thisIdx = items.indexOf(cell.getItem());
//	                       items.set(draggedIdx, cell.getItem());
//	                       items.set(thisIdx, db.getString());
//	                       List<String> itemscopy = new ArrayList<>(lstv_SelectedRowTypes.getItems());
//	                       lstv_SelectedRowTypes.getItems().setAll(itemscopy);	                  
//		                   event.setDropCompleted(true);
//		                   drag.set(null);
//	                   }
               } else {
                   event.setDropCompleted(false);
               }
           });
           
           cell.setOnDragDone(event -> 
           {
	        	   if (first.getItems().contains(cell.getItem()) && second.getItems().contains(cell.getItem())) 
	        	   {
	        		   second.getItems().remove(cell.getItem());
	        		   first.getItems().remove("");
	        	   }	        	   
           });
           
           return cell ;
        });
	}
    		
}