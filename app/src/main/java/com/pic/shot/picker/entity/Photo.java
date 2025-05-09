package com.pic.shot.picker.entity;

public class Photo {

    /* renamed from: id */
    private int id;
    private String path;

    public Photo(int i, String str) {
        this.id = i;
        this.path = str;
    }

    public Photo() {
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Photo) || this.id != ((Photo) obj).id) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        return this.id;
    }

    public String getPath() {
        return this.path;
    }

    public void setPath(String str) {
        this.path = str;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int i) {
        this.id = i;
    }
}
