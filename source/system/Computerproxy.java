package system;

import java.rmi.RemoteException;

import api.Result;
import api.Task;
import system.SpaceImpl;

public class Computerproxy implements Runnable {

	Computer cp;
    SpaceImpl sp;

	public Computerproxy(Computer c, SpaceImpl sp) {
		this.cp = c;
		this.sp = sp;
	}

	
	public void run() {
		Task<?> t = null;
		// TODO Auto-generated method stub
		if(!(sp.listTasks.isEmpty())){
		 t = sp.listTasks.remove();
		}
			else
				try{
				this.wait();
				}
		   catch(InterruptedException e){
			  e.printStackTrace(); 
		   }
		    try {
		    	Result<?> res ;
		    	res = cp.execute(t);
		    	sp.listResults.add(res);
		    	
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.err.println("Computer exception");
			}
			
			
	}
	}

