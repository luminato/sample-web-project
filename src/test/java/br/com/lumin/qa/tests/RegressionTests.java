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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Epic("Portfólio de Automação")
@Feature("Carrinho")
@Owner("Luminato QA")
@ExtendWith(ScreenshotOnFailureExtension.class)
class RegressionTests extends BaseTest {

    @Test
    @Tag("regression")
    @Severity(SeverityLevel.NORMAL)
    @Story("Adição de item")
    @DisplayName("Regressão - adicionar produto ao carrinho")
    @Description("Confirma que um produto pode ser adicionado ao carrinho e que o contador é atualizado.")
    void shouldAddProductToCart() {
        LoginPage loginPage = new LoginPage(page);
        loginPage.open(TestConfig.baseUrl());
        loginPage.login("standard_user", "secret_sauce");

        InventoryPage inventoryPage = new InventoryPage(page);
        inventoryPage.addBackpackToCart();

        Allure.step("Validar atualização do contador do carrinho");
        assertEquals("1", inventoryPage.cartBadgeText());
    }

    @Test
    @Tag("regression")
    @Severity(SeverityLevel.NORMAL)
    @Story("Remoção de item")
    @DisplayName("Regressão - remover produto do carrinho")
    @Description("Garante que o usuário consegue remover um item e limpar o carrinho com sucesso.")
    void shouldRemoveProductFromCart() {
        LoginPage loginPage = new LoginPage(page);
        loginPage.open(TestConfig.baseUrl());
        loginPage.login("standard_user", "secret_sauce");

        InventoryPage inventoryPage = new InventoryPage(page);
        inventoryPage.addBackpackToCart();
        inventoryPage.removeBackpackFromCart();

        Allure.step("Validar remoção visual do contador do carrinho");
        assertFalse(inventoryPage.isCartBadgeVisible());
    }

    @Test
    @Tag("regression")
    @Severity(SeverityLevel.NORMAL)
    @Story("Ordenação")
    @DisplayName("Regressão - ordenar produtos por menor preço")
    @Description("Demonstra que o catálogo respeita a ordenação visual por preço crescente.")
    void shouldSortProductsByLowestPrice() {
        LoginPage loginPage = new LoginPage(page);
        loginPage.open(TestConfig.baseUrl());
        loginPage.login("standard_user", "secret_sauce");

        InventoryPage inventoryPage = new InventoryPage(page);
        inventoryPage.sortByLowestPrice();

        Allure.step("Validar item mais barato na primeira posição");
        assertEquals("Sauce Labs Onesie", inventoryPage.firstProductName());
        assertEquals("$7.99", inventoryPage.firstProductPrice());
    }

    @Test
    @Tag("regression")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Validação de checkout")
    @DisplayName("Regressão - checkout exige dados obrigatórios")
    @Description("Comprova que o formulário de checkout valida nome, sobrenome e CEP antes de avançar.")
    void shouldRequireCustomerDataOnCheckout() {
        LoginPage loginPage = new LoginPage(page);
        loginPage.open(TestConfig.baseUrl());
        loginPage.login("standard_user", "secret_sauce");

        InventoryPage inventoryPage = new InventoryPage(page);
        inventoryPage.addBackpackToCart();
        inventoryPage.openCart();

        CheckoutPage checkoutPage = new CheckoutPage(page);
        checkoutPage.startCheckout();
        checkoutPage.continueCheckout();

        Allure.step("Validar erro de obrigatoriedade no primeiro nome");
        assertTrue(checkoutPage.errorMessage().contains("First Name is required"));
    }

    @Test
    @Tag("regression")
    @Severity(SeverityLevel.NORMAL)
    @Story("Sessão do usuário")
    @DisplayName("Regressão - usuário faz logout com sucesso")
    @Description("Valida que a sessão pode ser encerrada e o usuário retorna à tela inicial.")
    void shouldLogoutSuccessfully() {
        LoginPage loginPage = new LoginPage(page);
        loginPage.open(TestConfig.baseUrl());
        loginPage.login("standard_user", "secret_sauce");

        InventoryPage inventoryPage = new InventoryPage(page);
        inventoryPage.logout();

        Allure.step("Validar retorno para a tela de autenticação");
        assertTrue(loginPage.isLoginButtonVisible());
    }
}
