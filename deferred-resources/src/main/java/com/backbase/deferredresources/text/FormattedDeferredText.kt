package com.backbase.deferredresources.text

import android.content.Context
import com.backbase.deferredresources.DeferredFormattedString
import com.backbase.deferredresources.DeferredText
import dev.drewhamilton.extracare.DataApi

/**
 * Convert a [DeferredFormattedString] to a normal [DeferredText] by providing its [formatArgs] to be used when
 * resolved.
 */
@JvmSynthetic public fun DeferredFormattedString.withFormatArgs(vararg formatArgs: Any): DeferredText =
    FormattedDeferredText(wrapped = this, formatArgs = formatArgs)

/**
 * A [DeferredText] implementation that wraps a [DeferredFormattedString] along with its [formatArgs]. Designed for use
 * cases where the format args are determined at the declaring site rather than the resolving site.
 *
 * If the format args are to be determined at the resolving site, stick with [DeferredFormattedString].
 */
@DataApi public class FormattedDeferredText private constructor(
    // Private constructor marker allows vararg constructor overload while retaining DataApi toString generation
    @Suppress("UNUSED_PARAMETER") privateConstructorMarker: Int,
    private val wrapped: DeferredFormattedString,
    private val formatArgs: Array<out Any>
) : DeferredText {

    /**
     * Initialize with the given [wrapped] [DeferredFormattedString] and [formatArgs].
     *
     * This constructor protects against array mutability by making a copy of [formatArgs].
     */
    public constructor(
        wrapped: DeferredFormattedString,
        vararg formatArgs: Any
    ) : this(1, wrapped, arrayOf(*formatArgs))

    /**
     * Resolve [wrapped] with [formatArgs] using the given [context].
     */
    override fun resolve(context: Context): CharSequence = wrapped.resolve(context, *formatArgs)

    /**
     * Two instances of [FormattedDeferredText] are considered equals if they wrap equals [DeferredFormattedString]s,
     * they hold the same number of [formatArgs], and each format arg in this instance is equals to the corresponding
     * format arg in [other].
     */
    override fun equals(other: Any?): Boolean = other is FormattedDeferredText &&
            this.wrapped == other.wrapped &&
            this.formatArgs.contentEquals(other.formatArgs)

    /**
     * A hash of the formatted string and its format arguments.
     */
    override fun hashCode(): Int = 31 * wrapped.hashCode() + formatArgs.contentHashCode()
}
