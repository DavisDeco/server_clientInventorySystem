/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package daviscahtech_assetmanagement.Utils;

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.*;

/**
 *
 * @author davis
 */
public class HeaderFooterPageEvent extends PdfPageEventHelper{
    Font bfNormal = new Font(Font.FontFamily.TIMES_ROMAN, 8);
    
    public  void onEndPage(PdfWriter writer,Document document){
    
        ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_CENTER,
                new Phrase("Daviscah Tech Ltd || www.daviscahtech.com",bfNormal), 105, 30, 0);
        
        ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_CENTER,
                new Phrase("page "+ document.getPageNumber(),bfNormal), 550, 30, 0);
    }
    
}// end of page
