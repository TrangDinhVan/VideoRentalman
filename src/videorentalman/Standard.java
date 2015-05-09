package videorentalman;

public class Standard extends Account{
	
    public Standard(String n, String e){
            super(n,e);
    }

    @Override
    public String toString(){
        return "Account Standard: <" + super.getID() +","+ super.getName() +","+
            super.getEmail() +">";
    }
}
