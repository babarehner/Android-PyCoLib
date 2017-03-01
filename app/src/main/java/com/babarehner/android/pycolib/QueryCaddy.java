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

    // constructor for e-mail
    public QueryCaddy(String fn, String p, String e, String b, String d ){
        fName = fn;
        bookTitle = b;
        email = e;
        phoneNo = p;
        date = d;
    }

    // constructor for text messaging

    public String getName(){ return fName; } // Public for use with Log.v

    String getBookTitle(){ return bookTitle; }

    String getEmail(){ return email; }

    String getPhoneNo(){ return phoneNo; }

    String getLoanDate(){ return date; }
}
