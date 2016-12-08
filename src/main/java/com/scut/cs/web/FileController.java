package com.scut.cs.web;

import com.scut.cs.domain.FileMeta;
import com.scut.cs.web.request.RequestUrls;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;

import static org.aspectj.weaver.tools.cache.SimpleCacheFactory.path;

/**
 * Created by NeilHY on 2016/8/26.
 */
@RestController
public class FileController  {
    private static Logger logger = LoggerFactory.getLogger(FileController.class);
    Random r = new Random();
    String dir = File.separator+"pic"+File.separator;
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
//        logger.info(session.getId());
        Iterator<String> itr =  request.getFileNames();
        MultipartFile mpf = null;
        if(itr.hasNext()){
            mpf = request.getFile(itr.next());
            fileMeta = new FileMeta();
            fileMeta.setFileName(mpf.getOriginalFilename());
            fileMeta.setFileSize(mpf.getSize()/1024+" Kb");
            fileMeta.setFileType(mpf.getContentType());
            try {
                fileMeta.setBytes(mpf.getBytes());
            } catch (IOException e) {
                // TODO Auto-generated catch block
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
    @RequestMapping(value = RequestUrls.GetPic, method = RequestMethod.GET)
    public void getPic(HttpServletRequest request, HttpServletResponse response, @PathVariable String value){
        logger.info("开始获取图片...");
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

    @RequestMapping(value = RequestUrls.GetLocalPic,method = RequestMethod.GET)
    public void getLocalPic(HttpServletResponse response,@PathVariable String fileName){
        logger.info("开始获取本地图片..."+"名字为:"+fileName);
        String path = dir + fileName + ".jpg";
        if(!fileName.equals("")) {
            try {
                FileInputStream fis = new FileInputStream(path);
                int sz = fis.available();
                byte[] buf = new byte[sz];
                fis.read(buf);
                fis.close();
                response.setContentType("image/*");
                response.setHeader("Content-disposition", "attachment; filename=\"" + path + "\"");
                FileCopyUtils.copy(buf, response.getOutputStream());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    @RequestMapping(value = RequestUrls.SavePic,method = RequestMethod.GET)
    public String savePic(HttpServletRequest request) {
        logger.info("开始保存图片");
        String fileName = "";
        FileMeta getFile = (FileMeta)request.getSession().getAttribute("fileMeta");
        if(getFile!=null) {
            fileName = produceRandomName();
            String filePath = dir+fileName+".jpg";
            File f = new File(dir);
            if(!f.exists()) {
                f.mkdirs();
            }
            //logger.info(fileName);
            try {
                FileOutputStream fos = new FileOutputStream(filePath);
                byte[] data = getFile.getBytes();
                fos.write(data,0,data.length);
                fos.flush();
                fos.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return fileName;
    }


    String produceRandomName() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
        String str = sdf.format(new Date());
        int rn = (int)(r.nextDouble()*(99999-10000+1)) + 10000;
        return str + rn;
    }

}
