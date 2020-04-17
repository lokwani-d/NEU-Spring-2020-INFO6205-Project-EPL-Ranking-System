package src.main;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

import org.apache.commons.math3.distribution.PoissonDistribution;

/**
 * 
 * @author deepak_lokwani
 * @author rupesh_alasundkar
 * 
 *         NUID: 001316769 & 001304532
 * 
 *         Project: Predictive Ranking System of English Premiere League
 *         this.File: Driver.java Factors Considered: Location - Home or Away
 *         Attack Strength - goal scoring ability Defense Strength - goal
 *         conceding weakness
 *
 */
public class Driver {

	/**
	 * This is my main class. It initiates all the pre-requisite functions that are
	 * required to tabulate the final point table. The driver constructor initially
	 * runs and makes assigns the combination id to each of the match that is played
	 * or is supposed to played.
	 * 
	 * This class contains the algorithm design for predicting the ranking of all
	 * the clubs playing in the EPL based on the previous records of the current
	 * season.
	 * 
	 * This class contains two such methods, the details of which are explained in
	 * detail in the project report or the readme file
	 */

	public Driver() {
		/**
		 * The driver constructor initially runs and makes assigns the combination id to
		 * each of the match that is played or is supposed to played.
		 */
		MakePairs makePairs = new MakePairs();
		for (int x = 0; x < RankingSystem.allTeams.length - 1; x++) {
			for (int y = x + 1; y < RankingSystem.allTeams.length; y++) {
				makePairs.MakePairsOnce(RankingSystem.allTeams[x], RankingSystem.allTeams[y]);
			}
		}

		makePairs.matchesPlayed();
		makePairs.matchesNotPlayed();
	}

	public static void main(String[] args) {
		/**
		 * This is my main class. It primarily calls two functions and does two
		 * functions. One being it, calls the function to tabulate the final point table
		 * Second being, it creates a new input reader to be able to take the input from
		 * the reader and find out the probability density function between the two
		 * given teams
		 */

		Driver driver = new Driver();
		try (Scanner reader = new Scanner(System.in)) {
			System.out.println("Let' get started with creating a LEAGUE POINT TABLE");
			System.out.println("We have deployed two methods to calculate the table");
			System.out.println(
					"\nMethod A: It takes the cumulative probability of a team to win over the opponent and takes the decision ");
			System.out.println(
					"Method B: It takes the discrete probability of all goal-score combination and then takes the decision ");
			System.out.println("I'd prefer Method B though for finer accuracy ;)");
			System.out.print("Please choose Your Method (A or B): ");
			String userInputMethod = reader.nextLine();
			System.out.println("Please wait while we compute the point table");
			/*
			 * call the method to create final league point table
			 */
			driver.createPointTable(userInputMethod);

			System.out.println("");
			System.out.println("Please Enter Club Names to find one-on-one Probability Distribution Function");
			System.out.println("Team names are case-sensitive");

			System.out.print("Choose Home Team: ");
			String userInputHomeTeam = reader.nextLine();

			System.out.print("Choose Away Team: ");
			String userInputAwayTeam = reader.nextLine();
			System.out.println("");

			try {
				if (MakePairs.combination_id.containsKey(userInputHomeTeam + "-" + userInputAwayTeam)) {
					driver.ProbabilityDistributionOfTwoTeams(userInputHomeTeam, userInputAwayTeam, "c");
				} else {
					/*
					 * Try catch blocks throws the new exception when the input team names are not
					 * found in the record
					 */
					throw new Exception();
				}
			} catch (Exception e) {
				System.out.println("Sorry! You seem to have entered wrong Club details");
				System.out.println("Club names are case-sensitive");
				System.out.println("Please enter the valid English Premiere Clubs");
//				e.printStackTrace();
			}
		}
	}

	/**
	 * This method is responsible for creating a point table and sorting it
	 * rank-wise on the console
	 * 
	 * @param userInputMethod : user inputs the method A or B
	 */
	public void createPointTable(String userInputMethod) {

		Driver driver = new Driver();
		try {
			if (userInputMethod.equalsIgnoreCase("a") || userInputMethod.equalsIgnoreCase("b")) {
				

				/*
				 * This loop iterates over each match that was supposed to be played but could
				 * not be played and thus makes the prediction based on the previous records
				 * 
				 */
				for (String combination : MakePairs.notPlayed.keySet()) {
					String teams[] = combination.split("-");
					String teamA = teams[0];
					String teamB = teams[1];
					driver.ProbabilityDistributionOfTwoTeams(teamA, teamB, userInputMethod);
				}
			} else {
				/*
				 * Try catch blocks throws the new exception when the input method names are not
				 * valid
				 */
				throw new Exception();

			}
		} catch (Exception e) {
			System.out.println("We couldn't understand your choice");
			System.out.println("Please select any one of the above methods");
			System.out.println("Meanwhile below is the table for the matches played until now");
//			e.printStackTrace();
		}

		/*
		 * @param: sortedbyValue contains the sorted point table
		 */
		Map<String, Integer> sortedByValue = sortByValue(MakePairs.PointTable);
		System.out.println("The updated point table with selected method is as below");
		System.out.println("\n\nRank\t\tClub Name\t\t\t\t\tFinalPoints\n");
		int i = 1;
		for (String eachItem : sortedByValue.keySet()) {
			System.out.println(i + "\t\t" + eachItem + "\t\t\t\t\t" + sortedByValue.get(eachItem));
			i++;
		}
		System.out.println("\n" + sortedByValue);

	}



