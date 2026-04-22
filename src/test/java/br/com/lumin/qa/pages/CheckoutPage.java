package br.com.lumin.qa.pages;

import br.com.lumin.qa.core.AcceptanceReportManager;
import com.microsoft.playwright.Page;
import io.qameta.allure.Step;

public class CheckoutPage {

    private final Page page;

    public CheckoutPage(Page page) {
        this.page = page;
    }

    @Step("Iniciar checkout a partir do carrinho")
    public void startCheckout() {
        page.locator("[data-test='checkout']").click();
        AcceptanceReportManager.recordStep(page, "Iniciou o fluxo de checkout a partir do carrinho");
    }

    @Step("Preencher dados do checkout")
    public void fillCustomerInformation(String firstName, String lastName, String postalCode) {
        page.locator("[data-test='firstName']").fill(firstName);
        page.locator("[data-test='lastName']").fill(lastName);
        page.locator("[data-test='postalCode']").fill(postalCode);
        AcceptanceReportManager.recordStep(page, "Preencheu os dados obrigatórios do checkout");
    }

    @Step("Avançar para a revisão do pedido")
    public void continueCheckout() {
        page.locator("[data-test='continue']").click();
        AcceptanceReportManager.recordStep(page, "Avançou para a revisão do pedido");
    }

    @Step("Finalizar a compra")
    public void finishCheckout() {
        page.locator("[data-test='finish']").click();
        AcceptanceReportManager.recordStep(page, "Finalizou a compra e concluiu o fluxo");
    }

    @Step("Ler mensagem de erro do checkout")
    public String errorMessage() {
        return page.locator("[data-test='error']").textContent();
    }

    @Step("Ler mensagem final de sucesso")
    public String successMessage() {
        return page.locator("[data-test='complete-header']").textContent();
    }
}
