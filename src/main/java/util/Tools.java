package util;

import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Tools {

    /**
     * 获取页面的 A-Z 字母
     * @return
     */

    public static String[] getLetters(){
        ArrayList<String> arrayList = new ArrayList<>();
        try{
            ClassPathResource resource=new ClassPathResource("Letter.txt");
            InputStream inputStream=resource.getInputStream();
            InputStreamReader inputReader = new InputStreamReader(inputStream);
            BufferedReader bf = new BufferedReader(inputReader);
            // 按行读取字符串
            String str;
            while ((str = bf.readLine()) != null) {
                arrayList.add(str);
            }
            bf.close();
            inputReader.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        int length=arrayList.size();
        String[] array=new String[length];
        for(int i=0;i<length;i++){
            array[i]=arrayList.get(i);
        }
        return array;
    }
}
