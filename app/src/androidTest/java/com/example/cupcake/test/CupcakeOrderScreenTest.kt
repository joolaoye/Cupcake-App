package com.example.cupcake.test

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.example.cupcake.data.DataSource
import com.example.cupcake.data.OrderUiState
import com.example.cupcake.ui.OrderSummaryScreen
import com.example.cupcake.ui.SelectOptionScreen
import com.example.cupcake.ui.StartOrderScreen
import org.junit.Rule
import org.junit.Test

class CupcakeOrderScreenTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private val flavors = listOf("Vanilla", "Chocolate", "Hazelnut", "Cookie", "Mango")

    private val subtotal = "$100"

    fun setSelectOptionScreen() {
        composeTestRule.setContent {
            SelectOptionScreen(subtotal = subtotal, options = flavors)
        }
    }

    @Test
    fun selectOptionScreen_verifyContent() {
        setSelectOptionScreen()

        flavors.forEach { flavor ->
            composeTestRule.onNodeWithText(flavor).assertIsDisplayed()
        }

        composeTestRule.onNodeWithText(
            composeTestRule.activity.getString(
                com.example.cupcake.R.string.subtotal_price,
                subtotal
            )
        ).assertIsDisplayed()

        composeTestRule.onNodeWithStringId(com.example.cupcake.R.string.next).assertIsNotEnabled()
    }

    @Test
    fun selectOptionScreen_optionSelected_nextButtonIsEnabled() {
        setSelectOptionScreen()

        composeTestRule.onNodeWithText(flavors[0]).performClick()

        composeTestRule.onNodeWithStringId(com.example.cupcake.R.string.next).assertIsEnabled()
    }

    @Test
    fun StartOrderScreen_verifyContent() {
        val quantityOptions = DataSource.quantityOptions

        composeTestRule.setContent {
            StartOrderScreen(
                quantityOptions = quantityOptions,
                onNextButtonClicked = {}
            )
        }

        quantityOptions.forEach {
            option ->

            composeTestRule.onNodeWithStringId(option.first).assertIsDisplayed()
        }
    }

    @Test
    fun SummaryScreen_verifyContent() {
        val orderUiState = OrderUiState(6, "Chocolate", "Thu Jun 20", "$12.00")

        composeTestRule.setContent {
            OrderSummaryScreen(
                orderUiState = orderUiState,
                onSendButtonClicked = { subject: String, summary: String -> },
                onCancelButtonClicked = {}
            )
        }

        composeTestRule.onNodeWithText("${orderUiState.quantity} cupcakes").assertIsDisplayed()
        composeTestRule.onNodeWithText("${orderUiState.flavor}").assertIsDisplayed()
        composeTestRule.onNodeWithText("${orderUiState.date}").assertIsDisplayed()

        composeTestRule.onNodeWithText("Subtotal ${orderUiState.price}").assertIsDisplayed()

        composeTestRule.onNodeWithStringId(com.example.cupcake.R.string.send).assertIsDisplayed()
        composeTestRule.onNodeWithStringId(com.example.cupcake.R.string.cancel).assertIsDisplayed()
    }

}