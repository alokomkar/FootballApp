package app.football.com.footballapp.model;

import android.util.Log;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by cognitive on 10/24/16.
 */
public class FootballTeam {

    private String teamName;
    private int teamScore;
    private int GD; //Total Goal Scored - Total Goal Conceded
    private int totalMatch;
    private int totalWon;
    private int totalLost;
    private int totalDrawn;
    private int totalPoints;

    private ArrayList<Scores> scores = new ArrayList<>();
    private String descriptionText;

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public ArrayList<Scores> getScores() {
        return scores;
    }

    public void setScores(ArrayList<Scores> scores) {
        this.scores = scores;
    }

    public int getTeamScore() {
        return teamScore;
    }

    public void setTeamScore(int teamScore) {
        this.teamScore = teamScore;
    }

    public int getGD() {
        return GD;
    }

    public void setGD(int GD) {
        this.GD = GD;
    }

    public int getTotalMatch() {
        return totalMatch;
    }

    public void setTotalMatch(int totalMatch) {
        this.totalMatch = totalMatch;
    }

    public int getTotalWon() {
        return totalWon;
    }

    public void setTotalWon(int totalWon) {
        this.totalWon = totalWon;
    }

    public int getTotalLost() {
        return totalLost;
    }

    public void setTotalLost(int totalLost) {
        this.totalLost = totalLost;
    }

    public int getTotalDrawn() {
        return totalDrawn;
    }

    public void setTotalDrawn(int totalDrawn) {
        this.totalDrawn = totalDrawn;
    }

    public int getTotalPoints() {
        totalPoints = totalDrawn + totalWon;
        return totalPoints;
    }

    public static FootballTeam createObject(Map.Entry<String, JsonElement> map) {

        FootballTeam footballTeam = new FootballTeam();
        footballTeam.setTeamName( map.getKey() );
        footballTeam.setTeamScore(0);

        ArrayList<Scores> scores = new ArrayList<>();
        footballTeam.setScores( scores );
        JsonObject jsonObject = map.getValue().getAsJsonObject();
        int teamScore = footballTeam.getTeamScore();
        int goalsScored = 0;
        int goalsConceded = 0;
        int totalWon = 0;
        int totalLost = 0;
        int totalDrawn = 0;
        for( Map.Entry<String, JsonElement> key : jsonObject.entrySet() ) {
            Log.d("Values", key.getKey() + " : " + key.getValue().getAsString());
            Scores scores1 = new Scores( key.getKey(), key.getValue().getAsString());
            int ownScore = scores1.getOwnScore();
            int opponentScore = scores1.getOpponentScore();
            goalsScored += ownScore;
            goalsConceded += opponentScore;
            scores.add( scores1 );
            if( opponentScore > ownScore ) {
                totalLost++;
            }
            else if( opponentScore < ownScore ) {
                totalWon++;
            }
            else {
                totalDrawn++;
            }
            teamScore += scores1.calculateTeamScore();
        }

        footballTeam.setTotalDrawn(totalDrawn);
        footballTeam.setTotalWon(totalWon);
        footballTeam.setTotalLost(totalLost);
        footballTeam.setTotalMatch(scores.size());
        footballTeam.setGD(goalsScored - goalsConceded);
        footballTeam.setTeamScore(teamScore);
        return footballTeam;
    }

    public String getDescriptionText(int rank) {
        if( descriptionText == null ) {
            descriptionText = "Rank " + ( rank + 1 ) +": " + getTeamName() + "\n\n";
            descriptionText += "Played : " + totalMatch + "\n";
            descriptionText += "Won : " + totalWon + "\n";
            descriptionText += "Lost : " + totalLost + "\n";
            descriptionText += "Drawn : " + totalDrawn + "\n";
            descriptionText += "GD : " + ((GD > 0) ? "+" + GD : GD == 0 ? "0" : ""+GD );
        }
        return descriptionText;
    }

    public void setDescriptionText(String descriptionText) {
        this.descriptionText = descriptionText;
    }

    public static class Scores {

        private String matchName;
        private String matchScore;
        private int ownScore;
        private int opponentScore;

        public Scores(String matchName, String matchScore) {
            this.matchName = matchName;
            this.matchScore = matchScore;
            this.ownScore = Integer.parseInt(matchScore.split("-")[0]);
            this.opponentScore = Integer.parseInt(matchScore.split("-")[1]);
        }

        public String getMatchName() {
            return matchName;
        }

        public void setMatchName(String matchName) {
            this.matchName = matchName;
        }

        public String getMatchScore() {
            return matchScore;
        }

        public void setMatchScore(String matchScore) {
            this.matchScore = matchScore;
        }

        public int getOwnScore() {
            return ownScore;
        }

        public void setOwnScore(int ownScore) {
            this.ownScore = ownScore;
        }

        public int getOpponentScore() {
            return opponentScore;
        }

        public void setOpponentScore(int opponentScore) {
            this.opponentScore = opponentScore;
        }

        public int calculateTeamScore() {
            if( ownScore > opponentScore ) {
                return 3;
            }
            else if( opponentScore == ownScore ) {
                return 1;
            }
            else
            return 0;
        }
    }
}
