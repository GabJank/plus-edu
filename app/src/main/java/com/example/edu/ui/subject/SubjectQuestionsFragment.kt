package com.example.edu.ui.subject

import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import com.example.edu.MainActivity
import com.example.edu.R
import com.example.edu.databinding.FragmentSubjectQuestionsBinding
import org.json.JSONObject
import kotlin.getValue

class SubjectQuestionsFragment : Fragment() {
    private val subjectQuestionsViewModel: SubjectQuestionsViewModel by activityViewModels()
    private var _binding: FragmentSubjectQuestionsBinding? = null
    private val binding get() = _binding!!
    private var currentQuestionIndex = 0
    private var questions: List<Pair<Int, JSONObject>> = emptyList()
    private lateinit var answerButtons: List<CardView>
    private lateinit var selectedViews: List<View>
    private val optionLetters = listOf("A", "B", "C", "D")
    private var selectedOption: String? = null
    private var countDownTimer: CountDownTimer? = null
    private val totalTimeMillis = 10 * 60 * 1000L
    private var minutosTotais: Long = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSubjectQuestionsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        answerButtons = listOf(
            binding.answer1,
            binding.answer2,
            binding.answer3,
            binding.answer4
        )

        selectedViews = listOf(
            binding.selected1,
            binding.selected2,
            binding.selected3,
            binding.selected4
        )

        setupAnswerClicks()

        subjectQuestionsViewModel.config.observe(viewLifecycleOwner) { json ->
            questions = extractQuestions(json)

            if (questions.isNotEmpty()) {
                if (currentQuestionIndex >= questions.size) {
                    currentQuestionIndex = 0
                }
                showQuestion(currentQuestionIndex)
            }
        }

        binding.confirmButton.setOnClickListener {
            confirmAnswer()
        }

        startTimer()

        return root
    }

    private fun setupAnswerClicks() {
        answerButtons.forEachIndexed { index, card ->
            card.setOnClickListener {
                selectedViews.forEach { sel -> sel.visibility = View.INVISIBLE }

                selectedViews[index].visibility = View.VISIBLE

                selectedOption = optionLetters[index]
            }
        }
    }

    private fun extractQuestions(json: JSONObject): List<Pair<Int, JSONObject>> {
        val list = mutableListOf<Pair<Int, JSONObject>>()
        val keys = json.keys()

        while (keys.hasNext()) {
            val key = keys.next()
            val posicao = key.toIntOrNull() ?: continue
            val obj = json.optJSONObject(key) ?: continue
            list.add(posicao to obj)
        }

        return list.sortedBy { it.first }
    }

    private fun showQuestion(index: Int) {
        if (questions.isEmpty()) return
        if (index !in questions.indices) return

        val (numeroQuestao, objQuestao) = questions[index]

        val pergunta = objQuestao.optString("pergunta")
        binding.questionTitle.text = "$numeroQuestao. $pergunta"

        val totalQuestions = questions.size
        val questionsString = "${index + 1}/$totalQuestions"
        binding.textPontos.text = questionsString

        val proporcao = (index + 1).toFloat() / totalQuestions.toFloat()
        val maxWidth = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            360f,
            resources.displayMetrics
        )
        val largura = (proporcao * maxWidth).toInt()
        binding.loading.layoutParams.width = largura
        binding.loading.requestLayout()

        binding.textNome.text = objQuestao.optString("A")
        binding.textNome2.text = objQuestao.optString("B")
        binding.textNome3.text = objQuestao.optString("C")
        binding.textNome4.text = objQuestao.optString("D")

        selectedOption = null
        selectedViews.forEach { it.visibility = View.INVISIBLE }
        resetAnswerColors()
    }
    private fun confirmAnswer() {
        if (questions.isEmpty()) return

        val chosen = selectedOption
        if (chosen == null) {
            Toast.makeText(requireContext(), "Selecione uma alternativa", Toast.LENGTH_SHORT).show()
            return
        }

        val (_, objQuestao) = questions[currentQuestionIndex]
        val correta = objQuestao.optString("correta")

        if (chosen == correta) {
            Toast.makeText(requireContext(), "Resposta correta!", Toast.LENGTH_SHORT).show()
            nextQuestion()
        } else {
            Toast.makeText(
                requireContext(),
                "Resposta incorreta. Correta: $correta",
                Toast.LENGTH_SHORT
            ).show()
            highlightAnswers(chosen, correta)
        }
    }

    private fun highlightAnswers(chosen: String, correta: String) {
        answerButtons.forEachIndexed { index, card ->
            val letter = optionLetters[index]

            when (letter) {
                correta -> {
                    card.setCardBackgroundColor(0xFF4CAF50.toInt())
                }
                chosen -> {
                    card.setCardBackgroundColor(0xFFF44336.toInt())
                }
                else -> {
                    card.setCardBackgroundColor(0xFFFFFFFF.toInt())
                }
            }
        }
    }

    private fun resetAnswerColors() {
        answerButtons.forEach { card ->
            card.setCardBackgroundColor(0xFFFFFFFF.toInt()) // branco
        }
    }

    private fun startTimer() {
        countDownTimer?.cancel()

        countDownTimer = object : CountDownTimer(totalTimeMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val elapsedMillis = totalTimeMillis - millisUntilFinished
                val totalSeconds = elapsedMillis / 1000
                val minutes = totalSeconds / 60

                minutosTotais = minutes
            }

            override fun onFinish() {
                minutosTotais = 10L
            }
        }.start()
    }


    private fun nextQuestion() {
        if (questions.isEmpty()) return
        if (currentQuestionIndex < questions.lastIndex) {
            currentQuestionIndex++
            showQuestion(currentQuestionIndex)
        } else {
            val activity = requireActivity() as MainActivity
            val subjectItem = activity.subjectResumeViewModel.config.value

            subjectItem?.let { item ->
                Log.d("+EDU", item.titulo)
                activity.marcarModuloComoAprovado(item.titulo, minutosTotais)
            }

            val options = navOptions {
                popUpTo(R.id.navigation_subject) {
                    inclusive = true
                }
            }

            findNavController().navigate(
                R.id.action_navigation_subject_questions_to_subject_finished,
                null,
                options
            )
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
