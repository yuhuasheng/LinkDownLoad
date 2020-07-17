package Test;

import mailtools.SendMail;

import javax.mail.MessagingException;
import java.util.*;

public class SendMailTest {
    public static void main(String[] args) {
        try{
            Map<String,String> map= new HashMap<String,String>();
            SendMail mail = new SendMail("1139325792@qq.com","");
            map.put("mail.smtp.host", "smtp.qq.com");
            map.put("mail.smtp.auth", "true");
            map.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            map.put("mail.smtp.port", "465");
            map.put("mail.smtp.socketFactory.port", "465");
            mail.setPros(map);
            mail.initMessage();
            List<String> list = new ArrayList<String>();
            list.add("****@qq.com");
            list.add("****@qq.com");
            mail.setRecipients(list);
            mail.setSubject("测试邮箱");
            //mail.setText("谢谢合作");
            mail.setDate(new Date());
            mail.setFrom("MY");
//      mail.setMultipart("D:你你你.txt");
            mail.setContent("谢谢合作", "text/html; charset=UTF-8");
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
