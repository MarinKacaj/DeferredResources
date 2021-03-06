package com.backbase.deferredresources

import android.content.Context
import androidx.annotation.StringRes
import dev.drewhamilton.extracare.DataApi

/**
 * A wrapper for resolving text on demand.
 */
public interface DeferredText {

    /**
     * Resolve the text.
     */
    public fun resolve(context: Context): CharSequence

    /**
     * A wrapper for a constant text [value].
     */
    @DataApi public class Constant(
        private val value: CharSequence
    ) : DeferredText {
        /**
         * Always resolves to [value], ignoring [context].
         */
        override fun resolve(context: Context): CharSequence = value
    }

    /**
     * A wrapper for a text [resId].
     */
    @DataApi public class Resource @JvmOverloads constructor(
        @StringRes private val resId: Int,
        private val type: Type = Type.STRING
    ) : DeferredText {

        /**
         * Resolve [resId] to text with the given [context].
         *
         * If [type] is [Type.STRING], resolves a localized string. If [type] is [Type.TEXT], resolves a localized,
         * styled [CharSequence].
         *
         * @see android.content.Context.getString
         * @see android.content.Context.getText
         */
        override fun resolve(context: Context): CharSequence = when (type) {
            Type.STRING -> context.getString(resId)
            Type.TEXT -> context.getText(resId)
        }

        /**
         * The type of text resource to resolve.
         */
        public enum class Type {
            STRING, TEXT
        }
    }
}
