package com.mcwilliams.theninjamethod.ui.home;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.navigation.NavDirections;
import com.mcwilliams.theninjamethod.R;
import java.lang.IllegalArgumentException;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.HashMap;

public class HomeFragmentDirections {
  private HomeFragmentDirections() {
  }

  @NonNull
  public static ActionHomeFragmentToHomeSecondFragment actionHomeFragmentToHomeSecondFragment(
      @NonNull String myArg) {
    return new ActionHomeFragmentToHomeSecondFragment(myArg);
  }

  public static class ActionHomeFragmentToHomeSecondFragment implements NavDirections {
    private final HashMap arguments = new HashMap();

    private ActionHomeFragmentToHomeSecondFragment(@NonNull String myArg) {
      if (myArg == null) {
        throw new IllegalArgumentException("Argument \"myArg\" is marked as non-null but was passed a null value.");
      }
      this.arguments.put("myArg", myArg);
    }

    @NonNull
    public ActionHomeFragmentToHomeSecondFragment setMyArg(@NonNull String myArg) {
      if (myArg == null) {
        throw new IllegalArgumentException("Argument \"myArg\" is marked as non-null but was passed a null value.");
      }
      this.arguments.put("myArg", myArg);
      return this;
    }

    @Override
    @SuppressWarnings("unchecked")
    @NonNull
    public Bundle getArguments() {
      Bundle __result = new Bundle();
      if (arguments.containsKey("myArg")) {
        String myArg = (String) arguments.get("myArg");
        __result.putString("myArg", myArg);
      }
      return __result;
    }

    @Override
    public int getActionId() {
      return R.id.action_HomeFragment_to_HomeSecondFragment;
    }

    @SuppressWarnings("unchecked")
    @NonNull
    public String getMyArg() {
      return (String) arguments.get("myArg");
    }

    @Override
    public boolean equals(Object object) {
      if (this == object) {
          return true;
      }
      if (object == null || getClass() != object.getClass()) {
          return false;
      }
      ActionHomeFragmentToHomeSecondFragment that = (ActionHomeFragmentToHomeSecondFragment) object;
      if (arguments.containsKey("myArg") != that.arguments.containsKey("myArg")) {
        return false;
      }
      if (getMyArg() != null ? !getMyArg().equals(that.getMyArg()) : that.getMyArg() != null) {
        return false;
      }
      if (getActionId() != that.getActionId()) {
        return false;
      }
      return true;
    }

    @Override
    public int hashCode() {
      int result = 1;
      result = 31 * result + (getMyArg() != null ? getMyArg().hashCode() : 0);
      result = 31 * result + getActionId();
      return result;
    }

    @Override
    public String toString() {
      return "ActionHomeFragmentToHomeSecondFragment(actionId=" + getActionId() + "){"
          + "myArg=" + getMyArg()
          + "}";
    }
  }
}
