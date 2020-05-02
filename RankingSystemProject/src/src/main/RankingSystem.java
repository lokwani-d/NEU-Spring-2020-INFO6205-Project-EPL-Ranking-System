package src.main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * 
 * @author deepak_lokwani
 * @author rupesh_alasundkar
 * 
 *         NUID: 001316769 & 001304532
 * 
 *         Project: Predictive Ranking System of English Premiere League
 *         this.File: RankingSystem.java Factors Considered: Location - Home or
 *         Away Attack Strength - goal scoring ability Defense Strength - goal
 *         conceding weakness
 *
 */

public class RankingSystem {

	/**
	 * this class is primarily my CSV file reader of sorts. It reads all the
	 * required data values that would be required further in the driver class for
	 * the calculation of the PDF of a match results
	 */

	// Absolute Path
	// static String csvFilePath =
	// "C:\\Users\\deepa\\eclipse-workspace\\testing\\RankingSystemProject\\src\\src\\main\\datasets\\2019-2020.csv";
	// Relative Path
	static String csvFilePath = ".//src//src//main//datasets//2019-2020.csv";
	static String line = "";
	static String cvsSplitBy = ",";
	static String allTeams[] = { "Arsenal", "Aston Villa", "Bournemouth", "Brighton", "Burnley", "Chelsea",
			"Crystal Palace", "Everton", "Leicester", "Liverpool", "Man City", "Man United", "Newcastle", "Norwich",
			"Sheffield United", "Southampton", "Tottenham", "Watford", "West Ham", "Wolves" };

	/**
	 * Master Data Retriever
	 * 
	 * @param team     : input team for which data needs to be found out
	 * @param location : home, away or all
	 * @return an array of complete dataset of the matches
	 */

	public float[] GoalsScoredEachTeamData(String team, String location) {

		/*
		 * Initializing all my variables to be calculated further
		 */
		float goalsScoredEachTeamAtHome = 0;
		float goalsScoredEachTeamAway = 0;

		float goalsConcededTeamAway = 0;
		float goalsConcededTeamAwayAverage = 0;

		float matchesPlayedEachTeamAtHome = 0;
		float matchesPlayedEachTeamAway = 0;

		float goalsScoredEachTeamAtHomeAverage = 0;
		float goalsScoredEachTeamAwayAverage = 0;

		float goalsScoredEachTeamAll = 0;
		float matchesPlayedEachTeamAll = 0;
		float goalsScoredEachTeamAverageAll = 0;

		/*
		 * This float array contains all my resultant varivables
		 */
		float goalsScoredEachTeamDataSet[] = new float[11];

		try (BufferedReader br = new BufferedReader(new FileReader(csvFilePath))) {
			while ((line = br.readLine()) != null) {

				String[] data = line.split(cvsSplitBy);
				String homeTeam = data[3];
				String awayTeam = data[4];

				/*
				 * checks for the home location
				 */
				if ((location).equals("home")) {
					if (homeTeam.equalsIgnoreCase(team)) {
						matchesPlayedEachTeamAtHome++;
						goalsScoredEachTeamAtHome += Integer.parseInt(data[5]);
					}
				}

				/*
				 * checks for the away location
				 */
				else if ((location).equals("away")) {
					if (awayTeam.equalsIgnoreCase(team)) {
						matchesPlayedEachTeamAway++;
						goalsScoredEachTeamAway += Integer.parseInt(data[6]);
						goalsConcededTeamAway += Integer.parseInt(data[5]);
					}
				}

				/*
				 * checks for the all locations
				 */
				else if ((location).equals("all")) {
					if (homeTeam.equalsIgnoreCase(team)) {
						matchesPlayedEachTeamAtHome++;
						goalsScoredEachTeamAtHome += Integer.parseInt(data[5]);
					} else if (awayTeam.equalsIgnoreCase(team)) {
						matchesPlayedEachTeamAway++;
						goalsScoredEachTeamAway += Integer.parseInt(data[6]);
					}
				}
			}
		} catch (IOException e) {
			/*
			 * Try catch blocks throws the new exception when the input data are not found
			 * in the record
			 */
			e.printStackTrace();
		}

		/*
		 * based on the location based switch case is run all the data is manipulated
		 */
		switch (location) {

		/*
		 * checks for the home locations
		 */
		case "home":
			if (matchesPlayedEachTeamAtHome == 0) {
				return goalsScoredEachTeamDataSet;
			} else if (matchesPlayedEachTeamAtHome != 0) {
				goalsScoredEachTeamAtHomeAverage = goalsScoredEachTeamAtHome / matchesPlayedEachTeamAtHome;
			}
			break;

		/*
		 * checks for the away locations
		 */
		case "away":
			if (matchesPlayedEachTeamAway == 0) {
				return goalsScoredEachTeamDataSet;
			} else if (matchesPlayedEachTeamAway != 0) {
				goalsScoredEachTeamAwayAverage = goalsScoredEachTeamAway / matchesPlayedEachTeamAway;
				goalsConcededTeamAwayAverage = goalsConcededTeamAway / matchesPlayedEachTeamAway;
			}
			break;

		/*
		 * checks for the all locations
		 */
		case "all":
			matchesPlayedEachTeamAll = matchesPlayedEachTeamAtHome + matchesPlayedEachTeamAway;
			goalsScoredEachTeamAll = goalsScoredEachTeamAtHome + goalsScoredEachTeamAway;
			if (matchesPlayedEachTeamAll == 0) {
				return goalsScoredEachTeamDataSet;
			} else if (matchesPlayedEachTeamAll != 0) {
				goalsScoredEachTeamAverageAll = goalsScoredEachTeamAll / matchesPlayedEachTeamAll;
			}
			break;

		}

		/*
		 * collating back the entire array
		 */
		goalsScoredEachTeamDataSet[0] = goalsScoredEachTeamAtHome;
		goalsScoredEachTeamDataSet[1] = matchesPlayedEachTeamAtHome;
		goalsScoredEachTeamDataSet[2] = goalsScoredEachTeamAtHomeAverage;
		goalsScoredEachTeamDataSet[3] = goalsScoredEachTeamAway;
		goalsScoredEachTeamDataSet[4] = matchesPlayedEachTeamAway;
		goalsScoredEachTeamDataSet[5] = goalsScoredEachTeamAwayAverage;
		goalsScoredEachTeamDataSet[6] = goalsScoredEachTeamAll;
		goalsScoredEachTeamDataSet[7] = matchesPlayedEachTeamAll;
		goalsScoredEachTeamDataSet[8] = goalsScoredEachTeamAverageAll;
		goalsScoredEachTeamDataSet[9] = goalsConcededTeamAway;
		goalsScoredEachTeamDataSet[10] = goalsConcededTeamAwayAverage;

		return goalsScoredEachTeamDataSet;

	}

