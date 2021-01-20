package it.uniseats.model.dao;

import it.uniseats.model.beans.StudenteBean;
import it.uniseats.utils.DriverManagerConnectionPool;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Classe DAO che ci permette di effettuare operazioni sullo StudenteBean.
 */
public class StudenteDAO {

  public static final String doRetrieveByMatricola = "doRetrieveByMatricola";
  public static final String doRetrieveAll = "doRetrieveAll";
  public static final String doSave = "doSave";
  public static final String doRetrieveByEmail = "doRetrieveByEmail";
  public static final String doUpdate = "doUpdate";
  public static final String doDelete = "doDelete";

  private static final String TABLE_NAME = "studente";
  private static final String DATASOURCE_ERROR =
      "[STUDENTEDAO] Errore: il DataSource non risulta essere configurato correttamente";

  /**
   * Metodo per effettuare query.
   *
   * @param methodName nome della <b>query</b>
   * @param parameter  <b>parametro</b> passato alla query
   * @return un <b>object</b>
   * @throws SQLException
   */
  public static synchronized Object doQuery(String methodName, Object parameter) throws SQLException {

    Connection connection = null;
    PreparedStatement preparedStatement = null;
    String querySQL;

    try {

      connection = DriverManagerConnectionPool.getConnection();

      switch (methodName) {

        case doRetrieveByMatricola:
          querySQL = "SELECT * FROM " + TABLE_NAME + " WHERE matricola=?";
          preparedStatement = connection.prepareStatement(querySQL);
          return doRetrieveByMatricola(preparedStatement, (String) parameter);

        case doRetrieveAll:
          querySQL = "SELECT * FROM " + TABLE_NAME;
          preparedStatement = connection.prepareStatement(querySQL);
          return doRetriveAll(preparedStatement);

        case doSave:
          querySQL = "INSERT INTO " + TABLE_NAME + " (anno, cognome, dipartimento, email, matricola, nome, password ) VALUES (?,?,?,?,?,?,?)";
          preparedStatement = connection.prepareStatement(querySQL);
          return doSave(preparedStatement, (StudenteBean) parameter);

        case doRetrieveByEmail:
          querySQL = "SELECT * FROM " + TABLE_NAME + " WHERE email=?";
          preparedStatement = connection.prepareStatement(querySQL);
          return doRetrieveByEmail(preparedStatement, (String) parameter);

        case doUpdate:
          querySQL = "UPDATE " + TABLE_NAME + " SET anno=? WHERE matricola=?";
          preparedStatement = connection.prepareStatement(querySQL);
          return doUpdate(preparedStatement, (StudenteBean) parameter);

        case doDelete:
          querySQL = "DELETE FROM " + TABLE_NAME + " WHERE matricola=?";
          preparedStatement = connection.prepareStatement(querySQL);
          return doDelete(preparedStatement, (String) parameter);

        default:
          return null;

      }

    } finally {

      try {

        if (preparedStatement != null) {
          preparedStatement.close();
        }

      } finally {

        DriverManagerConnectionPool.releaseConnection(connection);

      }

    }

  }


  /**
   * Query che effettua una ricerca per matricola di uno Studente.
   *
   * @param preparedStatement <b>query SQL</b>
   * @param matricola         <b>matricola</b> dello studente che si intende cercare
   * @return lo studente associato alla matricola
   * @throws SQLException
   */
  private static synchronized StudenteBean doRetrieveByMatricola(PreparedStatement preparedStatement, String matricola) throws SQLException {

    preparedStatement.setString(1, matricola);
    ResultSet rs = preparedStatement.executeQuery();
    StudenteBean studenteBean = new StudenteBean();

    while (rs.next()) {
      studenteBean = getStudentInfo(rs);
    }

    return studenteBean;

  }

