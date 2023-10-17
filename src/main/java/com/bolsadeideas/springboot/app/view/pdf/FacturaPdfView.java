package com.bolsadeideas.springboot.app.view.pdf;

import java.awt.Color;
import java.util.Locale;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.view.document.AbstractPdfView;

import com.bolsadeideas.springboot.app.models.entity.Factura;
import com.bolsadeideas.springboot.app.models.entity.ItemFactura;
import com.lowagie.text.Document;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component("factura/ver.pdf")
public class FacturaPdfView extends AbstractPdfView {

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private LocaleResolver localeResolver;

    @Override
    protected void buildPdfDocument(Map<String, Object> model, Document document, PdfWriter writer,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        Locale locale = localeResolver.resolveLocale(request);

        Factura factura = (Factura) model.get("factura");

        PdfPTable tabla1 = new PdfPTable(1);
        tabla1.setSpacingAfter(20);

        PdfPCell cell = null;

        cell = new PdfPCell(new Phrase(messageSource.getMessage("text.factura.ver.datos.cliente", null, locale)));
        cell.setBackgroundColor(new Color(184, 218, 255));
        cell.setPadding(8f);
        tabla1.addCell(cell);

        tabla1.addCell(factura.getCliente().getNombre() + " " + factura.getCliente().getApellido());
        tabla1.addCell(factura.getCliente().getEmail());

        PdfPTable tabla2 = new PdfPTable(1);
        tabla2.setSpacingAfter(20);

        cell = new PdfPCell(new Phrase(messageSource.getMessage("text.factura.ver.datos.factura", null, locale)));
        cell.setBackgroundColor(new Color(195, 230, 203));
        cell.setPadding(8f);
        tabla2.addCell(cell);

        tabla2.addCell(messageSource.getMessage("text.cliente.factura.folio", null, locale)+": " + factura.getId());
        tabla2.addCell(messageSource.getMessage("text.cliente.factura.descripcion", null, locale)+": " + factura.getDescripcion());
        tabla2.addCell(messageSource.getMessage("text.cliente.factura.fecha", null, locale)+": " + factura.getCreateAt());

        PdfPTable tabla3 = new PdfPTable(4);
        tabla3.setWidths(new float[] {3.5f, 1, 1, 1});
        tabla3.addCell(messageSource.getMessage("text.factura.form.item.nombre", null, locale));
        tabla3.addCell(messageSource.getMessage("text.factura.form.item.precio", null, locale));
        tabla3.addCell(messageSource.getMessage("text.factura.form.item.cantidad", null, locale));
        tabla3.addCell(messageSource.getMessage("text.factura.form.item.total", null, locale));

        for (ItemFactura item: factura.getItems()) {
            tabla3.addCell(item.getProducto().getNombre());
            tabla3.addCell(item.getProducto().getPrecio().toString()+"€");

            cell = new PdfPCell(new Phrase(item.getCantidad().toString()));
            cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
            tabla3.addCell(cell);

            tabla3.addCell(item.calcularImporte().toString()+"€");
        }

        cell = new PdfPCell(new Phrase(messageSource.getMessage("text.factura.form.item.total", null, locale)+": "));
        cell.setColspan(3);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
        tabla3.addCell(cell);
        tabla3.addCell(factura.getTotal().toString()+"€");

        document.add(tabla1);
        document.add(tabla2);
        document.add(tabla3);

    }

}
