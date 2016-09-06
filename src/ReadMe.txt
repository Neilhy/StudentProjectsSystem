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


