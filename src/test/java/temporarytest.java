import static org.junit.jupiter.api.Assertions.*;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

public class temporarytest {
    public static void main(String[] args){
        System.setProperty("webdriver.gecko.driver", "src/test/geckodriver");
        FirefoxDriver driver=new FirefoxDriver();
        driver.get("http://www.google.com/");
    }
}