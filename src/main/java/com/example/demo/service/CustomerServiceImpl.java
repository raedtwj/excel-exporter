package com.example.demo.service;

import com.example.demo.entity.Customer;

import com.example.demo.repository.CustomerRepo;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import org.apache.poi.xssf.extractor.XSSFImportFromXML;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFMap;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerServiceImpl implements CustomerService {
    org.slf4j.Logger logger = LoggerFactory.getLogger(CustomerServiceImpl.class);

    @Autowired
    private CustomerRepo customerRepo;

    @Override
    public Customer createCustomer(Customer customer) {
        return customerRepo.save(customer);
    }

    @Override
    public List<Customer> readCustomer() {
        List<Customer> out = (List<Customer>) customerRepo.findAll();
        for (Customer customer : out) {
            // System.out.println(customer.email);
        }
        return out;
    }

    @Override
    // for now this method will del old entity and add new one
    public Customer updateCustomer(Customer customer, Long id) {
        Customer out = customerRepo.findById(id).get();
        if (out != null) {
            customerRepo.deleteById(id);
            customerRepo.save(customer);
        }
        return out;
    }

    @Override
    public void deleteCustomer(Long id) {
        customerRepo.deleteById(id);
    }

    // for test purposes
    @Override
    public String xmlExtracto(List<Customer> customers) {
        String xmlString = "";
        try {
            JAXBContext context = JAXBContext.newInstance(Customer.class);
            Marshaller m = context.createMarshaller();

            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE); // To format XML

            StringWriter sw = new StringWriter();
            for (Customer customer : customers) {
                m.marshal(customer, sw);
            }
            xmlString = sw.toString();

        } catch (JAXBException e) {
            e.printStackTrace();
        }

        return xmlString;
    }

    @Override
    public InputStream xssfExtractor(List<Customer> targetList, HashMap<String, String> headers) {
        // workbook object
        XSSFWorkbook workbook = new XSSFWorkbook();

        // spreadsheet object (Set the name of this sheet to it class name)
        XSSFSheet spreadsheet = workbook.createSheet(targetList.get(0).getClass().toString());
        int rowId = 0;
        XSSFRow row = spreadsheet.createRow(rowId++);
        int cellId = 0;
        for (Field defualtHeader : targetList.get(0).getClass().getDeclaredFields()) {
            defualtHeader.setAccessible(true);
            String defualValue = defualtHeader.getName();
            row.createCell(cellId++)
                    .setCellValue(
                            headers.getOrDefault(defualValue, defualValue));
        }

        for (Customer o : targetList) {
            cellId = 0;
            row = spreadsheet.createRow(rowId++);

            for (Field field : o.getClass().getDeclaredFields()) {
                try {
                    field.setAccessible(true);
                    XSSFCell cell = row.createCell(cellId++);
                    cell.setCellValue(String.valueOf(field.get(o)));

                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

            }
        }

        // cell style
        for (int i = 0; i < cellId; i++) {
            spreadsheet.autoSizeColumn(i);
        }

        // Write the output to a file
        ByteArrayOutputStream fileOut = null;
        try {
            fileOut = new ByteArrayOutputStream();
            workbook.write(fileOut);
            fileOut.flush();
            fileOut.close();
        } catch (Exception e) {
            System.out.println(e);
        }

        return new ByteArrayInputStream(fileOut.toByteArray());
    }

}