package com.devotion.blue.web.menu;

import com.jfinal.core.JFinal;

public class MenuItem {

    private String id;
    private String url;
    private String text;

    public MenuItem() {
    }

    public MenuItem(String id, String url, String text) {
        this.id = id;
        this.url = url;
        this.text = text;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String generateHtml() {
        return String.format("<li id=\"%s\"><a href=\"%s\">%s</a></li>", id, JFinal.me().getContextPath() + url, text);
    }

    @Override
    public String toString() {
        return "MenuItem [id=" + id + ", url=" + url + ", text=" + text + "]";
    }

}
