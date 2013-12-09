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
