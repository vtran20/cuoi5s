<%@ page import="java.util.*" %>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<HTML>
<HEAD>
    <TITLE>Team Divide Random </TITLE>
    <META NAME="Generator" CONTENT="EditPlus">
    <META NAME="Author" CONTENT="">
    <META NAME="Keywords" CONTENT="">
    <META NAME="Description" CONTENT="">
</HEAD>

<style>
    td.header {
        color: #ff0000;
        text-align: center;
    }

    td.data_input input {
        color: #008000;
    }
</style>
<%!
    class Player implements Comparable<Player>{
        private String name;
        private boolean join;
        private String attack;
        private String defense;
        private String performance;
        private float total;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public boolean isJoin() {
            return join;
        }

        public void setJoin(boolean join) {
            this.join = join;
        }
        public void setJoin(String join) {
            if ("on".equalsIgnoreCase(join) || "true".equalsIgnoreCase(join) || "y".equalsIgnoreCase(join)) {
                this.join = true;
            } else {
                this.join = false;
            }

        }

        public String getAttack() {
            return attack;
        }

        public void setAttack(String attack) {
            this.attack = attack;
        }

        public String getDefense() {
            return defense;
        }

        public void setDefense(String defense) {
            this.defense = defense;
        }

        public String getPerformance() {
            return performance;
        }

        public void setPerformance(String performance) {
            this.performance = performance;
        }

        public float getTotal() {
            float temp = 0;
            if (attack != null) {
                temp += Float.parseFloat(attack);
            }
            if (defense != null) {
                temp += Float.parseFloat(defense);
            }
            if (performance != null) {
                temp += Float.parseFloat(performance);
            }
            return temp;
        }

        public void setTotal(float total) {
            this.total = total;
        }

        @Override
        public int compareTo(Player o) {
            if (o == null) return -1;
            float temp = o.getTotal() - this.getTotal();
            if (temp > 0) return 1;
            else if (temp < 0) return -1;
            else return 0;
        }
    }

    class Team implements Comparable <Team> {
        List players = new ArrayList();
        public void add (Player player) {
            players.add(player);
        }
        public float getTotalPoint () {
            float thisTotal = 0;
            List l = this.getPlayers();
            for (int i=0; i<l.size(); i++) {
                thisTotal += ((Player)l.get(i)).getTotal();
            }
            return thisTotal;
        }

        public List getPlayers() {
            return players;
        }

        public void setPlayers(List players) {
            this.players = players;
        }

        @Override
        public int compareTo(Team o) {
            float thisTotal = 0;
            List l = this.getPlayers();
            for (int i=0; i<l.size(); i++) {
                thisTotal += ((Player)l.get(i)).getTotal();
            }

            float oTotal = 0;
            if (o != null) {
                l = o.getPlayers();
                for (int i=0; i<l.size(); i++) {
                    oTotal += ((Player)l.get(i)).getTotal();
                }
            }
            float temp = oTotal - thisTotal;
            if (temp > 0) return 1;
            else if (temp < 0) return -1;
            else return 0;

        }
    }
    public static int NUMBER_PLAYER = 16;
    public static int CONSTRAINT_POINT = 2;
%>
<BODY>
<%
    String[] players = new String[] {"A Chieu", "Duc", "Nghia AA", "Ban", "Thai", "Tuan", "Vu", "Minh (Ga)", "Nghia DT", "B Anh", "Nguyen", "Chinh", "Hieu", "Duy", "Minh (MSU)", "Other"};
    List listJoinPlayers = new ArrayList();
    List allListPlayers = new ArrayList();
    for (int i = 0; i < NUMBER_PLAYER; i++) {
        String name = request.getParameter("name"+i);
        if (name == null) {
            name = players[i];
        }
        Player player = new Player();
        player.setName(name);
        player.setJoin(request.getParameter("join" + i) != null ? request.getParameter("join" + i) : "N");
        player.setAttack(request.getParameter("attack" + i) != null ? request.getParameter("attack" + i) : "0");
        player.setDefense(request.getParameter("defense" + i) != null ? request.getParameter("defense" + i) : "0");
        player.setPerformance(request.getParameter("performance" + i) != null ? request.getParameter("performance" + i) : "0");
        allListPlayers.add(player);
        if (player.isJoin()) {
            listJoinPlayers.add(player);
        }

    }
%>
<TABLE>
    <form name="team_divide" action="/team1.html" method="get">
    <TR>
        <TD class="header">Name</TD>
        <TD class="header">Join(Y/N)</TD>
        <TD class="header">Attack</TD>
        <TD class="header">Defense</TD>
        <TD class="header">Performance</TD>
        <TD class="header">Total</TD>
    </TR>
    <%
        for (int i = 0; i < allListPlayers.size(); i++) {
            Player player = (Player) allListPlayers.get(i);
    %>
    <TR>
        <TD class="data_input"><input type="text" name="name<%=i%>" value="<%=player.getName()%>"/></TD>
        <TD class="data_input"><input type="checkbox" name="join<%=i%>" <%=player.isJoin()? "checked":""%>/></TD>
        <TD class="data_input"><input type="text" name="attack<%=i%>" value="<%=player.getAttack()%>"/></TD>
        <TD class="data_input"><input type="text" name="defense<%=i%>" value="<%=player.getDefense()%>"/></TD>
        <TD class="data_input"><input type="text" name="performance<%=i%>" value="<%=player.getPerformance()%>"/></TD>
        <TD class="data_input"><%=player.getTotal()%></TD>
    </TR>
    <%}%>
