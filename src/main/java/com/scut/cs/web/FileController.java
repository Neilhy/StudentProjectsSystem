package com.scut.cs.web;

import org.apache.commons.io.FileUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

/**
 * Created by NeilHY on 2016/8/26.
 */
@RestController
public class FileController  {

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public String upload(MultipartHttpServletRequest request) {
        MultipartFile multipartFile=null;
        Iterator<String> itr=request.getFileNames();
        while (itr.hasNext()) {
            multipartFile = request.getFile(itr.next());
            System.out.println(multipartFile.getOriginalFilename()+" uploaded!"+ multipartFile.getSize());
            try {
                FileUtils.writeByteArrayToFile(new File("D:/javaweb/Springboot/upload/" + multipartFile.getOriginalFilename()), multipartFile.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
                return "wrong";
            }
        }
        return "Ok all";
    }
}
