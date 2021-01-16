<%--
  Created by IntelliJ IDEA.
  User: silve
  Date: 13/01/2021
  Time: 10:31
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>



<html>
<head>
    <title>Nuova Prenotazione</title>
    <script
            src="https://code.jquery.com/jquery-3.5.1.min.js"
            integrity="sha256-9/aliU8dGd2tb6OSsuzixeV4y/faTqgFtohetphbbj0="
            crossorigin="anonymous"></script>
    <link href="../../css/NuovaPrenotazione.css" rel="stylesheet" type="text/css">
</head>
<body>

<%@include file="../HeaderView.jsp" %>
<div class="background">

    <h1>Nuova prenotazione</h1>

</div>

<div class="form">
    <label for="calendar">Seleziona la data della prenotazione:</label><br>
    <input type="date" id="calendar"><br>

    <form action="../../prenotazione" method="post">
        <input type="hidden" value="prenotazioneSingola" name="action">
        <input type="hidden" value="" name="dateValueSingolo" id="dateInputSingolo">

        <div class="bottoni">
            <input type="submit" value="Posto singolo" id="btnPrenotazioneSingola" style="display: none">
        </div>
    </form>

    <form action="../../prenotazione" method="post">
        <input type="hidden" value="prenotazioneGruppo" name="action">
        <input type="hidden" value="" name="dateValueGruppo" id="dateInputGruppo">

        <div class="bottoni">
            <input type="submit" value="Gruppo di studio" id="btnPrenotazioneGruppo" style="display: none">
        </div>
    </form>

</div>
    <script src="../../js/nuovaPrenotazione.js"></script>
</body>
</html>