  /**
   * Query che effettua una ricerca di tutti gli studenti presenti nel database.
   *
   * @param preparedStatement <b>query SQL</b>
   * @return una lista di Studenti
   * @throws SQLException
   */
  private static synchronized ArrayList<StudenteBean> doRetriveAll(PreparedStatement preparedStatement) throws SQLException {

    ArrayList<StudenteBean> list = new ArrayList<>();
    ResultSet rs = preparedStatement.executeQuery();

    while (rs.next()) {
      StudenteBean studenteBean = getStudentInfo(rs);
      list.add(studenteBean);
    }

    return list;

  }

  /**
   * Query per rendere persistente uno Studente.
   *
   * @param preparedStatement <b>query SQL</b>
   * @param studente          lo <b>studente</b> che si vuole salvare sul database
   * @return <b>1</b> se l'operazione ha successo, <b>0</b> se l'operazione non ha successo
   * @throws SQLException
   */
  private static synchronized int doSave(PreparedStatement preparedStatement, StudenteBean studente) throws SQLException {

    preparedStatement.setInt(1, studente.getAnno());
    preparedStatement.setString(2, studente.getCognome());
    preparedStatement.setString(3, studente.getDipartimento());
    preparedStatement.setString(4, studente.getEmail());
    preparedStatement.setString(5, studente.getMatricola());
    preparedStatement.setString(6, studente.getNome());
    preparedStatement.setString(7, studente.getPassword());

    return preparedStatement.executeUpdate();

  }

  /**
   * Query che effettua una ricerca per email di uno Studente.
   *
   * @param preparedStatement <b>query SQL</b>
   * @param email             <b>email</b> dello studente che si intende cercare
   * @return lo studente associato alla email
   * @throws SQLException
   */
  private static synchronized StudenteBean doRetrieveByEmail(PreparedStatement preparedStatement, String email) throws SQLException {

    preparedStatement.setString(1, email);
    ResultSet rs = preparedStatement.executeQuery();
    StudenteBean studenteBean = new StudenteBean();

    while (rs.next()) {
      studenteBean = getStudentInfo(rs);
    }

    return studenteBean;

  }

  /**
   * Query per modificare uno Studente.
   *
   * @param preparedStatement <b>query SQL</b>
   * @param studenteBean      lo <b>studente</b> che si vuole modificare
   * @return <b>1</b> se l'operazione ha successo, <b>0</b> se l'operazione non ha successo
   * @throws SQLException
   */
  private static synchronized int doUpdate(PreparedStatement preparedStatement, StudenteBean studenteBean) throws SQLException {

    preparedStatement.setInt(1, studenteBean.getAnno());

    preparedStatement.setString(2, studenteBean.getMatricola());

    return preparedStatement.executeUpdate();

  }

  /**
   * Query per eliminare uno Studente dal database.
   *
   * @param preparedStatement <b>query SQL</b>
   * @param matricola         la <b>matricola</b>  dello studente che si vuole eliminare dal database
   * @return <b>1</b> se l'operazione ha successo, <b>0</b> se l'operazione non ha successo
   * @throws SQLException
   */
  private static synchronized boolean doDelete(PreparedStatement preparedStatement, String matricola) throws SQLException {

    preparedStatement.setString(1, matricola);

    return (preparedStatement.executeUpdate() != 0);

  }

  /**
   * Metodo per ottenere le informazioni su uno Studente.
   *
   * @param rs ResultSet
   * @return uno studente bean
   * @throws SQLException
   */
  private static StudenteBean getStudentInfo(ResultSet rs) throws SQLException {

    StudenteBean studenteBean = new StudenteBean();
    studenteBean.setNome(rs.getString("nome"));
    studenteBean.setCognome(rs.getString("cognome"));
    studenteBean.setMatricola(rs.getString("matricola"));
    studenteBean.setEmail(rs.getString("email"));
    studenteBean.setPassword(rs.getString("password"));
    studenteBean.setAnno(rs.getInt("anno"));
    studenteBean.setDipartimento(rs.getString("dipartimento"));

    return studenteBean;

  }

}
