package com.scut.cs.web;

import com.scut.cs.domain.Admin;
import com.scut.cs.service.AdminsService;
import com.scut.cs.web.request.ChangeStatus;
import com.scut.cs.web.request.RequestUrls;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by NeilHY on 2016/8/29.
 */
@Controller
public class AdminController {
    @Autowired
    AdminsService adminsService;

    @RequestMapping(value = RequestUrls.GetAdminsUrl, method = RequestMethod.GET)
    @ResponseBody
    public List<Admin> getAdmin(@PathVariable String role) {
        System.out.println("开始取管理员名单。。。");
        return adminsService.getAdmins(role);
    }

    @RequestMapping(value = RequestUrls.AddAdminUrl, method = RequestMethod.POST,consumes = "application/json")
    public String addAdmin(@RequestBody Admin admin) {
        adminsService.addAdmin(admin);
        return "userManagement";
    }

    @RequestMapping(value = RequestUrls.ModifyAdminUrl, method = RequestMethod.POST,consumes = "application/json",produces = "application/json")
    @ResponseBody
    public Admin modifyAdmin(@RequestBody Admin admin) {
        return adminsService.modifyAdmin(admin);
    }

    @RequestMapping(value = RequestUrls.ChangeRoleTypeUrl, method = RequestMethod.GET)
    @ResponseBody
    public int changeRoleType(@PathVariable String name,@PathVariable String type) {
        return adminsService.changeRoleType(name , type);
    }

    @RequestMapping(value = RequestUrls.DeleteAdminUrl, method = RequestMethod.POST,consumes = "application/json",produces = "application/json")
    @ResponseBody
    public List<String> deleteAdmin(@RequestBody List<String> name) {
        try {
            return adminsService.delelteAdminList(name);
        } catch (IllegalArgumentException e) {
            System.out.println("捕获到参数异常，并且返回空list  "+e.getMessage());
            return null;
        }
    }

    @RequestMapping(value = RequestUrls.ChangeStatus, method = RequestMethod.POST,consumes = "application/json",produces = "application/json")
    @ResponseBody
    public  void  changeStatus(@RequestBody ChangeStatus changeStatus) {
        List<String> name = changeStatus.getName();
        String status = changeStatus.getStatus();
        try {
            adminsService.changeStatus(name,status);
        } catch (IllegalArgumentException e) {
            System.out.println("捕获到参数异常，并且返回空list  "+e.getMessage());
        }
    }

    @RequestMapping(value = RequestUrls.CheckPwd,method = RequestMethod.GET)
    @ResponseBody
    public String checkPwd (@PathVariable Long id,@PathVariable String pwd) {
        boolean res = adminsService.checkPwd(id,pwd);
        if(res == true) {
            return "success";
        } else {
            return "fail";
        }
    }

}
