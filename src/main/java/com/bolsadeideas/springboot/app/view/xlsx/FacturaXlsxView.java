package com.bolsadeideas.springboot.app.view.xlsx;

import java.util.Locale;
import java.util.Map;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.view.document.AbstractXlsxView;

import com.bolsadeideas.springboot.app.models.entity.Factura;
import com.bolsadeideas.springboot.app.models.entity.ItemFactura;
import com.bolsadeideas.springboot.app.util.formatter.FormatterText;

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

        String archiveTitle = String
                .format(FormatterText.removeAccents(messageSource.getMessage("text.factura.txt", null, locale)) + "-"
                        + FormatterText.removeAccents(factura.getCliente().getNombre()) + "-"
                        + FormatterText.removeAccents(factura.getCliente().getApellido()) + "-"
                        + factura.getId() + ".xlsx")
                .toLowerCase();

        if (archiveTitle.startsWith("-")) {
            archiveTitle = "invoce" + archiveTitle;
        }

        response.setHeader("Content-Disposition", "attachement; filename=" + archiveTitle);

        Sheet sheet = workbook.createSheet(String.format(messageSource.getMessage("text.factura.txt", null, locale)+ " " + factura.getCliente().getNombre() + " " + factura.getCliente().getApellido()));
        sheet.setColumnWidth(0, 50 * 256);

        // stylos para excel
        CellStyle headerStyleLightBlue = createHeaderStyle(workbook, IndexedColors.LIGHT_TURQUOISE.getIndex());
        CellStyle headerStyleGold = createHeaderStyle(workbook, IndexedColors.GOLD.getIndex());
        CellStyle headerStyleLightGreen = createHeaderStyle(workbook, IndexedColors.LIGHT_GREEN.getIndex());

        CellStyle tbodyStyle = workbook.createCellStyle();
        tbodyStyle.setBorderBottom(BorderStyle.THIN);
        tbodyStyle.setBorderLeft(BorderStyle.THIN);
        tbodyStyle.setBorderRight(BorderStyle.THIN);
        tbodyStyle.setBorderTop(BorderStyle.THIN);

        // Datos cliente
        Row row = sheet.createRow(0);
        Cell cell = row.createCell(0);
        cell.setCellValue(messageSource.getMessage("text.factura.ver.datos.cliente", null, locale));
        cell.setCellStyle(headerStyleLightBlue);

        int startingRowBodyClient = 1; // La fila desde la cual deseas comenzar a crear las celdas
        String[] messagesBodyClient = {
                factura.getCliente().getNombre() + " " + factura.getCliente().getApellido(),
                factura.getCliente().getEmail(),
        };

        for (int i = 0; i < messagesBodyClient.length; i++) {
            Row rowbc = sheet.createRow(startingRowBodyClient + i);
            Cell cellbc = rowbc.createCell(0);
            cellbc.setCellValue(messagesBodyClient[i]);
            cellbc.setCellStyle(tbodyStyle);
        }

        // Datos factura
        Row headerInfoFactura = sheet.createRow(4);
        headerInfoFactura.createCell(0)
                .setCellValue(messageSource.getMessage("text.factura.ver.datos.factura", null, locale));
        headerInfoFactura.getCell(0).setCellStyle(headerStyleLightGreen);

        int startingRowBodyInvoce = 5; // La fila desde la cual deseas comenzar a crear las celdas
        String[] messagesBodyInvoce = {
                messageSource.getMessage("text.cliente.factura.folio", null, locale) + ": " + factura.getId(),
                messageSource.getMessage("text.cliente.factura.descripcion", null, locale) + ": "+ factura.getDescripcion(),
                messageSource.getMessage("text.cliente.factura.fecha", null, locale) + ": " + factura.getCreateAt()
        };

        for (int i = 0; i < messagesBodyInvoce.length; i++) {
            Row rowbf = sheet.createRow(startingRowBodyInvoce + i);
            Cell cellbf = rowbf.createCell(0);
            cellbf.setCellValue(messagesBodyInvoce[i]);
            cellbf.setCellStyle(tbodyStyle);
        }

        // Datos Items
        Row header = sheet.createRow(9);
        header.createCell(0).setCellValue(messageSource.getMessage("text.factura.form.item.nombre", null, locale));
        header.createCell(1).setCellValue(messageSource.getMessage("text.factura.form.item.precio", null, locale));
        header.createCell(2).setCellValue(messageSource.getMessage("text.factura.form.item.cantidad", null, locale));
        header.createCell(3).setCellValue(messageSource.getMessage("text.factura.form.item.total", null, locale));

        header.getCell(0).setCellStyle(headerStyleGold);
        header.getCell(1).setCellStyle(headerStyleGold);
        header.getCell(2).setCellStyle(headerStyleGold);
        header.getCell(3).setCellStyle(headerStyleGold);

        int rownum = 10;

        for (ItemFactura item : factura.getItems()) {
            Row fila = sheet.createRow(rownum++);

            cell = fila.createCell(0);
            cell.setCellValue(item.getProducto().getNombre());
            cell.setCellStyle(tbodyStyle);

            cell = fila.createCell(1);
            cell.setCellValue(item.getProducto().getPrecio());
            cell.setCellStyle(tbodyStyle);

            cell = fila.createCell(2);
            cell.setCellValue(item.getCantidad());
            cell.setCellStyle(tbodyStyle);

            cell = fila.createCell(3);
            cell.setCellValue(item.calcularImporte());
            cell.setCellStyle(tbodyStyle);
        }

        Row filatotal = sheet.createRow(rownum);
        cell = filatotal.createCell(2);
        cell.setCellValue(messageSource.getMessage("text.factura.form.item.total", null, locale) + ": ");
        cell.setCellStyle(tbodyStyle);

        cell = filatotal.createCell(3);
        cell.setCellValue(factura.getTotal());
        cell.setCellStyle(tbodyStyle);

    }

    // Función para crear el estilo con un color específico
    private CellStyle createHeaderStyle(Workbook workbook, short color) {
        CellStyle theaderStyle = workbook.createCellStyle();
        theaderStyle.setBorderBottom(BorderStyle.MEDIUM);
        theaderStyle.setBorderLeft(BorderStyle.MEDIUM);
        theaderStyle.setBorderRight(BorderStyle.MEDIUM);
        theaderStyle.setBorderTop(BorderStyle.MEDIUM);
        theaderStyle.setFillForegroundColor(color);
        theaderStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        return theaderStyle;
    }
}
