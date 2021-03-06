package it.uniseats.model.beans;

/**
 * La classe <code>PostoBean</code> rappresenta il posto
 * che sarà occupato da uno studente che effettua una prenotazione
 * con il sistema UniSeats
 * Un oggetto <code>PostoBean</code> rappresenta un singolo posto all'interno di un'aula.
 * Ogni posto ha un codice identificativo (A1-01, A1-02,..)
 * Ogni posto ha il codice dell'aula a cui appartiene (A1, A2, ..)
 */

public class PostoBean {

  /**
   * Variabile istanza.
   */
  private String codice;
  private String codiceAula;

  /**
   * Costruttore vuoto.
   */
  public PostoBean() {
  }


  /**
   * Costruttore.
   *
   * @param codice     è il <b>codice</b> identificativo di un posto
   * @param codiceAula è il <b>codice</b> dell'aula a cui appartiene
   */
  public PostoBean(String codice, String codiceAula) {
    this.codice = codice;
    this.codiceAula = codiceAula;
  }

  /**
   * Prelevo codice posto.
   *
   * @return il <b>codice</b> del posto
   */
  public String getCodice() {
    return codice;
  }

  /**
   * Prelevo codice aula.
   *
   * @return il <b>codice</b> dell'aula
   */
  public String getCodiceAula() {
    return codiceAula;
  }

  /**
   * Modifica il codice dell'aula.
   *
   * @param codiceAula è il <b>codice</b> dell'aula a cui appartiene
   */
  public void setCodiceAula(String codiceAula) {
    this.codiceAula = codiceAula;
  }

  /**
   * Modifica il codice del posto.
   *
   * @param codice è il <b>codice</b> identificativo del posto
   */
  public void setCodice(String codice) {
    this.codice = codice;
  }
}


