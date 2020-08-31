package au.net.tech.app

import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.text.method.PasswordTransformationMethod
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.LayoutRes

import androidx.core.content.ContextCompat.getColor
import androidx.core.content.res.ResourcesCompat
import com.google.android.material.snackbar.Snackbar
import java.util.*


fun ViewGroup.inflate(@LayoutRes layoutRes: Int, attachToRoot: Boolean = false): View {
    return LayoutInflater.from(context).inflate(layoutRes, this, attachToRoot)
}

fun View.changeViewBg(id: Int) {
    this.background = ResourcesCompat.getDrawable(context.resources, id, null)
}

fun View.changeViewBgColor(id: Int) {
    this.setBackgroundColor(ResourcesCompat.getColor(context.resources, id, null))
}


fun View.showSnackBar(msg: String) {
    Snackbar.make(this, msg, Snackbar.LENGTH_LONG).show()
}

fun TextView.changeTextColor(id: Int) {
    this.setTextColor(ResourcesCompat.getColor(context.resources, id, null))
}

fun TextView.setTextViewDrawableColor(color: Int) {
    for (drawable in this.compoundDrawables) {
        drawable?.colorFilter =
            PorterDuffColorFilter(getColor(context, color), PorterDuff.Mode.SRC_IN)
    }
}

fun ImageView.changeTint(color: Int) {
    this.setColorFilter(getColor(context, color), PorterDuff.Mode.SRC_IN);

}

fun EditText.getTrimmedText(): String {
    return this.text.toString().trim()
}

fun EditText.moveCourserAtEnd() {
    this.setSelection(this.text.length)
}

fun EditText.setCustomAnimation(view : View)
{
    this.setOnFocusChangeListener{ _, focus ->
            this.enableEditText(view,focus)
    }
}


fun EditText.enableEditText(view: View, flag: Boolean) {
    if (flag) {
        view.visibility = View.VISIBLE
        view.changeViewBg(R.color.colorAccent)
        val anim = AnimationUtils.loadAnimation(view.context, R.anim.enable_edittext)
        view.startAnimation(anim)
    } else {
        view.changeViewBgColor(R.color.colorDivider)
    }
}

fun EditText.showHidePassword(isShowing: Boolean) {
    val sel = this.selectionStart
    if (isShowing) {
        this.transformationMethod = null

    } else {
        this.transformationMethod = PasswordTransformationMethod()
    }
    this.setSelection(sel)
}




fun TextView.getTrimmedText(): String {
    return this.text.toString().trim()
}


fun CharSequence?.isValidEmail() =
    !isNullOrEmpty() && Patterns.EMAIL_ADDRESS.matcher(this).matches()



