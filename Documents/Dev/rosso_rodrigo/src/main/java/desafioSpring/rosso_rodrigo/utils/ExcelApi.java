package desafioSpring.rosso_rodrigo.utils;
import desafioSpring.rosso_rodrigo.exceptions.ApiException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpStatus;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class ExcelApi
{
    public FileInputStream fis = null;
    public FileOutputStream fos = null;
    public XSSFWorkbook workbook = null;
    public XSSFSheet sheet = null;
    public XSSFRow row = null;
    public XSSFCell cell = null;
    String xlFilePath;

    private static final String[] columnasTicket = {"PurchaseId", "ProductId", "Quantity", "Pryce"};

    public ExcelApi(String xlFilePath) throws Exception
    {
        File file = ResourceUtils.getFile(xlFilePath);
        fis = new FileInputStream(file);
        workbook = new XSSFWorkbook(fis);
        fis.close();
    }

    public void saveDataNewSheet(String pathFile) throws ApiException {
        File file = new File("");
        file = new File(file.getAbsolutePath() + pathFile);
        try
        {
            XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(file));
            int a = workbook.getSheetIndex("Tickets");

            if(workbook.getSheetIndex("Tickets") == -1)
            {
                Sheet sheet = workbook.createSheet("Tickets");
                Row headerRow = sheet.createRow(0);

                for(int i = 0; i < columnasTicket.length; i++)
                {
                    Cell cell = headerRow.createCell(i);
                    cell.setCellValue(columnasTicket[i]);
                }

                for(int i = 0; i < columnasTicket.length; i++)
                {
                    sheet.autoSizeColumn(i);
                }

                FileOutputStream fileOutputStream = new FileOutputStream(pathFile);
                workbook.write(fileOutputStream);
                fileOutputStream.close();
            }
        }
        catch (Exception ex)
        {
            throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al escribir archivo");
        }
    }

    public boolean setCellData(String sheetName, String colName, int rowNum, String value)
    {
        try
        {
            int col_Num = -1;
            sheet = workbook.getSheet(sheetName);

            row = sheet.getRow(0);
            for (int i = 0; i < row.getLastCellNum(); i++) {
                if (row.getCell(i).getStringCellValue().trim().equals(colName))
                {
                    col_Num = i;
                }
            }

            sheet.autoSizeColumn(col_Num);
            row = sheet.getRow(rowNum - 1);
            if(row==null)
                row = sheet.createRow(rowNum - 1);

            cell = row.getCell(col_Num);
            if(cell == null)
                cell = row.createCell(col_Num);

            cell.setCellValue(value);

            fos = new FileOutputStream(xlFilePath);
            workbook.write(fos);
            fos.close();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            return  false;
        }
        return true;
    }
}
