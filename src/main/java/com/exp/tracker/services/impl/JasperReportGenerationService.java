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
    public static final String SETTLEMENT_REPORT_FILE_NAME = "/reports/SettlementSubReport.jrxml";
    /**
     * The expense report file name,
     */
    public static final String EXPENSE_REPORT_FILE_NAME = "/reports/ExpenseReport.jrxml";
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
    	// Get hold of the servlet context
        ServletContext context = (ServletContext) ctx.getExternalContext()
                .getNativeContext();
        String settlementReportTemplatePath = context.getRealPath(SETTLEMENT_REPORT_FILE_NAME);
        String expenseReportTemplatePath = context.getRealPath(EXPENSE_REPORT_FILE_NAME);
        
        byte[] settlementPdfBytes = genSettlementReportInternal(sid, settlementReportTemplatePath);

        // chain expense report
        byte[] expensePdfBytes = genExpenseReportInternal(sid, expenseReportTemplatePath);
        // chain email send
        emailService.sendSettlementEmail(sid, settlementPdfBytes,
                expensePdfBytes);
        logger.info("Settlement process ended");
    }

    public byte[] genSettlementReportInternal(Long sid, String reportTemplatePath)
    {
        byte[] pdfBytes = null;
        // input file stream
        InputStream isJrxmlFile = null;
        // compiler output stream
        ByteArrayOutputStream compilerOutputStream = new ByteArrayOutputStream();
        // fill input stream
        ByteArrayInputStream fillInputStream = null;

        try {
            // get the input file contents as a stream
            isJrxmlFile = new FileInputStream(reportTemplatePath);
            
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
            logger.error("Jasper Error while creating expense report.", e);
        } catch (FileNotFoundException fnfe) {
            logger.error("Report file not found.", fnfe);
        }

        logger.info("Settlement PDF created and save to DB");
        return pdfBytes;
    }

    public byte[] genExpenseReportInternal(Long sid, String reportTemplatePath)
    {
        byte[] pdfBytes = null;
        // input file stream
        InputStream isJrxmlFile = null;
        // compiler output stream
        ByteArrayOutputStream compilerOutputStream = new ByteArrayOutputStream();
        // fill input stream
        ByteArrayInputStream fillInputStream = null;

        try {
            // get the input file contents as a stream
            isJrxmlFile = new FileInputStream(reportTemplatePath);
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
            logger.error("Jasper Error while creating expense report.", e);           
        } catch (FileNotFoundException fnfe) {
            logger.error("Expense Report File not found.", fnfe);               
        }
        logger.info("Expense report generated and saved to DB");      
        return pdfBytes;
    }

    
    public void generateExpenseReport(Long sid, RequestContext ctx)
    {
    	// Get hold of the servlet context
        ServletContext context = (ServletContext) ctx.getExternalContext()
                .getNativeContext();
        String expenseReportTemplatePath = context.getRealPath(EXPENSE_REPORT_FILE_NAME);
        
        genExpenseReportInternal(sid, expenseReportTemplatePath);
    }

    @Transactional
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

        logger.info("Settlement Report PDF saved to database");      
    }

    @Transactional
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
        logger.info("Expense Report PDF saved to database"); 
    }

    @Transactional(readOnly = true)
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

    @Transactional(readOnly = true)
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
    @Transactional(readOnly = true)
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

    @Transactional(readOnly = true)
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
