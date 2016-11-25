package com.babarehner.android.pycolib;

/**
 * Created by mike on 11/18/16.
 */

public class Book {

    private String mTitle;
    private String mAuthor;
    private int mYearPublished;
    private final static boolean CHECKED_OUT = true;
    private boolean mIsCheckedOut = CHECKED_OUT;
    private int mImageResourceId;

    // Not sure where to handle this field if empty since its a toast
    //private String mToast;

    public Book(String title, String author, int yearPublished, boolean isCheckedOut, int imageResourceId){
        mTitle = title;
        mAuthor = author;
        mYearPublished = yearPublished;
        mIsCheckedOut = isCheckedOut;
        mImageResourceId = imageResourceId;
    }

    public String getmTitle() {
        return mTitle;
    }

    public String getmAuthor() {
        return mAuthor;
    }

    public int getmYearPublished() {
        return mYearPublished;
    }

    public int getmImageResourceId() { return mImageResourceId;}

    public boolean ismIsCheckedOut() { return mIsCheckedOut; }

    public boolean bookCheckedOut() {return mIsCheckedOut && CHECKED_OUT; }

    public String toString(){
        return "Book{" +
                "mTitle= " + mTitle + '\'' +
                ", mAuthor= " + mAuthor + '\'' +
                ", mYearPublished= " + mYearPublished + '\'' +
                '}';
    }
}

