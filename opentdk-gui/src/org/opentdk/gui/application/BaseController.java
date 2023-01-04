/* 
 * BSD 2-Clause License
 * 
 * Copyright (c) 2022, LK Test Solutions GmbH
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. 
 */
package org.opentdk.gui.application;

import javafx.scene.Parent;
import javafx.stage.Stage;

/**
 * BaseControlle is the framework template that is used as extend for any new JavaFX controller class,
 * implemented in a JavaFX application.<p>
 * 
 * Sample usage:<br>
 * <pre>
 * import com.lk.javafx.application.BaseController;
 * public class myController extends BaseController{
 * ...
 * }</pre>
 * 
 * @author HWA (LK Test Solutions)
 */
public class BaseController {

    /** 
     * Reference to the runtime stage, created by the FXML and controller.
     */
    private Stage stage;
    
    /** 
     * Reference to the parent FXML element, which is the root element of a FXML.
     */
    private Parent parent;
    
    
    /**
     * Returns an object of type Parent, which represents the root element of a FXML description.
     * 
     * @return	Object of type Parent with the root element of a FXML description.
     * 
     * @see javafx.scene.Parent
     */
    public Parent getParent() {
    	return parent;
    }
    
    /**
     * Assigns an instance of type Parent to the Parent property of this class. The Parent parameter
     * should include an instance of the root element of a FXML description e.g. AnchorPane, BorderPane etc.
     * 
     * @param p		an object of type Parent with the root element of a FXML
     * 
     * @see javafx.scene.Parent
     */
    public void setParent(Parent p) {
    	parent = p;
    }
    
    /**
     * Returns the JavaFX stage, which is the container for all GUI elements, defined in the controller.
     * A stage represents a window of the JavaFX application.
     * The getStage method gives the controller access to the stage, so that every method of the stage
     * can be called from the returned object.<p>
     * 
     * e.g.<br>
     * <code>getStage().getWidth();</code><br>
     * This will return the current width of the window or area, shown by the stage.
     * 
     * @return	the JavaFX stage that was created by the instance of the controller
     * 
     * @see javafx.stage.Stage
     */
    public Stage getStage() {
    	return stage;
    }
    
    /**
     * Sets the value of the stage property of this controller with the associated stage, that will be
     * created when instantiating a controller which is representing a window. The setStage method 
     * should only be called by the <code>showStage</code> method of the class BaseApplication.
     * 
     * @param st	the JavaFX stage that was created by the instance of the controller
     * 
     * @see org.opentdk.gui.application.BaseApplication
     * @see javafx.stage.Stage
     */
    public void setStage(Stage st) {
    	stage = st;
    }
    
    /**
     * Return the root layout, which is the top level JavaFX element, defined in the FXML element of this controller.
     * 
     * @return	Top level JavaFX element, defined in the associated FXML element of this controller class.
     */
    public Parent getRootLayout() {
    	return stage.getScene().getRoot();
    }

}
