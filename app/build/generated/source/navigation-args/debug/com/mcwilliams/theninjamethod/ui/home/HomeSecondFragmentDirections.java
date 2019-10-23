package com.mcwilliams.theninjamethod.ui.home;

import androidx.annotation.NonNull;
import androidx.navigation.ActionOnlyNavDirections;
import androidx.navigation.NavDirections;
import com.mcwilliams.theninjamethod.R;

public class HomeSecondFragmentDirections {
  private HomeSecondFragmentDirections() {
  }

  @NonNull
  public static NavDirections actionHomeSecondFragmentToHomeFragment() {
    return new ActionOnlyNavDirections(R.id.action_HomeSecondFragment_to_HomeFragment);
  }
}
