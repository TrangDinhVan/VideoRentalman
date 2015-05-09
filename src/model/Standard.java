package model;

import model.Document;
import model.Account;

public class Standard extends Account implements Document{
	
    public Standard(String n, String e){
            super(n,e);
    }

    @Override
    public String toString(){
        return "Account Standard: <" + super.getID() +","+ super.getName() +","+
            super.getEmail() +">";
    }
    @Override
    public String toHtmlDoc() {
        String StandardDoc;
        StandardDoc = "<html>";
        StandardDoc += "<head><title>Standard:" + getName() + " - " + getEmail() + "</title></head>";
        StandardDoc += "<body>";
        StandardDoc += getName() + " " + getEmail();
        StandardDoc += "</body></html>";
        return StandardDoc;
    }
}
