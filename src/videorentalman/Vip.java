package videorentalman;

public class Vip extends Account implements Document{
    private String address;

    //constructor
    public Vip(String n, String e, String a){
            super(n,e);
            this.address = a;
    }

    public String getAddress(){
            return this.address;
    }

    public void setAddress(String a){
            this.address = a;
    }

    @Override
    public String toString(){
            return "Account Vip: <" +super.getID() +","+ super.getName() +","+ super.getEmail()
                            +","+ getAddress() +">";
    }

    @Override
    public String toHtmlDoc() {
        String VipDoc;
        VipDoc = "<html>";
        VipDoc += "<head><title>Vip:" + getName() + " - " + getEmail() + "</title></head>";
        VipDoc += "<body>";
        VipDoc += getName() + " " + getEmail() + " " + getAddress();
        VipDoc += "</body></html>";
        return VipDoc;
    }

}
