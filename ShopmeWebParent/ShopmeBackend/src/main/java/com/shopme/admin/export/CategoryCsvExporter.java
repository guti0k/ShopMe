package com.shopme.admin.export;

import com.shopme.common.entity.Category;
import jakarta.servlet.http.HttpServletResponse;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import java.io.IOException;
import java.util.List;

public class CategoryCsvExporter extends AbstractExporter{

    public void export(List<Category> listCategory, HttpServletResponse httpServletResponse) throws IOException {
        super.setResponseHeader(httpServletResponse, "text/csv", ".csv", "categories_");

        ICsvBeanWriter csvBeanWriter = new CsvBeanWriter(httpServletResponse.getWriter(), CsvPreference.STANDARD_PREFERENCE);

        String[] csvHeader = { "Category ID", "Category Name"};
        String[] fieldMapping = {"id", "name"};

        csvBeanWriter.writeHeader(csvHeader);
        for (Category category: listCategory) {
            category.setName(category.getName().replace("--", "  "));
            csvBeanWriter.write(category, fieldMapping);
        }

        csvBeanWriter.close();
    }
}
