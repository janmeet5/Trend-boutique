package com.example

import com.example.data.BoutiqueRepository
import com.example.model.FabricSource
import com.example.model.SizingChoice
import com.example.viewmodel.BoutiqueViewModel
import com.example.viewmodel.Screen
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleUnitTest {
  @Test
  fun addition_isCorrect() {
    assertEquals(4, 2 + 2)
  }

  @Test
  fun verifyBoutiqueCatalogAndRepository() {
    val repository = BoutiqueRepository()
    assertTrue(repository.suitCatalog.isNotEmpty())
    assertTrue(repository.punjabCities.contains("Ludhiana"))
    assertTrue(repository.punjabCities.contains("Amritsar"))
    assertTrue(repository.savedProfiles.value.isNotEmpty())
  }

  @Test
  fun verifySmartSizingFlowCalculation() {
    val viewModel = BoutiqueViewModel()
    val suit = viewModel.suitCatalog.first()
    viewModel.openProductDetail(suit)
    assertEquals(Screen.PRODUCT_DETAIL, viewModel.currentScreen.value)

    viewModel.updateFabricSource(FabricSource.BOUTIQUE_FABRIC)
    val totalWithBoutiqueFabric = viewModel.customization.value.calculateTotal()
    val expectedPremium = if (viewModel.customization.value.selectedNeck.contains("Tilla")) 200 else 0
    assertEquals(suit.price + 650 + expectedPremium, totalWithBoutiqueFabric)

    viewModel.proceedToSmartMeasurement()
    assertEquals(Screen.SMART_MEASUREMENT, viewModel.currentScreen.value)

    viewModel.setSizingChoice(
      SizingChoice.DoorstepSample(
        sampleCount = 1,
        fitAdjustmentNotes = "Match exact fit",
        saveFitToProfile = true
      )
    )
    viewModel.proceedToCheckout()
    assertEquals(Screen.CHECKOUT, viewModel.currentScreen.value)
  }
}
