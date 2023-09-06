
package com.example.demo.controller;

import com.example.demo.entity.Customer;
import com.example.demo.service.CustomerService;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.apache.xmlbeans.impl.common.IOUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class Test {
    @Autowired
    private CustomerService service;

    @RequestMapping("/")
    @ResponseBody
    public void excelGetter(HttpServletResponse response) {
        // service.createCustomer(new Customer());
        HashMap renameMap = new HashMap<String, String>();
        renameMap.put("id", "potato");
        InputStream fileStream = service.xssfExtractor(service.readCustomer(), renameMap);
        response.setHeader("Content-disposition", "attachment; filename=test.xls");
        if (fileStream != null) {
            try {
                IOUtil.copyCompletely(fileStream, response.getOutputStream());
                response.flushBuffer();
            } catch (Exception e) {
                throw new RuntimeException(e);

            }
            ;
            // response.
        }

    }
}