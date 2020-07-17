package download;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class UrlLinkDownLoad {
    static WebDriver driver = null;
    static WebElement liElement;
    public static void main(String[] args) {
        System.setProperty("webdriver.chrome.driver","C:\\chromedriver_win32\\chromedriver.exe");
        driver = new ChromeDriver(); // 新建一个WebDriver 的对象，new一个ChromeDriver的驱动
    }
}
