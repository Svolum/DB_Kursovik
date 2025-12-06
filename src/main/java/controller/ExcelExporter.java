package controller;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class ExcelExporter {

    public static boolean exportToExcel(String title, String[] headers, List<Object[]> data) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Сохранить как Excel файл");
        fileChooser.setFileFilter(new FileNameExtensionFilter("Excel Files (*.xlsx)", "xlsx"));
        fileChooser.setSelectedFile(new java.io.File(title + ".xlsx"));

        int userSelection = fileChooser.showSaveDialog(null);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            java.io.File fileToSave = fileChooser.getSelectedFile();
            String filePath = fileToSave.getAbsolutePath();

            // Добавляем расширение .xlsx если его нет
            if (!filePath.toLowerCase().endsWith(".xlsx")) {
                filePath += ".xlsx";
                fileToSave = new java.io.File(filePath);
            }

            // Проверяем, существует ли файл и спрашиваем о перезаписи
            if (fileToSave.exists()) {
                int response = JOptionPane.showConfirmDialog(null,
                        "Файл уже существует. Перезаписать?",
                        "Подтверждение",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE);

                if (response != JOptionPane.YES_OPTION) {
                    return false;
                }
            }

            return createExcelFile(fileToSave, title, headers, data);
        }

        return false;
    }

    private static boolean createExcelFile(java.io.File file, String title,
                                           String[] headers, List<Object[]> data) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet(title);

            // Стили для заголовков
            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setFontHeightInPoints((short) 12);
            headerStyle.setFont(headerFont);
            headerStyle.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            // Создаем строку заголовков
            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
                sheet.autoSizeColumn(i);
            }

            // Заполняем данными
            int rowNum = 1;
            for (Object[] rowData : data) {
                Row row = sheet.createRow(rowNum++);

                for (int i = 0; i < rowData.length; i++) {
                    Cell cell = row.createCell(i);

                    if (rowData[i] != null) {
                        if (rowData[i] instanceof Number) {
                            cell.setCellValue(((Number) rowData[i]).doubleValue());
                        } else if (rowData[i] instanceof java.util.Date) {
                            cell.setCellValue((java.util.Date) rowData[i]);
                        } else {
                            cell.setCellValue(rowData[i].toString());
                        }
                    }
                }
            }

            // Автоподбор ширины столбцов
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            // Сохраняем файл
            try (FileOutputStream fileOut = new FileOutputStream(file)) {
                workbook.write(fileOut);
            }

            JOptionPane.showMessageDialog(null,
                    "Файл успешно сохранен:\n" + file.getAbsolutePath(),
                    "Экспорт завершен",
                    JOptionPane.INFORMATION_MESSAGE);

            return true;

        } catch (IOException e) {
            JOptionPane.showMessageDialog(null,
                    "Ошибка при сохранении файла: " + e.getMessage(),
                    "Ошибка",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return false;
        }
    }
}