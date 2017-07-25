# Android Tiles

Android tiles is a project that provides 4 different "tiles" or options for your app. They are configurable custom views that can be used
for selecting options.

On this library you will find 4 different choices for displaying options:

1. OptionTileSmall, a small version that only provides a Content, and can be stacked.
2. OptionTile, a larger version that contains a Title and a Content.
3. OptionExpandableTile, a version that contains a recyclerView and can be expanded to select different options.
4. StatusTile, a tile that has different states and can have a step, a title and a content.

## Download

For using the library all you have to do is add the following import to
the project Gradle file:

```java
compile 'com.nsemeniuk.tiles:tiles:0.0.1'
```

## Types of Tiles

As said there are four different contained on this library. Below you can find each one and how to
properly use them and configure them.

### StatusTile

The status tile is a Tile that has several types or states. Useful to track user interactions
and display them accordingly.

The four states that we can use are:

- Enabled, means that the tile is enabled for interaction.
- Disabled, means that the tile is not enabled for interaction.
- Completed, changes the icon to a check (o whatever you want to show).
- CompletedDisabled, this special case is used when you consider that the option has been
completed, but it cannot be changed.

You can see how they look below:

![Preview](http://i.imgur.com/YQfwGIOl.png)

#### Usage

For using the tile all you have to do is define it on your XML like:

```xml
    <com.nsemeniuk.tiles.StatusTile
      app:statusTile_contentText="@string/tile_content"
      app:statusTile_stepText="@string/tile_step"
      app:statusTile_titleText="@string/tile_title"
      app:statusTile_type="enabled"/>
```

#### Styling

By default the status tile has black text and the step is highlighted with
the accent color of the app compat theme. It has a divider for stacking them.

##### Colors

If you want to change it you can set the style on your styles.xml file:

```xml
  <style name="MaterialTheme" parent="Theme.AppCompat.Light.NoActionBar">
 
     <item name="statusTile_stepTextColor">@color/colorAccent</item>
     <item name="statusTile_titleTextColor">@color/colorPrimary</item>
     <item name="statusTile_contentTextColor">@color/colorPrimary</item>
   
     <item name="statusTile_stepTextColorDisabledr">@color/colorAccentDisabled</item>
     <item name="statusTile_titleTextColorDisabled">@color/colorPrimaryDisabled</item>
     <item name="statusTile_contentTextColorDisabled">@color/colorPrimaryDisabled</item>
   
  </style>
```

##### Icons

You can also specify custom icons for each state, setting it on the XML:

```xml
    <com.nsemeniuk.tiles.StatusTile
      app:statusTile_enabledIcon="@drawable/ic_my_icon_enabled"
      app:statusTile_disabledIcon="@drawable/ic_my_icon_disabled"
      app:statusTile_completedIcon="@drawable/ic_my_icon_completed"
      app:statusTile_completedDisabledIcon="@drawable/ic_my_icon_completed_disabled"/>
```

##### Divider

You can also configure if the divider is on or off by setting 

```xml
    <com.nsemeniuk.tiles.StatusTile
      app:statusTile_showDivider="false"/>
```