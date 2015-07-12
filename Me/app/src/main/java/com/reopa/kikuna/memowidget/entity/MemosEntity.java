package com.reopa.kikuna.memowidget.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by arthurvc on 2015/07/12.
 */
public class MemosEntity implements Serializable {

    /**
     * Memo ID (P-KEY).
     */
    private Integer id;
    /**
     * Memo text
     */
    private String memoText;


    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMemoText() {
        return this.memoText;
    }

    public void setMemoText(String memoText) {
        this.memoText = memoText;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer(this.getClass().toString());
        sb.append("[");
        sb.append("ID").append(getId());
        sb.append("MemoText").append(getMemoText());
        sb.append("]");
        return sb.toString();
    }
}
