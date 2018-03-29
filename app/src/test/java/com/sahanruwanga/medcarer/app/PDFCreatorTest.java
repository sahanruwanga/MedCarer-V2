package com.sahanruwanga.medcarer.app;

import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Created by Sahan Ruwanga on 3/25/2018.
 */
public class PDFCreatorTest {
    @Test
    public void createPdf1() throws Exception {
        PDFCreator pdfCreator = Mockito.mock(PDFCreator.class);
        Mockito.verify(pdfCreator).createPdf();
    }

    @Test
    public void createTableWithColumns() throws Exception {
    }

    @Test
    public void setFooter() throws Exception {

    }

    @Test
    public void setHeader() throws Exception {
    }

    @Test
    public void createPdf() throws Exception {
    }

}