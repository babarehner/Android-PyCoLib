package com.babarehner.android.pycolib;



/**
 * Created by mike on 3/1/17.
 */

public class QueryCaddy {
    private String fName;
    private String bookTitle;
    private String email;
    private String phoneNo;
    private String date;

    // constructor
    public QueryCaddy(String fn, String p, String e, String b, String d ){
        fName = fn;
        bookTitle = b;
        email = e;
        phoneNo = p;
        date = d;
    }

    public String getName(){ return fName; }

    public String getBookTitle(){ return bookTitle; }

    public String getEmail(){ return email; }

    public String getPhoneNo(){ return phoneNo; }

    public String getLoanDate(){ return date; }
}
