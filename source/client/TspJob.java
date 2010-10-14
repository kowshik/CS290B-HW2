package client;

import java.awt.geom.Point2D;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;

import tasks.TspTask;
import api.Result;
import api.Space;
import api.Task;

/**
 * Defines a Travelling Salesman Problem through the generic @{link client.Job
 * Job} interface
 * 
 * @author Manasa Chandrasekhar
 * @author Kowshik Prakasam
 * 
 */
public class TspJob extends Job {

	private double[][] cities;
	private int numOfTasks;
	private int[] minRoute;

	// Used to time the execution of methods for profiling
	private Map<String, Long> timeMap;

	/**
	 * @param cities
	 *            Represents the x and y coordinates of cities. cities[i][0] is
	 *            the x-coordinate of city[i] and cities[i][1] is the
	 *            y-coordinate of city[i].
	 */
	public TspJob(final double[][] cities) {
		this.cities = cities.clone();
		this.timeMap = new HashMap<String, Long>();
		this.numOfTasks = 0;
	}

	/**
	 * Decomposes the Travelling Salesman Problem computation into into a list
	 * of smaller tasks of type @{link tasks.TspTask TspTask}, each of which are
	 * executed remotely in a compute space ({@link api.Space Space})
	 * 
	 * @param space
	 *            Compute space to which @{link tasks.TspTask TspTask} objects
	 *            should be sent for execution
	 * @throws RemoteException
	 * 
	 * @see client.Job Job
	 */
	public void generateTasks(Space space) throws RemoteException {
		int endCity = 0;

		for (int city = endCity + 1; city < cities.length; city++) {
			int startCity = city;
			String taskIdentifier = startCity + "," + endCity;
			Task<int[]> aTspTask = new TspTask(startCity, endCity, cities,
					taskIdentifier);
			timeMap.put(taskIdentifier, System.currentTimeMillis());
			space.put(aTspTask);
			numOfTasks++;
		}

	}

	/**
	 * Gathers {@link api.Result Result} objects from the compute space and
	 * caches them in a simple data structure that can be quickly retrieved by
	 * the client through the {@link #getAllResults getAllResults()} method
	 * 
	 * @param space
	 *            Compute space containing the results obtained after remote
	 *            execution of tasks
	 * @throws RemoteException
	 * @see client.Job Job
	 */
	@Override
	public void collectResults(Space space) throws RemoteException {
		long computerTotalTime = 0L;
		long clientTotalTime = 0L;

		double minLength = Double.MAX_VALUE;

		for (int i = 0; i < numOfTasks; i++) {
			Result<int[]> r = (Result<int[]>) space.takeResult();
			long currentTime = System.currentTimeMillis();
			System.out.println("\nClient Task : " + (i + 1) + " Start time : "
					+ timeMap.get(r.getTaskIdentifier()) + " ms");
			System.out.println("Client Task : " + (i + 1) + " End   time : "
					+ currentTime + " ms");
			System.out.println("Client Task : " + (i + 1)
					+ " Elapsed   time : "
					+ (currentTime - timeMap.get(r.getTaskIdentifier()))
					+ " ms");
			clientTotalTime += (currentTime - timeMap
					.get(r.getTaskIdentifier()));
			computerTotalTime += (r.getEndTime() - r.getStartTime());
			int[] route = r.getValue();
			double length = this.findLength(route);
			if (length < minLength) {
				this.minRoute = route;
				minLength = length;
			}
			System.out.println("\nComputer Task : " + (i + 1)
					+ " Start time : " + r.getStartTime() + " ms");
			System.out.println("Computer Task : " + (i + 1) + " End   time : "
					+ r.getEndTime() + " ms");
			System.out.println("Computer Task : " + (i + 1)
					+ " Elapsed   time : "
					+ (r.getEndTime() - r.getStartTime()) + " ms");
			computerTotalTime += (r.getEndTime() - r.getStartTime());
		}
		System.out
				.println("\n\nClient Total time : " + clientTotalTime + " ms");
		System.out.println("Client Average time : "
				+ ((0.0f + clientTotalTime) / (numOfTasks + 0.0f)) + " ms");
		System.out.println("\n\nComputer Total time : " + computerTotalTime
				+ " ms");
		System.out.println("Computer Average time : "
				+ ((0.0f + computerTotalTime) / (numOfTasks + 0.0f)) + " ms");

	}

	/**
	 * Returns values cached by {@link #collectResults(Space)
	 * collectResults(Space space)} method. Each value in the returned array
	 * represents the a city index in the optimal solution to the Travelling
	 * Salesman Problem.
	 * 
	 * @return An array that contains cities from all {@link api.Result Result}
	 *         objects that form an optimal solution to TSP
	 * @see client.Job Job
	 */
	@Override
	public int[] getAllResults() {
		return this.minRoute;
	}

	private double findLength(int[] route) {
		double thisLength = 0;
		int i = 0;
		for (i = 0; i < route.length - 1; i++) {
			thisLength += findLength(cities[route[i]][0], cities[route[i]][1],
					cities[route[i + 1]][0], cities[route[i + 1]][1]);
		}
		thisLength += findLength(cities[route[i]][0], cities[route[i]][1],
				cities[route[0]][0], cities[route[0]][1]);
		return thisLength;
	}

	private double findLength(double x1, double y1, double x2, double y2) {
		return Point2D.distance(x1, y1, x2, y2);

	}

}
