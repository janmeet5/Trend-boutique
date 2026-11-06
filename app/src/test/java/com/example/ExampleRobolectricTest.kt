package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.BoutiqueRepository
import com.example.model.FabricSource
import com.example.model.SizingChoice
import com.example.viewmodel.BoutiqueViewModel
import com.example.viewmodel.Screen
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("The Trend Boutique", appName)
  }

  @Test
  fun `verify boutique catalog and repository defaults`() {
    val repository = BoutiqueRepository()
    assertTrue(repository.suitCatalog.isNotEmpty())
    assertTrue(repository.punjabCities.contains("Ludhiana"))
    assertTrue(repository.punjabCities.contains("Amritsar"))
    assertTrue(repository.savedProfiles.value.isNotEmpty())
  }

  @Test
  fun `verify smart sizing flow calculation`() {
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
