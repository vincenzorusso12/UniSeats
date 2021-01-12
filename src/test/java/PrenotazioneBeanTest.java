import it.uniseats.model.beans.AulaBean;
import it.uniseats.model.beans.PrenotazioneBean;
import it.uniseats.model.beans.StudenteBean;
import org.junit.jupiter.api.Test;



import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class PrenotazioneBeanTest{

    /**
     * Constructor Testing
     */
    @Test
    void testPrenotazioneConstructorEmpty(){
        PrenotazioneBean prenotazioneBean = new PrenotazioneBean();
        assertNotNull(prenotazioneBean);
    }
    @Test
    void testPrenotazioneConstructor() throws ParseException {
        DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT, Locale.ITALY);
        Date date = df.parse("10/02/2021");
        PrenotazioneBean prenotazioneBean = new PrenotazioneBean("0001","0001-0512105933",date, false, "1", "F3", "INF-01", "0512105933");
        assertNotNull(prenotazioneBean);
    }

    /**
     * Getter Methods Testing
     * @throws ParseException
     */
    @Test
    void testGetCodice() throws ParseException {
        DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT, Locale.ITALY);
        Date date = df.parse("10/02/2021");
        PrenotazioneBean prenotazioneBean = new PrenotazioneBean("0001","0001-0512105933",date, false, "1", "F3", "INF_01", "0512105933");
        assertEquals("0001", prenotazioneBean.getCodice());

    }


    @Test
    void testGetQrCode() throws ParseException {
        DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT, Locale.ITALY);
        Date date = df.parse("10/02/2021");
        PrenotazioneBean prenotazioneBean = new PrenotazioneBean("0001","0001-0512105933-10022021",date, false, "1","F3", "INF_01", "0512105933");
        assertEquals("0001-0512105933-10022021", prenotazioneBean.getQrCode());

    }

    @Test
    void testGetData() throws ParseException {
        DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT, Locale.ITALY);
        Date date = df.parse("10/02/2021");
        PrenotazioneBean prenotazioneBean = new PrenotazioneBean("0001","0001-0512105933",date, false, "1", "F3", "INF_01", "0512105933");
        assertEquals(date, prenotazioneBean.getData());

    }

    @Test
    void testIsGruppo() throws ParseException {
        DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT, Locale.ITALY);
        Date date = df.parse("10/02/2021");
        PrenotazioneBean prenotazioneBean = new PrenotazioneBean("0001","0001-0512105933",date, false, "1","F3", "INF_01", "0512105933");
        assertEquals(false, prenotazioneBean.isGruppo());

    }

    @Test
    void testGetMatricolaStudente() throws ParseException {
        DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT, Locale.ITALY);
        Date date = df.parse("10/02/2021");
        PrenotazioneBean prenotazioneBean = new PrenotazioneBean("0001","0001-0512105933",date, false, "1","F3", "INF_01", "0512105933");
        assertEquals("0512105933", prenotazioneBean.getMatricolaStudente());

    }

    @Test
    void testGetCodiceAula() throws ParseException {
        DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT, Locale.ITALY);
        Date date = df.parse("10/02/2021");
        PrenotazioneBean prenotazioneBean = new PrenotazioneBean("0001","0001-0512105933",date, false, "1", "F3", "INF_01", "0512105933");
        assertEquals("INF_01", prenotazioneBean.getCodiceAula());
 }

    @Test
    void testGetCodicePosto() throws ParseException{
        DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT, Locale.ITALY);
        Date date = df.parse("10/02/2021");
        PrenotazioneBean prenotazioneBean = new PrenotazioneBean("0001","0001-0512105933",date, false, "1", "F3", "INF_01", "0512105933");
        assertEquals("1", prenotazioneBean.getCodicePosto());

    }

    @Test
    void testGetEdificio() throws ParseException{
        DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT, Locale.ITALY);
        Date date = df.parse("10/02/2021");
        PrenotazioneBean prenotazioneBean = new PrenotazioneBean("0001","0001-0512105933",date, false, "1", "F3","INF_01", "0512105933");
        assertEquals("F3", prenotazioneBean.getEdificio());


    }

    /**
     * Setter Methods Testing
     */
    @Test
    void testSetCodice(){
        PrenotazioneBean prenotazioneBean = new PrenotazioneBean();
        prenotazioneBean.setCodice("002");
        assertEquals("002", prenotazioneBean.getCodice());
    }

    @Test
    void testSetQrCode(){
        PrenotazioneBean prenotazioneBean = new PrenotazioneBean();
        prenotazioneBean.setQrCode("0002-0512105949-19022021");
        assertEquals("0002-0512105949-19022021", prenotazioneBean.getQrCode());
    }

    @Test
    void testSetData() throws ParseException {
        PrenotazioneBean prenotazioneBean = new PrenotazioneBean();
        DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT, Locale.ITALY);
        Date date = df.parse("19/02/2021");
        prenotazioneBean.setData(date);
        assertEquals(date, prenotazioneBean.getData());
    }

    @Test
    void testSetGruppo(){
        PrenotazioneBean prenotazioneBean = new PrenotazioneBean();
        prenotazioneBean.setGruppo(true);
        assertEquals(true, prenotazioneBean.isGruppo());
    }

    @Test
    void testSetStudente(){
        PrenotazioneBean prenotazioneBean = new PrenotazioneBean();
        prenotazioneBean.setMatricolaStudente("0512105949");
        assertEquals("0512105949", prenotazioneBean.getMatricolaStudente());
    }

    @Test
    void testSetCodiceAula(){
        PrenotazioneBean prenotazioneBean = new PrenotazioneBean();
        prenotazioneBean.setCodiceAula("INF-02");
        assertEquals("INF-02", prenotazioneBean.getCodiceAula());
    }

    @Test
    void testSetCodicePosto(){
        PrenotazioneBean prenotazioneBean = new PrenotazioneBean();
        prenotazioneBean.setCodicePosto("2");
        assertEquals("2", prenotazioneBean.getCodicePosto());
    }
    @Test
    void testSetEdificio(){
        PrenotazioneBean prenotazioneBean = new PrenotazioneBean();
        prenotazioneBean.setEdificio("F2");
        assertEquals("F2", prenotazioneBean.getEdificio());


    }






}
