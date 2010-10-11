package tasks;

import java.io.Serializable;

import api.Task;

/**
 * Computes the <a href="http://en.wikipedia.org/wiki/Mandelbrot_set">Mandelbrot
 * set</a>
 * 
 * @author Manasa Chandrasekhar
 * @author Kowshik Prakasam
 */
public final class MandelbrotSetTask implements Task<Integer>, Serializable {

	private static final long serialVersionUID = -7445556151823532932L;
	private double lowerX;
	private double lowerY;
	private int iterLimit;
	private static double MANDELBROT_LIMIT = 2.0;
	private String taskIdentifier;
	
	/**
	 * 
	 * @param lowerX
	 *            X-coordinate of the lower left corner of a square in the
	 *            complex plane
	 * @param lowerY
	 *            Y-coordinate of the lower left corner of a square in the
	 *            complex plane
	 * @param iterLimit
	 *            Defines when the representative point of a region is
	 *            considered to be in the Mandelbrot set.
	 */
	public MandelbrotSetTask(double lowerX, double lowerY, int iterLimit, String taskIdentifier) {

		this.lowerX = lowerX;
		this.lowerY = lowerY;
		this.iterLimit = iterLimit;
		this.taskIdentifier=taskIdentifier;
	}

	/**
	 * 
	 * Generates the solution
	 * 
	 * @see api.Task Task
	 */
	public Integer execute() {

		double zLowerReal = this.lowerX;
		double zLowerComplex = this.lowerY;
		double zReal = zLowerReal;
		double zComplex = zLowerComplex;

		int k;
		for (k = 0; k < this.iterLimit
				&& (zReal * zReal + zComplex * zComplex <= MandelbrotSetTask.MANDELBROT_LIMIT); k++) {
			double zPrevReal = zReal;
			zReal = zReal * zReal - zComplex * zComplex + zLowerReal;
			zComplex = 2 * zPrevReal * zComplex + zLowerComplex;
			zPrevReal = zReal;

		}

		if (zReal * zReal + zComplex * zComplex <= MandelbrotSetTask.MANDELBROT_LIMIT) {

			return this.iterLimit;
		}

		return k;
	}
	
	/**
	 * Returns the unique identifier of this task
	 * 
	 * @see api.Task Task
	 */
	public String getTaskIdentifier(){
		return taskIdentifier;
	}
}
