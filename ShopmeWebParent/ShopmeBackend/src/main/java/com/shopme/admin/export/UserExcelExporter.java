package com.shopme.admin.export;

import com.shopme.common.entity.User;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.xssf.usermodel.*;

import java.io.IOException;
import java.util.Date;
import java.util.List;

public class UserExcelExporter extends AbstractExporter {

    private XSSFWorkbook xssfWorkbook;
    private XSSFSheet sheet;
    public UserExcelExporter()
    {
        xssfWorkbook = new XSSFWorkbook();
    }

    private void writeHeaderLine() {
        sheet = xssfWorkbook.createSheet("Users");

        XSSFCellStyle xssfCellStyle = xssfWorkbook.createCellStyle();
        XSSFFont xssfFont = xssfWorkbook.createFont();
        xssfFont.setBold(true);
        xssfFont.setFontHeight(16);
        xssfCellStyle.setFont(xssfFont);

        XSSFRow row = sheet.createRow(0);
        createCell(row, 0, "User ID", xssfCellStyle);
        createCell(row, 1, "Email", xssfCellStyle);
        createCell(row, 2, "First Name", xssfCellStyle);
        createCell(row, 3, "Last Name", xssfCellStyle);
        createCell(row, 4, "Roles", xssfCellStyle);
        createCell(row, 5, "Enabled", xssfCellStyle);

    }

    private void writeDataLine(List<User> users) {
        XSSFCellStyle cellStyle = xssfWorkbook.createCellStyle();
        XSSFFont font = xssfWorkbook.createFont();
        font.setFontHeight(14);
        cellStyle.setFont(font);

        int rowIndex = 1;

        for (User user: users) {
            int columnIndex = 0;

            XSSFRow row = sheet.createRow(rowIndex++);
            createCell(row, columnIndex++, user.getId(), cellStyle);
            createCell(row, columnIndex++, user.getEmail(), cellStyle);
            createCell(row, columnIndex++, user.getFirstName(), cellStyle);
            createCell(row, columnIndex++, user.getLastName(), cellStyle);
            createCell(row, columnIndex++, user.getRoles().toString(), cellStyle);
            createCell(row, columnIndex++, user.isEnabled(), cellStyle);

        }
    }

    private void createCell(XSSFRow row, int columnIndex, Object value, CellStyle cellStyle) {
        XSSFCell cell = row.createCell(columnIndex);
        sheet.autoSizeColumn(columnIndex);

        if(value instanceof Boolean) {
            cell.setCellValue((Boolean) value);
        } else if (value instanceof Integer) {
            cell.setCellValue((Integer) value);
        } else if (value instanceof String) {
            cell.setCellValue((String) value);
        } else if (value instanceof Double) {
            cell.setCellValue((Double) value);
        }  else if (value instanceof Date) {
            cell.setCellValue((Date) value);
        }

        cell.setCellStyle(cellStyle);
    }
    public void export(List<User> users, HttpServletResponse httpServletResponse) throws IOException {
        super.setResponseHeader(httpServletResponse, "application/octet-stream", ".xlsx", "users_");

        writeHeaderLine();
        writeDataLine(users);

        ServletOutputStream servletOutputStream = httpServletResponse.getOutputStream();
        xssfWorkbook.write(servletOutputStream);
        xssfWorkbook.close();
        servletOutputStream.close();

    }
}
