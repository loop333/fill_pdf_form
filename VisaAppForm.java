// Press F9 to build & run with atom build plugin
// Press F8 to toggle result screen
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.nio.charset.StandardCharsets;
import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

public class VisaAppForm {

  public static void main(String[] args) throws IOException {

    String fileName = "VisaAppForm.txt";
    File file = new File(fileName);
    FileInputStream fis = new FileInputStream(file);
    InputStreamReader isr = new InputStreamReader(fis, StandardCharsets.UTF_8);
    BufferedReader br = new BufferedReader(isr);

    File fin = null;
    File fout = null;
    String outFileName = null;
    PDDocument doc = null;
    PDPage page = null;
    PDPageContentStream cs = null;

    String line;
    while ((line=br.readLine()) != null){
//         System.out.println(line);
         if (line.length() == 0 || line.charAt(0) == '#') {
           continue;
         }
         String[] parts = line.split("=");
//         System.out.println(parts[0]);
         switch (parts[0]) {
           case "in":
             fin = new File(parts[1]);
             doc = PDDocument.load(fin);
             break;
           case "out":
             outFileName = parts[1];
             break;
           case "page":
             if (cs != null) {
               cs.close();
               cs = null;
             }
             page = doc.getPage(Integer.parseInt(parts[1])-1);
             cs = new PDPageContentStream(doc, page, PDPageContentStream.AppendMode.APPEND, false);
             break;
           default:
             String[] tp = parts[0].split(",");
             cs.beginText();
             cs.newLineAtOffset(Integer.parseInt(tp[0]), Integer.parseInt(tp[1]));
             cs.setFont(PDType1Font.TIMES_ROMAN, Integer.parseInt(tp[2]));
             cs.showText(parts[1]);
             cs.endText();
             break;
         }
    }
    br.close();

    if (doc != null && outFileName != null && cs != null) {
      cs.close();
      doc.save(outFileName);
      doc.close();
    }

  }

}
