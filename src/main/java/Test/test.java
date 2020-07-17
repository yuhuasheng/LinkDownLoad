package Test;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
public class test {
    private static String currentWindow;
    public static void main(String[] args) {
        System.setProperty("webdriver.chrome.driver","C:\\chromedriver_win32\\chromedriver.exe");
        WebDriver driver = new ChromeDriver();
        //       WebDriver driver=DriverFactory.getFirefoxDriver();
        driver.get("http://www.lhgtj.gov.cn/article.asp?ClassID=86&page=1");

        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
        driver.manage().timeouts().pageLoadTimeout(60, TimeUnit.SECONDS);
        WebElement nextPage=driver.findElement(By.xpath("//tr/td/a[@title='下一页']"));
        while(nextPage.isDisplayed())
        {

            List<WebElement> links=driver.findElements(By.xpath("//tr/td/a[starts-with(@href,'article_show.asp?ID=') and @title!='' ]"));
            links.get(0).getText();
            for(WebElement link:links)
            {
                WebDriver window;
                System.out.println(link.getText());
                try {
                    writeToTXT(link.getText());
                } catch (IOException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
                link.click();
                currentWindow = driver.getWindowHandle();
                //get all windows
                Set<String> handles= driver.getWindowHandles();
                for (String s : handles)
                {
                    //current page is don't close
                    if (s.equals(currentWindow))
                    {
                        continue;
                    }
                    else
                    {
                        window =driver.switchTo().window(s);
                        window.manage().window().maximize();
                        window.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
                        window.manage().timeouts().pageLoadTimeout(60, TimeUnit.SECONDS);
                        //get all tables
                        List<WebElement> tbs=window.findElements(By.xpath("//tbody/tr/td/p"));
                        for(WebElement tb:tbs)
                        {
                            System.out.println(tb.getText());
                            try {
                                writeToTXT(tb.getText()+"\n");
                            } catch (IOException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }

                        }
                        //close the table window
                        window .close() ;
                    }
                    //swich to current window
                    driver.switchTo().window(currentWindow);
                }

            }
            // click next page
            nextPage.click();
            //set next page to current page
            driver=driver.switchTo().window(driver.getWindowHandle());
            driver.manage().window().maximize();
            driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
            driver.manage().timeouts().pageLoadTimeout(60, TimeUnit.SECONDS);
            nextPage=driver.findElement(By.xpath("//tr/td/a[@title='下一页']"));

        }


    }
    //write logs
    public static void  writeToTXT(String message) throws IOException
    {
        BufferedWriter bf = null;
        try {
            //set true ,avoid
            bf = new BufferedWriter(new FileWriter("report.txt", true));
            bf.write(message);
            bf.flush();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        finally
        {
            bf.close();
        }

    }
    }

