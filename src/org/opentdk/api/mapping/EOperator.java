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
package org.opentdk.api.mapping;

import org.opentdk.api.dispatcher.*;

public class EOperator extends BaseDispatcher {
	public static final BaseDispatchComponent CONTAINS = new BaseDispatchComponent(EOperator.class, "CONTAINS", "like");
	public static final BaseDispatchComponent CONTAINS_DATE = new BaseDispatchComponent(EOperator.class, "CONTAINS_DATE", "");
	public static final BaseDispatchComponent CONTAINS_DATE_AFTER = new BaseDispatchComponent(EOperator.class, "CONTAINS_DATE_AFTER", "");
	public static final BaseDispatchComponent CONTAINS_DATE_BEFORE = new BaseDispatchComponent(EOperator.class, "CONTAINS_DATE_BEFORE", "");
	public static final BaseDispatchComponent CONTAINS_IGNORE_CASE = new BaseDispatchComponent(EOperator.class, "CONTAINS_IGNORE_CASE", "");
	public static final BaseDispatchComponent DATE_AFTER = new BaseDispatchComponent(EOperator.class, "DATE_AFTER", "");
	public static final BaseDispatchComponent DATE_BEFORE = new BaseDispatchComponent(EOperator.class, "DATE_BEFORE", "");
	public static final BaseDispatchComponent DATE_EQUALS = new BaseDispatchComponent(EOperator.class, "DATE_EQUALS", "");
	public static final BaseDispatchComponent ENDS_WITH = new BaseDispatchComponent(EOperator.class, "ENDS_WITH", "");
	public static final BaseDispatchComponent ENDS_WITH_IGNORE_CASE = new BaseDispatchComponent(EOperator.class, "ENDS_WITH_IGNORE_CASE", "");
	public static final BaseDispatchComponent EQUALS = new BaseDispatchComponent(EOperator.class, "EQUALS", "=");
	public static final BaseDispatchComponent EQUALS_IGNORE_CASE = new BaseDispatchComponent(EOperator.class, "EQUALS_IGNORE_CASE", "=");
	public static final BaseDispatchComponent GREATER_THAN = new BaseDispatchComponent(EOperator.class, "GRATER_THAN", ">");
	public static final BaseDispatchComponent GREATER_OR_EQUAL_THAN = new BaseDispatchComponent(EOperator.class, "GRATER_OR_EQUAL_THAN", ">=");
	public static final BaseDispatchComponent LESS_THAN = new BaseDispatchComponent(EOperator.class, "LESS_THAN", "<");
	public static final BaseDispatchComponent LESS_OR_EQUAL_THAN = new BaseDispatchComponent(EOperator.class, "LESS_OR_EQUAL_THAN", "<=");
	public static final BaseDispatchComponent NOT_EQUALS = new BaseDispatchComponent(EOperator.class, "NOT_EQUALS", "<>");
	public static final BaseDispatchComponent NOT_EQUALS_IGNORE_CASE = new BaseDispatchComponent(EOperator.class, "NOT_EQUALS_IGNORE_CASE", "<>");
	public static final BaseDispatchComponent STARTS_WITH = new BaseDispatchComponent(EOperator.class, "STARTS_WITH", "");
	public static final BaseDispatchComponent STARTS_WITH_IGNORE_CASE = new BaseDispatchComponent(EOperator.class, "STARTS_WITH_IGNORE_CASE", "");
	public static final BaseDispatchComponent AND = new BaseDispatchComponent(EOperator.class, "AND", "AND");
	public static final BaseDispatchComponent OR = new BaseDispatchComponent(EOperator.class, "OR", "OR");
	public static final BaseDispatchComponent IN = new BaseDispatchComponent(EOperator.class, "IN", "IN");
	public static final BaseDispatchComponent BETWEEN = new BaseDispatchComponent(EOperator.class, "BETWEEN", "BETWEEN");
}