package com.example.music.service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface ExcelService {

    /**
     * 生成Excel报表
     * @param response
     * @throws IOException
     */
    void generateUserExcel(HttpServletResponse response) throws IOException;
}
