package com.example.edu.ui.subject

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.edu.MainActivity
import com.example.edu.R
import com.example.edu.databinding.FragmentSubjectResumeBinding
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import kotlin.getValue

class SubjectResumeFragment : Fragment() {
    private val subjectResumeViewModel: SubjectResumeViewModel by activityViewModels()
    private var _binding: FragmentSubjectResumeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSubjectResumeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val youTubePlayerView : YouTubePlayerView = binding.youtubePlayerView
        val resume_text: TextView = binding.resumeText
        val questions_button: CardView = binding.questionsButton

        subjectResumeViewModel.config.observe(viewLifecycleOwner) { json ->
            val videoUrl = json.link
            val videoId = videoUrl.substringAfter("v=")

            youTubePlayerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
                override fun onReady(youTubePlayer: YouTubePlayer) {
                    youTubePlayer.cueVideo(videoId, 0f)
                }
            })

            resume_text.text = json.resumo_aula

            questions_button.setOnClickListener {
                findNavController().navigate(
                    R.id.action_navigation_subject_resume_to_subject_questions
                )

                (requireActivity() as MainActivity).subjectQuestionsViewModel.setConfig(json.topicos_quiz!!)
            }
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}