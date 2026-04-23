package br.com.lumin.qa.tests;

import br.com.lumin.qa.core.BaseTest;
import br.com.lumin.qa.core.ScreenshotOnFailureExtension;
import br.com.lumin.qa.core.TestConfig;
import br.com.lumin.qa.pages.CheckoutPage;
import br.com.lumin.qa.pages.InventoryPage;
import br.com.lumin.qa.pages.LoginPage;
import io.qameta.allure.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Epic("Portfólio de Automação")
@Feature("Autenticação")
@Owner("Luminato QA")
@ExtendWith(ScreenshotOnFailureExtension.class)
class SmokeTests extends BaseTest {

    @Test
    @Tag("smoke")
    @Tag("critical")
    @Severity(SeverityLevel.BLOCKER)
    @Story("Acesso ao catálogo")
    @DisplayName("Smoke - usuário padrão acessa o catálogo")
    @Description("Valida o fluxo crítico de login com credenciais válidas e acesso à página de produtos.")
    void shouldLoginSuccessfully() {
        LoginPage loginPage = new LoginPage(page);
        loginPage.open(TestConfig.baseUrl());
        loginPage.login("standard_user", "secret_sauce");

        InventoryPage inventoryPage = new InventoryPage(page);
        Allure.step("Validar redirecionamento para a área de inventário");
        assertTrue(page.url().contains("inventory"));
        assertEquals("Products", inventoryPage.title());
    }

    @Test
    @Tag("smoke")
    @Tag("critical")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Validação de credenciais")
    @DisplayName("Smoke - sistema bloqueia login inválido")
    @Description("Garante que o sistema apresenta mensagem clara quando a senha informada é inválida.")
    void shouldShowErrorForInvalidLogin() {
        LoginPage loginPage = new LoginPage(page);
        loginPage.open(TestConfig.baseUrl());
        loginPage.login("standard_user", "senha_invalida");

        Allure.step("Validar mensagem de erro amigável para o usuário");
        assertTrue(loginPage.errorMessage().contains("Username and password do not match"));
    }

    @Test
    @Tag("smoke")
    @Tag("critical")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Catálogo inicial")
    @DisplayName("Smoke - catálogo carrega com os produtos esperados")
    @Description("Valida que a vitrine principal exibe a quantidade padrão de produtos após o login.")
    void shouldDisplayProductCatalog() {
        LoginPage loginPage = new LoginPage(page);
        loginPage.open(TestConfig.baseUrl());
        loginPage.login("standard_user", "secret_sauce");

        InventoryPage inventoryPage = new InventoryPage(page);
        Allure.step("Validar presença dos itens essenciais no catálogo");
        assertEquals(6, inventoryPage.productCount());
    }

    @Test
    @Tag("smoke")
    @Tag("critical")
    @Severity(SeverityLevel.BLOCKER)
    @Story("Checkout")
    @DisplayName("Smoke - usuário conclui checkout com sucesso")
    @Description("Executa o fluxo completo de compra para demonstrar estabilidade do caminho crítico principal.")
    void shouldCompleteCheckoutSuccessfully() {
        LoginPage loginPage = new LoginPage(page);
        loginPage.open(TestConfig.baseUrl());
        loginPage.login("standard_user", "secret_sauce");

        InventoryPage inventoryPage = new InventoryPage(page);
        inventoryPage.addBackpackToCart();
        inventoryPage.openCart();

        CheckoutPage checkoutPage = new CheckoutPage(page);
        checkoutPage.startCheckout();
        checkoutPage.fillCustomerInformation("Lumin", "Consultoria", "01310-100");
        checkoutPage.continueCheckout();
        checkoutPage.finishCheckout();

        Allure.step("Validar mensagem final de compra concluída");
        assertTrue(checkoutPage.successMessage().contains("Thank you"));
    }
}