	/**
	 * ~~~~~~~~~~~~~~~~~~~~~~~~~~ Home Team parameters of Each team
	 * DOWN~~~~~~~~~~~~~~~~~~
	 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	 * 
	 * 
	 */
	public float GoalsScoredEachteamHome(String team) {
		float[] goalsScoredEachteamHome = GoalsScoredEachTeamData(team, "home");
		return goalsScoredEachteamHome[0];
	}

	public float MatchesPlayedEachTeamHome(String team) {
		float[] matchesPlayedEachTeamHome = GoalsScoredEachTeamData(team, "home");
		return matchesPlayedEachTeamHome[1];
	}

	public float GoalsScoredEachTeamHomeAverage(String team) {
		float[] goalsScoredEachTeamHomeAverage = GoalsScoredEachTeamData(team, "home");
		return goalsScoredEachTeamHomeAverage[2];
	}

	/*
	 * ~~~~~~~~~~~~~~~~~~~~~~~~~~ Home Team parameters of Each team
	 * UP~~~~~~~~~~~~~~~~~~~~~
	 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	 * ~~~~~~~
	 * 
	 * 
	 */

	/**
	 * ~~~~~~~~~~~~~~~~~~~~~~~~~~ Away Team parameters of Each team
	 * DOWN~~~~~~~~~~~~~~~~~~
	 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	 * 
	 *
	 */

	public float GoalsScoredEachTeamAway(String team) {
		float[] goalsScoredEachteamAway = GoalsScoredEachTeamData(team, "away");
		return goalsScoredEachteamAway[3];
	}

	public float MatchesPlayedEachTeamAway(String team) {
		float[] matchesPlayedEachTeamAway = GoalsScoredEachTeamData(team, "away");
		return matchesPlayedEachTeamAway[4];
	}

	public float GoalsScoredEachTeamAwayAverage(String team) {
		float[] goalsScoredEachTeamAwayAverage = GoalsScoredEachTeamData(team, "away");
		return goalsScoredEachTeamAwayAverage[5];
	}

	public float GoalsConcededEachTeamAwayAverage(String team) {
		float[] goalsConcededEachTeamAwayAverage = GoalsScoredEachTeamData(team, "away");
		return goalsConcededEachTeamAwayAverage[10];
	}

	/*
	 * ~~~~~~~~~~~~~~~~~~~~~~~~~~ Away Team parameters of Each team
	 * UP~~~~~~~~~~~~~~~~~~
	 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	 * ~~~~
	 * 
	 * 
	 */

	/**
	 * ~~~~~~~~~~~~~~~~~~~~~~~~~~ Team parameters of Each team All
	 * DOWN~~~~~~~~~~~~~~~~~~~
	 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	 * 
	 * 
	 */

	public float GoalsScoredEachteamAll(String team) {
		float[] goalsScoredEachteamAll = GoalsScoredEachTeamData(team, "all");
		return goalsScoredEachteamAll[6];
	}

	public float MatchesPlayedEachTeamAll(String team) {
		float[] matchesPlayedEachTeamAll = GoalsScoredEachTeamData(team, "all");
		return matchesPlayedEachTeamAll[7];
	}

