package routeFinder;

import java.util.*;

public class SearchMethods {

	private static final long TIMEOUT_MILLIS = 10000;

	public static List<String> breadthFirstSearch(HashMap<String, List<String>> adjacencyList, String start,
			String goal) {
		long startTime = System.nanoTime();
		// Open list (FIFO queue)
		Queue<List<String>> open = new LinkedList<>();
		// Closed set to track visited nodes
		Set<String> closed = new HashSet<>();

		// Initialize queue with the start city as the first path
		open.add(Arrays.asList(start));

		while (!open.isEmpty()) {
			if (((System.nanoTime() - startTime) / 1_000_000.0) > TIMEOUT_MILLIS) {
				System.out.println("Time-out: No path found within 10 seconds.");
				return null;
			}

			// Remove the first path from the queue
			List<String> path = open.poll();
			String currentCity = path.get(path.size() - 1); // Get last city in path

			// Goal check
			if (currentCity.equals(goal)) {
				System.out.printf("Search took %.3f ms%n", ((System.nanoTime() - startTime) / 1_000_000.0));
				
				return path; // Return the path found
			}

			// Mark as visited
			closed.add(currentCity);

			// Expand neighbors
			for (String neighbor : adjacencyList.getOrDefault(currentCity, new ArrayList<>())) {
				if (!closed.contains(neighbor)) {
					// Create new path with this neighbor added
					List<String> newPath = new ArrayList<>(path);
					newPath.add(neighbor);
					open.add(newPath);
				}
			}
		}

		return null; // No path found
	}

	public static List<String> dfs(HashMap<String, List<String>> adjacencyList, String start, String goal) {
		long startTime = System.nanoTime();
		// Open list (Stack - LIFO)
		Stack<List<String>> open = new Stack<>();
		// Closed set to track visited nodes
		Set<String> closed = new HashSet<>();

		// Initialize stack with the start city as the first path
		open.push(Arrays.asList(start));

		while (!open.isEmpty()) {
			if (((System.nanoTime() - startTime) / 1_000_000.0) > TIMEOUT_MILLIS) {
				System.out.println("Time-out: No path found within 10 seconds.");
				return null;
			}

			// Remove the last inserted path (LIFO)
			List<String> path = open.pop();
			String currentCity = path.get(path.size() - 1); // Get last city in path

			// Goal check
			if (currentCity.equals(goal)) {
				System.out.printf("Search took %.3f ms%n", ((System.nanoTime() - startTime) / 1_000_000.0));
				return path; // Return the path found
			}

			// Mark as visited
			closed.add(currentCity);

			// Expand neighbors (LIFO order)
			for (String neighbor : adjacencyList.getOrDefault(currentCity, new ArrayList<>())) {
				if (!closed.contains(neighbor)) {
					// Create new path with this neighbor added
					List<String> newPath = new ArrayList<>(path);
					newPath.add(neighbor);
					open.push(newPath);
				}
			}
		}

		return null; // No path found
	}

	public static List<String> iddfs(HashMap<String, List<String>> adjacencyList, String start, String goal) {
		long startTime = System.nanoTime();
		int depth = 0;

		while (true) { // Keep increasing depth until a solution is found
			if (((System.nanoTime() - startTime) / 1_000_000.0) > TIMEOUT_MILLIS) {
				System.out.println("Time-out: No path found within 10 seconds.");
				return null;
			}

			Set<String> visited = new HashSet<>();
			List<String> result = depthLimitedDFS(adjacencyList, start, goal, depth, visited, new ArrayList<>());

			if (result != null) {
				System.out.printf("Search took %.3f ms%n", ((System.nanoTime() - startTime) / 1_000_000.0));
				return result; // Goal found, return the path
			}

			if (visited.size() == adjacencyList.size()) {
				return null; // If all nodes have been explored, no solution exists
			}

			depth++; // Increase the depth limit and try again
		}
	}

	private static List<String> depthLimitedDFS(HashMap<String, List<String>> adjacencyList, String current, String goal,
			int depth, Set<String> visited, List<String> path) {
		long startTime = System.nanoTime();
		if (((System.nanoTime() - startTime) / 1_000_000.0) > TIMEOUT_MILLIS) {
			return null;
		}
		// Add current city to path
		path.add(current);
		visited.add(current);

		// Goal check
		if (current.equals(goal)) {
			return new ArrayList<>(path);
		}

		// Depth limit reached
		if (depth == 0) {
			path.remove(path.size() - 1); // Backtrack
			return null;
		}

		// Expand neighbors
		for (String neighbor : adjacencyList.getOrDefault(current, new ArrayList<>())) {
			if (!visited.contains(neighbor)) {
				List<String> result = depthLimitedDFS(adjacencyList, neighbor, goal, depth - 1, visited, path);
				if (result != null) {
					return result; // Goal found in deeper search
				}
			}
		}

		// Backtrack
		path.remove(path.size() - 1);
		return null;
	}

