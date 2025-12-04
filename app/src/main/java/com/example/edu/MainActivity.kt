package com.example.edu

import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.WindowManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.edu.databinding.ActivityMainBinding
import com.example.edu.ui.config.AchievementsViewModel
import com.example.edu.ui.config.ConfigViewModel
import com.example.edu.ui.config.LeaderBoardViewModel
import com.example.edu.ui.home.HomeViewModel
import com.example.edu.ui.profile.ProfileViewModel
import org.json.JSONObject
import androidx.core.content.edit
import com.example.edu.ui.subject.SubjectFinishedViewModel
import com.example.edu.ui.subject.SubjectQuestionsViewModel
import com.example.edu.ui.subject.SubjectResumeViewModel
import com.example.edu.ui.subject.SubjectViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var leaderboardViewModel: LeaderBoardViewModel
    private lateinit var achievementViewModel: AchievementsViewModel
    private lateinit var configViewModel: ConfigViewModel
    private lateinit var profileViewModel: ProfileViewModel
    lateinit var subjectViewModel: SubjectViewModel
    lateinit var subjectResumeViewModel: SubjectResumeViewModel
    lateinit var subjectQuestionsViewModel: SubjectQuestionsViewModel
    lateinit var subjectFinishedViewModel: SubjectFinishedViewModel
    private lateinit var json: JSONObject

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)

        json = loadConfigJson()

        homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        homeViewModel.setConfig(json)
        leaderboardViewModel = ViewModelProvider(this)[LeaderBoardViewModel::class.java]
        leaderboardViewModel.setConfig(json)
        achievementViewModel = ViewModelProvider(this)[AchievementsViewModel::class.java]
        achievementViewModel.setConfig(json)
        configViewModel = ViewModelProvider(this)[ConfigViewModel::class.java]
        configViewModel.setConfig(json)
        profileViewModel = ViewModelProvider(this)[ProfileViewModel::class.java]
        profileViewModel.setConfig(json)
        subjectViewModel = ViewModelProvider(this)[SubjectViewModel::class.java]
        subjectResumeViewModel = ViewModelProvider(this)[SubjectResumeViewModel::class.java]
        subjectQuestionsViewModel = ViewModelProvider(this)[SubjectQuestionsViewModel::class.java]
        subjectFinishedViewModel = ViewModelProvider(this)[SubjectFinishedViewModel::class.java]

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_achievements,
                R.id.navigation_leaderboard,
                R.id.navigation_home,
                R.id.navigation_config,
                R.id.navigation_profile
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    private fun loadConfigJson(): JSONObject {
        val prefs = getSharedPreferences("config_prefs", MODE_PRIVATE)
        val savedJsonString = prefs.getString("config_json", null)

        return if (savedJsonString != null) {
            JSONObject(savedJsonString)
        } else {
            val jsonString = assets.open("data.json")
                .bufferedReader()
                .use { it.readText() }

            prefs.edit { putString("config_json", jsonString) }
            JSONObject(jsonString)
        }
    }

    fun resetConfigJson() {
        val prefs = getSharedPreferences("config_prefs", MODE_PRIVATE)

        val jsonString = assets.open("data.json")
            .bufferedReader()
            .use { it.readText() }

        prefs.edit { putString("config_json", jsonString) }
        val j = JSONObject(jsonString)
        atualizarJson(j)
    }

    fun atualizarJson(jsons: JSONObject) {
        homeViewModel.setConfig(jsons)
        leaderboardViewModel.setConfig(jsons)
        achievementViewModel.setConfig(jsons)
        configViewModel.setConfig(jsons)
        profileViewModel.setConfig(jsons)

        saveConfigJson(jsons)
    }

    fun marcarModuloComoAprovado(tituloModulo: String, minutosTotais: Long) {
        val disciplinas = json.optJSONArray("conteudo_disciplinas") ?: return
        val ultima_disciplina = json.optString("ultima_disciplina") ?: return
        val achievements = json.optJSONObject("achievements") ?: return
        for (i in 0 until disciplinas.length()) {
            val disc = disciplinas.optJSONObject(i) ?: continue
            val nome_disc = disc.optString("disciplina")
            val modulos = disc.optJSONArray("modulos") ?: return
            if (nome_disc == ultima_disciplina) {
                for (i in 0 until modulos.length()) {
                    val modulo = modulos.optJSONObject(i) ?: continue
                    if (modulo.optString("titulo") == tituloModulo) {
                        modulo.put("avaliado", true)
                        break
                    }
                }
                val keys = achievements.keys()

                while (keys.hasNext()) {
                    val key = keys.next()
                    val posicao = key.toIntOrNull() ?: continue
                    val obj = achievements.optJSONObject(key) ?: continue

                    val quantidade = obj.optString("quantidade")
                    val partes = quantidade.split("/")
                    var numerador = partes[0].toInt()
                    val denominador = partes[1].toInt()

                    if (posicao == 1 && numerador < denominador) {
                        numerador = numerador + minutosTotais.toInt()
                        if (numerador >= 10) {
                            numerador = 10
                        }
                        obj.put("quantidade", "$numerador/$denominador")
                    }

                    if (posicao == 2 && numerador < denominador) {
                        numerador++
                        obj.put("quantidade", "$numerador/$denominador")
                    }

                    if (nome_disc == "HISTÓRIA") {
                        if (posicao == 3 && numerador < denominador) {
                            numerador++
                            obj.put("quantidade", "$numerador/$denominador")
                        }
                    }
                }
            }
        }
        atualizarJson(json)
    }


    private fun saveConfigJson(jsons: JSONObject) {
        val prefs = getSharedPreferences("config_prefs", MODE_PRIVATE)
        prefs.edit { putString("config_json", jsons.toString()) }
        json = loadConfigJson()
    }

}