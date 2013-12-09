package com.exp.tracker.data.model;

import java.util.List;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

public class SettlementGenerationDataSource implements JRDataSource {

	private int index = -1;
	private int numberOfRecords = 0;
	private List<UserSettlementBean> usbList = null;
	
	public SettlementGenerationDataSource() {
	}
	
	public SettlementGenerationDataSource(SettlementBean sb) {
		numberOfRecords = sb.getUserSettlementList().size();
		usbList = sb.getUserSettlementList();
	}
	/**
	 * Returns a field value
	 * 
	 */
	public Object getFieldValue(JRField field) throws JRException {
		UserSettlementBean usb = usbList.get(index);
		Object value = null;		
		String fieldName = field.getName();
		
		if ("amount".equals(fieldName))
		{
			value = usb.getAmount();
		}
		else if ("userName".equals(fieldName))
		{
			value = usb.getUserName();
		}
		else if ("userShare".equals(fieldName))
		{
			value = usb.getUserShare();
		}
		else if ("userPaid".equals(fieldName))
		{
			value = usb.getUserPaid();
		}
		return value;
	}

	/**
	 * Returns true is more records exist to process
	 */
	public boolean next() throws JRException {
		index++;
		return (index < numberOfRecords);
	}

}
