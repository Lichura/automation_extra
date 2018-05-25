package com.project.pages;

import org.openqa.selenium.By;
import com.automation.model.testing.UserStory;
import com.automation.model.testing.objects.PageObject;

public class ExtranjeriaHomePage extends PageObject {

    // region WebElements
    // region WebElements
    private By nie = By.name("txtNie");
    private By nacimiento = By.name("txtAnnonac");
    private By nacionalidad = By.name("lstNacion");
    private By caducidad = By.name("txtFCaduca");
    private By sexo = By.name("lstSex");
    private By estado = By.name("lstEstCivil");
    private By domicilio = By.name("txtDom");
    private By numero = By.name("txtNmh");
    private By piso = By.name("txtPih");
    private By letra = By.name("txtLth");
    private By provincia = By.name("lstProv");
    private By localidad = By.name("lstMuni");
    // endregion


    public ExtranjeriaHomePage(UserStory userS) {
        super(userS);
    }


    // region Methods
    public ExtranjeriaHomePage consultarConFormulario() {
        debugBegin();

        //webDriver.moveToElement(dressesBtn);
        webDriver.click(By.cssSelector(".mf-icon-next-16"));

        debugEnd();

        return this;
    }

    public ExtranjeriaHomePage rellenarConsulta() {
        debugBegin();

        //webDriver.moveToElement(dressesBtn);
        webDriver.appendText(nie, "Y4930436D");
        webDriver.appendText(nacimiento, "1987");
        webDriver.appendText(nacionalidad, "MEXICO");
        webDriver.appendText(caducidad, "20/06/2018");
        webDriver.click(By.cssSelector(".mf-icon-next-16"));
        debugEnd();

        return this;
    }

    public ExtranjeriaHomePage aceptarConformidad() {
        debugBegin();

        webDriver.click(By.cssSelector(".mf-icon-next-16"));

        debugEnd();

        return this;
    }

    public ExtranjeriaHomePage rellenarDatos() throws InterruptedException {
        debugBegin();


        //webDriver.moveToElement(dressesBtn);
        webDriver.appendText(sexo, "MUJER");
        webDriver.appendText(estado, "SOLTERO/A");
        webDriver.appendText(domicilio, "CARRER DE CORSEGA");
        webDriver.appendText(numero, "540");
        webDriver.appendText(piso, "ES");
        webDriver.appendText(letra, "4");
        webDriver.appendText(provincia, "BARCELONA");
        webDriver.appendText(localidad, "BAR");
        webDriver.wait();
        webDriver.click(By.cssSelector(".mf-icon-next-16"));

        debugEnd();

        return this;

    }

    public String buscarelemento() {
        debugBegin();
        String newElement = webDriver.getText(By.className("infoAzul"));
        debugEnd();

       return newElement;
    }
    // endregion
}
