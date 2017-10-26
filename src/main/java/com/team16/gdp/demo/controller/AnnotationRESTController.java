package com.team16.gdp.demo.controller;

import com.team16.gdp.demo.data.DataModel;
import com.team16.gdp.demo.data.Quotation;
import com.team16.gdp.demo.data.Settings;
import javafx.beans.binding.IntegerBinding;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
public class AnnotationRESTController {

    private final Logger logger = LoggerFactory.getLogger(AnnotationRESTController.class);

    @PostMapping("/putquotation")
    public ResponseEntity<Integer> putquotation(@RequestBody Map<String, Object> quotation) {

        DataModel dataModel = new DataModel(new Settings());
        int id = dataModel.createQuotation(
                (Integer) quotation.get("caseId"),
                (Integer) quotation.get("startIndex"),
                (Integer) quotation.get("endIndex"),
                (String) quotation.get("quote")
        );
        return new ResponseEntity<>(id, HttpStatus.OK);
    }

    @PostMapping("/putannotation")
    public ResponseEntity<Integer> putannotation(@RequestBody Map<String, Object> annotation) {

        DataModel dataModel = new DataModel(new Settings());
        int id = dataModel.createAnnotation(
                (Integer) annotation.get("caseId"),
                (Integer) annotation.get("authorId"),
                (Integer) annotation.get("quotationId"),
                (String) annotation.get("text")
        );
        return new ResponseEntity<>(id, HttpStatus.OK);
    }

}
