package routeFinder;

import java.io.*;
import java.util.*;

public class Utility {

	public static HashMap<String, double[]> getCities() {
		HashMap<String, double[]> cityMap = new HashMap<>();

		try (BufferedReader br = new BufferedReader(new FileReader("coordinates.csv"))) {
			String line;

			while ((line = br.readLine()) != null) {
				String[] parts = line.split(",");
				if (parts.length == 3) {
					String cityName = parts[0].trim();
					double latitude = Double.parseDouble(parts[1].trim());
					double longitude = Double.parseDouble(parts[2].trim());
					cityMap.put(cityName, new double[] { latitude, longitude });
				}
			}
			System.out.println("Loaded " + cityMap.size() + " cities.");
		} catch (IOException e) {
			System.err.println("Error loading cities: " + e.getMessage());
		}

		return cityMap;
	}

	public static HashMap<String, List<String>> getAdjacencyList() {
		HashMap<String, List<String>> adjacencyList = new HashMap<>();

		try (BufferedReader br = new BufferedReader(new FileReader("Adjacencies.txt"))) {
			String line;

			while ((line = br.readLine()) != null) {
				String[] parts = line.split(" ");
				if (parts.length == 2) {
					String city1 = parts[0].trim();
					String city2 = parts[1].trim();

					// Add bidirectional connection
					adjacencyList.putIfAbsent(city1, new ArrayList<>());
					adjacencyList.putIfAbsent(city2, new ArrayList<>());
					adjacencyList.get(city1).add(city2);
					adjacencyList.get(city2).add(city1);
				}
			}
			System.out.println("Loaded " + adjacencyList.size() + " city connections.");
		} catch (IOException e) {
			System.err.println("Error loading adjacencies: " + e.getMessage());
		}

		return adjacencyList;
	}

	public static void displayMenu() {
		System.out.println("\nSelect a search method:");
		System.out.println("1. Breadth-First Search (BFS)");
		System.out.println("2. Depth-First Search (DFS)");
		System.out.println("3. Iterative Deepening Depth-First Search (ID-DFS)");
		System.out.println("4. Best-First Search");
		System.out.println("5. A* Search");
		System.out.println("6. Exit");
		System.out.print("Enter your choice: ");
	}

	public static void displayResults(HashMap<String, double[]> cityMap, List<String> route, String startTown,
			String endTown) {
		double distance = calculateRouteDistance(route, cityMap);
		System.out.println("Route found: " + String.join(" → ", route));
		System.out.printf("Total Distance: %.2f km%n", distance);

	}

	public static double euclideanDistance(double[] coord1, double[] coord2) {
		final double KM_PER_DEGREE = 111.0;
		double latDiff = (coord2[0] - coord1[0]) * KM_PER_DEGREE;
		double lonDiff = (coord2[1] - coord1[1]) * KM_PER_DEGREE * Math.cos(Math.toRadians((coord1[0] + coord2[0]) / 2));
		return Math.sqrt(latDiff * latDiff + lonDiff * lonDiff);
	}

	public static double calculateRouteDistance(List<String> route, HashMap<String, double[]> cityMap) {
		if (route == null || route.size() < 2) {
			return 0.0; // No route or only one town
		}

		double totalDistance = 0.0;
		for (int i = 0; i < route.size() - 1; i++) {
			double[] city1 = cityMap.get(route.get(i));
			double[] city2 = cityMap.get(route.get(i + 1));

			if (city1 != null && city2 != null) {
				totalDistance += euclideanDistance(city1, city2);
			}
		}
		return totalDistance;
	}

}
