package com.practice.cafesystem.restImpl;

import com.practice.cafesystem.constants.CafeConstants;
import com.practice.cafesystem.pojo.Bill;
import com.practice.cafesystem.rest.BillRest;
import com.practice.cafesystem.service.BillService;
import com.practice.cafesystem.utils.CafeUtils;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BillImpl implements BillRest {

    @Autowired
    BillService billService;

    @Override
    public ResponseEntity<String> generateReport(Map<String, Object> requestMap) {
        try {
            return billService.generateReport(requestMap);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
