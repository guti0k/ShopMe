package com.shopme.admin.export;

import com.shopme.common.entity.User;
import jakarta.servlet.http.HttpServletResponse;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class UserCsvExporter extends AbstractExporter {

    public void export(List<User> users, HttpServletResponse httpServletResponse) throws IOException {
        super.setResponseHeader(httpServletResponse, "text/csv; charset=UTF-8", ".csv", "users_");

//        new PrintWriter(new OutputStreamWriter(httpServletResponse.getOutputStream(), "UTF-8"))
        PrintWriter writer = httpServletResponse.getWriter();
        ICsvBeanWriter iCsvBeanWriter = new CsvBeanWriter(writer, CsvPreference.STANDARD_PREFERENCE);

        String[] csvHeader = { "User ID", "Email", "First Name", "Last Name", "Roles", "Enabled" };
        String[] fieldMapping = {"id", "email", "firstName", "lastName", "roles", "enabled"};

        iCsvBeanWriter.writeHeader(csvHeader);

        for (User user: users) {
            iCsvBeanWriter.write(user, fieldMapping);
        }
        iCsvBeanWriter.close();
    }
}
