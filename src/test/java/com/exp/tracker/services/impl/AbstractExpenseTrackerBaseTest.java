package com.exp.tracker.services.impl;

import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring/root-applicationContext.xml")
@ActiveProfiles("demo")
@WebAppConfiguration
@Transactional
public abstract class AbstractExpenseTrackerBaseTest {

}
