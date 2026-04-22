package br.com.lumin.qa.pages;

import br.com.lumin.qa.core.AcceptanceReportManager;
import com.microsoft.playwright.Page;
import io.qameta.allure.Step;

public class LoginPage {

    private final Page page;

    public LoginPage(Page page) {
        this.page = page;
    }

    @Step("Acessar a página inicial de login")
    public LoginPage open(String baseUrl) {
        page.navigate(baseUrl);
        AcceptanceReportManager.recordStep(page, "Acessou a página inicial de login");
        return this;
    }

    @Step("Preencher usuário: {username}")
    public LoginPage fillUsername(String username) {
        page.locator("[data-test='username']").fill(username);
        AcceptanceReportManager.recordStep(page, "Preencheu o usuário informado para autenticação");
        return this;
    }

    @Step("Preencher senha")
    public LoginPage fillPassword(String password) {
        page.locator("[data-test='password']").fill(password);
        AcceptanceReportManager.recordStep(page, "Preencheu a senha do cenário");
        return this;
    }

    @Step("Enviar formulário de login")
    public void submit() {
        page.locator("[data-test='login-button']").click();
        AcceptanceReportManager.recordStep(page, "Enviou o formulário de login");
    }

    @Step("Realizar tentativa de login")
    public void login(String username, String password) {
        fillUsername(username);
        fillPassword(password);
        submit();
    }

    @Step("Capturar mensagem de erro apresentada na tela")
    public String errorMessage() {
        return page.locator("[data-test='error']").textContent();
    }

    @Step("Validar presença do botão de login na tela inicial")
    public boolean isLoginButtonVisible() {
        return page.locator("[data-test='login-button']").isVisible();
    }
}