</TABLE>
<br>
<div>Number of Team (2 or 3 teams): <input type="text" name="team" value="<%=request.getParameter("team") != null?request.getParameter("team"): "3"%>"/></div>
<div><input type="submit" name="refresh" value="Refresh"></div>
</form>

<%
if (!(request.getParameter("team") != null && (request.getParameter("team").equals("2") || request.getParameter("team").equals("3")))) {
%>
    <div>Only support 2 or 3 teams at the time.</div>
<%
} else {
%>
<%--
1. Sort players list by total point.
2. Divide by group based on number of team. Ex: 3 teams divide to each group 3 players.
3. Algorithm:
   + First 3 groups: pick random player for each team.
   + The rest groups will be picked based on total point.
--%>
<%
    Collections.sort(listJoinPlayers);
    String numTeam = request.getParameter("team");
    int team = 3;
    if (numTeam != null) {
        team = Integer.parseInt(numTeam);
    }

    //3 teams
    List allTeams = new ArrayList();
    if (team == 3) {
        int group = listJoinPlayers.size()/team;
        Team teamA;
        Team teamB;
        Team teamC;
        Random rand = new Random();
        //2 first group will be divided randomly.
        boolean continues = true;
        do {
            teamA = new Team();
            teamB = new Team();
            teamC = new Team();
            allTeams.add(teamA);
            allTeams.add(teamB);
            allTeams.add(teamC);

            for (int i = 0; i < group; i++) {
                List groupPlayer = new ArrayList<String>(listJoinPlayers.subList(i * team, (i * team) + team));
                int r = 0;
                if (i < 2) { //generate team randomly
                    r = rand.nextInt(groupPlayer.size()) + 1;
                    teamA.add((Player) groupPlayer.remove(r - 1));
                    r = rand.nextInt(groupPlayer.size()) + 1;
                    teamB.add((Player) groupPlayer.remove(r - 1));
                    r = rand.nextInt(groupPlayer.size()) + 1;
                    teamC.add((Player) groupPlayer.remove(r - 1));
                } else { //generate team based on the existing team.
                    Collections.sort(allTeams);
                    teamA.add((Player) groupPlayer.get(2));
                    teamB.add((Player) groupPlayer.get(1));
                    teamC.add((Player) groupPlayer.get(0));
                }
            }

            //Sort total point
            Collections.sort(allTeams);
            Team first = (Team) allTeams.get(0);
            Team last = (Team) allTeams.get(team-1);
            out.write(""+(first.getTotalPoint() - last.getTotalPoint()));
            if (first.getTotalPoint() - last.getTotalPoint() < CONSTRAINT_POINT) {
                continues = false;
            }
        } while (continues);
%>
<TABLE border=1>
    <TR>
        <TD class="header">Team A</TD>
        <TD class="header">Team B</TD>
        <TD class="header">Team C</TD>
    </TR>
    <%
        for (int i = 0; i < teamA.getPlayers().size(); i++) {
    %>
    <TR>
        <TD class="data_input"><%=((Player)teamA.getPlayers().get(i)).getName()%></TD>
        <TD class="data_input"><%=((Player)teamB.getPlayers().get(i)).getName()%></TD>
        <TD class="data_input"><%=((Player)teamC.getPlayers().get(i)).getName()%></TD>
    </TR>
    <%
        }
    %>
    <TR>
        <TD class="data_input"><%=teamA.getTotalPoint()%></TD>
        <TD class="data_input"><%=teamB.getTotalPoint()%></TD>
        <TD class="data_input"><%=teamC.getTotalPoint()%></TD>
    </TR>

</TABLE>

<%  //2 teams
    } else if (team == 2) {
        Team teamA ;
        Team teamB;
        int group = listJoinPlayers.size()/team;

        Random rand = new Random();
        //2 first group will be divided randomly.
    boolean continues = true;
    do {
        teamA = new Team();
        teamB = new Team();
        allTeams.add(teamA);
        allTeams.add(teamB);
        for (int i=0; i < group; i++) {
            List groupPlayer = new ArrayList<String>(listJoinPlayers.subList(i*team, (i*team)+team));
            int r = 0;
            if (i<2) { //generate team randomly
                r = rand.nextInt(groupPlayer.size()) + 1;
                teamA.add((Player) groupPlayer.remove(r - 1));
                r = rand.nextInt(groupPlayer.size()) + 1;
                teamB.add((Player) groupPlayer.remove(r - 1));
            } else { //generate team based on the existing team.
                Collections.sort(allTeams);
                teamA.add((Player) groupPlayer.get(1));
                teamB.add((Player) groupPlayer.get(0));
            }
        }
        //Sort total point
        Collections.sort(allTeams);
        Team first = (Team) allTeams.get(0);
        Team last = (Team) allTeams.get(team-1);
        if (first.getTotalPoint() - last.getTotalPoint() < CONSTRAINT_POINT) {
            continues = false;
        }
    } while (continues);

%>
<TABLE border=1>
    <TR>
        <TD class="header">Team A</TD>
        <TD class="header">Team B</TD>
    </TR>
    <%
        for (int i = 0; i < teamA.getPlayers().size(); i++) {
    %>
    <TR>
        <TD class="data_input"><%=((Player)teamA.getPlayers().get(i)).getName()%></TD>
        <TD class="data_input"><%=((Player)teamB.getPlayers().get(i)).getName()%></TD>
    </TR>
    <%
        }
    %>
    <TR>
        <TD class="data_input"><%=teamA.getTotalPoint()%></TD>
        <TD class="data_input"><%=teamB.getTotalPoint()%></TD>
    </TR>

</TABLE>
<%
    }
}
%>
</BODY>
</HTML>