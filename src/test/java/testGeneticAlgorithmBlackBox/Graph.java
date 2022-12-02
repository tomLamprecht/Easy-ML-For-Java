package testGeneticAlgorithmBlackBox;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class Graph
{

	private int[][] matrix;

	public Graph(int[][] matrix) {
		this.matrix = matrix;
	}

	public int getVertexCount() {
		return matrix.length;
	}

	public int distance(int i, int j) {
		return matrix[i][j];
	}

	public int[] getNeighbours(int i) {
		ArrayList<Integer> list = new ArrayList<>();
		for(int j = 0; j < matrix[i].length; j++) {
			if(distance(i, j) != 0)
				list.add(j);
		}
		return list.stream().mapToInt(integer -> integer).toArray();
	}

	public static Graph loadGraph(String filepath) {
		if(!filepath.endsWith(".csv"))
			throw new IllegalArgumentException("file must be a .csv");

		try(BufferedReader br = new BufferedReader(new FileReader(filepath))) {
			String in = br.readLine();
			if(in == null)
				throw new IllegalArgumentException("the given file is empty");

			String[] nums = in.split(",");
			int vertexCount = nums.length;

			if(vertexCount <= 1)
				throw new IllegalArgumentException("graph must have at least two vertices");

			int[][] matrix = new int[vertexCount][vertexCount];
			int rowCount = 0;
			while (in != null) {
				nums = in.split(",");
				if(nums.length != vertexCount) {
					throw new IllegalArgumentException("all lines must be of the same length");
				}
				addLineToMatrix(matrix, rowCount, nums);
				in = br.readLine();
				rowCount++;
			}
			if(rowCount != vertexCount)
				throw new IllegalArgumentException("amount of rows must be the same as amount of columns");
			return new Graph(matrix);
		} catch (FileNotFoundException e) {
			throw new IllegalArgumentException("file could not be found");
		} catch (IOException e) {
			throw new RuntimeException("something went wrong with IO");
		}
	}

	private static void addLineToMatrix(int[][] matrix, int row, String[] line) {
		try {
			for (int j = 0; j < matrix[row].length; j++) {
				matrix[row][j] = Integer.parseInt(line[j]);
			}
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException("line has invalid characters: not a number");
		}
	}

	public int[][] getMatrixCopy()
	{
		return Arrays.stream(matrix).map(int[]::clone).toArray(int[][]::new);
	}
}
