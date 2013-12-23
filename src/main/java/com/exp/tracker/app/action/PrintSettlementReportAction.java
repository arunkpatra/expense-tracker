package com.exp.tracker.app.action;

import java.io.OutputStream;
import java.io.Serializable;

import javax.servlet.http.HttpServletResponse;

import org.springframework.webflow.action.AbstractAction;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;

public class PrintSettlementReportAction extends AbstractAction implements Serializable{

	private static final long serialVersionUID = 507509485586632380L;
	private byte[] pdfBytes = null;
	@Override
	protected Event doExecute(RequestContext context) throws Exception {
		System.out.println("in action");
		HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getNativeResponse();
		response.setContentType("application/octet-stream");
		response.setContentLength(pdfBytes.length);
		
//		response.getOutputStream().write(pdfBytes);
		
		OutputStream ouputStream = response.getOutputStream();
		ouputStream.write(pdfBytes);
		ouputStream.flush();
		ouputStream.close();
		
//		context.getExternalContext().recordResponseComplete();
		
		return success();
//		ObjectOutputStream oos = new ObjectOutputStream(ouputStream);
//		oos.write(pdfBytes);
//		oos.flush();
//		oos.close();
//		
//		ouputStream.flush();
//		ouputStream.close();
		
		
	}
	
	public void setReportGenerationResult(byte[] pdfBytes) {
		this.pdfBytes = pdfBytes;
	}

}
