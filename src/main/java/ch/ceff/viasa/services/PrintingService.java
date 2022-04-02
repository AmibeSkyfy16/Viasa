package ch.ceff.viasa.services;

import javafx.print.*;
import javafx.scene.control.Control;
import javafx.scene.layout.Region;
import javafx.scene.transform.Scale;

public class PrintingService{

    public static void print(Region control){

        final PrinterJob printerJob = PrinterJob.createPrinterJob();

        if(printerJob != null){
            if(printerJob.showPrintDialog(control.getScene().getWindow())){
                final Printer printer = printerJob.getPrinter();
                final PageLayout pageLayout = printer.createPageLayout(Paper.A4, PageOrientation.LANDSCAPE, Printer.MarginType.HARDWARE_MINIMUM);

                JobSettings jobSettings = printerJob.getJobSettings();
                jobSettings.setPageRanges(new PageRange(1,1));

                double scaleX = pageLayout.getPrintableWidth() / control.getWidth();
                double scaleY = pageLayout.getPrintableHeight() / control.getHeight();

//                final Scale scale = new Scale(scaleX, scaleY);

//                control.getTransforms().add(scale);

                boolean success = printerJob.printPage(pageLayout, control);
                if(success){
                    printerJob.endJob();
                }
            }
        }


    }

}
