package selenium;

import static org.junit.jupiter.api.Assertions.*;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.chrome.*;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

public class temporarytest {
    public static void main(String[] args){
        System.setProperty("webdriver.chrome.driver", "selenium/chromedriver");
        ChromeDriver driver=new ChromeDriver();
        driver.get("http://www.google.com/");
    }
}