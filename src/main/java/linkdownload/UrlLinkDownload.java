package linkdownload;

import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import util.Tools;

import javax.xml.bind.Element;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class UrlLinkDownload {
    static WebDriver driver = null ;
    static WebElement liElement;
    static String letterContents = null ; //字母信息
    static String urlMessage = null ; //字母下的链接信息
    static List<String> downloadMessageList ; //存放爬虫得到的每个字母下的URL
    static Integer currentPageAccount = null ; //当前页数
    static Integer allPageNumber = null ; //总页数
    public static void main(String[] args) {
        downloadMessageList = new ArrayList<>();
        System.setProperty("webdriver.chrome.driver","C:\\chromedriver_win32\\chromedriver.exe");
        driver = new ChromeDriver(); // 新建一个WebDriver 的对象，new一个ChromeDriver的驱动
        getLetterMessage();
        writeUrlMessage();
    }

    /**
     * 获取26个字母显示的A标签
     */
    public static void getLetterMessage(){
        String[] lettrrArray = Tools.getLetters();
        for(int i = 0 ; i < lettrrArray.length ; i ++ ){
            letterContents = lettrrArray[i];
            downloadMessageList.add(letterContents);
            List<String> resultList = getHomeUrlMessage();
            if(resultList == null || resultList.size() <= 0){
                return;
            }else{
                directionSecondUrl(resultList);
            }
        }
    }
    /**
     * 获取首页<a>标签下的链接集合信息
     */
    public static List<String> getHomeUrlMessage(){
        Boolean check = null;
        List<String> skipUrlList = new ArrayList<>();
        driver.get("http://www.kmdbioscience.com/products/typeview/100");
        check = judgeLetterName(); //判断当前字母是否是在网页div的范围之内
        if(!check){
            driver.close();
            return null;
        }
        WebElement ulElements = driver.findElement(By.xpath("//*[@id=\"moleculeList\"]"));
        List<WebElement> liElements = ulElements.findElements(By.tagName("li")); //获取列表清单
        for(WebElement liElement : liElements){
//            if(!downloadMessageList.contains(liElement.getText())){
//                downloadMessageList.add(liElement.getText());
//            }
            System.out.println("主页下<a/>标签的名字"+liElement.getText());
            String skipUrl = liElement.findElement(By.tagName("a")).getAttribute("href"); //跳转链接
            skipUrlList.add(skipUrl);
        }
        for(String message : skipUrlList){
            System.out.println(message);
        }
        return skipUrlList;
    }


    /**
     * 判断是否和网页上的26个字母匹配
     * @return
     */
    public static Boolean judgeLetterName(){
        String divElementName = driver.findElement(By.xpath("//*[@id=\"firstLetterList\"]")).getText(); //获取a-z字母的元素集合
        String[] letterArrays = divElementName.split(" ");
        Boolean flag = false ;
        for(int k = 0 ; k < letterArrays.length ; k ++){
            if(letterContents.equals(letterArrays[k])){
                flag = true;
                break;
            }
        }
        return flag;
    }

    public static void directionSecondUrl(List<String> skipUrlList){
        for(String link : skipUrlList){
            driver.get(link);
            String pageUrl = driver.getCurrentUrl(); //获取当前请求链接
            driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
            urlMessage = pageUrl.substring(pageUrl.lastIndexOf("=")+1,pageUrl.length());
            if(!downloadMessageList.contains(urlMessage)){
                downloadMessageList.add(urlMessage);
            }
            getPageCount();
            if(currentPageAccount < allPageNumber){ //遍历多页
                downloadMoreUrl();
                driver = new ChromeDriver();
            }else if (currentPageAccount == allPageNumber){ //遍历一页
                downloadOneUrl();
                driver = new ChromeDriver();
            }
        }
    }

    /**
     * 确定当前页和总页数的关系
     */
    public static void getPageCount(){
        //定位下一页的按钮是否可用
        WebElement pageDivElement = driver.findElement(By.xpath("/html/body/div[5]/div/div[4]/div[1]/div"));
        String currentPageNumberString = pageDivElement.findElement(By.xpath("/html/body/div[5]/div/div[4]/div[1]/div/ul/li[2]/span")).getText(); //获取当前页数
        currentPageAccount = Integer.parseInt(currentPageNumberString); //当前页数
        String allPageNumberString = pageDivElement.findElement(By.xpath("/html/body/div[5]/div/div[4]/div[1]/div/ul/li[4]/span[2]")).getText(); //获取总页数
        String AllpageNumber = allPageNumberString.replace(" ","").replaceAll("[a-zA-Z]","");
        allPageNumber = Integer.parseInt(AllpageNumber); //总页数
    }
    /**
     * 遍历多页
     */
    public static void downloadMoreUrl(){
        while (currentPageAccount < allPageNumber){ //存在下一页
            WebElement pageEleMents = driver.findElement(By.xpath("/html/body/div[5]/div/div[4]/div[1]/div/ul/li[3]/a")); //重新定位到下一页这个图标
            getUrlMessage(); //获取URL信息
            pageEleMents.click(); //获取下一页
            getPageCount(); //重新定位当前页和下一页的关系
        }
        if(currentPageAccount == allPageNumber){
            downloadOneUrl(); //获取Url
        }
        driver.close(); //浏览器关闭
    }

    public static void downloadOneUrl(){
        getUrlMessage();
        driver.close(); //浏览器关闭
    }

    /**
     * 获取URL信息
     */
    public static void getUrlMessage(){
        WebElement tableElement = driver.findElement(By.xpath("/html/body/div[5]/div/div[4]/div[1]/table/tbody")); //获取整个table结构
        List<WebElement> trElements = tableElement.findElements(By.tagName("tr"));
        for(WebElement trElement : trElements){
            System.out.println(trElement.getText());
            WebElement tdElement = trElement.findElements(By.tagName("td")).get(1);
            WebElement aElement = tdElement.findElement(By.tagName("a"));
            String relativeUrl = aElement.getAttribute("href");
            if(!downloadMessageList.contains(relativeUrl)){
                downloadMessageList.add(relativeUrl);
            }else{
                downloadMessageList.add("存在相同的Url");
            }
        }
    }


    public static void writeUrlMessage(){
        File resultFile = new File("D:\\url\\网址.txt");
        OutputStreamWriter resultSuccessWrite = null;
        try {
            resultSuccessWrite = new OutputStreamWriter(new FileOutputStream(resultFile), "GBK");
            BufferedWriter successBufferedWriter = new BufferedWriter(resultSuccessWrite);
            StringBuilder successBuilder = new StringBuilder();
            if (!resultFile.exists()) {
                resultFile.createNewFile();
            }
            if(downloadMessageList != null && downloadMessageList.size() > 0){
                for(int i =0 ; i < downloadMessageList.size() ; i++){
                    successBuilder.append((String)downloadMessageList.get(i));
                    successBuilder.append("\r\n");
                }
                successBufferedWriter.write(successBuilder.toString());
                successBufferedWriter.close();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    /**
     * 获取第二页信息
     */
    public static Boolean getSecondUrlMessage(WebElement liElement){
        Boolean check = null;
        liElement.click(); //跳转到第二页
        WebElement pageDivElement = driver.findElement(By.xpath("/html/body/div[5]/div/div[4]/div[1]/div"));
        String currentPageNumberString = pageDivElement.findElement(By.xpath("/html/body/div[5]/div/div[4]/div[1]/div/ul/li[2]/span")).getText(); //获取当前页数
        Integer currentPageAccount = Integer.parseInt(currentPageNumberString);
        String allPageNumberString = pageDivElement.findElement(By.xpath("/html/body/div[5]/div/div[4]/div[1]/div/ul/li[4]/span[2]")).getText(); //获取总页数
        String AllpageNumber = allPageNumberString.replace(" ","").replaceAll("[a-zA-Z]","");
        Integer allPageNumber = Integer.parseInt(AllpageNumber);
        if(currentPageAccount < allPageNumber){ //判断当前页不是最后一页

        }else if(currentPageAccount == allPageNumber){
            check = getThirdUrlMessage();
            if(check){
                return true;
            }else{
                return false;
            }
        }
        return true;
    }

    public static Boolean getThirdUrlMessage(){
        Boolean check = null;
        WebElement tableElement = driver.findElement(By.xpath("/html/body/div[5]/div/div[4]/div[1]/table/tbody")); //获取整个table结构
        List<WebElement> trElements = tableElement.findElements(By.tagName("tr"));
        for(WebElement trElement : trElements){
            trElement.getText();
            List<WebElement> tdElements = trElement.findElements(By.tagName("td"));
            check = getHrefUrl(tdElements);
            if(check){
                continue;
            }else{
                return false;
            }
        }
        return true;
    }

    public static Boolean getHrefUrl(List<WebElement> tdElements){
        Boolean flag = false ;
        try {
            WebElement tdElement = tdElements.get(1).findElement(By.tagName("a"));
            String relativeUrl = tdElement.getAttribute("href");
            System.out.println(relativeUrl);
            flag = true ;
        }catch (Exception e){
            e.printStackTrace();
        }
        return flag ;
    }
}
