/**
 * Copyright 2013 Arun K Patra

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */
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
