package control;

import static org.junit.jupiter.api.Assertions.assertEquals;

import it.uniseats.control.visualizzaPrenotazioni.ManagePrenotazioneServlet;
import it.uniseats.model.beans.PrenotazioneBean;
import it.uniseats.model.dao.PrenotazioneDAO;
import java.io.IOException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.Locale;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.mock.web.MockServletConfig;


class ManagePrenotazioneServletTest {

  private ManagePrenotazioneServlet servlet;
  private MockHttpServletRequest request;
  private MockHttpServletResponse response;
  private MockHttpSession session;

  /*
  @AfterAll
  public static void deletePrenotazione() throws SQLException {
      PrenotazioneDAO.doQuery("doDelete","0001-0512105933");
  }
  */

  @BeforeEach
  public void setUp() {
    servlet = new ManagePrenotazioneServlet();
    request = new MockHttpServletRequest();
    response = new MockHttpServletResponse();
    request.setSession(session);
  }

  @BeforeEach
  public void oneWaySetup() throws ServletException {
    ServletConfig sg = new MockServletConfig();
    servlet.init(sg);
  }

  @Test
  public void updateDataTestSuccesfull() throws ParseException, SQLException, ServletException, IOException {
    DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT, Locale.ITALY);
    PrenotazioneBean prenotazioneBean =
        (PrenotazioneBean) PrenotazioneDAO.doQuery("doRetrieveByCode", "9-0512108336-21/01/2021");

    Date date1 = df.parse("25/02/2021");

    request.addParameter("action", "modificaPrenotazione");
    request.getSession().setAttribute("codice", prenotazioneBean.getCodice());

    request.getSession().setAttribute("data", date1);
    request.addParameter("tipologia", "singolo");
    request.addParameter("codice", prenotazioneBean.getCodice());
    servlet.doPost(request, response);
    assertEquals("/view/prenotazioni_effettuate/VisualizzaPrenotazioniView.jsp", response.getForwardedUrl());
  }

  @Test
  public void updateDataTestSameDate() throws ParseException, SQLException, ServletException, IOException {
    DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT, Locale.ITALY);
    PrenotazioneBean prenotazioneBean =
        (PrenotazioneBean) PrenotazioneDAO.doQuery("doRetrieveByCode", "9-0512108336-21/01/2021");



    request.addParameter("action", "modificaPrenotazione");
    request.getSession().setAttribute("codice", prenotazioneBean.getCodice());

    request.getSession().setAttribute("data", prenotazioneBean.getData());

    request.addParameter("tipologia", "singolo");
    request.addParameter("codice", prenotazioneBean.getCodice());
    servlet.doPost(request, response);
    assertEquals("Non è più possibile modificare la prenotazione", request.getAttribute("error"));
  }

  @Test
  public void deletePrenotazioneSuccesfull() throws ParseException, SQLException, ServletException, IOException {

    PrenotazioneBean prenotazioneBean =
            (PrenotazioneBean) PrenotazioneDAO.doQuery("doRetrieveByCode", "9-0512108336-21/01/2021");


    request.addParameter("action", "eliminaPrenotazione");
    request.addParameter("codice", prenotazioneBean.getCodice());


    servlet.doPost(request, response);
    assertEquals("/view/prenotazioni_effettuate/VisualizzaPrenotazioniView.jsp", response.getForwardedUrl());
  }





}
