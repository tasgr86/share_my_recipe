package tasos.grigoris.sharemyrecipe

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*
import tasos.grigoris.sharemyrecipe.Model.*

interface RetrofitAPI {

    @GET("fetch_recipes.php")
    fun fetchRecipes(): Call<ArrayList<TheRecipes>>

    @GET("fetch_categories.php")
    fun fetchCategories(): Call<ArrayList<TheCategories>>

    @GET("fetch_add_recipe.php")
    fun fetchAddRecipe(): Call<TheAddRecipeReq>

//    @GET("fetch_add_recipe.php")
//    fun fetchAddRecipe(): Call<String>

    @FormUrlEncoded
    @POST("fetch_ingredientsOfRecipe.php")
    fun fetchIngredientsOfRecipe(@Field("recipeID") recipeID: Int): Call<ArrayList<TheIngredients>>

    @FormUrlEncoded
    @POST("fetch_ingredientsOfRecipe.php")
    fun fetchStepsAndIngredients(@Field("recipeID") recipeID: Int, @Field("userID") userID: Int): Call<TheStepsAndIngredients>

    @FormUrlEncoded
    @POST("fetch_steps.php")
    fun fetchSteps(@Field("recipeID") recipeID: Int): Call<ArrayList<TheSteps>>

    @FormUrlEncoded
    @POST("fetch_recipes_of_category.php")
    fun fetchRecipesOfCategory(@Field("categoryID") recipeID: Int): Call<ArrayList<TheRecipes>>

    @FormUrlEncoded
    @POST("submit_rating.php")
    fun submitRating(@Field("userID") userID: Int, @Field("recipeID") recipeID: Int,
                     @Field("rating") rating: Int): Call<String>

    @Multipart
//    @FormUrlEncoded
    @POST("publish_recipe.php")
    fun sendRecipe(@Part("file1") file1 : RequestBody,
                   @Part fileF1 : MultipartBody.Part,
                   @Part("file2") file2 : RequestBody,
                   @Part fileF2 : MultipartBody.Part,
                   @Part("title") title: RequestBody,
                   @Part("steps") steps: RequestBody,
                   @Part("ingredients") ingredients: RequestBody,
                   @Part("short_desc") shortDesc: RequestBody,
                   @Part("posotitaID") posotitaID: RequestBody,
                   @Part("posotitaLabel") posotitaLabel: RequestBody,
                   @Part("categoryID") categoryID: RequestBody,
                   @Part("prepTime") prepTime: RequestBody,
                   @Part("nationality") nationality: RequestBody,
                   @Part("difficulty") difficulty: RequestBody,
                   @Part("userID") userID: RequestBody): Call<String>


}