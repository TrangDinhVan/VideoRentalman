package videorentalman;

public class VideoRentalDemo {
	public static void main(String [] args){
		Account a1 = new Standard("A","A@gmail.com");
		Account a2 = new Standard("B","B@gmail.com");
		Account a3 = new Standard("C","C@gmail.com");
		Account a4 = new Vip("D","D@gmail.com","Hanoi");
		Account a5 = new Vip("E","E@gmail.com","HCM");
		Video v1 = new Video("Action",2,15);
		Video v2 = new Video("Horror",1,16);
		Video v3 = new Video("Porn",3,20);
		Video v4 = new Video("Romance",2,17);
		Video v5 = new Video("Anime",1,19);
		Contract c1 = new Contract(a1,v1,"d1",2,false);
		Contract c2 = new Contract(a2,v2,"d2",3,false);
		Contract c3 = new Contract(a3,v3,"d3",4,false);
		Contract c4 = new Contract(a4,v4,"d4",5,false);
		Contract c5 = new Contract(a5,v5,"d5",4,true);
		Contract c6 = new Contract(a1,v5,"d6",3,false);
		Contract c7 = new Contract(a2,v4,"d7",3,true);
		Contract c8 = new Contract(a3,v2,"d8",2,false);
		Contract c9 = new Contract(a4,v1,"d9",5,false);
		
		VideoRentalManager manage = new VideoRentalManager();
		manage.addContract(c9);
		manage.addContract(c8);
		manage.addContract(c7);
		manage.addContract(c6);
		manage.addContract(c5);
		manage.addContract(c4);
		manage.addContract(c3);
		manage.addContract(c2);
		manage.addContract(c1);
		
		System.out.print("Press Enter to see the report of all contracts.");
		TextIO.getln();
		System.out.println(manage.reportAll());
		System.out.println("Press Enter to see the opened contracts.");
		TextIO.getln();
		System.out.println(manage.reportOpenContract());
		System.out.println("Press Enter to continue.");
		TextIO.getln();
		System.out.println("Press Enter to close some contracts.");
		TextIO.getln();
		manage.closeContract(c9);
		manage.closeContract(c8);
		manage.closeContract(c4);
		manage.closeContract(c3);
		System.out.println("Done. Press Enter to see the report of descending sorted contracts as fee");
		TextIO.getln();
		manage.sort();
		System.out.println(manage.reportOpenContract());
		System.out.println("Press Enter to exit.");
		TextIO.getln();
		
	}
}
