package com.exp.tracker.services.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.servlet.ServletContext;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.webflow.execution.RequestContext;

import com.exp.tracker.data.entities.ReportEntity;
import com.exp.tracker.data.entities.SettlementEntity;
import com.exp.tracker.data.model.ExpenseReportDataBean;
import com.exp.tracker.data.model.ExpenseReportDataSource;
import com.exp.tracker.data.model.SettlementBean;
import com.exp.tracker.data.model.SettlementGenerationDataSource;
import com.exp.tracker.services.api.EmailService;
import com.exp.tracker.services.api.ReportGenerationService;

/**
 * A JPA based report generation service.
 * 
 * @author Arun Patra
 *
 */
@Service("reportService")
@Repository
public class JasperReportGenerationService implements ReportGenerationService
{

    /**
     * The logger.
     */
    private static final Log logger = LogFactory.getLog(JasperReportGenerationService.class);
    /**
     * The settlement report file name.
     */
    private static final String SETTLEMENT_REPORT_FILE_NAME = "/reports/SettlementSubReport.jrxml";
    /**
     * The expense report file name,
     */
    private static final String EXPENSE_REPORT_FILE_NAME = "/reports/ExpenseReport.jrxml";
    /**
     * The email service.
     */
    private EmailService emailService;
    /**
     * The entity manager.
     */
    private EntityManager em;

    @PersistenceContext
    public void setEntityManager(EntityManager entityManager)
    {
        this.em = entityManager;
    }

    /**
     * This is a pretty heavyweight process. So, we will execute it
     * asynchronously
     */
    @Transactional
    @Async
    public void generateSettlementReport(Long sid, RequestContext ctx)
    {
        byte[] settlementPdfBytes = genSettlementReportInternal(sid, ctx);

        // chain expense report
        byte[] expensePdfBytes = genExpenseReportInternal(sid, ctx);
        // chain email send
        emailService.sendSettlementEmail(sid, settlementPdfBytes,
                expensePdfBytes);
        System.out.println("Settlement process ended...");
    }

    private byte[] genSettlementReportInternal(Long sid, RequestContext ctx)
    {
        byte[] pdfBytes = null;
        // Get hold of the servlet context
        ServletContext context = (ServletContext) ctx.getExternalContext()
                .getNativeContext();
        // input file stream
        InputStream isJrxmlFile = null;
        // compiler output stream
        ByteArrayOutputStream compilerOutputStream = new ByteArrayOutputStream();
        // fill input stream
        ByteArrayInputStream fillInputStream = null;

        try {
            // get the input file contents as a stream
            isJrxmlFile = new FileInputStream(
                    context.getRealPath(SETTLEMENT_REPORT_FILE_NAME));
            JasperCompileManager.compileReportToStream(isJrxmlFile,
                    compilerOutputStream);
            // compile completed
            // get ready to fill
            // get data to be filled
            JRDataSource fillDataSource = getSettlementDataSource(sid);
            Map<String, Object> filParams = getSettlementReportParamMap(sid);
            // fill now
            fillInputStream = new ByteArrayInputStream(
                    compilerOutputStream.toByteArray());

            JasperPrint jasperPrint = JasperFillManager.fillReport(
                    fillInputStream, filParams, fillDataSource);
            // now export to PDF
            pdfBytes = JasperExportManager.exportReportToPdf(jasperPrint);
            // now save file
            saveSettlementPdfToDataBase(sid, pdfBytes);

        } catch (JRException e) {
            System.out.println("Jasper Error... while creating expense report");
            e.printStackTrace();
        } catch (FileNotFoundException fnfe) {
            System.out.println("File not found...");
        }

        System.out.println("Settlement PDF created and save to DB");
        return pdfBytes;
    }

    private byte[] genExpenseReportInternal(Long sid, RequestContext ctx)
    {
        byte[] pdfBytes = null;
        // Get hold of the servlet context
        ServletContext context = (ServletContext) ctx.getExternalContext()
                .getNativeContext();
        // input file stream
        InputStream isJrxmlFile = null;
        // compiler output stream
        ByteArrayOutputStream compilerOutputStream = new ByteArrayOutputStream();
        // fill input stream
        ByteArrayInputStream fillInputStream = null;

        try {
            // get the input file contents as a stream
            isJrxmlFile = new FileInputStream(
                    context.getRealPath(EXPENSE_REPORT_FILE_NAME));
            JasperCompileManager.compileReportToStream(isJrxmlFile,
                    compilerOutputStream);
            // compile completed
            // get ready to fill
            // get data to be filled
            JRDataSource fillDataSource = getExpenseDataSource(sid);
            Map<String, Object> filParams = getExpenseReportParamMap(sid);
            // fill now
            fillInputStream = new ByteArrayInputStream(
                    compilerOutputStream.toByteArray());

            JasperPrint jasperPrint = JasperFillManager.fillReport(
                    fillInputStream, filParams, fillDataSource);
            // now export to PDF
            pdfBytes = JasperExportManager.exportReportToPdf(jasperPrint);
            // now save file
            saveExpensePdfToDataBase(sid, pdfBytes);

        } catch (JRException e) {
            System.out.println("Jasper Error... while creating expense report");
            e.printStackTrace();
        } catch (FileNotFoundException fnfe) {
            System.out.println("Expense Report File not found...");
        }
        System.out.println("*** Expense report generated and saved to DB");
        return pdfBytes;
    }

