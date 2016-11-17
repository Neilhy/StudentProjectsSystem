package com.scut.cs.web;

import com.scut.cs.domain.Dict;
import com.scut.cs.service.DictService;
import com.scut.cs.web.request.AddDicts;
import com.scut.cs.web.request.RequestUrls;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Jack on 2016/8/30.
 */
@Controller
public class DictController {
    @Autowired
    private DictService dictService;

    @PreAuthorize("hasRole('ROLE_ADMIN')")//TODO 这个哪些人可以操作待讨论，以下方法类似
    @RequestMapping(value = RequestUrls.GetKeywordsUrl,method = RequestMethod.GET)
    @ResponseBody
    public List<String> getKeywords(){
        System.out.println("开始取数据类型...");
        return dictService.findKeywords();
//        String ret = buildString(keywords);
//        return keywords;
    }
    @RequestMapping(value = RequestUrls.GetDictItems,method = RequestMethod.POST,consumes = "application/json")
    @ResponseBody
    public List<String> getDictItems(@RequestBody Dict dict) {
        String keyword = dict.getKeyword();
        System.out.println("开始取" + keyword + "的数据项...");
        List<Dict> dicts= dictService.getItems(keyword);
        return dicts.stream().map(Dict::getItemName).collect(Collectors.toList());
    }

/*    private String buildString(List<String> list) {
        StringBuilder sb = new StringBuilder("");
        for(String item:list) {
            sb.append(item+',');
        }
        sb.deleteCharAt(sb.length()-1);
//        System.out.println(sb.toString());
        return sb.toString();
    }*/

    @RequestMapping(value = RequestUrls.AddDicts,method = RequestMethod.POST,consumes = "application/json")
    public String addDictItems(@RequestBody AddDicts addDicts) {
        System.out.println("开始AddDicts...");
        if (addDicts != null) {
            List<Dict> dicts = addDicts.getDictList();
            String flag = addDicts.getFlag();
            String keyword = dicts.get(0).getKeyword();
            if (dicts != null && dicts.size()>0) {
                dictService.addDicts(dicts,flag,keyword);
                if(flag.equals("update")) {
                    dictService.updateByKeyword(keyword);
                } else {
                    dictService.upDateKeywords();
                }
            }
        }
        return "dictManagement";
    }

    @RequestMapping(value = RequestUrls.DeleteKeyword,method = RequestMethod.GET)
    public String deleteKeyword(@PathVariable String keyword) {
        System.out.println("开始删除"+keyword+"...");
        dictService.deleteKeyword(keyword);
        dictService.upDateKeywords();
        return "dictManagement";
    }



   /* @RequestMapping(value = "/excel" , method = RequestMethod.GET)
    public void exportExcel(HttpServletResponse response) {
        System.out.print("开始导出Excel...");
        ArrayList<String> headers = new ArrayList<>();
        String[] titles = {"姓名","编号"};
        for(String title:titles) {
            headers.add(title);
        }
        ArrayList<ArrayList> rows = new ArrayList<>();
        ArrayList<String> row = new ArrayList<>();
        row.add("张三");
        row.add("1");
        rows.add(row);
        row = new ArrayList<>();
        row.add("张三");
        row.add("1");
        rows.add(row);
        try {
            OutputStream out = response.getOutputStream();
            response.reset();
            response.setContentType("application/vnd.ms-excel");
            ExcelFileGenerator generator = new ExcelFileGenerator(headers, rows);
            generator.expordExcel(out);
            System.setOut(new PrintStream(out));
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

}
