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
package org.opentdk.gui.controls;

import java.util.List;
import java.util.stream.Collectors;

import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.TreeItem;
import javafx.util.Callback;

/**
 * A helper class to get the number of tree items of a tree table.
 */
@Deprecated
public class RecursiveTreeItem<T> extends TreeItem<T> {

    private Callback<T, ObservableList<T>> childrenFactory;

    private Callback<T, Node> graphicsFactory;

    public RecursiveTreeItem(Callback<T, ObservableList<T>> childrenFactory){
        this(null, childrenFactory);
    }

    public RecursiveTreeItem(final T value, Callback<T, ObservableList<T>> childrenFactory){
        this(value, (item) -> null, childrenFactory);
    }

    public RecursiveTreeItem(final T value, Callback<T, Node> graphicsFactory, Callback<T, ObservableList<T>> childrenFactory){
        super(value, graphicsFactory.call(value));

        this.graphicsFactory = graphicsFactory;
        this.childrenFactory = childrenFactory;

        if(value != null) {
            addChildrenListener(value);
        }

        valueProperty().addListener((obs, oldValue, newValue)->{
            if(newValue != null){
                addChildrenListener(newValue);
            }
        });

        this.setExpanded(true);
    }

    private void addChildrenListener(T value){
        final ObservableList<T> children = childrenFactory.call(value);

        /* Get children of a tree item with LAMBDA expression */
        
        children.forEach(child ->  RecursiveTreeItem.this.getChildren().add(
            new RecursiveTreeItem<>(child, this.graphicsFactory, childrenFactory)));

        children.addListener((ListChangeListener<T>) change -> {
            while(change.next()){

                if(change.wasAdded()){
                    change.getAddedSubList().forEach(t-> RecursiveTreeItem.this.getChildren().add(
                        new RecursiveTreeItem<>(t, this.graphicsFactory, childrenFactory)));
                }

                if(change.wasRemoved()){
                    change.getRemoved().forEach(t->{
                        final List<TreeItem<T>> itemsToRemove = RecursiveTreeItem.this
                                .getChildren()
                                .stream()
                                .filter(treeItem -> treeItem.getValue().equals(t))
                                .collect(Collectors.toList());

                        RecursiveTreeItem.this.getChildren().removeAll(itemsToRemove);
                    });
                }

            }
        });
    }
}
