package com.mcwilliams.theninjamethod.ui.home;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.navigation.NavArgs;
import java.lang.IllegalArgumentException;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.HashMap;

public class HomeSecondFragmentArgs implements NavArgs {
  private final HashMap arguments = new HashMap();

  private HomeSecondFragmentArgs() {
  }

  private HomeSecondFragmentArgs(HashMap argumentsMap) {
    this.arguments.putAll(argumentsMap);
  }

  @NonNull
  @SuppressWarnings("unchecked")
  public static HomeSecondFragmentArgs fromBundle(@NonNull Bundle bundle) {
    HomeSecondFragmentArgs __result = new HomeSecondFragmentArgs();
    bundle.setClassLoader(HomeSecondFragmentArgs.class.getClassLoader());
    if (bundle.containsKey("myArg")) {
      String myArg;
      myArg = bundle.getString("myArg");
      if (myArg == null) {
        throw new IllegalArgumentException("Argument \"myArg\" is marked as non-null but was passed a null value.");
      }
      __result.arguments.put("myArg", myArg);
    } else {
      throw new IllegalArgumentException("Required argument \"myArg\" is missing and does not have an android:defaultValue");
    }
    return __result;
  }

  @SuppressWarnings("unchecked")
  @NonNull
  public String getMyArg() {
    return (String) arguments.get("myArg");
  }

  @SuppressWarnings("unchecked")
  @NonNull
  public Bundle toBundle() {
    Bundle __result = new Bundle();
    if (arguments.containsKey("myArg")) {
      String myArg = (String) arguments.get("myArg");
      __result.putString("myArg", myArg);
    }
    return __result;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
        return true;
    }
    if (object == null || getClass() != object.getClass()) {
        return false;
    }
    HomeSecondFragmentArgs that = (HomeSecondFragmentArgs) object;
    if (arguments.containsKey("myArg") != that.arguments.containsKey("myArg")) {
      return false;
    }
    if (getMyArg() != null ? !getMyArg().equals(that.getMyArg()) : that.getMyArg() != null) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int result = 1;
    result = 31 * result + (getMyArg() != null ? getMyArg().hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return "HomeSecondFragmentArgs{"
        + "myArg=" + getMyArg()
        + "}";
  }

  public static class Builder {
    private final HashMap arguments = new HashMap();

    public Builder(HomeSecondFragmentArgs original) {
      this.arguments.putAll(original.arguments);
    }

    public Builder(@NonNull String myArg) {
      if (myArg == null) {
        throw new IllegalArgumentException("Argument \"myArg\" is marked as non-null but was passed a null value.");
      }
      this.arguments.put("myArg", myArg);
    }

    @NonNull
    public HomeSecondFragmentArgs build() {
      HomeSecondFragmentArgs result = new HomeSecondFragmentArgs(arguments);
      return result;
    }

    @NonNull
    public Builder setMyArg(@NonNull String myArg) {
      if (myArg == null) {
        throw new IllegalArgumentException("Argument \"myArg\" is marked as non-null but was passed a null value.");
      }
      this.arguments.put("myArg", myArg);
      return this;
    }

    @SuppressWarnings("unchecked")
    @NonNull
    public String getMyArg() {
      return (String) arguments.get("myArg");
    }
  }
}
