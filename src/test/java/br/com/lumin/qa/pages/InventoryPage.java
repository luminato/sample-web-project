package br.com.lumin.qa.pages;

import br.com.lumin.qa.core.AcceptanceReportManager;
import com.microsoft.playwright.Page;
import io.qameta.allure.Step;

public class InventoryPage {

    private final Page page;

    public InventoryPage(Page page) {
        this.page = page;
    }

    @Step("Ler título principal da área de produtos")
    public String title() {
        return page.locator(".title").textContent();
    }

    @Step("Contar produtos exibidos no catálogo")
    public int productCount() {
        return page.locator(".inventory_item").count();
    }

    @Step("Adicionar mochila principal ao carrinho")
    public void addBackpackToCart() {
        page.locator("[data-test='add-to-cart-sauce-labs-backpack']").click();
        AcceptanceReportManager.recordStep(page, "Adicionou a mochila principal ao carrinho");
    }

    @Step("Remover mochila principal do carrinho")
    public void removeBackpackFromCart() {
        page.locator("[data-test='remove-sauce-labs-backpack']").click();
        AcceptanceReportManager.recordStep(page, "Removeu a mochila principal do carrinho");
    }

    @Step("Ler contador do carrinho")
    public String cartBadgeText() {
        return page.locator("[data-test='shopping-cart-badge']").textContent();
    }

    @Step("Validar se o contador do carrinho está visível")
    public boolean isCartBadgeVisible() {
        return page.locator("[data-test='shopping-cart-badge']").count() > 0;
    }

    @Step("Abrir carrinho")
    public void openCart() {
        page.locator("[data-test='shopping-cart-link']").click();
        AcceptanceReportManager.recordStep(page, "Abriu o carrinho para revisão dos itens");
    }

    @Step("Ordenar catálogo por menor preço")
    public void sortByLowestPrice() {
        page.locator("[data-test='product-sort-container']").selectOption("lohi");
        AcceptanceReportManager.recordStep(page, "Aplicou ordenação do menor para o maior preço");
    }

    @Step("Ler nome do primeiro produto exibido")
    public String firstProductName() {
        return page.locator("[data-test='inventory-item-name']").first().textContent();
    }

    @Step("Ler preço do primeiro produto exibido")
    public String firstProductPrice() {
        return page.locator("[data-test='inventory-item-price']").first().textContent();
    }

    @Step("Realizar logout do sistema")
    public void logout() {
        page.locator("#react-burger-menu-btn").click();
        page.locator("#logout_sidebar_link").waitFor();
        page.locator("#logout_sidebar_link").click();
        AcceptanceReportManager.recordStep(page, "Realizou logout e retornou à tela inicial");
    }
}
