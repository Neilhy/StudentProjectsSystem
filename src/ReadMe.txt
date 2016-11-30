2016-9-3    数据字典V1.0 开发完毕
    主要改动地方：
        * 新增数据字典操作的相关函数及实体类
        * 新增数据字典操作页面dictIndex.html及相关的js
    使用方法：
        * 导入dict.js文件
        * 在$(document).ready(function(
                ...
           ))中调用getSelectItems(selName,keyword)
        其中selName是select的名称，keyword是数据类型，可在dictIndex中找到对应的信息
        当前版本仅适用于select

2016-9-3    数据字典V2.0
     改动地方：
        * 新增删除数据类型的功能
        * 优化页面刷新方式，改为异步刷新

2016-9-4    优化文件结构，删除冗余数据，为所有用到select的地方添加数据字典

2016-9-6    实现用户列表显示（筛选），用户添加和删除功能
            修正数据字典bug（不录入数据时会数组越界）


2016-9-8    增加了dict的缓存技术和密码加密的方法。其中admin,outer,inner是程序启动的时候录入的。以后可以放在添加管理员
            页面录入。

2016-9-15   保存用户修改内容
            管理员修改密码不用输入原密码

2016-10-5   重做用户管理页面逻辑，管理员改密码集成到页面头部，新增管理员重置密码功能
            通过Spring Security隐藏非管理员用户不能看到的选项
            发现bug:点击修改密码弹出模态框时，退出选项会被压缩（字消失了），而且点了之后会报错

2016-10-11  修改项目添加页面，新增项目添加功能
            新增项目显示功能
            bug：筛选不正常，初步估计可能是由于缓存的问题

2016-10-13  新增项目筛选功能，修正上一版本的bug
            可以按照各关键字进行筛选，修改新增项目页面

2016-10-13  新增分页功能，以及批量操作（删除，通过，不通过）

2016-10-14  修正修改密码无法登出的bug

2016-10-16  修正字典缓存bug

2016-10-16  增加图片保存本地和读取已上传图片的功能
            增加导出excel功能

2016-11-18  解决问题：
                1. 录入信息时学院选项只有计算机
                2. inner应该有审核权力，outer应该有删除的权力
                3. 审核不通过可以填写理由

2016-11-25  修正项目修改BUG


2016-11-26  1.将学生的学号去掉了“唯一”键。
                使得每次添加项目的时候，不会检查是否已经有了该学生。
                也就是说学生表里可以有多个相同学号的学生，以至于不能用学号来标示一个学生，只能用id来标示。
            2.在修改项目的时候，不要用自己定义的delete语句，如ProjectRepository中的void removeById(Long id);
                用jpa自己的delete语句，如ProjectServiceImpl中的修改函数：public Project modifyProject(Project project)；
                中的projectRepository.delete(project.getId());
                以下是命令行输出，用自己写的 “void removeById(Long id);” 的测试结果：
                Hibernate: select project0_.project_id as project_1_2_, project0_.captain_college as captain_2_2_, project0_.file_path as file_pat3_2_, project0_.level as level4_2_, project0_.msg_forbid as msg_forb5_2_, project0_.note as note6_2_, project0_.photo_status as photo_st7_2_, project0_.project_date as project_8_2_, project0_.project_name as project_9_2_, project0_.rank as rank10_2_, project0_.state as state11_2_, project0_.teacher as teacher12_2_ from project project0_ where project0_.project_id=?
                Hibernate: select studentlis0_.project_id as project_2_3_0_, studentlis0_.student_id as student_1_3_0_, student1_.student_id as student_1_4_1_, student1_.captain_or_not as captain_2_4_1_, student1_.class_name as class_na3_4_1_, student1_.college as college4_4_1_, student1_.grade as grade5_4_1_, student1_.register_id as register6_4_1_, student1_.student_name as student_7_4_1_ from project_student_list studentlis0_ inner join student student1_ on studentlis0_.student_id=student1_.student_id where studentlis0_.project_id=?
                Hibernate: insert into student (captain_or_not, class_name, college, grade, register_id, student_name) values (?, ?, ?, ?, ?, ?)
                Hibernate: update project set captain_college=?, file_path=?, level=?, msg_forbid=?, note=?, photo_status=?, project_date=?, project_name=?, rank=?, state=?, teacher=? where project_id=?
                Hibernate: delete from project_student_list where project_id=?
                Hibernate: insert into project_student_list (project_id, student_id) values (?, ?)

                可以看到只删除了project，没有级联删除student！！！
            3.project中与student多对多的联系中加入了CascadeType.PERSIST,CascadeType.DELETE,CascadeType.REMOVE

2016-11-26  1.开放inner的修改项目和删除项目权限
            2.审核状态为通过时不能保存修改，也不能删除项目
            3.outer添加项目时只能添加自己学院的
            4.修改项目后重定向回主页面
            5.修改密码后重定向回登陆页面，这样就不会再出现token的bug了

2016-11-30  1.添加项目时队长只能是本学院的，队员可以是其他学院的
            2.页面加载之前所有按钮设为不可操作，等到加载完成后才可操作

