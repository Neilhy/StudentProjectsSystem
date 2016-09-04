package com.scut.cs.web.request;

import com.scut.cs.domain.Dict;

import java.util.List;

/**
 * Created by Jack on 2016/8/31.
 */
public class AddDicts {
    private List<Dict> dictList;
    private String flag;
    public AddDicts() {}
    public AddDicts(List<Dict> dictList, String flag) {
        this.dictList = dictList;
        this.flag = flag;
    }

    public List<Dict> getDictList() {
        return dictList;
    }

    public void setDictList(List<Dict> dictList) {
        this.dictList = dictList;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }
}
