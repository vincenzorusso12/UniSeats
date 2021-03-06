package it.uniseats.control.gestioneutente;


import it.uniseats.model.beans.StudenteBean;
import it.uniseats.model.dao.StudenteDao;
import java.io.IOException;
import java.sql.SQLException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet per gestire il profilo dello studente.
 */

@WebServlet(name = "ProfiloUtenteServlet")
public class ProfiloUtenteServlet extends HttpServlet {

  /**
   * Metodo per effettuare richieste doPost.
   *
   * @param request  HttpServletRequest
   * @param response HttpServletResponse
   * @throws ServletException se si verifica una eccezione
   * @throws IOException se si verifica una eccezione
   */

  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    String action = request.getParameter("action");


    RequestDispatcher dispatcher =
        getServletContext().getRequestDispatcher("/view/profiloutente/ProfiloUtenteView.jsp");

    if (action != null) {

      switch (action) {

        case "confermaDelete":
          try {
            deleteProfile(request, response);
          } catch (SQLException throwables) {
            throwables.printStackTrace();
          }
          break;

        case "confermaUpdate":
          try {

            updateAnno(request, response, dispatcher);
          } catch (SQLException throwables) {
            throwables.printStackTrace();
          }
          break;

        default:
          break;

      }

    } else {
      dispatcher.forward(request, response);
    }

  }

  public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    doPost(request, response);
  }

  /**
   * Metodo per aggiornare l'anno di corso di uno studente.
   *
   * @param request HttpServletRequest
   * @param response HttpServletResponse
   * @param dispatcher Dispatcher
   * @throws SQLException se si verifica una eccezione
   * @throws ServletException se si verifica una eccezione
   * @throws IOException se si verifica una eccezione
   */
  private void updateAnno(HttpServletRequest request, HttpServletResponse response,
                          RequestDispatcher dispatcher)
      throws SQLException, ServletException, IOException {

    StudenteBean studMod = (StudenteBean) StudenteDao
        .doQuery(StudenteDao.doRetrieveByMatricola, request.getSession().getAttribute("matricola"));
    

    if (studMod.getMatricola() != null) {

      int anno = Integer.parseInt(request.getParameter("annomod"));
      studMod.setAnno(anno);


      StudenteDao.doQuery("doUpdate", studMod);
      request.getSession().setAttribute("anno", studMod.getAnno());

    } else {

      String message = "Si è verificato un errore";
      request.setAttribute("errore", message);

    }

    dispatcher.forward(request, response);

  }


  /**
   * Metodo per eliminare il profilo di uno studente dal sistema UniSeats.
   *
   * @param request HttpServletRequest
   * @param response HttpServletResponse
   * @throws ServletException se si verifica una eccezione
   * @throws IOException se si verifica una eccezione
   * @throws SQLException se si verifica una eccezione
   */
  private void deleteProfile(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException, SQLException {

    StudenteDao.doQuery(StudenteDao.doDelete, request.getSession().getAttribute("matricola"));

    String message = "Profilo eliminato con successo";
    request.setAttribute("errore", message);
    request.getSession().invalidate();
    RequestDispatcher dispatcher =
        getServletContext().getRequestDispatcher("/view/login/LoginView.jsp");
    dispatcher.forward(request, response);


  }

}
