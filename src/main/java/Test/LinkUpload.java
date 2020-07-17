package Test;

import jdk.nashorn.internal.runtime.regexp.joni.Regex;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class LinkUpload {
    public static void main(String[] args) {
       String str = "25yhg";
        System.out.println(str.replaceAll("[a-zA-Z]",""));
    }
}
