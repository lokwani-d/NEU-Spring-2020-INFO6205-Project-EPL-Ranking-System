package src.main;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author deepak_lokwani
 * @author rupesh_alasundkar
 * 
 * NUID: 001316769  & 001304532
 * 
 * Project: Predictive Ranking System of English Premiere League
 * this.File: MakePairs.java
 * 
 * Factors Considered: 
 * Location - Home or Away
 * Attack Strength - goal scoring ability
 * Defense Strength - goal conceding weakness
 *
 */

public class MakePairs {
/**
 * this class makes the pairs of team to face a match and each match to be played 
 * or already played is given a combination ID and is stored in a HashMap
 * All the pairs are grouped of tw, played or not played
 */
	static Map<String, Integer> combination_id = new HashMap<String, Integer>();
	static Map<String, Integer> played = new HashMap<String, Integer>();
	static Map<String, Integer> notPlayed = new HashMap<String, Integer>();
	static Map<String, Integer> PointTable = new HashMap<String, Integer>();

	int i = 1;
	
	public void MakePairsOnce(String homeTeam, String awayTeam) {
		
		combination_id.put(homeTeam + "-" + awayTeam, i);
		i++;
		combination_id.put(awayTeam + "-" + homeTeam, i);
		i++;
	}

	public void matchesPlayed() {
		/*
		 * This method groups the combinations id whether the match is
		 *  played or not by finding its presence in the CSV data file
		 */

		/*
		 * Point table is initialized as soon as the matches played are 
		 * detected so as the point table can be updated  along with it
		 */
		
		for (String eachTeam : RankingSystem.allTeams) {
			PointTable.put(eachTeam, 0);
		}

		try (BufferedReader br = new BufferedReader(new FileReader(RankingSystem.csvFilePath))) {
			while ((RankingSystem.line = br.readLine()) != null) {

				String[] data = RankingSystem.line.split(RankingSystem.cvsSplitBy);
				String homeTeam = data[3];
				String awayTeam = data[4];

				Integer i = combination_id.get(homeTeam + "-" + awayTeam);
				played.put(homeTeam + "-" + awayTeam, i);
				String winner = data[7];
				
				/*
				 * It adds 3 points to the winner, and 1 point to each team for a draw
				 */
				if (winner.equals("H")) {
					int homeTeamPoints = PointTable.get(homeTeam);
					PointTable.put(homeTeam, homeTeamPoints + 3);
				} else if (winner.equals("A")) {
					int awayTeamPoints = PointTable.get(awayTeam);
					PointTable.put(awayTeam, 3 + awayTeamPoints);
				} else if (winner.equals("D")) {
					PointTable.put(homeTeam, 1 + PointTable.get(homeTeam));
					PointTable.put(awayTeam, 1 + PointTable.get(awayTeam));
				}
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void matchesNotPlayed() {
		
		/*
		 * This method groups the combinations id whether the match is
		 *  played or not by finding its presence in the CSV data file
		 */

		for (String combination : combination_id.keySet()) {
			boolean b = played.containsKey(combination);
			if (!b) {
				notPlayed.put(combination, combination_id.get(combination));
			}
		}
	}
}
