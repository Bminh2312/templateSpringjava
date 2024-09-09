package com.example.test.models;

public enum Rank {
    GIOI("Giỏi"),KHA("Khá"),TRUNG_BINH("Trung bình"),YEU("Yếu");
    private String xl;
    Rank(String xl){
        this.xl = xl;
    }

    public String getRank(){
        return this.xl;
    }
}
