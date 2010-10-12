package system;

/**
 * Remote server's view of Space
 */
public interface Computer2Space extends java.rmi.Remote {
	
	String SERVICE_NAME="Space";
	/**
	 * Computer objects can use this method to register with the Space
	 * 
	 * @param computer
	 *            Registers a remote computer with the resource allocator that
	 *            manages the cluster operations
	 * @throws java.rmi.RemoteException
	 */
	void register(Computer computer) throws java.rmi.RemoteException;
}