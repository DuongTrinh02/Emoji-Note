package com.mood.base

import android.app.Dialog
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.viewbinding.ViewBinding
import com.mood.R
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


abstract class BaseBottomSheetDialog<VB : ViewBinding> : BottomSheetDialogFragment() {

    private var _binding: VB? = null
    lateinit var binding: VB

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        if (isExpanded()) {
            dialog.setOnShowListener {
                Handler(Looper.getMainLooper()).post {
                    val bottomSheet =
                        (dialog as? BottomSheetDialog)?.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as? FrameLayout
                    bottomSheet?.layoutParams?.height =
                        (resources.displayMetrics.heightPixels * expandedOffset()).toInt()
                    bottomSheet?.let {
                        BottomSheetBehavior.from(it).let { behavior ->
                            behavior.peekHeight =
                                (resources.displayMetrics.heightPixels * expandedOffset()).toInt()
                            behavior.state = BottomSheetBehavior.STATE_EXPANDED
                            behavior.isDraggable = isDraggable()
                        }
                    }
                }
            }
        }
        return dialog
    }

    open fun expandedOffset(): Float {
        return 0.85f
    }

    open fun isDraggable(): Boolean {
        return true
    }

    open fun isExpanded(): Boolean {
        return false
    }

    @Suppress("UNCHECKED_CAST")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = inflateBinding(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initData()
        initListener()
    }

    protected abstract fun initView()

    protected abstract fun initData()

    protected abstract fun initListener()

    protected abstract fun inflateBinding(layoutInflater: LayoutInflater): VB

    override fun getTheme(): Int = R.style.BaseBottomSheetDialog
}