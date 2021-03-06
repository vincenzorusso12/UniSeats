<%@ page import="java.util.Collection" %>
<%@ page import="java.util.Iterator" %>
<%--
  Created by IntelliJ IDEA.
  User: benedettosimone
  Date: 30/12/20
  Time: 18:54
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%
    String message = (String) request.getAttribute("message");

    Collection<?> dipartimenti = (Collection<?>)  request.getSession().getAttribute("dipartimenti");
    if(dipartimenti==null){
        response.sendRedirect(response.encodeRedirectURL(request.getContextPath()+"/registrazione?action=getDipartimenti"));
        return;
    }
%>

<html>
<head>
    <title>Registrazione</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link href="${pageContext.servletContext.contextPath}/css/Registrazione.css" rel="stylesheet" type="text/css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/jquery-validation@1.19.2/dist/jquery.validate.min.js"></script>
</head>
<body>

<%@include file="../HeaderView.jsp" %>
<div class="background">

    <h1>Registrati</h1>

</div>


<%

    if (message != null) {

        if (message.contains("Esiste")) {

%>
<h3 class="fallita"> <%

			}else if(message.contains("successo")){


        	%>
    <h3 class="successo">

            <%	} else{

        	%> <h3 class="fallita">


        <% } %>

        <%=message%>

    </h3>
            <%
        	 }
        		  %>


        <div class="form">


            <form id="registrazione" action="${pageContext.servletContext.contextPath}/registrazione" method="post">

                <input type="hidden" value="add" name="action">

                <div class="md-textfield">
                    <input class="md-textfield-input" id="email" type="text" name="email" required
                           onchange="emailObserver()"/>

                    <label id="email_l" for="email">Email istituzionale</label>

                    <div class="indicator"></div>
                </div>


                <div class="md-textfield">
                    <input class="md-textfield-input" id="password" type="password" name="password" required
                           onchange="passwordObserver()"/>

                    <label id="password_l" for="password">Password</label>
                    <div class="indicator"></div>
                </div>

                <br>

                <div class="md-textfield x1">
                    <input class="md-textfield-input" id="nome" type="text" name="nome" required
                           onchange="nomeObserver()"/>

                    <label id="nome_l" for="nome">Nome</label>
                    <div class="indicator"></div>
                </div>


                <div class="md-textfield x">
                    <input class="md-textfield-input" id="cognome" type="text" name="cognome" required
                           onchange="cognomeObserver()"/>

                    <label id="cognome_l" for="cognome">Cognome</label>
                    <div class="indicator"></div>
                </div>

                <br>

                <div class="md-textfield">
                    <input class="md-textfield-input" id="matricola" type="text" name="matricola" required
                           onchange="matricolaObserver()"/>

                    <label id="matricola_l" for="matricola">Matricola</label>
                    <div class="indicator"></div>
                </div>

                <br>

                <div class="md-textfield">
                    <select class="md-textfield-input" id="dipartimento" name="dipartimento" required >

                        <%
                            if(dipartimenti.size()>0){
                                Iterator<?> it = dipartimenti.iterator();
                                while(it.hasNext()){
                                    String d = (String) it.next();

                        %>



                        <option value="<%=d%>"><%=d%></option>

                        <%
                                }
                            }

                        %>
                    </select>
                </div>



                <div class="md-textfield ">

                    <select class="md-textfield-input" id="anno" name="anno" required >
                        <option value="1">Primo anno</option>
                        <option value="2">Secondo anno</option>
                        <option value="3">Terzo anno</option>
                        <option value="4">Quarto anno</option>
                        <option value="5">Quinto anno</option>
                    </select>


                </div>

                <br>


                <div class="bottoni">
                    <input type="submit" value="Registrati"/>
                </div>
            </form>


        </div>
        <script src="${pageContext.servletContext.contextPath}/js/Registrazione.js"></script>

</body>
</html>
