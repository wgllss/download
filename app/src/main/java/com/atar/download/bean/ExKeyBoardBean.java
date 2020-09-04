package com.atar.download.bean;

/**
 * @author：atar
 * @date: 2020/9/4
 * @description:
 */
public class ExKeyBoardBean {
    private int type;
    private String name;

    public ExKeyBoardBean(int type, String name) {
        this.type = type;
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
