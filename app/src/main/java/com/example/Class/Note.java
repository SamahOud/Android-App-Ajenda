package com.example.Class;

import android.graphics.Bitmap;

public class Note {
    private int noteId;
    private String name;
    private String detail;
    private Bitmap images;
    private String date;
    private int view;

    public Note(int noteId, String name, String detail, Bitmap images, String date) {
        this.noteId = noteId;
        this.name = name;
        this.detail = detail;
        this.images = images;
        this.date = date;
    }

    public Note(int noteId, String name, String detail, String date) {
        this.noteId = noteId;
        this.name = name;
        this.detail = detail;
        this.date = date;
    }

    public int get_NoteId() { return noteId; }
    public void set_NoteId(int noteId) { this.noteId = noteId; }

    public String get_Name() { return name; }
    public void set_Name(String name) { this.name = name; }

    public String get_Detail() { return detail; }
    public void set_Detail(String detail) { this.detail = detail; }

    public Bitmap get_Images() { return images; }
    public void set_Images(Bitmap images) { this.images = images; }

    public String get_Date() { return date; }
    public void set_Date(String date) { this.date = date; }

    public int get_View() { return view; }
    public void set_View(int view) { this.view = view; }

}