	public float GoalsScoredEachTeamAllAverage(String team) {
		float[] goalsScoredEachTeamAwayAverage = GoalsScoredEachTeamData(team, "all");
		return goalsScoredEachTeamAwayAverage[8];
	}

	/*
	 * ~~~~~~~~~~~~~~~~~~~~~~~~~~ Team parameters of Each team All
	 * UP~~~~~~~~~~~~~~~~~~
	 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	 * ~~~
	 * 
	 * 
	 */

	/**
	 * ~~~~~~~~~~~~~~~~~~~~~~~~~~ Total Home Team parameters
	 * DOWN~~~~~~~~~~~~~~~~~~~~~~~~~
	 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	 * 
	 * @return
	 */
	public float TotalMatchesPlayedByAllTeamsAtHome() {
		float totalMatchesPlayedByAllTeamsAtHome = 0;
		for (String eachTeam : allTeams) {
			float x = MatchesPlayedEachTeamHome(eachTeam);
			totalMatchesPlayedByAllTeamsAtHome += x;
		}
		return totalMatchesPlayedByAllTeamsAtHome;
	}

	public float TotalGoalsScoredByAllTeamsAtHome() {
		float totalGoalsScoredByAllTeamsAtHome = 0;
		for (String eachTeam : allTeams) {
			float x = GoalsScoredEachteamHome(eachTeam);
			totalGoalsScoredByAllTeamsAtHome += x;
		}
		return totalGoalsScoredByAllTeamsAtHome;
	}

	public float AverageGoalsScoredByAllTeamsAtHome() {
		float averageGoalsScoredByAllTeamsAtHome;
		float a = TotalGoalsScoredByAllTeamsAtHome();
		float b = TotalMatchesPlayedByAllTeamsAtHome();
		averageGoalsScoredByAllTeamsAtHome = a / b;
		return averageGoalsScoredByAllTeamsAtHome;
	}
	/*
	 * ~~~~~~~~~~~~~~~~~~~~~~~~~~ Total Home Team parameters
	 * UP~~~~~~~~~~~~~~~~~~~~~~~~~~~
	 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	 * ~~~~~~
	 * 
	 */

	/**
	 * ~~~~~~~~~~~~~~~~~~~~~~~~~~ Total Away Team parameters DOWN
	 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~
	 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	 * 
	 * 
	 */
	public float TotalMatchesPlayedByAllTeamsAway() {
		float totalMatchesPlayedByAllTeamsAway = 0;
		for (String eachTeam : allTeams) {
			float x = MatchesPlayedEachTeamAway(eachTeam);
			totalMatchesPlayedByAllTeamsAway += x;
		}
		return totalMatchesPlayedByAllTeamsAway;
	}

	public float TotalGoalsScoredByAllTeamsAway() {
		float totalGoalsScoredByAllTeamsAway = 0;
		for (String eachTeam : allTeams) {
			float x = GoalsScoredEachTeamAway(eachTeam);
			totalGoalsScoredByAllTeamsAway += x;
		}
		return totalGoalsScoredByAllTeamsAway;
	}

	public float AverageGoalsScoredByAllTeamsAway() {
		float averageGoalsScoredByAllTeamsAway;
		float a = TotalGoalsScoredByAllTeamsAway();
		float b = TotalMatchesPlayedByAllTeamsAway();
		averageGoalsScoredByAllTeamsAway = a / b;
		return averageGoalsScoredByAllTeamsAway;
	}
	/*
	 * ~~~~~~~~~~~~~~~~~~~~~~~~~~ Total Away Team parameters
	 * UP~~~~~~~~~~~~~~~~~~~~~~~~~~
	 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	 * ~~~~~
	 * 
	 */

	/**
	 * ~~~~~~~~~~~~~~~~~~~~~~~~~~ Total All Team parameters
	 * DOWN~~~~~~~~~~~~~~~~~~~~~~~~~~
	 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	 * 
	 * 
	 */
	public float TotalMatchesPlayedInLeague() {
		float totalMatchesPlayedInLeague = 0;
		for (String eachTeam : allTeams) {
			float x = MatchesPlayedEachTeamHome(eachTeam);
			totalMatchesPlayedInLeague += x;
		}
		return totalMatchesPlayedInLeague;
	}

	public float TotalGoalsScoredInLeague() {
		float totalGoalsScoredInLeague = 0;
		for (String eachTeam : allTeams) {
			float x = (GoalsScoredEachTeamAway(eachTeam) + GoalsScoredEachteamHome(eachTeam));
			totalGoalsScoredInLeague += x;
		}
		return totalGoalsScoredInLeague;
	}

	public float AverageGoalsScoredInLeague() {
		float averageGoalsScoredInLeague;
		float a = TotalGoalsScoredInLeague();
		float b = TotalMatchesPlayedInLeague();
		averageGoalsScoredInLeague = a / b;
		return averageGoalsScoredInLeague;
	}
	/*
	 * ~~~~~~~~~~~~~~~~~~~~~~~~~~ Total All Team parameters
	 * UP~~~~~~~~~~~~~~~~~~~~~~~~~~
	 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	 * ~~~~~~
	 * 
	 */

}
