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
public final class MandelbrotSetTask implements Task<int[][]>, Serializable {

	private static final long serialVersionUID = -7445556151823532932L;
	private double lowerX;
	private double lowerY;
	private int iterLimit;
	private static final double MANDELBROT_LIMIT = 2.0;
	private String taskIdentifier;
	private int n;
	private double edgeLength;
	
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
	 * @param taskIdentifier A unique task identifier for this task
	 */
	public MandelbrotSetTask(double lowerX, double lowerY, double edgeLength, int n, int iterLimit, String taskIdentifier) {

		this.lowerX = lowerX;
		this.lowerY = lowerY;
		this.edgeLength = edgeLength;
		this.n = n;
		this.iterLimit = iterLimit;
		this.taskIdentifier=taskIdentifier;
	}

	/**
	 * 
	 * Generates the solution
	 * 
	 * @see api.Task Task
	 */
	public int[][] execute() {
		
		
		int[][] values = new int[n][n];
		int i = 0, j = 0;
		for (double xIndex = this.lowerX; i<n; xIndex += edgeLength, i++) {
			j = 0;
			
			for (double yIndex = this.lowerY; j<n; yIndex += edgeLength, j++) {
				double zLowerReal = xIndex;
				double zLowerComplex = yIndex;
				double zReal = zLowerReal;
				double zComplex = zLowerComplex;

				int k;
				for (k = 0; k < this.iterLimit
						&& (modulus(zReal,zComplex) <= MandelbrotSetTask.MANDELBROT_LIMIT); k++) {
					double zPrevReal = zReal;
					zReal = zReal * zReal - zComplex * zComplex + zLowerReal;
					zComplex = 2 * zPrevReal * zComplex + zLowerComplex;
				}

				if (modulus(zReal,zComplex) <= MandelbrotSetTask.MANDELBROT_LIMIT) {

					values[i][j] = this.iterLimit;
				} else {

					values[i][j] = k;
				}
			}
		}
		return values;
	}
	
	/**
	 * Returns the unique identifier of this task
	 * 
	 * @see api.Task Task
	 */
	public String getTaskIdentifier(){
		return taskIdentifier;
	}
	
	
	private double modulus(double zReal, double zComplex){
		return Math.sqrt(zReal * zReal + zComplex * zComplex);
	}
}
