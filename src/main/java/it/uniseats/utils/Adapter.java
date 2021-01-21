package it.uniseats.utils;

import com.stark.ai.Jarvis;
import it.uniseats.model.beans.AulaBean;
import it.uniseats.model.beans.PostoBean;
import it.uniseats.model.beans.PrenotazioneBean;
import it.uniseats.model.beans.StudenteBean;
import it.uniseats.model.dao.AulaDAO;
import it.uniseats.model.dao.PostoDAO;
import it.uniseats.model.dao.PrenotazioneDAO;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;


public class Adapter {

  public static void todaySchedule() throws SQLException, ParseException,
      CloneNotSupportedException {

    String today = DateUtils.dateToString(new Date());

    ArrayList<String> list = (ArrayList<String>) AulaDAO.doQuery(AulaDAO.getDipartimenti, "temp");

    if (list != null) {

      ArrayList<String> parameter = new ArrayList<>();
      parameter.add(today);
      for (String dipartimento : list) {
        parameter.add(dipartimento);

        LinkedList<PrenotazioneBean> prenotazioniList =
            (LinkedList<PrenotazioneBean>) PrenotazioneDAO.doQuery(PrenotazioneDAO.findByDataDipartimento, parameter);

        if (prenotazioniList != null) {

          ArrayList<String> auleUtilizzate = getAuleUtilizzate(prenotazioniList);
          prenotazioniList.removeIf(prenotazione -> !prenotazione.getCodiceAula().equals("00"));

          int[] codiciPrenotazioni = getCodiciPrenotazioni(prenotazioniList);
          int count = 0;
          for (int i = prenotazioniList.size(); i < 20; i++) {
            codiciPrenotazioni[i] = 0;
            count++;
          }

          int[] disposizioneTemp = Jarvis.disponiPrenotazioni(codiciPrenotazioni);
          ArrayList<Integer> disposizioneArrayList = new ArrayList<>();

          for (int j : disposizioneTemp) {
            disposizioneArrayList.add(j);
          }

          for (int i = 0; count != 0; i++) {
            if (disposizioneArrayList.get(i) == 0) {
              disposizioneArrayList.remove(i);
              count--;
            }
          }

          int[] disposizione = new int[disposizioneArrayList.size()];
          for (int i = 0; i < disposizione.length; i++) {
            disposizione[i] = disposizioneArrayList.get(i);
          }

          ArrayList<AulaBean> listaAule =
              (ArrayList<AulaBean>) AulaDAO.doQuery(AulaDAO.doRetrieveAll, dipartimento);

          if (listaAule != null) {

            AulaBean aulaDaUtilizzare = getAulaVuota(listaAule, auleUtilizzate);

            if (aulaDaUtilizzare != null) {

              ArrayList<PrenotazioneBean> settedPrenotazioni = new ArrayList<>();

              for (int i = 0; i < disposizione.length; i++) {
                updatePrenotazione(i, prenotazioniList, disposizione[i], aulaDaUtilizzare,
                    settedPrenotazioni);
              }

              updateDataBase(settedPrenotazioni);

            }

          }

        }

        parameter.remove(dipartimento);

      }

    }

  }

  public static void listener(PrenotazioneBean p, StudenteBean s)
      throws SQLException, ParseException, CloneNotSupportedException {

    String todayString = DateUtils.dateToString(new Date());
    String prenDateString = DateUtils.dateToString(p.getData());

    if (todayString.equals(prenDateString)) {

      prenotazioneGiornoCorrente(p, s);

    } else {

      LinkedList<PrenotazioneBean> prenotazioniList = getPrenotazioni(p, s);

      if (prenotazioniList != null) {

        ArrayList<String> auleUtilizzate = getAuleUtilizzate(prenotazioniList);

        prenotazioniList.removeIf(prenotazione -> !prenotazione.getCodiceAula().equals("00"));

        if (prenotazioniList.size() == 20) {

          //parte JARVIS
          int[] codiciPrenotazioni = getCodiciPrenotazioni(prenotazioniList);

          int[] disposizione = Jarvis.disponiPrenotazioni(codiciPrenotazioni);

          ArrayList<AulaBean> listaAule =
              (ArrayList<AulaBean>) AulaDAO.doQuery(AulaDAO.doRetrieveAll, s.getDipartimento());

          if (listaAule != null) {

            AulaBean aulaDaUtilizzare = getAulaVuota(listaAule, auleUtilizzate);

            if (aulaDaUtilizzare != null) {

              ArrayList<PrenotazioneBean> settedPrenotazioni = new ArrayList<>();

              for (int i = 0; i < disposizione.length; i++) {
                updatePrenotazione(i, prenotazioniList, disposizione[i], aulaDaUtilizzare,
                    settedPrenotazioni);
              }

              updateDataBase(settedPrenotazioni);

            }

          }

        }

      }

    }

  }

