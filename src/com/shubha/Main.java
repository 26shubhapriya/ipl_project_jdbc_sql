package com.shubha;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;

public class Main {

    public static final int MATCH_ID = 0;
    public static final int SEASON = 1;
    public static final int CITY = 2;
    public static final int DATE = 3;
    public static final int TEAM1 = 4;
    public static final int TEAM2 = 5;
    public static final int TOSS_WINNER = 6;
    public static final int TOSS_DECISION = 7;
    public static final int RESULT = 8;
    public static final int DL_APPLIED = 9;
    public static final int WINNER = 10;
    public static final int WIN_BY_RUNS = 11;
    public static final int WIN_BY_WICKETS = 12;
    public static final int PLAYER_OF_MATCH = 13;
    public static final int VENUE = 14;

    public static final int INNING = 1;
    public static final int BATTING_TEAM = 2;
    public static final int BOWLING_TEAM = 3;
    public static final int OVER = 4;
    public static final int BALL = 5;
    public static final int BATSMAN = 6;
    public static final int NON_STRIKER = 7;
    public static final int BOWLER = 8;
    public static final int IS_SUPER_OVER = 9;
    public static final int WIDE_RUNS = 10;
    public static final int BYE_RUNS = 11;
    public static final int LEG_BYE_RUNS = 12;
    public static final int NO_BALL_RUNS = 13;
    public static final int PENALTY_RUNS = 14;
    public static final int BATSMAN_RUNS = 15;
    public static final int EXTRA_RUNS = 16;
    public static final int TOTAL_RUNS = 17;


    public static void findNumberOfMatchesInPerYear(List<Match> matchDataList) {
        Map<String, Integer> matchesPerYear = new TreeMap();

        for (int i = 0; i < matchDataList.size(); i++) {
            String year = matchDataList.get(i).getSeason();
            if (matchesPerYear.containsKey(year)) {
                int value = matchesPerYear.get(year);
                matchesPerYear.put(year, value + 1);
            } else {
                matchesPerYear.put(year, 1);
            }
        }

        System.out.println(matchesPerYear);

    }

    public static void findNumberOfMatchesWonPerTeamPerYear(List<Match> matchDataList) {
        Map<String, Integer> matchesWonByTeam = new TreeMap<>();

        for (int i = 0; i < matchDataList.size(); i++) {
            String team1 = matchDataList.get(i).getTeam1();
            if (!(matchesWonByTeam.containsKey(team1))) {
                matchesWonByTeam.put(team1, 0);
            }
        }
        for (int i = 0; i < matchDataList.size(); i++) {
            String winner = matchDataList.get(i).getWinner();
            if ((matchesWonByTeam.containsKey(winner))) {
                int value = matchesWonByTeam.get(winner);
                matchesWonByTeam.put(winner, value + 1);
            }
        }

        System.out.println(matchesWonByTeam);

    }

    public static void findExtraRunsConcededPerTeamIn2016(List<Match> matchDataList, List<Delivery> deliveryDataList) {
        Map<String, Integer> extraRunsPerTeam = new HashMap<>();
        List<Integer> matchIds = new ArrayList();

        for (int i = 0; i < matchDataList.size(); i++) {
            if (matchDataList.get(i).getSeason().equals("2016")) {
                matchIds.add(matchDataList.get(i).getId());
                if (!(extraRunsPerTeam.containsKey(matchDataList.get(i).getTeam1()))) {
                    extraRunsPerTeam.put(matchDataList.get(i).getTeam1(), 0);
                }
            }
        }
        for (Integer id : matchIds) {
            for (Delivery details : deliveryDataList) {
                if (details.getMatchId() == id) {
                    String battingTeamName = details.getBattingTeam();
                    int totalRuns = details.getExtraRuns();
                    int runsSecured = extraRunsPerTeam.get(battingTeamName);
                    extraRunsPerTeam.put(battingTeamName, totalRuns + runsSecured);
                }
            }
        }
        System.out.println(extraRunsPerTeam);

    }

