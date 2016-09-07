package com.scut.cs.web;

import com.scut.cs.domain.FileMeta;
import com.scut.cs.web.request.RequestUrls;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * Created by NeilHY on 2016/8/26.
 */
@RestController
public class FileController  {
    /***************************************************
     * URL: /upload
     * upload(): receives files
     * @param request : MultipartHttpServletRequest auto passed
     * @param response : HttpServletResponse auto passed
     * @return LinkedList<FileMeta> as json format
     ****************************************************/

    @RequestMapping(value= RequestUrls.UpLoadUrl, method = RequestMethod.POST)
    public @ResponseBody
    FileMeta upload(MultipartHttpServletRequest request, HttpServletResponse response) {
        FileMeta fileMeta = null;
        HttpSession session = request.getSession();
        System.out.println(session.getId());
        Iterator<String> itr =  request.getFileNames();
        MultipartFile mpf = null;
        if(itr.hasNext()){
            mpf = request.getFile(itr.next());
            fileMeta = new FileMeta();
            fileMeta.setFileName(mpf.getOriginalFilename());
            fileMeta.setFileSize(mpf.getSize()/1024+" Kb");
            fileMeta.setFileType(mpf.getContentType());
            System.out.println(fileMeta);
            try {
                fileMeta.setBytes(mpf.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        session.setAttribute("fileMeta",fileMeta);
        // result will be like this
        // [{"fileName":"app_engine-85x77.png","fileSize":"8 Kb","fileType":"image/png"},...]
        return fileMeta;

    }
    /***************************************************
     * URL: /get/{value}
     * get(): get file as an attachment
     * @param response : passed by the server
     * @return void
     ****************************************************/
    @RequestMapping(value = "/getPic/{value}", method = RequestMethod.GET)
    public void getPic(HttpServletRequest request, HttpServletResponse response, @PathVariable String value){
        System.out.println("开始获取图片...");
        FileMeta getFile = (FileMeta)request.getSession().getAttribute("fileMeta");

        if(getFile != null) {
            try {
                response.setContentType(getFile.getFileType());
                response.setHeader("Content-disposition", "attachment; filename=\"" + getFile.getFileName() + "\"");
                FileCopyUtils.copy(getFile.getBytes(), response.getOutputStream());
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }


}
