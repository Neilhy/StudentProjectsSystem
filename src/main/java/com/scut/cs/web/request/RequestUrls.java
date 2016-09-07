package com.scut.cs.web.request;

/**
 * Created by NeilHY on 2016/8/22.
 */
public class RequestUrls {
    public static final String LoginUrl = "/login";
    public static final String HomeUrl = "/";
    public static final String GetProjectsUrl = "/getProjects/{page}/{size}";
    public static final String AddProjectUrl = "/addProject";
    public static final String AddStudentsUrl = "/addStudents";
    public static final String DeleteProjectListUrl = "/deleteProjects";

    //Admin
    public static final String GetAdminsUrl = "/getAdmins/{role}";
    public static final String AddAdminUrl = "/addAdmin";
    public static final String ModifyAdminUrl = "/modifyAdmin";
    public static final String ChangeRoleTypeUrl = "/changeRoleType/{name}/{type}";
    public static final String DeleteAdminUrl = "/deleteAdmin";
    public static final String ChangeStatus = "/changeStatus";

    //数据字典
    public static final String GetKeywordsUrl = "/getKeywords";
    public static final String AddDicts = "/addDicts";
    public static final String GetDictItems = "/getDictItems/{keyword}";
    public static final String DeleteKeyword = "/deleteKeyword/{keyword}";

    public static final String UpLoadUrl = "/upload";

}
