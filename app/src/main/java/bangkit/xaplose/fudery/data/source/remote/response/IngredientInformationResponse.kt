package bangkit.xaplose.fudery.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class IngredientResponse(

	@field:SerializedName("shoppingListUnits")
	val shoppingListUnits: List<String>,

	@field:SerializedName("image")
	val image: String,

	@field:SerializedName("amount")
	val amount: Int,

	@field:SerializedName("original")
	val original: String,

	@field:SerializedName("categoryPath")
	val categoryPath: List<String>,

	@field:SerializedName("unitLong")
	val unitLong: String,

	@field:SerializedName("possibleUnits")
	val possibleUnits: List<String>,

	@field:SerializedName("estimatedCost")
	val estimatedCost: EstimatedCost,

	@field:SerializedName("aisle")
	val aisle: String,

	@field:SerializedName("consistency")
	val consistency: String,

	@field:SerializedName("originalName")
	val originalName: String,

	@field:SerializedName("unit")
	val unit: String,

	@field:SerializedName("nutrition")
	val nutrition: Nutrition,

	@field:SerializedName("unitShort")
	val unitShort: String,

	@field:SerializedName("meta")
	val meta: List<Any>,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("id")
	val id: Int
)

data class NutrientsItem(

	@field:SerializedName("amount")
	val amount: Double,

	@field:SerializedName("unit")
	val unit: String,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("title")
	val title: String
)

data class Nutrition(

	@field:SerializedName("caloricBreakdown")
	val caloricBreakdown: CaloricBreakdown,

	@field:SerializedName("weightPerServing")
	val weightPerServing: WeightPerServing,

	@field:SerializedName("flavanoids")
	val flavanoids: List<FlavanoidsItem>,

	@field:SerializedName("properties")
	val properties: List<PropertiesItem>,

	@field:SerializedName("nutrients")
	val nutrients: List<NutrientsItem>
)

data class WeightPerServing(

	@field:SerializedName("amount")
	val amount: Int,

	@field:SerializedName("unit")
	val unit: String
)

data class EstimatedCost(

	@field:SerializedName("unit")
	val unit: String,

	@field:SerializedName("value")
	val value: Double
)

data class FlavanoidsItem(

	@field:SerializedName("amount")
	val amount: Double,

	@field:SerializedName("unit")
	val unit: String,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("title")
	val title: String
)

data class PropertiesItem(

	@field:SerializedName("amount")
	val amount: Double,

	@field:SerializedName("unit")
	val unit: String,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("title")
	val title: String
)

data class CaloricBreakdown(

	@field:SerializedName("percentCarbs")
	val percentCarbs: Double,

	@field:SerializedName("percentProtein")
	val percentProtein: Double,

	@field:SerializedName("percentFat")
	val percentFat: Double
)
