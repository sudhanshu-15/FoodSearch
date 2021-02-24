package com.example.yummlyteam.app.adapter;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.yummlyteam.app.api.ApiClient;
import com.example.yummlyteam.app.api.ApiInterface;
import com.example.yummlyteam.app.util.SearchDiffUtilCallback;
import com.example.yummlyteam.yummly_project.R;
import com.example.yummlyteam.app.Util;
import com.example.yummlyteam.app.model.Match;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecipeListAdapter extends RecyclerView.Adapter<RecipeListAdapter.RecipeViewHolder> {

  private List<Match> recipeList;


  public RecipeListAdapter(List<Match> recipeList) {
    this.recipeList = recipeList;
  }

  @NonNull
  @Override
  public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
    View view = LayoutInflater.from(parent.getContext()).
        inflate(R.layout.recipe_row, parent, false);
    return new RecipeViewHolder(view);
  }

  @Override
  public void onBindViewHolder(@NonNull RecipeViewHolder recipeViewHolder, int i) {

    Match recipe = recipeList.get(i);

    recipeViewHolder.recipeName.setText(recipe.getRecipeName());
    String preparationTime = recipe.getTotalTimeInSeconds() != null
            ? Util.timeFormatter(recipe.getTotalTimeInSeconds())
            : "--";
    recipeViewHolder.totalTime.setText(preparationTime);
    recipeViewHolder.totalCalories.setText("--");
    recipeViewHolder.ingredients.setText(String.valueOf(recipe.getIngredients().size()));

    Drawable placeHolderDrawable = recipeViewHolder
            .itemView
            .getContext()
            .getDrawable(R.drawable.ic_food_placeholder);

    if (recipe.getSmallImageUrls() != null && !recipe.getSmallImageUrls().isEmpty()) {
      Picasso.with(recipeViewHolder.itemView.getContext())
              .load(recipe.getSmallImageUrls().get(0))
              .placeholder(placeHolderDrawable)
              .error(placeHolderDrawable)
              .networkPolicy(
                      Util.isNetworkConnectionAvailable(recipeViewHolder.itemView.getContext()) ?
                              NetworkPolicy.NO_CACHE : NetworkPolicy.OFFLINE)
              .into(recipeViewHolder.recipeImageView);
    } else {
      Picasso.with(recipeViewHolder.itemView.getContext())
              .load(R.drawable.ic_food_placeholder)
              .into(recipeViewHolder.recipeImageView);
    }


    if (recipe.getFlavors() != null && recipe.getFlavors().getBitter().equals(1f)) {
      recipeViewHolder.recipeBitternessIndicator.setVisibility(View.VISIBLE);
    } else {
      recipeViewHolder.recipeBitternessIndicator.setVisibility(View.GONE);
    }

    ApiInterface apiService =
            ApiClient.getClient().create(ApiInterface.class);
    Call<Match> getRecipeCall = apiService.getRecipe(recipe.getId());

    getRecipeCall.enqueue(new Callback<Match>() {
      @Override
      public void onResponse(Call<Match> call, Response<Match> response) {

      }

      @Override
      public void onFailure(Call<Match> call, Throwable t) {

      }
    });
  }

  @Override
  public int getItemCount() {
    return recipeList.size();
  }

  public boolean addItems(List<Match> newItems) {
    if (recipeList !=null) {
      int preSize = recipeList.size();
      List<Match> updatedList = new ArrayList<>(recipeList);
      updatedList.addAll(preSize, newItems);
      DiffUtil.DiffResult diffResult =
              DiffUtil.calculateDiff(new SearchDiffUtilCallback(recipeList, updatedList));
      recipeList = updatedList;
      diffResult.dispatchUpdatesTo(this);
      return true;
    }
    return false;
  }

  public void clearList() {
    if (recipeList != null) {
      recipeList.clear();
    }
    notifyDataSetChanged();
  }


  public static class RecipeViewHolder extends RecyclerView.ViewHolder {

    TextView ingredients, recipeName, totalCalories, totalTime, recipeBitternessIndicator;
    ImageView recipeImageView;

    public RecipeViewHolder(@NonNull View itemView) {
      super(itemView);
      recipeName = itemView.findViewById(R.id.recipeName);
      ingredients = itemView.findViewById(R.id.ingredients);
      totalCalories = itemView.findViewById(R.id.totalCalories);
      totalTime = itemView.findViewById(R.id.totalTime);
      recipeImageView = itemView.findViewById(R.id.recipeImageView);
      recipeBitternessIndicator = itemView.findViewById(R.id.bitter_label);
    }
  }

}
