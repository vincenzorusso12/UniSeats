package it.uniseats.control.visualizzaprenotazioni;


import it.uniseats.model.beans.PrenotazioneBean;
import it.uniseats.model.beans.StudenteBean;
import it.uniseats.model.dao.PrenotazioneDao;
import it.uniseats.model.dao.StudenteDao;
import it.uniseats.utils.Adapter;
import it.uniseats.utils.DateUtils;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Gestisce le prenotazioni effettuate in termini di modifica o eliminazione di una prenotazione.
 */
@WebServlet(name = "ManagePrenotazioneServlet")
public class ManagePrenotazioneServlet extends HttpServlet {

  private final String jspPath = "/view/prenotazionieffettuate/VisualizzaPrenotazioniView.jsp";
  private final String invalidDate = "La data scelta non è corretta";
  private final String tooLate = "Non è più possibile modificare la prenotazione";
  private final String impossibleChange = "Impossible effettuare la modifica";

  /**
   * Metodo per effettuare richieste doPost.
   *
   * @param request  HttpServletRequest
   * @param response HttpServletResponse
   * @throws ServletException se si verifica una eccezione
   * @throws IOException se si verifica una eccezione
   */
  public void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    String action = request.getParameter("action");


    if (action != null) {

      switch (action) {

        case "visualizzaPrenotazioni":
          try {

            visualizzaPrenotazioni(request, response);

          } catch (SQLException | ParseException throwables) {
            throwables.printStackTrace();
          }
          break;

        case "modificaPrenotazione":
          try {

            modificaPrenotazione(request, response);

          } catch (SQLException | ParseException | CloneNotSupportedException throwables) {
            throwables.printStackTrace();
          }
          break;

        case "modificaData":
          try {

            modificaData(request, response);

          } catch (SQLException | ParseException | CloneNotSupportedException throwables) {
            throwables.printStackTrace();
          }
          break;

        case "getSinglePren":
          try {

            getSinglePren(request, response);

          } catch (SQLException | ParseException throwables) {
            throwables.printStackTrace();
          }
          break;

        case "eliminaPrenotazione":
          try {

            eliminaPrenotazione(request, response);

          } catch (SQLException | ParseException throwables) {
            throwables.printStackTrace();
          }
          break;

        default:
          break;

      }

    } else {

      RequestDispatcher dispatcher = request.getServletContext().getRequestDispatcher(jspPath);
      dispatcher.forward(request, response);

    }

  }

  /**
   * Metedo per eliminare una prenotazione.
   *
   * @param request  HttpServletRequest
   * @param response HttpSErvletResponse
   * @throws ParseException se si verifica una eccezione
   * @throws SQLException se si verifica una eccezione
   * @throws ServletException se si verifica una eccezione
   * @throws IOException se si verifica una eccezione
   */
  private void eliminaPrenotazione(HttpServletRequest request, HttpServletResponse response)
      throws ParseException, SQLException, ServletException, IOException {

    String codice = request.getParameter("cod");

    PrenotazioneDao.doQuery(PrenotazioneDao.doDelete, codice);

    RequestDispatcher dispatcher = request.getServletContext().getRequestDispatcher(jspPath);
    request.setAttribute("error", "L'eliminazione è avvenuta con successo.");
    dispatcher.forward(request, response);

  }

  /**
   * Metodo per modificare la data di una prenotazione.
   *
   * @param request  HttpServletRequest
   * @param response HttpSErvletResponse
   * @throws ParseException se si verifica una eccezione
   * @throws SQLException se si verifica una eccezione
   * @throws ServletException se si verifica una eccezione
   * @throws IOException se si verifica una eccezione
   */
  private void modificaData(HttpServletRequest request, HttpServletResponse response)
      throws ParseException, SQLException, ServletException, IOException,
      CloneNotSupportedException {

    RequestDispatcher dispatcher = request.getServletContext().getRequestDispatcher(jspPath);

    String codice = request.getParameter("codice");
    String dateTemp = request.getParameter("data");


    String dataTransformed = DateUtils.englishToItalian(dateTemp);

    Date dataPrenotazione = DateUtils.parseDate(dataTransformed);

    Date today = DateUtils.parseDate(DateUtils.dateToString(new Date()));

    PrenotazioneBean prenotazioneBean =
        (PrenotazioneBean) PrenotazioneDao.doQuery(PrenotazioneDao.doRetrieveByCode, codice);

    if (prenotazioneBean.getData() != null) {

      //controllo che la data inserita sia diversa dalla data attuale della prenotazione
      if (DateUtils.parseDate(DateUtils.dateToString(prenotazioneBean.getData()))
          .compareTo(dataPrenotazione) != 0
          && checkPrenotazioni(prenotazioneBean.getMatricolaStudente(), dataTransformed)) {

        //controllo che la modifica della prenotazione venga
        // effettuata prima delle 07:00 del giorno
        // della prenotazione o in un giorno antecedente
        // la data per cui è prevista la prenotazione
        if (checkData(prenotazioneBean.getData())) {
          System.out.println(dataPrenotazione + "è data di oggi" + today);
          //la modifica è possibile solo se la nuova data è oggi
          // e il tipo di prenotazione sia singola o in generale
          // se la nuova data è diversa dalla data corrente
          if ((dataPrenotazione.compareTo(today) == 0 && prenotazioneBean.isSingolo())
              || dataPrenotazione.compareTo(today) > 0) {

            prenotazioneBean.setCodiceAula("00");
            prenotazioneBean.setCodicePosto("00");
            prenotazioneBean.setData(dataPrenotazione);
            PrenotazioneDao.doQuery(PrenotazioneDao.doUpdateData, prenotazioneBean);

            Adapter.listener(prenotazioneBean, getUser(request));

          } else {

            request.getSession().setAttribute("errorPrenotazione", impossibleChange);
          }

        } else {
          request.getSession().setAttribute("errorPrenotazione", tooLate);
        }

      } else {
        request.getSession().setAttribute("errorPrenotazione", invalidDate);
      }

    } else {
      request.getSession().setAttribute("errorPrenotazione", "Si è verificato un errore");
    }

    dispatcher.forward(request, response);

  }

  /**
   * Metodo per modificare la tipologia della prenotazione.
   *
   * @param request  HttpServletRequest
   * @param response HttpServletResponse
   * @throws SQLException se si verifica una eccezione
   * @throws ServletException se si verifica una eccezione
   * @throws IOException se si verifica una eccezione
   */
  private void modificaPrenotazione(HttpServletRequest request, HttpServletResponse response)
      throws SQLException, ServletException, IOException, ParseException,
      CloneNotSupportedException {

    PrenotazioneBean prenotazioneBean = (PrenotazioneBean) PrenotazioneDao
        .doQuery(PrenotazioneDao.doRetrieveByCode, request.getParameter("codice"));


    RequestDispatcher dispatcher;
    String tipologia = request.getParameter("tipologia");

    boolean singolo = false;

    if (tipologia.equalsIgnoreCase("singolo")) {
      singolo = true;
    }


    if (prenotazioneBean.getCodice() != null) {


      request.setAttribute("codice", prenotazioneBean.getCodice());

      //La modifica della prenotazione deve essere effettuata prima delle 07:00 del giorno prenotato
      //o in un giorno antecedente la data per cui è prevista la prenotazione
      if (checkData(prenotazioneBean.getData())) {

        //controllo che sia possibile modificare la prenotazione
        if (canIupdate(singolo, prenotazioneBean.getData())) {

          prenotazioneBean.setSingolo(singolo);
          System.out
              .println(PrenotazioneDao.doQuery(PrenotazioneDao.doUpdateTipo, prenotazioneBean));

          Adapter.listener(prenotazioneBean, getUser(request));

        } else {
          request.getSession().setAttribute("errorPrenotazione", impossibleChange);
        }

      } else {
        request.getSession().setAttribute("errorPrenotazione", tooLate);
      }

      dispatcher = request.getServletContext()
          .getRequestDispatcher("/view/prenotazionieffettuate/VisualizzaPrenotazioniView.jsp");

    } else {

      request.getSession().setAttribute("errorPrenotazione", "Si è verificato un errore");
      dispatcher = request.getServletContext().getRequestDispatcher(jspPath);

    }

    dispatcher.forward(request, response);

  }

  /**
   * Metodo per visualizzare le prenotazioni di uno studente.
   *
   *  @param request HttpServletRequest
   * @param response HttpServletResponse
   * @throws SQLException se si verifica una eccezione
   * @throws ServletException se si verifica una eccezione
   * @throws IOException se si verifica una eccezione
   * @throws ParseException se si verifica una eccezione
   */
  private void visualizzaPrenotazioni(HttpServletRequest request, HttpServletResponse response)
      throws SQLException, ServletException, IOException, ParseException {

    RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(jspPath);

    String parameter = (String) request.getSession().getAttribute("matricola");

    request.removeAttribute("prenotazioni");
    request.setAttribute("prenotazioni",
        PrenotazioneDao.doQuery(PrenotazioneDao.doFindPrenotazioni, parameter));

    dispatcher.forward(request, response);

  }

  /**
   * Metodo per effettuare richieste doGet.
   *
   * @param request  HttpServletRequest
   * @param response HttpServletResponse
   * @throws ServletException se si verifica una eccezione
   * @throws IOException se si verifica una eccezione
   */
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    doPost(request, response);
  }

  /**
   * Metodo che controlla se la <b>data della prenotazione</b>.
   *
   * @param date la <b>data</b> della p
   * @return false altrimenti
   */
  private boolean checkData(Date date) {

    Date today = new Date();
    return (((date.compareTo(today) == 0)) || date.compareTo(today) > 0);

  }

  /**
   * Controllo che lo studente non abbia già effettuato una prenotazione per la stessa data.
   *
   * @param matricola la <b>matricola</b> dello studente
   * @param date      la <b>data di prenotazione</b> selezionata
   * @return <b>false</b> altrimenti
   * @throws SQLException se si verifica una eccezione
   * @throws ParseException se si verifica una eccezione
   */
  private boolean checkPrenotazioni(String matricola, String date)
      throws SQLException, ParseException {

    Date selectedDay = DateUtils.parseDate(date);

    ArrayList<PrenotazioneBean> prenotazioni =
        (ArrayList<PrenotazioneBean>) PrenotazioneDao.doQuery("doFindPrenotazioni", matricola);

    if (prenotazioni != null && prenotazioni.size() > 0) {

      for (PrenotazioneBean p : prenotazioni) {

        if (DateUtils.parseDate(DateUtils.dateToString(p.getData())).compareTo(selectedDay) == 0) {
          return false;
        }

      }

      return true;
    } else {
      return false;
    }

  }

  /**
   * Metodo che controlla se è possibile modificare il tipo di prenotazione.
   *
   * @param singolo <b>true</b> se il tipo è singolo, <b>false</b> se il tipo è gruppo
   * @param date    la data per la quale è prevista la prenotazione
   * @return false se non è possibile effettuare la modifica
   */
  private boolean canIupdate(Boolean singolo, Date date) {
    Date today = new Date();

    //se la nuova tipologia di prenotazione è singola, posso sempre modificare
    if ((singolo && ((date.compareTo(today) == 0) || date.compareTo(today) > 0))) {
      return true;
    } else {
      //se è un gruppo, non posso modificare se la data della prenotazione è la data corrente
      //in tutti gli altri casi posso effettuare la modifica
      return singolo || date.compareTo(today) != 0;
    }
  }

  /**
   * Metodo che restituisce una prenotazione singola.
   *
   *  @param request HttpServletRequest
   * @param response HttpServletResponse
   * @throws SQLException se si verifica una eccezione
   * @throws ServletException se si verifica una eccezione
   * @throws IOException se si verifica una eccezione
   * @throws ParseException se si verifica una eccezione
   */
  private void getSinglePren(HttpServletRequest request, HttpServletResponse response)
      throws SQLException, ServletException, IOException, ParseException {

    RequestDispatcher dispatcher = getServletContext()
        .getRequestDispatcher("/view/prenotazionieffettuate/ModificaPrenotazioniView.jsp");

    String cod = request.getParameter("cod");

    request.setAttribute("prenotazionemod",
        PrenotazioneDao.doQuery(PrenotazioneDao.doRetrieveByCode, cod));

    dispatcher.forward(request, response);

  }

  /**
   * Restituisce lo studente loggato.
   *
   * @param request HttpServletRequest
   * @return lo <b>studente</b> loggato
   * @throws SQLException se si verifica una eccezione
   */
  private StudenteBean getUser(HttpServletRequest request) throws SQLException {

    HttpSession session = request.getSession(true);
    String email = (String) session.getAttribute("email");

    return (StudenteBean) StudenteDao.doQuery(StudenteDao.doRetrieveByEmail, email);

  }

}


