<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.Random" %>
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

<%
    String team = request.getParameter("team");
    if ("2".equals(team)) {
        List group1 = new ArrayList();
        group1.add(request.getParameter("teamA0") != null ? request.getParameter("teamA0") : "Ban");
        group1.add(request.getParameter("teamB0") != null ? request.getParameter("teamB0") : "A Chieu");
        group1.add(request.getParameter("teamA1") != null ? request.getParameter("teamA1") : "Tuan");
        group1.add(request.getParameter("teamB1") != null ? request.getParameter("teamB1") : "Nguyen");

        List group2 = new ArrayList();
        group2.add(request.getParameter("teamA2") != null ? request.getParameter("teamA2") : "Nghia DT");
        group2.add(request.getParameter("teamB2") != null ? request.getParameter("teamB2") : "Minh");
        group2.add(request.getParameter("teamA3") != null ? request.getParameter("teamA3") : "Duc");
        group2.add(request.getParameter("teamB3") != null ? request.getParameter("teamB3") : "Vu");

        List group3 = new ArrayList();
        group3.add(request.getParameter("teamA4") != null ? request.getParameter("teamA4") : "Thai");
        group3.add(request.getParameter("teamB4") != null ? request.getParameter("teamB4") : "Duy");
        group3.add(request.getParameter("teamA5") != null ? request.getParameter("teamA5") : "Ban Minh");
        group3.add(request.getParameter("teamB5") != null ? request.getParameter("teamB5") : "Ban Duc");

        List group4 = new ArrayList();
        group4.add(request.getParameter("teamA6") != null ? request.getParameter("teamA6") : "Fernando");
        group4.add(request.getParameter("teamB6") != null ? request.getParameter("teamB6") : "Ban Fernando");

        List teamA = new ArrayList();
        List teamB = new ArrayList();

        Random rand = new Random();
        int r = 0;
//Team A
        r = rand.nextInt(group1.size()) + 1;
        teamA.add(group1.remove(r - 1));
        r = rand.nextInt(group1.size()) + 1;
        teamA.add(group1.remove(r - 1));
        r = rand.nextInt(group2.size()) + 1;
        teamA.add(group2.remove(r - 1));
        r = rand.nextInt(group2.size()) + 1;
        teamA.add(group2.remove(r - 1));
        r = rand.nextInt(group3.size()) + 1;
        teamA.add(group3.remove(r - 1));
        r = rand.nextInt(group3.size()) + 1;
        teamA.add(group3.remove(r - 1));
        r = rand.nextInt(group4.size()) + 1;
        teamA.add(group4.remove(r - 1));

//Team B
        r = rand.nextInt(group1.size()) + 1;
        teamB.add(group1.remove(r - 1));
        r = rand.nextInt(group1.size()) + 1;
        teamB.add(group1.remove(r - 1));
        r = rand.nextInt(group2.size()) + 1;
        teamB.add(group2.remove(r - 1));
        r = rand.nextInt(group2.size()) + 1;
        teamB.add(group2.remove(r - 1));
        r = rand.nextInt(group3.size()) + 1;
        teamB.add(group3.remove(r - 1));
        r = rand.nextInt(group3.size()) + 1;
        teamB.add(group3.remove(r - 1));
        r = rand.nextInt(group4.size()) + 1;
        teamB.add(group4.remove(r - 1));

%>
<style>
    td.header {
        color: #ff0000;
    }

    td.data_input input {
        color: #008000;
    }
</style>
<BODY>
<div><a href="/team.html">3 Đội</a>&nbsp;&nbsp;&nbsp;<a href="/team.html?team=2">2 Đội</a></div>
<TABLE border=0>
    <form name="team" action="/team.html" method="get">
        <input type="hidden" name="team" value="<%=request.getParameter("team")%>">
        <TR>
            <TD class="header">Team A</TD>
            <TD class="header">Team B</TD>
        </TR>
        <%
            int index = 1;
            for (int i = 0; i < teamA.size(); i++) {
                if (i % 2 == 0) {
        %>
        <TR>
            <TD colspan="2" class="data_input">Group <%=index++%></TD>
        </TR>
        <%
                }
        %>
        <TR>
            <TD class="data_input"><input type="text" name="teamA<%=i%>" value="<%=teamA.get(i)%>"/></TD>
            <TD class="data_input"><input type="text" name="teamB<%=i%>" value="<%=teamB.get(i)%>"/></TD>
        </TR>
        <%
            }
        %>
        <TR>
            <TD colspan="3"><input type="submit" name="refresh" value="Refresh"></TD>
        </TR>

    </form>
<%
    } else {

        List group1 = new ArrayList();
        group1.add(request.getParameter("teamA0") != null ? request.getParameter("teamA0") : "Chinh");
        group1.add(request.getParameter("teamB0") != null ? request.getParameter("teamB0") : "A Chieu");
        group1.add(request.getParameter("teamC0") != null ? request.getParameter("teamC0") : "B Anh");

        List group2 = new ArrayList();
        group2.add(request.getParameter("teamA1") != null ? request.getParameter("teamA1") : "Tuan");
        group2.add(request.getParameter("teamB1") != null ? request.getParameter("teamB1") : "Ban");
        group2.add(request.getParameter("teamC1") != null ? request.getParameter("teamC1") : "Nguyen");

        List group3 = new ArrayList();
        group3.add(request.getParameter("teamA2") != null ? request.getParameter("teamA2") : "Thai");
        group3.add(request.getParameter("teamB2") != null ? request.getParameter("teamB2") : "Nghia AA");
        group3.add(request.getParameter("teamC2") != null ? request.getParameter("teamC2") : "Hieu");

        List group4 = new ArrayList();
        group4.add(request.getParameter("teamA3") != null ? request.getParameter("teamA3") : "Duc");
        group4.add(request.getParameter("teamB3") != null ? request.getParameter("teamB3") : "Nghia DT");
        group4.add(request.getParameter("teamC3") != null ? request.getParameter("teamC3") : "Vu");

        List teamA = new ArrayList();
        List teamB = new ArrayList();
        List teamC = new ArrayList();

        Random rand = new Random();
        int r = 0;
//Team A
        r = rand.nextInt(group1.size()) + 1;
        teamA.add(group1.remove(r - 1));
        r = rand.nextInt(group2.size()) + 1;
        teamA.add(group2.remove(r - 1));
        r = rand.nextInt(group3.size()) + 1;
        teamA.add(group3.remove(r - 1));
        r = rand.nextInt(group4.size()) + 1;
        teamA.add(group4.remove(r - 1));

//Team B
        r = rand.nextInt(group1.size()) + 1;
        teamB.add(group1.remove(r - 1));
        r = rand.nextInt(group2.size()) + 1;
        teamB.add(group2.remove(r - 1));
        r = rand.nextInt(group3.size()) + 1;
        teamB.add(group3.remove(r - 1));
        r = rand.nextInt(group4.size()) + 1;
        teamB.add(group4.remove(r - 1));

//Team C
        r = rand.nextInt(group1.size()) + 1;
        teamC.add(group1.remove(r - 1));
        r = rand.nextInt(group2.size()) + 1;
        teamC.add(group2.remove(r - 1));
        r = rand.nextInt(group3.size()) + 1;
        teamC.add(group3.remove(r - 1));
        r = rand.nextInt(group4.size()) + 1;
        teamC.add(group4.remove(r - 1));

%>
<style>
    td.header {
        color: #ff0000;
    }

    td.data_input input {
        color: #008000;
    }
</style>
<BODY>
<div><a href="/team.html">3 Đội</a>&nbsp;&nbsp;&nbsp;<a href="/team.html?team=2">2 Đội</a></div>
<TABLE border=0>
    <form name="team" action="/team.html" method="get">
        <input type="hidden" name="team" value="<%=request.getParameter("team")%>">
        <TR>
            <TD class="header">Team A</TD>
            <TD class="header">Team B</TD>
            <TD class="header">Team C</TD>
        </TR>
        <%
            for (int i = 0; i < teamA.size(); i++) {
        %>
        <TR>
            <TD class="data_input"><input type="text" name="teamA<%=i%>" value="<%=teamA.get(i)%>"/></TD>
            <TD class="data_input"><input type="text" name="teamB<%=i%>" value="<%=teamB.get(i)%>"/></TD>
            <TD class="data_input"><input type="text" name="teamC<%=i%>" value="<%=teamC.get(i)%>"/></TD>
        </TR>
        <%
            }
        %>
        <TR>
            <TD colspan="3"><input type="submit" name="refresh" value="Refresh"></TD>
        </TR>
    </form>
    <%
        }
    %>
</TABLE>
</BODY>
</HTML>
