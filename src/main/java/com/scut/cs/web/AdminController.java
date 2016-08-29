package com.scut.cs.web;

import com.scut.cs.domain.Admin;
import com.scut.cs.service.AdminsService;
import com.scut.cs.web.request.RequestUrls;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by NeilHY on 2016/8/29.
 */
@RestController
public class AdminController {
    @Autowired
    AdminsService adminsService;

    @RequestMapping(value = RequestUrls.GetAdminsUrl, method = RequestMethod.GET)
    public List<Admin> getAdmin() {
        System.out.println("开始取管理员名单。。。");
        return adminsService.getAllAdmins();
    }

    @RequestMapping(value = RequestUrls.AddAdminUrl, method = RequestMethod.POST,consumes = "application/json",produces = "application/json")
    public Admin addAdmin(@RequestBody Admin admin) {
        return adminsService.addAdmin(admin);
    }

    @RequestMapping(value = RequestUrls.ModifyAdminUrl, method = RequestMethod.POST,consumes = "application/json",produces = "application/json")
    public Admin modifyAdmin(@RequestBody Admin admin) {
        return adminsService.modifyAdmin(admin);
    }

    @RequestMapping(value = RequestUrls.ChangeRoleTypeUrl, method = RequestMethod.GET)
    public int changeRoleType(@PathVariable String name,@PathVariable String type) {
        return adminsService.changeRoleType(name , type);
    }

    @RequestMapping(value = RequestUrls.DeleteAdminUrl, method = RequestMethod.POST,consumes = "application/json",produces = "application/json")
    public List<String> deleteAdmin(@RequestBody List<String> name) {
        try {
            return adminsService.delelteAdminList(name);
        } catch (IllegalArgumentException e) {
            System.out.println("捕获到参数异常，并且返回空list  "+e.getMessage());
            return null;
        }
    }
}
