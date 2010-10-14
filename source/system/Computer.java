package system;

import java.rmi.Remote;
import java.rmi.RemoteException;

import api.Result;
import api.Task;

/**
 * Defines a computer that can execute a {@link api.Task Task}.
 * 
 * @author Manasa Chandrasekhar
 * @author Kowshik Prakasam
 */
public interface Computer extends Remote {
	/**
	 * @param t
	 *            Generic Task to be executed
	 * @return Returns the object returned by the task's execute method. Clients
	 *         should look at implementations of the abstract class :
	 *         {@link client.Job Job} to understand the types of
	 *         {@link api.Result Result} objects that can be returned by this
	 *         method.
	 * @throws java.rmi.RemoteException
	 */
	Result<?> execute(Task<?> t) throws RemoteException;
}
