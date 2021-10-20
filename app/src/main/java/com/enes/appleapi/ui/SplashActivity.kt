package com.enes.appleapi.ui

import android.animation.Animator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.enes.appleapi.R
import com.enes.appleapi.databinding.ActivitySplashBinding
import com.enes.appleapi.ui.search.MainSearchActivity

class SplashActivity : AppCompatActivity() {
    var binding : ActivitySplashBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_splash)
        binding?.lottieAnimation?.playAnimation()
        val intent = Intent(this, MainSearchActivity::class.java)
        binding?.lottieAnimation?.addAnimatorListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animator: Animator) {}
            override fun onAnimationEnd(animator: Animator) {
                startActivity(intent)
                finish()
            }
            override fun onAnimationCancel(animator: Animator) {}
            override fun onAnimationRepeat(animator: Animator) {}
        })
    }
}