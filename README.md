# CS-461-Program-1 LLM Prompts:

**LLM Used:**  ChatGPT 4o

1. *Copied and pasted program instructions and included the formats for the Adjacencies.txt and coordinates.csv files* -- Read the following instructions let me know if you understand them.
2. I am using Java to implement this program. What data structures would you recommend to store the adjacencies and coordinates?
   - GPT Suggestions:
     - HashMap<String, List<String>> adjacenyList for adjacencies
     - Hashmap<String, City> cityMap for coordinates
   - *I didn't want to make a City object with the fields for name, latitude, and longitude, so I just stored coordinates in Hashmap<String, double[]>
4. Can you write me a function to load the adjacencies into a Hashmap<String, String>
   - This resulted in the getAdjacencies method
5. Can you write me a method to load the coordinates into a Hashmap<String, Double[]>
   - This resulted in the getCityMap method
6. Can you write a method to calculate the euclidean distance between two coordinates.
   - Provided the distance formula, but the scale was off -- it was 1 to 1, so each difference in latitude/longitude accounted for 1 unit
   - Provided code did not account for how many km were in a degree, but I didn't notice until later on -- addressed later
7. Using the data structures provided, can you implement a breadth first search based on the following pseudocode:
   - ![image](https://github.com/user-attachments/assets/6e6d11fa-00de-48b3-b7e3-2900a2e9aeea)
   - Resulted in breadthFirstSearch method
   - No changes were necessary to make the given code work
8. Can you now implement a depth first search based on the following pseudocode:
   - ![image](https://github.com/user-attachments/assets/444bab46-3c69-4450-8b5b-2577bdcf93f3)
   - Resulted in dfs method
   - No changes were necessary to make the given code work
9. Can you now implement an ID-DFS search?
   - This resulted in initial iddfs method
   - Initial provided code for the iddfs had a maxDepth parameter, even though that should only be included in the depth limited search helper function
   - depthLimitedDFS method worked fine
10. Why is the max depth passed as a parameter to the iddfs function? That sets a limit, the depth should increase as the search goes on until nothing is found?
    - This provided a correct implementation of the iddfs method, where depth was increased dynamicall until the goal was found or no solutions existed
11. Can you now implement a best first search based on the following pseudocode:
    - ![image](https://github.com/user-attachments/assets/379b4c5d-81b3-46f3-aef6-b127a907ea1e)
    - Resulted in bestFirstSearch method
    - No changes were necessary to make the given code work
12. Can you now implement A* based on the following pseudocode:
    - ![image](https://github.com/user-attachments/assets/d7cd3c2c-683f-46de-bd4a-b3c0e2cf8111)
    - Resulted in aStarSearch method
    - No changes were necessary to make the given code work

*Note: Each of the search methods returned a List<String> containing the cities in order of the path*

13. How can I calculate the distance for the routes I've generated?
    - Resulted in calculateRouteDistance method
    - Added the euclidean distance of each pair of cities in the path
   
*After testing the functionality of the program and outputting distance, I noticed the distance in km was very low*

14. The total route distance for each route doesn't make sense. I think the euclidean distance method is a little off. The total route distances end up only being a few km (<10). Do you know what the issue is?
    - GPT explained that the degrees needed to be converted to km before calculating the distance. It also explained how longitude degrees varied by which latitude they were at and provided a formula to adjust them.
    - After explaining it provided an update euclideanDistance method that took km and longitude variation into account.
   
# Overall, I found ChatGPT 4o to perform these tasks very well. Since these are very well known algorithms, it had little problems with implementing them quickly. I only had a few issues with the code it generated, and making tweaks was relatively easy.







