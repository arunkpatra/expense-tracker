package com.exp.tracker.app.action;

import java.io.OutputStream;
import java.io.Serializable;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.webflow.action.AbstractAction;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;

public class PrintSettlementReportAction extends AbstractAction implements
        Serializable
{

    /**
     * The logger.
     */
    private static final Log logger = LogFactory
            .getLog(PrintSettlementReportAction.class);
    private static final long serialVersionUID = 507509485586632380L;
    private byte[] pdfBytes = null;

    @Override
    protected Event doExecute(RequestContext context) throws Exception
    {
        if (logger.isDebugEnabled()) {
            logger.debug("In PrintSettlementReportAction.doExecute()");
        }
        HttpServletResponse response = (HttpServletResponse) context
                .getExternalContext().getNativeResponse();
        response.setContentType("application/octet-stream");
        response.setContentLength(pdfBytes.length);

        OutputStream ouputStream = response.getOutputStream();
        ouputStream.write(pdfBytes);
        ouputStream.flush();
        ouputStream.close();

        return success();
    }

    public void setReportGenerationResult(byte[] pdfBytes)
    {
        this.pdfBytes = pdfBytes;
    }

}