	/**
	 * this function sorts my HashMap i.e. my final points table
	 * 
	 * @param pointmap : inputs the original point-table
	 * @return: sorted hashmap of the point table
	 */
	public static Map<String, Integer> sortByValue(final Map<String, Integer> pointmap) {
		return pointmap.entrySet().stream().sorted((Map.Entry.<String, Integer>comparingByValue().reversed()))
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
	}

	/**
	 * this class is used to find out the total probability distribution function of
	 * any two given teams based on the attack strength and the defense strength of
	 * a team this class is also used to decide the points distribution based for
	 * creating the point table
	 * 
	 * @param homeTeam: Home Team
	 * @param awayTeam: Away Team
	 * @param method:   method A, B or C ( method C is hard-coded for the internal
	 *                  use)
	 */
	public void ProbabilityDistributionOfTwoTeams(String homeTeam, String awayTeam, String method) {

		RankingSystem rankingSystem = new RankingSystem();

		float seasonAverageHomeGoals = rankingSystem.AverageGoalsScoredByAllTeamsAtHome();

		float homeTeamAverageHomeGoals = rankingSystem.GoalsScoredEachTeamHomeAverage(homeTeam);
		float homeTeamAttackStrength = homeTeamAverageHomeGoals / seasonAverageHomeGoals;

		float awayTeamAverageGoalsConcededAway = rankingSystem.GoalsConcededEachTeamAwayAverage(awayTeam);
		float awayTeamDefenseStrength = awayTeamAverageGoalsConcededAway / seasonAverageHomeGoals;

		/*
		 * homeTeamGoalsProbability: denotes the number of goals Home Team will score
		 */
		float homeTeamGoalsProbability = homeTeamAttackStrength * awayTeamDefenseStrength * seasonAverageHomeGoals;

		float seasonAverageAwayGoals = rankingSystem.AverageGoalsScoredByAllTeamsAway();
		float awayTeamAverageAwayGoals = rankingSystem.GoalsScoredEachTeamAwayAverage(awayTeam);
		float awayTeamAttackStrength = awayTeamAverageAwayGoals / seasonAverageAwayGoals;

		float homeTeamAvergaeGoalsConcededAway = rankingSystem.GoalsConcededEachTeamAwayAverage(homeTeam);
		float homeTeamDefenseStrength = homeTeamAvergaeGoalsConcededAway / seasonAverageAwayGoals;

		/*
		 * awayTeamGoalsProbability: denotes the number of goals Away Team will score
		 */
		float awayTeamGoalsProbability = awayTeamAttackStrength * homeTeamDefenseStrength * seasonAverageAwayGoals;

		/*
		 * Poisson distribution is used to find out the discrete as well as the
		 * cumulative distribution to know of the result of each match
		 */
		PoissonDistribution homeTeamPDF = new PoissonDistribution(homeTeamGoalsProbability);
		PoissonDistribution awayTeamPDF = new PoissonDistribution(awayTeamGoalsProbability);

		float AwayTeamWinningProbabilty = 0;
		float drawProability = 0;
		float HomeTeamWinningPrabibility = 0;

		double[][] finalScoreProbabilityMatrix = new double[6][6];
		int finalScore[][] = new int[1][2];
		finalScore[0][0] = 0;
		finalScore[0][1] = 0;

		double maximumProbabilityCellValue = 0;

		if (method.equalsIgnoreCase("c")) {
			System.out.println("Home Team " + homeTeam + "'s  final score probability:  "
					+ Math.round(homeTeamGoalsProbability * 100.0) / 100.0 + " goals");
			System.out.println("Away Team " + awayTeam + "'s  final score probability:  "
					+ Math.round(awayTeamGoalsProbability * 100.0) / 100.0 + " goals");
			System.out.println("\n" + homeTeam + "'s  and  " + awayTeam + "'s different score combinations and their "
					+ "corresponding probabilities are as follows: ");
		}

		/*
		 * this loop checks for all the 36 combinations of the final match score
		 * possibility and stores it in a variable of scoreProbabilty
		 */
		for (int homeTeamGoalCase = 0; homeTeamGoalCase < 6; homeTeamGoalCase++) {
			for (int awayTeamGoalCase = 0; awayTeamGoalCase < 6; awayTeamGoalCase++) {
				double scoreProability = homeTeamPDF.probability(homeTeamGoalCase)
						* awayTeamPDF.probability(awayTeamGoalCase) * 100;

				finalScoreProbabilityMatrix[homeTeamGoalCase][awayTeamGoalCase] = scoreProability;
				/*
				 * It prints my 6X6 matrix 
				 */
				if (method.equalsIgnoreCase("c")) {
					System.out.print(homeTeamGoalCase + " : " + awayTeamGoalCase + " = "
							+ Math.round(finalScoreProbabilityMatrix[homeTeamGoalCase][awayTeamGoalCase] * 100.0)
									/ 100.0
							+ " %      ");
				}

				if (maximumProbabilityCellValue < scoreProability) {
					maximumProbabilityCellValue = scoreProability;
					finalScore[0][0] = homeTeamGoalCase;
					finalScore[0][1] = awayTeamGoalCase;
				}

				/*
				 * checks the three condition of a team winning based on the final goal difference of a match 
				 */
				if (homeTeamGoalCase - awayTeamGoalCase < 0) {
					AwayTeamWinningProbabilty += homeTeamPDF.probability(homeTeamGoalCase)
							* awayTeamPDF.probability(awayTeamGoalCase) * 100;

				} else if (homeTeamGoalCase - awayTeamGoalCase == 0) {
					drawProability += homeTeamPDF.probability(homeTeamGoalCase)
							* awayTeamPDF.probability(awayTeamGoalCase) * 100;

				} else if (homeTeamGoalCase - awayTeamGoalCase > 0) {
					HomeTeamWinningPrabibility += homeTeamPDF.probability(homeTeamGoalCase)
							* awayTeamPDF.probability(awayTeamGoalCase) * 100;

				}
			}
			if (method.equalsIgnoreCase("c")) {
				System.out.println("");
			}
		}
		
		/*
		 * It prints my final values of the match
		 */
		if (method.equalsIgnoreCase("c")) {
			System.out.println("\nWe predict that the final score for " + homeTeam + " : " + awayTeam + "  "
					+ finalScore[0][0] + " : " + finalScore[0][1] + " has the maximum probability around "
					+ Math.round(maximumProbabilityCellValue * 100.0) / 100.0 + " %");
			System.out.println("");
			System.out.println("Chances of " + homeTeam + " winning are "
					+ Math.round(HomeTeamWinningPrabibility * 100.0) / 100.0 + " %");
			System.out.println("Chances of a Draw are  " + Math.round(drawProability * 100.0) / 100.0 + " %");
			System.out.println("Chances of " + awayTeam + " winning are "
					+ Math.round(AwayTeamWinningProbabilty * 100.0) / 100.0 + " %");
		}
		
		/*
		 * It is deplyed when the method A is passed
		 */
		if (method.equalsIgnoreCase("a")) {

			if ((HomeTeamWinningPrabibility > drawProability)
					&& (HomeTeamWinningPrabibility > AwayTeamWinningProbabilty)) {
				int currentPoints = MakePairs.PointTable.get(homeTeam);
				int updatedPoints = currentPoints + 3;
				MakePairs.PointTable.put(homeTeam, updatedPoints);
			}

			else if ((AwayTeamWinningProbabilty > drawProability)
					&& (AwayTeamWinningProbabilty > HomeTeamWinningPrabibility)) {
				int currentPoints = MakePairs.PointTable.get(awayTeam);
				int updatedPoints = currentPoints + 3;
				MakePairs.PointTable.put(awayTeam, updatedPoints);
			}

			else {
				int currentPoints1 = MakePairs.PointTable.get(homeTeam);
				int updatedPoints1 = currentPoints1 + 1;
				MakePairs.PointTable.put(homeTeam, updatedPoints1);
				int currentPoints2 = MakePairs.PointTable.get(awayTeam);
				int updatedPoints2 = currentPoints2 + 1;
				MakePairs.PointTable.put(awayTeam, updatedPoints2);
			}
		}

		/*
		 * It is deployed when method B is passed
		 */
		if (method.equalsIgnoreCase("b")) {
			if (finalScore[0][0] > finalScore[0][1]) {
				int currentPoints = MakePairs.PointTable.get(homeTeam);
				int updatedPoints = currentPoints + 3;
				MakePairs.PointTable.put(homeTeam, updatedPoints);
			} else if (finalScore[0][0] < finalScore[0][1]) {
				int currentPoints = MakePairs.PointTable.get(awayTeam);
				int updatedPoints = currentPoints + 3;
				MakePairs.PointTable.put(awayTeam, updatedPoints);
			} else if (finalScore[0][0] == finalScore[0][1]) {
				int currentPoints1 = MakePairs.PointTable.get(homeTeam);
				int updatedPoints1 = currentPoints1 + 1;
				MakePairs.PointTable.put(homeTeam, updatedPoints1);
				int currentPoints2 = MakePairs.PointTable.get(awayTeam);
				int updatedPoints2 = currentPoints2 + 1;
				MakePairs.PointTable.put(awayTeam, updatedPoints2);
			}
		}
	}
}
