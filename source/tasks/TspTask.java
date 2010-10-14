package tasks;

import java.awt.geom.Point2D;
import java.io.Serializable;
import java.util.Arrays;

import api.Task;

/**
 * Computes an optimal solution for the <a
 * href="http://en.wikipedia.org/wiki/Travelling_salesman_problem">Travelling
 * Salesman Problem</a>
 * 
 * @author Manasa Chandrasekhar
 * @author Kowshik Prakasam
 */
public final class TspTask implements Task<int[]>, Serializable {

	private static final long serialVersionUID = 3276207466199157936L;
	private double[][] cities;
	private int startCity;
	private int endCity;
	private String taskIdentifier;

	/**
	 * @param startCity Starting city to be assumed in computation of the solution
	 * @param endCity Ending city to be assumed in computation of the solution
	 * @param cities
	 *            Represents the x and y coordinates of cities. cities[i][0] is
	 *            the x-coordinate of city[i] and cities[i][1] is the
	 *            y-coordinate of city[i].
	 *  @param taskIdentifier A unique task identifier for this task
	 */
	public TspTask(int startCity, int endCity, double[][] cities,
			String taskIdentifier) {
		this.startCity = startCity;
		this.endCity = endCity;
		this.cities = cities.clone();
		this.taskIdentifier = taskIdentifier;
	}

	/**
	 * 
	 * Generates the solution to the Travelling Salesman Problem.
	 * 
	 * @return An array containing cities in order that constitute an optimal
	 *         solution to TSP
	 * 
	 * @see api.Task Task
	 */
	public int[] execute() {

		int[] permutation = getFirstPermutation(startCity, endCity, cities);
		int[] minRoute = null;

		double endToStartLength = findLength(cities[this.endCity][0],
				cities[this.endCity][1], cities[this.startCity][0],
				cities[this.startCity][1]);
		double minLength = Double.MAX_VALUE;
		do {
			double thisLength = 0;
			int i;
			for (i = 0; i < permutation.length - 1; i++) {
				thisLength += findLength(cities[permutation[i]][0],
						cities[permutation[i]][1],
						cities[permutation[i + 1]][0],
						cities[permutation[i + 1]][1]);
			}
			thisLength += findLength(cities[permutation[i]][0],
					cities[permutation[i]][1], cities[this.endCity][0],
					cities[this.endCity][1]);
			double startToNextLength = findLength(cities[this.startCity][0],
					cities[this.startCity][1], cities[permutation[0]][0],
					cities[permutation[0]][1]);
			
			thisLength+=endToStartLength+startToNextLength;
			
			if (thisLength < minLength) {
				minLength = thisLength;
				minRoute = permutation.clone();
			}

		} while (nextPermutation(permutation));
		
		int[] fullMinRoute=Arrays.copyOf(minRoute, minRoute.length+2);
		fullMinRoute[fullMinRoute.length-2]=this.endCity;
		fullMinRoute[fullMinRoute.length-1]=this.startCity;
		return fullMinRoute;
	}

	private int[] getFirstPermutation(int startCity, int endCity,
			double[][] cities) {
		int[] firstPermutation = new int[cities.length - 2];
		int index = 0;
		for (int i = 0; i < cities.length; i++) {
			if (i != endCity && i != startCity) {
				firstPermutation[index] = i;
				index++;

			}
		}
		Arrays.sort(firstPermutation);
		return firstPermutation;
	}

	/**
	 * Works with <a
	 * href='http://en.wikipedia.org/wiki/Permutation'>permutations</a> Accepts
	 * an array of <b>ints</b> and reorders it's elements to recieve
	 * lexicographically next permutation
	 * 
	 * @param p
	 *            permutation
	 * @return false, if given array is lexicographically last permutation, true
	 *         otherwise
	 */

	private boolean nextPermutation(int[] p) {
		int a = p.length - 2;
		while (a >= 0 && p[a] >= p[a + 1]) {
			a--;
		}
		if (a == -1) {
			return false;
		}
		int b = p.length - 1;
		while (p[b] <= p[a]) {
			b--;
		}
		int t = p[a];
		p[a] = p[b];
		p[b] = t;
		for (int i = a + 1, j = p.length - 1; i < j; i++, j--) {
			t = p[i];
			p[i] = p[j];
			p[j] = t;
		}
		return true;
	}

	/**
	 * Computes the distance between two points
	 */
	private double findLength(double x1, double y1, double x2, double y2) {
		return Point2D.distance(x1, y1, x2, y2);

	}

	/**
	 * Returns the unique identifier of this task
	 * 
	 * @see api.Task Task
	 */
	public String getTaskIdentifier() {
		return this.taskIdentifier;
	}

}
