package com.pengxh.autodingding.ui

import android.os.Bundle
import com.pengxh.autodingding.R
import com.pengxh.autodingding.databinding.ActivityEmailConfigBinding
import com.pengxh.autodingding.extensions.initImmersionBar
import com.pengxh.autodingding.extensions.sendEmail
import com.pengxh.autodingding.utils.Constant
import com.pengxh.autodingding.utils.EmailConfigKit
import com.pengxh.kt.lite.base.KotlinBaseActivity
import com.pengxh.kt.lite.extensions.isEmail
import com.pengxh.kt.lite.extensions.show
import com.pengxh.kt.lite.utils.SaveKeyValues
import com.pengxh.kt.lite.widget.TitleBarView
import com.pengxh.kt.lite.widget.dialog.AlertMessageDialog

class EmailConfigActivity : KotlinBaseActivity<ActivityEmailConfigBinding>() {

    private val context = this

    override fun initOnCreate(savedInstanceState: Bundle?) {
        val config = EmailConfigKit.getConfig()
        binding.emailSendAddressView.setText(config.emailSender)
        binding.emailSendCodeView.setText(config.permissionCode)
        binding.emailSendServerView.setText(config.senderServer)
        binding.emailSendPortView.setText(config.emailPort)
        binding.emailInboxView.setText(config.inboxEmail)
        binding.emailTitleView.setText(config.emailTitle)
    }

    override fun initViewBinding(): ActivityEmailConfigBinding {
        return ActivityEmailConfigBinding.inflate(layoutInflater)
    }

    override fun observeRequestState() {

    }

    override fun setupTopBarLayout() {
        binding.rootView.initImmersionBar(this, true, R.color.white)
        binding.titleView.setOnClickListener(object : TitleBarView.OnClickListener {
            override fun onLeftClick() {
                finish()
            }

            override fun onRightClick() {
                val emailSendAddress = binding.emailSendAddressView.text.toString()
                if (emailSendAddress.isBlank()) {
                    "发件箱地址为空".show(context)
                    return
                }
                if (!emailSendAddress.isEmail()) {
                    "发件箱格式错误，请检查".show(context)
                    return
                }
                SaveKeyValues.putValue(Constant.EMAIL_SEND_ADDRESS_KEY, emailSendAddress)

                val emailSendCode = binding.emailSendCodeView.text.toString()
                if (emailSendCode.isBlank()) {
                    "发件箱授权码为空".show(context)
                    return
                }
                SaveKeyValues.putValue(Constant.EMAIL_SEND_CODE_KEY, emailSendCode)

                val emailSendServer = binding.emailSendServerView.text.toString()
                if (emailSendServer.isBlank()) {
                    "发件箱服务器为空".show(context)
                    return
                }
                SaveKeyValues.putValue(Constant.EMAIL_SEND_SERVER_KEY, emailSendServer)

                val emailSendPort = binding.emailSendPortView.text.toString()
                if (emailSendPort.isBlank()) {
                    "发件箱服务器端口为空".show(context)
                    return
                }
                SaveKeyValues.putValue(Constant.EMAIL_SEND_PORT_KEY, emailSendPort)

                val emailInboxAddress = binding.emailInboxView.text.toString()
                if (emailInboxAddress.isBlank()) {
                    "收件箱地址为空".show(context)
                    return
                }
                if (!emailInboxAddress.isEmail()) {
                    "发件箱格式错误，请检查".show(context)
                    return
                }
                SaveKeyValues.putValue(Constant.EMAIL_IN_BOX_KEY, emailInboxAddress)

                SaveKeyValues.putValue(
                    Constant.EMAIL_TITLE_KEY, binding.emailTitleView.text.toString()
                )

                AlertMessageDialog.Builder().setContext(context).setTitle("温馨提醒")
                    .setMessage("邮箱配置完成")
                    .setPositiveButton("好的").setOnDialogButtonClickListener(object :
                        AlertMessageDialog.OnDialogButtonClickListener {
                        override fun onConfirmClick() {

                        }
                    }).build().show()
            }
        })
    }

    override fun initEvent() {
        binding.sendEmailButton.setOnClickListener {
            if (!EmailConfigKit.isEmailConfigured()) {
                "请先保存邮箱配置".show(context)
                return@setOnClickListener
            }
            "这是一封测试邮件，不必关注".sendEmail(this, "邮箱测试")
        }
    }
}