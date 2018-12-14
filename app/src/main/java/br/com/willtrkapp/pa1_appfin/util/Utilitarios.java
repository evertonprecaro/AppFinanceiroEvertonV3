package br.com.willtrkapp.pa1_appfin.util;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class  Utilitarios {
    public Long getUnixDate(Date pData)
    {
        return pData.getTime() / 1000;
    }
    public Long getCurrentUnixDate() {
        Date dtToday = null;
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        try {
            dtToday = formatter.parse(formatter.format(new Date()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return getUnixDate(dtToday);
    }
}
