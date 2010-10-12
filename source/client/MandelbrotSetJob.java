package client;

import java.awt.geom.Point2D;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import tasks.MandelbrotSetTask;
import api.Result;
import api.Space;
import api.Task;

/**
 * A job to perform remote computation of <a
 * href="http://en.wikipedia.org/wiki/Mandelbrot_set">Mandelbrot Set</a>
 * 
 * @author Manasa Chandrasekhar
 * @author Kowshik Prakasam
 * 
 */
public class MandelbrotSetJob extends Job {

	private double lowerX;
	private double lowerY;
	private double edgeLength;
	private int n;
	private int iterLimit;
	
	protected List<Result<Integer>> results;
	protected Map<String, Point2D> taskIdentifierMap;

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

	}

	/**
	 * Decomposes the Mandelbrot Set computation into into a list of smaller
	 * tasks of type @{link tasks.MandelbrotSetTask MandelbrotSetTask}, each of
	 * which are executed remotely in a compute space
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

		for (double xIndex = this.lowerX; xIndex < this.lowerX + edgeLength * n; xIndex += edgeLength, i++) {
			j = 0;
			for (double yIndex = this.lowerY; yIndex < this.lowerY + edgeLength
					* n; yIndex += edgeLength, j++) {
				Task<Integer> mandelbrotSetTask = new MandelbrotSetTask(lowerX,
						lowerY, iterLimit, i + "," + j);
				space.put(mandelbrotSetTask);
				this.taskIdentifierMap.put(i + "," + j,
						new Point2D.Double(i, j));
			}
		}

	}

	/**
	 * Gathers {@link api.Result Result} objects from the compute space and populates them into the attribute : {@link #results results}
	 * @param space Compute space containing the results obtained after remote execution of tasks
	 * @throws RemoteException
	 * @see client.Job Job
	 */
	public void collectResults(Space space) throws RemoteException {
		for (int i = 0; i < taskIdentifierMap.size(); i++) {
			Result<Integer> r = (Result<Integer>) space.takeResult();
			this.results.add(r);
		}
	}


	/**
	 * Extracts values from {@link api.Result Result} objects, populates them into a square array and returns it. Each value in the returned array represents a pixel to be displayed on the screen.
	 * 
	 * @return An array that contains Mandelbrot Set integer values from all {@link api.Result Result}
	 *         objects
	 * @see client.Job Job
	 */
	public int[][] getAllResults() {
		int[][] allValues = new int[n][n];
		for (Result<Integer> r : results) {
			int value = (Integer) r.getValue();
			String identifier = r.getTaskIdentifier();
			Point2D point = this.taskIdentifierMap.get(identifier);
			int x = new Double(point.getX()).intValue();
			int y = new Double(point.getY()).intValue();
			allValues[x][y] = value;
		}
	
		
		return allValues;
	}

}
