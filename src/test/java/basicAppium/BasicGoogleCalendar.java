package basicAppium;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.URL;
import java.time.Duration;

public class BasicGoogleCalendar {

    private AndroidDriver driver;

    @BeforeEach
    public void setUp() throws Exception {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("platformName", "Android");
        capabilities.setCapability("appium:automationName", "UiAutomator2");
        capabilities.setCapability("appium:deviceName", "UCBx");
        capabilities.setCapability("appium:udid", "emulator-5554");
        capabilities.setCapability("appium:platformVersion", "17.0");
        capabilities.setCapability("appium:appPackage", "com.google.android.calendar");

        // Sin appActivity para evitar bloqueos de seguridad al iniciar la sesión
        capabilities.setCapability("appium:appActivity", "com.android.calendar.AllInOneActivity");
        capabilities.setCapability("appium:appWaitActivity", "com.google.android.*,com.android.calendar.*");
        capabilities.setCapability("appium:noReset", true);
        //capabilities.setCapability("appium:dontStopAppOnReset", true);

        driver = new AndroidDriver(new URL("http://127.0.0.1:4723/"), capabilities);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(15));

        // ¡Método infalible! Forzamos la apertura de la app de forma segura tras iniciar la sesión
         driver.activateApp("com.google.android.calendar");
    }

    @Test
    public void verifyGoogleCalendarOpens() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(12));

        // Verificamos que el paquete actual ya sea el de Google Calendar
        String currentPackage = driver.getCurrentPackage();
        Assertions.assertEquals("com.google.android.calendar", currentPackage, "El paquete activo corresponde a Google Calendar");

        // Verificación visual de que la interfaz cargó correctamente
        WebElement calendarRoot = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//*[@resource-id='com.google.android.calendar:id/toolbar' or contains(@resource-id, 'calendar') or @class='android.view.ViewGroup']")
        ));

        Assertions.assertTrue(calendarRoot.isDisplayed(), "La interfaz principal de Google Calendar está visible.");


    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
            System.out.println("Sesión de Appium finalizada.");
        }
    }
}