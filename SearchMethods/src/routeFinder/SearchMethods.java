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
		while (true) {
			if (((System.nanoTime() - startTime) / 1_000_000.0) > TIMEOUT_MILLIS) {
				System.out.println("Time-out: No path found within 10 seconds.");
				return null;
			}
			Set<String> visited = new HashSet<>(); // Track visited nodes for each iteration
			List<String> path = new ArrayList<>(); // Track the current path
			List<String> result = depthLimitedDFS(adjacencyList, start, goal, depth, visited, path, startTime);
			if (result != null) {
				System.out.printf("Search took %.3f ms%n", ((System.nanoTime() - startTime) / 1_000_000.0));
				return result; // Return the path if the target is found
			}
			// If the entire graph has been explored and the target is not found, break
			if (adjacencyList.keySet().stream().allMatch(visited::contains)) {
				break;
			}
			depth++; // Increment depth for the next iteration
		}
		return null; // Return null if the target is not found
	}

	private static List<String> depthLimitedDFS(HashMap<String, List<String>> adjacencyList, String current, String goal,
			int depth, Set<String> visited, List<String> path, long startTime) {
		if (((System.nanoTime() - startTime) / 1_000_000.0) > TIMEOUT_MILLIS) {
			System.out.println("Time-out: No path found within 10 seconds.");
			return null;
		}
		if (depth == 0 && current.equals(goal)) {
			path.add(current); // Add the target node to the path
			return path; // Return the path if the target is found
		}
		if (depth > 0) {
			visited.add(current); // Mark the current node as visited
			path.add(current); // Add the current node to the path

			for (String neighbor : adjacencyList.getOrDefault(current, new ArrayList<>())) {
				if (!visited.contains(neighbor)) {
					List<String> result = depthLimitedDFS(adjacencyList, neighbor, goal, depth - 1, visited,
							new ArrayList<>(path), startTime);
					if (result != null) {
						return result; // Return the path if the target is found in the subtree
					}
				}
			}

			visited.remove(current); // Backtrack: unmark the current node
			path.remove(path.size() - 1); // Backtrack: remove the current node from the path
		}
		return null; // Return null if the target is not found at this depth
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
