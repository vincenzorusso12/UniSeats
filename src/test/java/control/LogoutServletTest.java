package control;

import static org.junit.jupiter.api.Assertions.assertEquals;

import it.uniseats.control.gestioneaccesso.LogoutServlet;
import it.uniseats.model.beans.StudenteBean;
import java.io.IOException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.mock.web.MockServletConfig;

class LogoutServletTest {

  private LogoutServlet servlet;
  private MockHttpServletRequest request;
  private MockHttpServletResponse response;
  private MockHttpSession session;
  private StudenteBean studente;

  @BeforeEach
  public void setUp() {
    servlet = new LogoutServlet();
    request = new MockHttpServletRequest();
    response = new MockHttpServletResponse();
    request.setSession(session);
  }

  @Test
  public void oneWaySetup() throws ServletException {
    ServletConfig sg = new MockServletConfig();
    servlet.init(sg);
  }

  @Test
  public void logoutTest() throws ServletException, IOException {

    request.getSession().setAttribute("logged", true);
    request.getSession().setAttribute("nome", "Benedetto");
    request.getSession().setAttribute("cognome", "Simone");
    request.getSession().setAttribute("matricola", "05121058521");
    request.getSession().setAttribute("email", "b.simone@studenti.unisa.it");
    request.getSession().setAttribute("anno", "3");
    request.getSession().setAttribute("dipartimento", "Informatica");
    servlet.doGet(request, response);
    assertEquals("/view/login/LoginView.jsp", response.getRedirectedUrl());
  }

}


