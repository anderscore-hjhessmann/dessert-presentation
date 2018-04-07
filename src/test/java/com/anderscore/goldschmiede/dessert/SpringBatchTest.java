package com.anderscore.goldschmiede.dessert;

import de.spricom.dessert.assertions.SliceAssertions;
import de.spricom.dessert.slicing.Slice;
import de.spricom.dessert.slicing.SliceContext;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.Job;
import org.springframework.batch.test.StepRunner;
import org.w3c.dom.Element;

public class SpringBatchTest {

    @Test
    public void testArchitecture() {
        SliceContext sc = new SliceContext();
        Slice springBatch = sc.packageTreeOf("org.springframework.batch");
        Slice springBatchCore = sc.packageTreeOf(Job.class);
        Slice springBatchTest = sc.packageTreeOf(StepRunner.class);
        Slice springBatchInfrastructure =
                springBatch.without(springBatchCore).without(springBatchTest);
        SliceAssertions.assertThat(springBatchInfrastructure).doesNotUse(springBatchCore);
    }

    @Test
    public void testCoreDependencies() {
        SliceContext sc = new SliceContext();
        Slice springBatchCore = sc.packageTreeOf(Job.class);
        SliceAssertions.assertThat(springBatchCore)
                .uses(sc.packageTreeOf("org.springframework"))
                .and(sc.packageTreeOf("java"))
                .and(sc.packageTreeOf("org.apache.commons.logging"))
                .and(sc.packageTreeOf("org.aspectj.lang.annotation"))
                .and(sc.packageTreeOf("org.aopalliance"))
                .and(sc.packageTreeOf("com.thoughtworks.xstream"))
                .and(sc.packageTreeOf("com.fasterxml.jackson"))
                .and(sc.packageTreeOf(Element.class))
                .only();
    }
}
