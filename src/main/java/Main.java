import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import java.io.FileOutputStream;
import java.io.IOException;

public class Main {

    private static final String INPUT = "100 БП.001.578";
    private static final String SRC = "oldPDF.pdf";
    private static final String DEST = "D:/newPDF.pdf";
    private static final int WIDTH = 400;
    private static final int HEIGHT = 100;
    private static final int ABSOLUTEx = 30;
    private static final int ABSOLUTEy = 700;
    private static final String FONT = "src/main/resources/font/Times.ttf";


    public static void main(String[] args) {

        createNewPDF(FONT, INPUT, SRC, DEST, WIDTH, HEIGHT);

    }

    static void createNewPDF(String f, String inputString, String srcPDF,
                             String destPDF, int width, int height) {

        Document document = new Document();
        try {
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(destPDF));
            document.open();
            PdfContentByte cb = writer.getDirectContent();

            PdfReader reader = new PdfReader(srcPDF);
            PdfImportedPage page = writer.getImportedPage(reader, 1);
            document.newPage();
            cb.addTemplate(page, 0, 0);

            BaseFont bf = BaseFont.createFont(f, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            Font font = new Font(bf, 12, 1, BaseColor.RED);
            Paragraph paragraph = new Paragraph(inputString, font);

            document.add(getTextImage(paragraph, cb, WIDTH, HEIGHT, ABSOLUTEx, ABSOLUTEy));
        } catch (DocumentException | IOException e) {
            e.printStackTrace();
        }
        document.close();

    }

    static Image getTextImage(Paragraph paragraph, PdfContentByte cb,
                              int width, int height, int absoluteX, int absoluteY) throws DocumentException {

        PdfTemplate textTemplate = cb.createTemplate(width, height);
        ColumnText columnText = new ColumnText(textTemplate);
        columnText.setSimpleColumn(0, 0, width, height);
        columnText.setRunDirection(PdfWriter.RUN_DIRECTION_DEFAULT);
        columnText.addElement(paragraph);
        columnText.go();
        Image textImg = Image.getInstance(textTemplate);
        textImg.setInterpolation(true);
        textImg.scaleAbsolute(width, height);
        textImg.setRotationDegrees(90);
        textImg.setAbsolutePosition(absoluteX, absoluteY);
        return textImg;
    }

}
