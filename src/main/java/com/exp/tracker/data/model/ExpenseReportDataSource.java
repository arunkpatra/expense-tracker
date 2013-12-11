/*
 * Copyright 2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.exp.tracker.data.model;

import java.util.List;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

public class ExpenseReportDataSource implements JRDataSource {

	private int index = -1;
	private int numberOfRecords = 0;
	List<ExpenseReportDataBean> erdbList;
	
	public ExpenseReportDataSource() {
	}
	
	public ExpenseReportDataSource(List<ExpenseReportDataBean> erdbList) {
		numberOfRecords = erdbList.size();
		this.erdbList = erdbList;
	}
	
	public Object getFieldValue(JRField field) throws JRException {
		ExpenseReportDataBean erdb = erdbList.get(index);
		Object value = null;		
		String fieldName = field.getName();
		
		if ("expenseId".equals(fieldName))
		{
			value = erdb.getExpenseId();
		}
		else if ("expenseDate".equals(fieldName))
		{
			value = erdb.getExpenseDate();
		}
		else if ("expenseAmount".equals(fieldName))
		{
			value = erdb.getExpenseAmount();
		}
		else if ("expenseDescription".equals(fieldName))
		{
			value = erdb.getExpenseDescription();
		}
		else if ("paidBy".equals(fieldName))
		{
			value = erdb.getPaidBy();
		}
		else if ("userShareAmount".equals(fieldName))
		{
			value = erdb.getUserShareAmount();
		}
		else if ("userName".equals(fieldName))
		{
			value = erdb.getUserName();
		}
		return value;
	}

	public boolean next() throws JRException {
		index++;
		return (index < numberOfRecords);
	}

}
