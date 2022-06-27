/*
 * Copyright (c) 2011, 2013, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */
package com.kostikiadis.charts;

import java.util.Collections;
import java.util.List;

import javafx.collections.ListChangeListener.Change;
import javafx.collections.ObservableList;

@SuppressWarnings("rawtypes")
public abstract class NonIterableChange extends Change {

    private final int from;
    private final int to;
    private boolean invalid = true;

    @SuppressWarnings("unchecked")
	protected NonIterableChange(int from, int to, ObservableList list) {
        super(list);
        this.from = from;
        this.to = to;
    }

    @Override
    public int getFrom() {
        checkState();
        return from;
    }

    @Override
    public int getTo() {
        checkState();
        return to;
    }

    private static final int[] EMPTY_PERM = new int[0];

    @Override
    protected int[] getPermutation() {
        checkState();
        return EMPTY_PERM;
    }

    @Override
    public boolean next() {
        if (invalid) {
            invalid = false;
            return true;
        }
        return false;
    }

    @Override
    public void reset() {
        invalid = true;
    }

    public void checkState() {
        if (invalid) {
            throw new IllegalStateException("Invalid Change state: next() must be called before inspecting the Change.");
        }
    }

    @Override
    public String toString() {
        boolean oldInvalid = invalid;
        invalid = false;
        String ret;
        if (wasPermutated()) {
            ret = ChangeHelper.permChangeToString(getPermutation());
        } else if (wasUpdated()) {
            ret = ChangeHelper.updateChangeToString(from, to);
        } else {
            ret = ChangeHelper.addRemoveChangeToString(from, to, getList(), getRemoved());
        }
        invalid = oldInvalid;
        return "{ " + ret + " }";
    }

    public static class GenericAddRemoveChange extends NonIterableChange {

        private final List removed;

        public GenericAddRemoveChange(int from, int to, List removed, ObservableList list) {
            super(from, to, list);
            this.removed = removed;
        }

        @Override
        public List getRemoved() {
            checkState();
            return removed;
        }

    }

    public static class SimpleRemovedChange<E> extends NonIterableChange {

        private final List removed;
        public SimpleRemovedChange(int from, int to, E removed, ObservableList list) {
            super(from, to, list);
            this.removed = Collections.singletonList(removed);
        }

        @Override
        public boolean wasRemoved() {
            checkState();
            return true;
        }

        @Override
        public List getRemoved() {
            checkState();
            return removed;
        }

    }

    public static class SimpleAddChange extends NonIterableChange {

        public SimpleAddChange(int from, int to, ObservableList list) {
            super(from, to, list);
        }

        @Override
        public boolean wasRemoved() {
            checkState();
            return false;
        }

        @Override
        public List getRemoved() {
            checkState();
            return Collections.emptyList();
        }

    }

    public static class SimplePermutationChange extends NonIterableChange{

        private final int[] permutation;

        public SimplePermutationChange(int from, int to, int[] permutation, ObservableList list) {
            super(from, to, list);
            this.permutation = permutation;
        }


        @Override
        public List getRemoved() {
            checkState();
            return Collections.emptyList();
        }

        @Override
        protected int[] getPermutation() {
            checkState();
            return permutation;
        }
    }

    public static class SimpleUpdateChange extends NonIterableChange{

        public SimpleUpdateChange(int position, ObservableList list) {
            this(position, position + 1, list);
        }

        public SimpleUpdateChange(int from, int to, ObservableList list) {
            super(from, to, list);
        }

        @Override
        public List getRemoved() {
            return Collections.emptyList();
        }

        @Override
        public boolean wasUpdated() {
            return true;
        }

    }

}