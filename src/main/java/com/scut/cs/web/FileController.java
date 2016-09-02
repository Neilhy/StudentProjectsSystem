package com.scut.cs.web;

import org.apache.commons.io.FileUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Random;

/**
 * Created by NeilHY on 2016/8/26.
 */
@RestController
public class FileController  {

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public String upload(MultipartHttpServletRequest request) {
        MultipartFile multipartFile=null;
        Iterator<String> itr=request.getFileNames();
        String filename=null;
        while (itr.hasNext()) {
            multipartFile = request.getFile(itr.next());
            filename=getFileName(multipartFile.getOriginalFilename());
            System.out.println(multipartFile.getOriginalFilename()+" uploaded!"+ multipartFile.getSize());
            System.out.println(filename+" uploaded!"+ multipartFile.getSize());
            try {
                FileUtils.writeByteArrayToFile(new File("upload/" + filename), multipartFile.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
        return filename;
    }
    static private String getFileName(String fileName) {
        String prefix="."+fileName.substring(fileName.lastIndexOf(".")+1);
        Random random=new Random();
        int tmp=random.nextInt(1000)+1;
        return String.valueOf(System.currentTimeMillis())+tmp+prefix;
    }
}
