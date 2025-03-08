package routeFinder;

import java.util.*;

public class RouteFinder {

	public static void main(String[] args) {
		HashMap<String, double[]> cityMap = Utility.getCities();
		HashMap<String, List<String>> adjacencyList = Utility.getAdjacencyList();

		Scanner input = new Scanner(System.in);
		String startTown, endTown, runAgain;
		boolean cityExists, runSearches;

		System.out.println("Welcome to the Route Finder!");

		do {
			System.out.println("\nEnter the starting town: ");
			startTown = input.nextLine();

			System.out.println("Enter the destination: ");
			endTown = input.nextLine();

			runSearches = true;

			while (runSearches) {
				if (!cityMap.containsKey(startTown) || !cityMap.containsKey(endTown)) {
					System.out.println("\nError: One or both towns are not in the graph.");
					break;
				}

				Utility.displayMenu();

				int choice = input.nextInt();
				input.nextLine();
				
				System.out.println();

				List<String> route = null;

				switch (choice) {
				case 1:
					route = SearchMethods.breadthFirstSearch(adjacencyList, startTown, endTown);
					break;
				case 2:
					route = SearchMethods.dfs(adjacencyList, startTown, endTown);
					break;
				case 3:
					route = SearchMethods.iddfs(adjacencyList, startTown, endTown);
					break;
				case 4:
					route = SearchMethods.bestFirstSearch(adjacencyList, cityMap, startTown, endTown);
					break;
				case 5:
					route = SearchMethods.aStarSearch(adjacencyList, cityMap, startTown, endTown);
					break;
				case 6:
					runSearches = false;
					System.out.println("Exiting Search Menu");
					break;
				default:
					System.out.println("Invalid choice! Please select a number between 1 and 6.");
					continue;
				}
				
				if (route != null && !route.isEmpty()) {
					Utility.displayResults(cityMap, route, startTown, endTown);
				} else {
					System.out.println("No route found between " + startTown + " and " + endTown);
				}

			}
			
			System.out.print("\nWould you like to try another route? \n[y to redo | any other key to exit]: ");
			runAgain = input.nextLine();
			runAgain = runAgain.toLowerCase();

		} while (runAgain.equals("y"));
	}

}
