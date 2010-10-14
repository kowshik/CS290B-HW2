package client;

import java.awt.geom.Point2D;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import tasks.MandelbrotSetTask;
import api.Result;
import api.Space;
import api.Task;

/**
 * A job to perform remote computation of <a
 * href="http://en.wikipedia.org/wiki/Mandelbrot_set">Mandelbrot Set</a> by
 * splitting it up into smaller tasks of type {@link api.Task task}
 * 
 * @author Manasa Chandrasekhar
 * @author Kowshik Prakasam
 * 
 */
public class MandelbrotSetJob extends Job {

	private static final int TASK_SIZE = 32;
	private double lowerX;
	private double lowerY;
	private double edgeLength;
	private int n;
	private int iterLimit;

	private int[][] allValues;

	// Maps each Task object to its input
	private Map<String, Point2D> taskIdentifierMap;

	// Used to time the execution of methods for profiling
	private Map<String, Long> timeMap;

	private int numOfTasks;

	/**
	 * 
	 * @param lowerX
	 *            X-coordinate of the lower left corner of a square in the
	 *            complex plane
	 * @param lowerY
	 *            Y-coordinate of the lower left corner of a square in the
	 *            complex plane
	 * @param edgeLength
	 *            Edge length of the square in the complex plane, whose sides
	 *            are parallel to the axes
	 * @param n
	 *            Square region of the complex plane subdivided into n X n
	 *            squares, each of which is visualized by 1 pixel
	 * @param iterLimit
	 *            Defines when the representative point of a region is
	 *            considered to be in the Mandelbrot set.
	 */
	public MandelbrotSetJob(double lowerX, double lowerY, double edgeLength,
			int n, int iterLimit) {
		super();
		this.lowerX = lowerX;
		this.lowerY = lowerY;
		this.edgeLength = edgeLength;
		this.n = n;
		this.iterLimit = iterLimit;
		this.allValues = new int[n][n];
		this.taskIdentifierMap = new HashMap<String, Point2D>();
		this.timeMap = new HashMap<String, Long>();
		this.numOfTasks = 0;
	}

	/**
	 * Decomposes the Mandelbrot Set computation into into a list of smaller
	 * tasks of type {@link tasks.MandelbrotSetTask MandelbrotSetTask}, each of
	 * which are executed remotely in a compute space ({@link api.Space Space})
	 * 
	 * @param space
	 *            Compute space to which @{link tasks.MandelbrotSetTask
	 *            MandelbrotSetTask} objects should be sent for execution
	 * @throws RemoteException
	 * 
	 * @see client.Job Job
	 */
	public void generateTasks(Space space) throws RemoteException {

		int i = 0, j = 0;
		double jump = edgeLength / n;
		for (double xIndex = this.lowerX; i < n; xIndex += jump
				* MandelbrotSetJob.TASK_SIZE, i += MandelbrotSetJob.TASK_SIZE) {
			j = 0;
			for (double yIndex = this.lowerY; j < n; yIndex += jump
					* MandelbrotSetJob.TASK_SIZE, j += MandelbrotSetJob.TASK_SIZE) {
				String taskIdentifier = i + "," + j;
				Task<int[][]> aMandelbrotSetTask = new MandelbrotSetTask(
						xIndex, yIndex, jump, MandelbrotSetJob.TASK_SIZE,
						iterLimit, taskIdentifier);
				timeMap.put(taskIdentifier, System.currentTimeMillis());
				space.put(aMandelbrotSetTask);
				this.taskIdentifierMap.put(taskIdentifier, new Point2D.Double(
						i, j));
				numOfTasks++;
			}
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
	public void collectResults(Space space) throws RemoteException {
		long computerTotalTime = 0L;
		long clientTotalTime = 0L;

		for (int i = 0; i < taskIdentifierMap.size(); i++) {
			Result<int[][]> r = (Result<int[][]>) space.takeResult();
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
			int[][] values = (int[][]) r.getValue();
			String identifier = r.getTaskIdentifier();
			Point2D point = this.taskIdentifierMap.get(identifier);
			double startX = new Double(point.getX());
			double startY = new Double(point.getY());

			for (int valuesRow = 0; valuesRow < values.length; valuesRow++) {
				for (int valuesCol = 0; valuesCol < values[0].length; valuesCol++) {
					int actualX = new Double(valuesRow + startX).intValue();
					int actualY = new Double(n - 1 - (valuesCol + startY))
							.intValue();
					allValues[actualX][actualY] = values[valuesRow][valuesCol];
				}
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
	 * represents the colour of a pixel to be displayed on the screen to
	 * represent the Mandelbrot Set.
	 * 
	 * @return An array that contains Mandelbrot Set integer values from all
	 *         {@link api.Result Result} objects
	 * @see client.Job Job
	 */
	public int[][] getAllResults() {
		return allValues;
	}

}