    @Transactional
    public void generateExpenseReport(Long sid, RequestContext ctx)
    {
        genExpenseReportInternal(sid, ctx);
    }

    private void saveSettlementPdfToDataBase(Long sid, byte[] pdfBytes)
    {
        ReportEntity re = new ReportEntity();
        re.setSettlementId(sid);
        re.setReportContent(pdfBytes);
        re.setReportType(ReportEntity.PDF_REPORT_TYPE);
        re.setReportName(ReportEntity.SETTLEMENT_REPORT);
        Calendar calendar = Calendar.getInstance();
        re.setCreatedDate(calendar.getTime());
        em.persist(re);

        System.out.println("Settlement Report PDF saved to database");
    }

    private void saveExpensePdfToDataBase(Long sid, byte[] pdfBytes)
    {
        ReportEntity re = new ReportEntity();
        re.setSettlementId(sid);
        re.setReportContent(pdfBytes);
        re.setReportType(ReportEntity.PDF_REPORT_TYPE);
        re.setReportName(ReportEntity.EXPENSE_REPORT);
        Calendar calendar = Calendar.getInstance();
        re.setCreatedDate(calendar.getTime());
        em.persist(re);
        System.out.println("Expense Report PDF saved to database");
    }

    private Map<String, Object> getSettlementReportParamMap(Long sid)
    {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        Query queryGetSettlementForId = em
                .createNamedQuery("getSettlementForId");
        queryGetSettlementForId.setParameter("id", sid);
        SettlementEntity se = (SettlementEntity) queryGetSettlementForId
                .getSingleResult();
        paramMap.put("reportTitle", "Settlement Report");
        paramMap.put("startDate", se.getStartDate());
        paramMap.put("endDate", se.getEndDate());
        paramMap.put("volume", se.getVolume());
        return paramMap;
    }

    private Map<String, Object> getExpenseReportParamMap(Long sid)
    {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        Query queryGetSettlementForId = em
                .createNamedQuery("getSettlementForId");
        queryGetSettlementForId.setParameter("id", sid);
        SettlementEntity se = (SettlementEntity) queryGetSettlementForId
                .getSingleResult();
        paramMap.put("reportTitle", "Expense Report");
        paramMap.put("startDate", se.getStartDate());
        paramMap.put("endDate", se.getEndDate());
        paramMap.put("totalVolume", se.getVolume());
        return paramMap;
    }

    private JRDataSource getSettlementDataSource(Long sid)
    {
        // get data from back end
        Query queryGetSettlementForId = em
                .createNamedQuery("getSettlementForId");
        queryGetSettlementForId.setParameter("id", sid);
        SettlementEntity se = (SettlementEntity) queryGetSettlementForId
                .getSingleResult();
        // wrap it up
        SettlementBean sb = new SettlementBean(se);
        // create data source
        SettlementGenerationDataSource sgds = new SettlementGenerationDataSource(
                sb);
        // return data
        return sgds;
    }

    @SuppressWarnings("unchecked")
    private JRDataSource getExpenseDataSource(Long sid)
    {
        // get data from back end
        Query queryGetExpensesForSettlementId = em
                .createNamedQuery("getExpensesForSettlementId");
        queryGetExpensesForSettlementId.setParameter("settlementId", sid);
        List<ExpenseReportDataBean> erdbList = new ArrayList<ExpenseReportDataBean>();
        Collection<Object[]> results = queryGetExpensesForSettlementId
                .getResultList();
        for (Object[] oa : results) {
            ExpenseReportDataBean erdb = new ExpenseReportDataBean();
            // Each object is a list
            erdb.setExpenseId(((Long) oa[0]).longValue());
            erdb.setExpenseAmount(((Float) oa[1]).floatValue());
            erdb.setExpenseDate(((Date) oa[2]));
            erdb.setExpenseDescription(((String) oa[3]));
            erdb.setPaidBy(((String) oa[4]));
            erdb.setUserName(((String) oa[5]));
            erdb.setUserShareAmount(((Float) oa[6]).floatValue());
            // set in list
            erdbList.add(erdb);
        }
        ExpenseReportDataSource erds = new ExpenseReportDataSource(erdbList);
        return erds;
    }

    public byte[] getReportForSettlement(Long sid, String reportName)
    {
        Query queryGetReport = em.createNamedQuery("getReport");
        queryGetReport.setParameter("sid", sid);
        queryGetReport.setParameter("reportName", reportName);
        ReportEntity re = (ReportEntity) queryGetReport.getSingleResult();
        return re.getReportContent();
    }

    public void setEmailService(EmailService emailService)
    {
        this.emailService = emailService;
    }
}