    public static void findTopEconomicalBowlersIn2015(List<Match> matchDataList, List<Delivery> deliveryDataList) {
        List<Integer> matchIds = new ArrayList();

        for (int i = 0; i < matchDataList.size(); i++) {
            if (matchDataList.get(i).getSeason().equals("2015")) {
                matchIds.add(matchDataList.get(i).getId());
            }
        }

        Map<String, Integer> bowlerAndNumberOfBalls = new HashMap<>();
        Map<String, Integer> bowlerAndGivenRuns = new HashMap<>();
        for (Integer id : matchIds) {
            for (Delivery details : deliveryDataList) {
                if (details.getMatchId() == id) {
                    String bowler = details.getBowler();
                    int numberOfTotalRuns = details.getTotalRuns();
                    if (bowlerAndNumberOfBalls.containsKey(bowler)) {
                        int numberOfBalls = bowlerAndNumberOfBalls.get(bowler);
                        int alreadyGivenRuns = bowlerAndGivenRuns.get(bowler);
                        bowlerAndNumberOfBalls.put(bowler, numberOfBalls + 1);
                        bowlerAndGivenRuns.put(bowler, numberOfTotalRuns + alreadyGivenRuns);

                    } else {
                        bowlerAndNumberOfBalls.put(bowler, 1);
                        bowlerAndGivenRuns.put(bowler, details.getTotalRuns());
                    }
                }
            }
        }

        Set<String> bowlerNames = bowlerAndNumberOfBalls.keySet();
        Map<Double, String> eco = new TreeMap<>();
        for (String name : bowlerNames) {
            float runs = bowlerAndGivenRuns.get(name);
            float balls = bowlerAndNumberOfBalls.get(name);
            int noOfOvers = (int) balls / 6;
            float remainedBalls = balls % 6;
            float remainedOvers = remainedBalls / 6;
            float wholeOvers = remainedOvers + noOfOvers;
            double economy = runs / wholeOvers;
            eco.put(economy, name);
        }
        TreeMap<Double, String> economyOfThreeBowlers = new TreeMap<>();
        Set<Double> economyOfBowlers = eco.keySet();
        int count = 0;
        for (Double economy : economyOfBowlers) {
            count++;
            economyOfThreeBowlers.put(economy, eco.get(economy));
            if (count == 3) {
                break;
            }
        }
        System.out.println(economyOfThreeBowlers);

    }

    public static void main(String[] args) {
        List<Delivery> deliveries = getDeliveriesData();
        List<Match> matches = getMatchesData();

        findNumberOfMatchesInPerYear(matches);
        findNumberOfMatchesWonPerTeamPerYear(matches);
        findExtraRunsConcededPerTeamIn2016(matches, deliveries);
        findTopEconomicalBowlersIn2015(matches, deliveries);
        findNumberOfTossWonByEachTeamInAllYears(matches);

    }

    private static void findNumberOfTossWonByEachTeamInAllYears(List<Match> matchDataList) {
        Map<String, Integer> tossWon = new TreeMap();
        for (int i = 0; i < matchDataList.size(); i++) {
            String winner = matchDataList.get(i).getTossWinner();
            if (!tossWon.containsKey(winner)) {
                int tossCount = 1;
                tossWon.put(winner, tossCount);
            } else {
                int tossCount1 = tossWon.get(winner);
                tossCount1++;
                tossWon.put(winner, tossCount1);

            }
        }
        System.out.println(tossWon);
        System.out.println();
    }

    private static List<Match> getMatchesData() {

        List<Match> matches = new ArrayList<>();

        try {
            String url = "jdbc:mysql://localhost:3306/matches";
            String username = "root";
            String password = "1234";
            String query = "select * from matchestable";

            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(url, username, password);

            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(query);

            while (rs.next()) {
                Match match = new Match();

                int matchId = Integer.parseInt(rs.getString(1));
                int season = Integer.parseInt(rs.getString(2));

                match.setId(matchId);
                match.setSeason(String.valueOf(season));
                match.setWinner(rs.getString(11));
                match.setTeam1(rs.getString(5));
                match.setTossWinner(rs.getString(7));
                matches.add(match);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return matches;
    }


    private static List<Delivery> getDeliveriesData() {

        List<Delivery> deliveries = new ArrayList<>();

        try {
            String url = "jdbc:mysql://localhost:3306/matches";
            String username = "root";
            String password = "1234";
            String query = "select * from deliveries_1";

            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(url, username, password);

            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(query);

            while (rs.next()) {
                Delivery delivery = new Delivery();

                int matchId = Integer.parseInt(rs.getString(1));
                int extraRuns = Integer.parseInt(rs.getString(17));
                int totalRuns = Integer.parseInt(rs.getString(18));
                int batsmanRuns = Integer.parseInt(rs.getString(16));

                delivery.setExtraRuns(extraRuns);
                delivery.setTotalRuns(totalRuns);
                delivery.setMatchId(matchId);
                delivery.setBowlingTeam(rs.getString(4));
                delivery.setBowler(rs.getString(9));
                delivery.setBatsmanRuns(batsmanRuns);
                delivery.setBattingTeam(rs.getString(3));
                deliveries.add(delivery);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return deliveries;
    }
}

