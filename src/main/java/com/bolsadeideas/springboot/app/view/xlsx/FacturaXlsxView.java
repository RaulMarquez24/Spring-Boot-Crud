package com.bolsadeideas.springboot.app.view.xlsx;

import java.util.Locale;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.view.document.AbstractXlsxView;

import com.bolsadeideas.springboot.app.models.entity.Factura;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component("factura/ver.xlsx")
public class FacturaXlsxView extends AbstractXlsxView {

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private LocaleResolver localeResolver;

    @Override
    protected void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        Locale locale = localeResolver.resolveLocale(request);

        Factura factura = (Factura) model.get("factura");

        Sheet sheet = workbook.createSheet(String.format(messageSource.getMessage("text.factura.txt", null, locale) + " " + factura.getCliente().getNombre() + " " + factura.getCliente().getApellido()));

        // Datos cliente
        Row row = sheet.createRow(0);
        Cell cell = row.createCell(0);
        cell.setCellValue(messageSource.getMessage("text.factura.ver.datos.cliente", null, locale));

        sheet.createRow(1).createCell(0).setCellValue(factura.getCliente().getNombre() + " " + factura.getCliente().getApellido());
        sheet.createRow(2).createCell(0).setCellValue(factura.getCliente().getEmail());

        // Datos factura
        sheet.createRow(4).createCell(0).setCellValue(messageSource.getMessage("text.factura.ver.datos.factura", null, locale));
        sheet.createRow(5).createCell(0).setCellValue(messageSource.getMessage("text.cliente.factura.folio", null, locale)+": " + factura.getId());
        sheet.createRow(6).createCell(0).setCellValue(messageSource.getMessage("text.cliente.factura.descripcion", null, locale)+": " + factura.getDescripcion());
        sheet.createRow(7).createCell(0).setCellValue(messageSource.getMessage("text.cliente.factura.fecha", null, locale)+": " + factura.getCreateAt());
        


    }

}