  private static void prenotazioneGiornoCorrente(PrenotazioneBean p, StudenteBean s)
      throws SQLException, ParseException {

    LinkedList<PrenotazioneBean> prenotazioniList = getPrenotazioni(p, s);

    if (prenotazioniList != null) {

      if (prenotazioniList.size() < 60) {

        ArrayList<AulaBean> listaAule =
            (ArrayList<AulaBean>) AulaDAO.doQuery(AulaDAO.doRetrieveAll, s.getDipartimento());

        if (listaAule != null) {

          for (AulaBean aula : listaAule) {
            System.out.println(aula.getCodice());

            ArrayList<PostoBean> posti =
                (ArrayList<PostoBean>) PostoDAO
                    .doQuery(PostoDAO.doRetrieveByAulaCode, aula.getCodice());

            if (posti != null) {

              for (PrenotazioneBean pren : prenotazioniList) {

                posti.removeIf(posto -> posto.getCodice().equals(pren.getCodicePosto()));

              }

              if (!posti.isEmpty()) {

                p.setCodicePosto(posti.get(0).getCodice());
                p.setCodiceAula(aula.getCodice());
                break;

              }

            }

          }

          if (!p.getCodicePosto().equals("00")) {

            //la prenotazione e' andata a buon fine
            PrenotazioneDAO.doQuery(PrenotazioneDAO.doUpdateAulaPosto, p);

          } else {

            //ERRORE
            System.out.println("ERRORE");

          }

        }

      }

    }

  }

  private static void updateDataBase(ArrayList<PrenotazioneBean> settedPrenotazioni)
      throws SQLException, ParseException {

    for (PrenotazioneBean prenotazione : settedPrenotazioni) {
      PrenotazioneDAO.doQuery(PrenotazioneDAO.doUpdateAulaPosto, prenotazione);
    }

  }

  private static void updatePrenotazione(int i, LinkedList<PrenotazioneBean> prenotazioni, int tipo,
                                         AulaBean aulaDaUtilizzare,
                                         ArrayList<PrenotazioneBean> settedPrenotazioni) {

    String progressivo = (i < 10) ? String.valueOf(i) : ("0" + i);
    String posto = aulaDaUtilizzare.getCodice() + "-" + progressivo;

    for (PrenotazioneBean prenotazione : prenotazioni) {

      if (!settedPrenotazioni.contains(prenotazione)) {

        if (prenotazione.isSingolo() && tipo == 0) {
          setPrenotazione(prenotazione, aulaDaUtilizzare.getCodice(), posto, settedPrenotazioni);
        }

        if (!prenotazione.isSingolo() && tipo == 1) {
          setPrenotazione(prenotazione, aulaDaUtilizzare.getCodice(), posto, settedPrenotazioni);
        }

        break;

      }

    }

  }

  private static void setPrenotazione(PrenotazioneBean prenotazione, String codice, String posto,
                                      ArrayList<PrenotazioneBean> settedPrenotazioni) {
    prenotazione.setCodiceAula(codice);
    prenotazione.setCodicePosto(posto);
    settedPrenotazioni.add(prenotazione);
  }


  private static AulaBean getAulaVuota(ArrayList<AulaBean> listaAule,
                                       ArrayList<String> auleUtilizzate) {

    for (AulaBean aula : listaAule) {

      if (!auleUtilizzate.contains(aula.getCodice())) {
        return aula;
      }

    }

    return null;

  }

  private static int[] getCodiciPrenotazioni(LinkedList<PrenotazioneBean> prenotazioniList) {

    int[] prenotazioni = new int[20];

    for (int i = 0; i < prenotazioniList.size(); i++) {
      if (prenotazioniList.get(i).isSingolo()) {
        prenotazioni[i] = 0;
      } else {
        prenotazioni[i] = 1;
      }
    }

    return prenotazioni;

  }

  private static ArrayList<String> getAuleUtilizzate(
      LinkedList<PrenotazioneBean> prenotazioniList) {

    ArrayList<String> auleUtilizzate = new ArrayList<>();
    for (PrenotazioneBean prenotazione : prenotazioniList) {
      if (!auleUtilizzate.contains(prenotazione.getCodiceAula())) {
        auleUtilizzate.add(prenotazione.getCodiceAula());
      }
    }

    return auleUtilizzate;

  }

  private static LinkedList<PrenotazioneBean> getPrenotazioni(PrenotazioneBean p, StudenteBean s)
      throws SQLException, ParseException {
    Date date = p.getData();
    String dipartimento = s.getDipartimento();

    ArrayList<String> parameter = new ArrayList<>();

    parameter.add(DateUtils.dateToString(date));
    parameter.add(dipartimento);

    return (LinkedList<PrenotazioneBean>) PrenotazioneDAO
        .doQuery(PrenotazioneDAO.findByDataDipartimento, parameter);
  }

}
