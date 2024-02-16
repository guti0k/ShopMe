package com.shopme.admin.export;

import jakarta.servlet.http.HttpServletResponse;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AbstractExporter {
    public void setResponseHeader(HttpServletResponse httpServletResponse, String contentType, String extension, String prefix) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        String timeStamp = simpleDateFormat.format(new Date());

        String fileName = prefix + timeStamp + extension;

        httpServletResponse.setContentType(contentType);
        String headerKey = "Content-Disposition";
        String headerBody = "attachment; filename=" + fileName;
        httpServletResponse.setHeader(headerKey, headerBody);
    }
}
