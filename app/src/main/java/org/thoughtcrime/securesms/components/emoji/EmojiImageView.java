package org.thoughtcrime.securesms.components.emoji;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

import org.thoughtcrime.securesms.R;
import org.signal.emoji.EmojiProvider;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

public class EmojiImageView extends AppCompatImageView {

  private final boolean forceJumboEmoji;

  public EmojiImageView(Context context) {
    this(context, null);
  }

  public EmojiImageView(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public EmojiImageView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.EmojiImageView, 0, 0);
    forceJumboEmoji = a.getBoolean(R.styleable.EmojiImageView_forceJumbo, false);
    a.recycle();
  }

  public void setImageEmoji(CharSequence emoji) {
    if (isInEditMode()) {
      setImageResource(R.drawable.ic_emoji);
    } else {
      setImageDrawable(EmojiProvider.getEmojiDrawable(getContext(), emoji, forceJumboEmoji));
      setContentDescription(emoji);
      Log.i("Signal", "===== setImageEmoji " + emoji);
      List<String> darkEmojis = Arrays.asList(getResources().getStringArray(R.array.dark_emojis_in_need_of_white_background));
      if (darkEmojis.contains(emoji.toString().toLowerCase())) {
        Log.i("Signal", "===== setImageEmoji " + emoji + " is dark - setting light background");
        Log.i("Signal", "===== setImageEmoji " + emoji + " resolved color to " + String.format("%08x", getResources().getColor(org.signal.core.ui.R.color.signal_light_colorSurface5)));
        //setBackgroundColor(getResources().getColor(org.signal.core.ui.R.color.signal_light_colorSurface5));
        setBackground(getResources().getDrawable(R.drawable.emoji_white_background));
      } else {
        StringBuilder hex = new StringBuilder();
        for (byte b: emoji.toString().getBytes(StandardCharsets.UTF_8)) {
          hex.append(String.format("%02x ", b));
        }
        Log.i("Signal", "===== setImageEmoji " + emoji + " is not dark = " + hex);
        hex.setLength(0);
        for (byte b: darkEmojis.get(25).getBytes(StandardCharsets.UTF_8)) {
          hex.append(String.format("%02x ", b));
        }
        Log.i("Signal", "===== setImageEmoji spider1 = " + darkEmojis.get(25) + " = " + hex);
      }
    }
  }
}
