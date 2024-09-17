package com.redis.RedisRetrieval.Controller;

import com.redis.RedisRetrieval.Entity.IndividualInfo;
import com.redis.RedisRetrieval.Service.IndividualInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/Individual")
public class IndividualInfoController {

    @Autowired
    private IndividualInfoService individualInfoService;


    @GetMapping("/{key}")
    public ResponseEntity<IndividualInfo> getIndividualInfo(@PathVariable String key)
    {
        IndividualInfo individualInfo=individualInfoService.getIndividualData(key);

        return new ResponseEntity<>(individualInfo, HttpStatus.OK);
    }
    @PostMapping("/{key}")
    public ResponseEntity<String> storeIndividualInfo(@PathVariable String key, @RequestBody IndividualInfo individualInfo)
    {
        boolean res=individualInfoService.storeIndividualInfo(key,individualInfo);
        if(!res)
            return new ResponseEntity<>("Store Individual Info failed",HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>("Individual Info Stored Successfully",HttpStatus.CREATED);
    }

}