	public static List<String> bestFirstSearch(HashMap<String, List<String>> adjacencyList,
			HashMap<String, double[]> cityMap, String start, String goal) {
		long startTime = System.nanoTime();
		// Priority queue to store paths, sorted by heuristic (Euclidean distance to
		// goal)
		PriorityQueue<List<String>> open = new PriorityQueue<>(Comparator
				.comparingDouble(path -> Utility.euclideanDistance(cityMap.get(path.get(path.size() - 1)), cityMap.get(goal))));

		// Closed set to track visited nodes
		Set<String> closed = new HashSet<>();

		// Initialize queue with the start city as the first path
		open.add(Arrays.asList(start));

		while (!open.isEmpty()) {
			if (((System.nanoTime() - startTime) / 1_000_000.0) > TIMEOUT_MILLIS) {
				System.out.println("Time-out: No path found within 10 seconds.");
				return null;
			}
			// Remove the best (closest heuristic) path from the queue
			List<String> path = open.poll();
			String currentCity = path.get(path.size() - 1); // Last city in path

			// Goal check
			if (currentCity.equals(goal)) {
				System.out.printf("Search took %.3f ms%n", ((System.nanoTime() - startTime) / 1_000_000.0));
				return path; // Return the path found
			}

			// Mark as visited
			closed.add(currentCity);

			// Expand neighbors
			for (String neighbor : adjacencyList.getOrDefault(currentCity, new ArrayList<>())) {
				if (!closed.contains(neighbor)) {
					// Create a new path with this neighbor added
					List<String> newPath = new ArrayList<>(path);
					newPath.add(neighbor);
					open.add(newPath); // Priority queue orders it automatically
				}
			}
		}

		return null; // No path found
	}

	public static List<String> aStarSearch(HashMap<String, List<String>> adjacencyList, HashMap<String, double[]> cityMap,
			String start, String goal) {
		long startTime = System.nanoTime();
		// Priority Queue sorted by (cost so far + heuristic)
		PriorityQueue<List<String>> open = new PriorityQueue<>(Comparator.comparingDouble(path -> gCost(cityMap, path)
				+ Utility.euclideanDistance(cityMap.get(path.get(path.size() - 1)), cityMap.get(goal))));

		// Maps to track the lowest cost to reach each node
		Map<String, Double> gScores = new HashMap<>();
		gScores.put(start, 0.0);

		// Closed set to track visited nodes
		Set<String> closed = new HashSet<>();

		// Initialize queue with the start city as the first path
		open.add(Arrays.asList(start));

		while (!open.isEmpty()) {
			if (((System.nanoTime() - startTime) / 1_000_000.0) > TIMEOUT_MILLIS) {
				System.out.println("Time-out: No path found within 10 seconds.");
				return null;
			}
			// Remove the best (lowest cost + heuristic) path from the queue
			List<String> path = open.poll();
			String currentCity = path.get(path.size() - 1); // Last city in path

			// Goal check
			if (currentCity.equals(goal)) {
				System.out.printf("Search took %.3f ms%n", ((System.nanoTime() - startTime) / 1_000_000.0));
				return path; // Return the path found
			}

			// Mark as visited
			closed.add(currentCity);

			// Expand neighbors
			for (String neighbor : adjacencyList.getOrDefault(currentCity, new ArrayList<>())) {
				if (closed.contains(neighbor))
					continue; // Skip visited nodes

				double tentativeGScore = gScores.getOrDefault(currentCity, Double.MAX_VALUE)
						+ Utility.euclideanDistance(cityMap.get(currentCity), cityMap.get(neighbor));

				// If a better path to neighbor is found, update it
				if (tentativeGScore < gScores.getOrDefault(neighbor, Double.MAX_VALUE)) {
					gScores.put(neighbor, tentativeGScore);

					// Create a new path with this neighbor added
					List<String> newPath = new ArrayList<>(path);
					newPath.add(neighbor);
					open.add(newPath);
				}
			}
		}

		return null; // No path found
	}

	// g-cost: Total path cost so far
	private static double gCost(HashMap<String, double[]> cityMap, List<String> path) {
		double cost = 0.0;
		for (int i = 1; i < path.size(); i++) {
			cost += Utility.euclideanDistance(cityMap.get(path.get(i - 1)), cityMap.get(path.get(i)));
		}
		return cost;
	}

}
