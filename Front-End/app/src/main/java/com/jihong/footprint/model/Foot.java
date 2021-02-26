package com.jihong.footprint.model;

public class Foot {
    private String addr1;
    private String contentid;
    private String contenttypeid;
    private String dist;
    private String firstimage;
    private String mapx;
    private String mapy;
    private String title;

    public Foot() {
    }

    public Foot(String addr1, String contentid, String contenttypeid, String dist, String firstimage, String mapx, String mapy, String title) {
        this.addr1 = addr1;
        this.contentid = contentid;
        this.contenttypeid = contenttypeid;
        this.dist = dist;
        this.firstimage = firstimage;
        this.mapx = mapx;
        this.mapy = mapy;
        this.title = title;
    }

    // addr1
    public String getAddr1() {
        return addr1;
    }

    public void setAddr1(String addr1) {
        this.addr1 = addr1;
    }

    // contentid
    public String getContentid() {
        return contentid;
    }

    public void setContentid(String contentid) {
        this.contentid = contentid;
    }

    // contenttypeid
    public String getContenttypeid() {
        return contenttypeid;
    }

    public void setContenttypeid(String contenttypeid) {
        this.contenttypeid = contenttypeid;
    }

    // dist
    public String getDist() {
        return dist;
    }

    public void setDist(String dist) {
        this.dist = dist;
    }

    // firstimage
    public String getFirstimage() {
        return firstimage;
    }

    public void setFirstimage(String firstimage) {
        this.firstimage = firstimage;
    }

    // mapx
    public String getMapx() {
        return mapx;
    }

    public void setMapx(String mapx) {
        this.mapx = mapx;
    }

    // mapy
    public String getMapy() {
        return mapy;
    }

    public void setMapy(String mapy) {
        this.mapy = mapy;
    }

    // title
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}