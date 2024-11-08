package com.example.yovohelper.views.adapters

import android.app.ActionBar
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.yovohelper.R
import com.example.yovohelper.databinding.DialogGuideBinding
import com.example.yovohelper.databinding.ItemSettingsBinding
import com.example.yovohelper.models.SettingsModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class  SettingsAdapter(var list: ArrayList<SettingsModel>, var context: Context) :
    RecyclerView.Adapter<SettingsAdapter.viewHolder>() {

    inner class viewHolder(var binding: ItemSettingsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(model: SettingsModel, position: Int) {
            binding.apply {
                settingsTitle.text = model.title
                settingsDesc.text = model.desc

                root.setOnClickListener {
                    when (position) {
                        0 -> {
                            // how to use 1st item
                            val dialog = Dialog(context)
                            val dialogBinding =
                                DialogGuideBinding.inflate((context as Activity).layoutInflater)
                            dialogBinding.okayBtn.setOnClickListener {
                                dialog.dismiss()
                            }
                            dialog.setContentView(dialogBinding.root)

                            dialog.window?.setLayout(
                                ActionBar.LayoutParams.MATCH_PARENT,
                                ActionBar.LayoutParams.WRAP_CONTENT
                            )

                            dialog.show()


                        }

                        2 -> {
                            Intent(Intent.ACTION_VIEW, Uri.parse("https://yovobot.blogspot.com/p/disclaimer.html")).apply {
                                context.startActivity(this)
                            }
                        }

                        3 -> {
                            Intent(Intent.ACTION_VIEW, Uri.parse("https://yovobot.blogspot.com/p/privacy-policy.html")).apply {
                                context.startActivity(this)
                            }

                        }

                        4 -> {
                            Intent(Intent.ACTION_SEND).apply {
                                type = "text/plain"
                                putExtra(Intent.EXTRA_SUBJECT,context.getString(R.string.app_name))
                                putExtra(Intent.EXTRA_TEXT,"Check out YovoBot! \uD83D\uDE80\n" +
                                        "\n" +
                                        "Need an app that saves time and cuts out the hassle? YovoBot is here! Download YouTube videos, save WhatsApp statuses, avoid endless redirect links, and edit videos all in one place. It's everything you need to boost productivity and make digital tasks a breeze. Try it out and see the difference!\n" +
                                        "\n" +
                                        "Download YovoBot and make life easier today! ✨ :https://play.google.com/store/apps/details?id=${context.packageName}")
                                context.startActivity(this)
                            }
                        }

                    }
                }

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        return viewHolder(ItemSettingsBinding.inflate(LayoutInflater.from(context), parent, false))
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        holder.bind(model = list[position], position)
    }
}