package com.example.profile

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.profile.databinding.ActivityProfileBinding

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.nameTextView.text = "Константин Западный"
        binding.bioTextView.text = "Люблю свою музыку, программировать игры на ассемблере"

        binding.infoItemValue1.text = "Музыкант"
        binding.infoItemValue2.text = "27 лет"
        binding.infoItemValue3.text = "185"

        binding.statisticTitle1.text = "299"
        binding.statisticSubtitle1.text = "Постов"
        binding.statisticTitle2.text = "299"
        binding.statisticSubtitle2.text = "Подписок"
        binding.statisticTitle3.text = "1300"
        binding.statisticSubtitle3.text = "Подписчиков"

        binding.aboutValue.text =
            "Йе, урождённый Канье Омари Вест — репер, певец, актер, автор, продюсер и дизайнер из Атланты/Чикаго." +
                    "\n\nОснователь собственного лейбла G.O.O.D. Music.\n" +
                    "\nВырос в Чикаго, где с юных лет был связан с музыкой."

        binding.hobby1.text = "Музыка"
        binding.hobby2.text = "Фитнес"
        binding.hobby3.text = "Плавание"
        binding.hobby4.text = "Программирование"
        binding.hobby5.text = "Кино"
        binding.hobby6.text = "Дизайн"
    }
}