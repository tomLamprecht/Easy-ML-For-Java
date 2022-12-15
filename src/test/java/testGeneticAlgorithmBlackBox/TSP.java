package testGeneticAlgorithmBlackBox;

import de.fhws.easyml.geneticalgorithm.Individual;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TSP implements Individual<TSP>
{

	private List<Integer> list;
	private Graph graph;
	double fitness;

	public TSP(Graph graph, List<Integer> list) {
		this.list = list;
		this.graph = graph;
	}

	public static TSP genRandomSolution(Graph graph){
		List<Integer> list = new ArrayList<>(graph.getVertexCount());

		for(int i = 0; i < graph.getVertexCount(); i++) {
			int r = (int) (Math.random()*graph.getVertexCount());
			if (!list.contains(r)) {
				list.add(r);
			}else {
				i--;
			}
		}
		return new TSP(graph, list);
	}

	@Override public void calcFitness()
	{
		int fitness = 0;
		for(int i = 0; i < list.size()-1; i++) {
			fitness += graph.distance(list.get(i), list.get(i+1));
		}
		fitness += graph.distance(list.get(list.size()-1), list.get(0));
		this.fitness = 1f/fitness;
	}

	@Override public double getFitness()
	{
		return this.fitness;
	}

	public TSP mutate(){
		int x = (int) (Math.random()*this.list.size());
		int y = x;
		while(x == y) {
			y = (int) (Math.random()*this.list.size());
		}
		Collections.swap(this.list, x, y);
		return this;
	}

	@Override public TSP copy()
	{
		List<Integer> copyList = new ArrayList<>();
		copyList.addAll(this.list);

		Graph copyGraph = new Graph(this.graph.getMatrixCopy());

		TSP copy = new TSP(copyGraph, copyList);
		copy.fitness = this.fitness;
		return copy;
	}

	public int getDist(){

		int dist = 0;
		for(int i = 0; i < list.size()-1; i++) {
			dist += graph.distance(list.get(i), list.get(i+1));
		}
		dist += graph.distance(list.get(list.size()-1), list.get(0));
		return dist;
	}

	@Override
	public String toString() {
		String res = "";
		for(int i : list) {
			res += i + " -> ";
		}

		int dist = getDist();


		return res.substring(0, res.length()-4) + " " + getFitness() + " dist:" + dist;
	}
}
